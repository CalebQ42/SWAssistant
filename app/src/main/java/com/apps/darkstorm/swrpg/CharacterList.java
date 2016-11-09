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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.StarWars.DriveLoadChars;
import com.apps.darkstorm.swrpg.StarWars.LoadChars;
import com.apps.darkstorm.swrpg.UI.CharacterCard;
import com.google.android.gms.common.api.GoogleApiClient;

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
    }

    Handler mainHandle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_character_list, container, false);
        chars = new ArrayList<>();
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        fab.show();
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = -1;
                for (Character chara:chars){
                    if (chara.ID>max){
                        max = chara.ID;
                    }
                }
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                        android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.content_navigation, CharacterEditMain.newInstance(max+1,gac))
                        .addToBackStack("Creating a new Character").commit();
                fab.hide();
            }
        });
        mainHandle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                fab.setEnabled(true);
                System.out.println("Handling");
                if (in.obj instanceof com.apps.darkstorm.swrpg.StarWars.Character) {
                    Character tmp = (Character)in.obj;
                    for (int i = 0; i < chars.size(); i++) {
                        if (chars.get(i).ID == tmp.ID) {
                            ((LinearLayout) top.findViewById(R.id.mainLay)).removeViewAt(i);
                            break;
                        }
                    }
                }else if(in.obj instanceof ArrayList){
                    ArrayList<Character> charsNew = (ArrayList<Character>)in.obj;
                    if (charsNew.size()!=chars.size()){
                        chars = charsNew;
                        ((LinearLayout)top.findViewById(R.id.mainLay)).removeAllViews();
                        for (Character chara:chars){
                            ((LinearLayout)top.findViewById(R.id.mainLay)).addView(new CharacterCard()
                                    .getCard(CharacterList.this,chara,mainHandle,gac));
                        }
                    }
                    Message snack = mainHandle.obtainMessage();
                    snack.arg1 = -100;
                    mainHandle.sendMessage(snack);
                }
                if(in.arg1 == 100){
                    snack = Snackbar.make(top,R.string.loading_snack,Snackbar.LENGTH_INDEFINITE);
                    snack.show();
                    fab.setEnabled(false);
                }else if(in.arg1 == -100){
                    if (snack != null && snack.isShownOrQueued()){
                        snack.dismiss();
                    }
                    fab.setEnabled(true);
                }
            }
        };
        return top;
    }

    public void onStart(){
        super.onStart();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
        }else {
            final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
            final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
            async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Message snack = mainHandle.obtainMessage();
                    snack.arg1 = 100;
                    mainHandle.sendMessage(snack);
                    if (pref.getBoolean(getString(R.string.cloud_key), false)) {
                        int timeout = 0;
                        if (gac == null || !gac.isConnected() &&gac.isConnecting()) {
                            while (gac == null || !gac.isConnected() || gac.isConnecting() || timeout >=10) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                timeout++;
                            }
                        }
                        DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(), gac);
                        dlc.saveToFile(CharacterList.this.getContext(), false);
                    }
                    LoadChars lc = new LoadChars(CharacterList.this.getContext());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.chars;
                    mainHandle.sendMessage(tmp);
                    return null;
                }
            };
            async.execute();
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

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults){
        if (requestCode == 50 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
            async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if (pref.getBoolean(getString(R.string.cloud_key), false)) {
                        int timeout = 0;
                        if (gac == null || !gac.isConnected() &&gac.isConnecting()) {
                            while (gac == null || !gac.isConnected() || gac.isConnecting() || timeout >=10) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                timeout++;
                            }
                        }
                        DriveLoadChars dlc = new DriveLoadChars(CharacterList.this.getContext(), gac);
                        dlc.saveToFile(CharacterList.this.getContext(), false);
                    }
                    LoadChars lc = new LoadChars(CharacterList.this.getContext());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.chars;
                    mainHandle.sendMessage(tmp);
                    return null;
                }
            };
            async.execute();
        }
    }
}
