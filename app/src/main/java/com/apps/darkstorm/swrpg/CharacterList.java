package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.StarWars.DriveLoadChars;
import com.apps.darkstorm.swrpg.StarWars.LoadChars;
import com.apps.darkstorm.swrpg.UI.CharacterCard;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.util.ArrayList;

public class CharacterList extends Fragment {
    private OnListInteractionListener mListener;

    GoogleApiClient gac = null;
    ArrayList<Character> chars;
    AsyncTask<Void,Void,Void> async;
    Snackbar snack;

    public CharacterList() {}

    public static CharacterList newInstance(GoogleApiClient gac) {
        CharacterList frag = new CharacterList();
        frag.gac = gac;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    Handler mainHandle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_character_list, container, false);
        chars = new ArrayList<>();
        Toast.makeText(getContext(),R.string.swipe_refresh,Toast.LENGTH_SHORT).show();
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        fab.show();
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getBoolean(getString(R.string.cloud_key),false) && pref.getBoolean(getString(R.string.sync_key),true) &&
                        (gac == null || !gac.isConnected())){
                    Snackbar snackbar = Snackbar.make(top,R.string.cloud_fail,Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    ArrayList<Integer> has = new ArrayList<>();
                    for (Character chara : chars) {
                        has.add(chara.ID);
                    }
                    int max = 0;
                    while (has.contains(max)) {
                        max++;
                    }
                    getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.content_navigation, CharacterEditMain.newInstance(max, gac))
                            .addToBackStack("Creating a new Character").commit();
                    fab.hide();
                }
            }
        });
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout)top.findViewById(R.id.swipe_refresh);
        mainHandle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                fab.setEnabled(true);
                System.out.println("Handling");
                if (in.obj instanceof com.apps.darkstorm.swrpg.StarWars.Character) {
                    Character tmp = (Character)in.obj;
                    for (int i = 0; i < chars.size(); i++) {
                        if (chars.get(i).ID == tmp.ID) {
                            ((LinearLayout) top.findViewById(R.id.mainLay)).removeViewAt(i);
                            System.out.println(i);
                            break;
                        }
                    }
                }else if(in.obj instanceof ArrayList){
                    ArrayList<Character> charsNew = (ArrayList<Character>)in.obj;
                    chars = charsNew;
                    ((LinearLayout)top.findViewById(R.id.mainLay)).removeAllViews();
                    if (CharacterList.this.getContext() != null) {
                        for (Character chara : chars) {
                            ((LinearLayout) top.findViewById(R.id.mainLay)).addView(new CharacterCard()
                                    .getCard(CharacterList.this, chara, mainHandle, gac, false));
                        }
                    }
                    Message snack = mainHandle.obtainMessage();
                    snack.arg1 = -100;
                    mainHandle.sendMessage(snack);
                }
                if(in.arg1 == 100){
                    refresh.setRefreshing(true);
                    ((LinearLayout)top.findViewById(R.id.mainLay)).removeAllViews();
//                    snack = Snackbar.make(top,R.string.loading_snack,Snackbar.LENGTH_INDEFINITE);
//                    snack.show();
                    fab.setEnabled(false);
                }else if(in.arg1 == -100){
//                    if (snack != null && snack.isShownOrQueued()){
//                        snack.dismiss();
//                    }
                    fab.setEnabled(true);
                    refresh.setRefreshing(false);
                }else if(in.arg1 == -1){
                    Snackbar fail = Snackbar.make(top,R.string.cloud_fail,Snackbar.LENGTH_LONG);
                    fail.setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            async = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    try {
                                        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
                                        Message snack = mainHandle.obtainMessage();
                                        snack.arg1 = 100;
                                        mainHandle.sendMessage(snack);
                                        if (pref.getBoolean(getString(R.string.cloud_key), false)) {
                                            int timeout = 0;
                                            while ((gac == null || !gac.hasConnectedApi(Drive.API) || gac.isConnecting()) && timeout < 33) {
                                                try {
                                                    Thread.sleep(300);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                timeout++;
                                            }
                                            if (timeout < 33) {
                                                DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(), gac);
                                                dlc.saveToFile(CharacterList.this.getContext(), gac);
                                                System.out.println("Loaded");
                                            } else {
                                                Message timed = mainHandle.obtainMessage();
                                                timed.arg1 = -1;
                                                mainHandle.sendMessage(timed);
                                            }
                                        }
                                        LoadChars lc = new LoadChars(CharacterList.this.getContext());
                                        Message tmp = mainHandle.obtainMessage();
                                        tmp.obj = lc.chars;
                                        mainHandle.sendMessage(tmp);
                                    }catch(IllegalStateException ignored){}
                                    async = null;
                                    return null;
                                }
                            };
                            async.execute();
                        }
                    });
                    fail.show();
                }else if (in.arg1 == 20){
                    Snackbar fail = Snackbar.make(top,R.string.cloud_fail,Snackbar.LENGTH_LONG);
                    fail.show();
                }
            }
        };
        TypedValue primary = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary,primary,true);
        TypedValue accent = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent,accent,true);
        refresh.setColorSchemeResources(primary.resourceId,accent.resourceId);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
                        Message snack = mainHandle.obtainMessage();
                        snack.arg1 = 100;
                        mainHandle.sendMessage(snack);
                        if (pref.getBoolean(getString(R.string.cloud_key), false)) {
                            int timeout = 0;
                            while ((gac == null || !gac.hasConnectedApi(Drive.API) || gac.isConnecting()) && timeout < 33) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                timeout++;
                            }
                            if (timeout < 33) {
                                DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(), gac);
                                dlc.saveToFile(CharacterList.this.getContext(), gac);
                                System.out.println("Loaded");
                            } else {
                                Message timed = mainHandle.obtainMessage();
                                timed.arg1 = -1;
                                mainHandle.sendMessage(timed);
                            }
                        }
                        LoadChars lc = new LoadChars(CharacterList.this.getContext());
                        Message tmp = mainHandle.obtainMessage();
                        tmp.obj = lc.chars;
                        mainHandle.sendMessage(tmp);
                    }catch(IllegalStateException ignored){}
                    async = null;
                    return null;
                }
            };
                async.execute();
            }
        });
        async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
                    Message snack = mainHandle.obtainMessage();
                    snack.arg1 = 100;
                    mainHandle.sendMessage(snack);
                    if (pref.getBoolean(getString(R.string.cloud_key), false)) {
                        int timeout = 0;
                        while ((gac == null || !gac.hasConnectedApi(Drive.API) || gac.isConnecting()) && timeout < 33) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            timeout++;
                        }
                        if (timeout < 33) {
                            DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(), gac);
                            dlc.saveToFile(CharacterList.this.getContext(), gac);
                            System.out.println("Loaded");
                        } else {
                            Message timed = mainHandle.obtainMessage();
                            timed.arg1 = -1;
                            mainHandle.sendMessage(timed);
                        }
                    }
                    LoadChars lc = new LoadChars(CharacterList.this.getContext());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.chars;
                    mainHandle.sendMessage(tmp);
                }catch(IllegalStateException ignored){}
                async = null;
                return null;
            }
        };
        async.execute();
        return top;
    }

    public void onStart(){
        super.onStart();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListInteractionListener) {
            mListener = (OnListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListInteractionListener {
        void onListInteraction();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        if (grantResults.length == 1 && requestCode == 50 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
            async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
                        Message snack = mainHandle.obtainMessage();
                        snack.arg1 = 100;
                        mainHandle.sendMessage(snack);
                        if (pref.getBoolean(getString(R.string.cloud_key), false)) {
                            int timeout = 0;
                            while ((gac == null || !gac.hasConnectedApi(Drive.API) || gac.isConnecting()) && timeout < 33) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                timeout++;
                            }
                            if (timeout < 33) {
                                DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(), gac);
                                dlc.saveToFile(CharacterList.this.getContext(), gac);
                                System.out.println("Loaded");
                            } else {
                                Message timed = mainHandle.obtainMessage();
                                timed.arg1 = -1;
                                mainHandle.sendMessage(timed);
                            }
                        }
                        LoadChars lc = new LoadChars(CharacterList.this.getContext());
                        Message tmp = mainHandle.obtainMessage();
                        tmp.obj = lc.chars;
                        mainHandle.sendMessage(tmp);
                    }catch(IllegalStateException ignored){}
                    async = null;
                    return null;
                }
            };
            async.execute();
        }
    }
}
