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
import com.apps.darkstorm.swrpg.UI.Char.CharacterCard;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.util.ArrayList;

public class GMFragment extends Fragment {
    private OnGMInteractionListener mListener;

    GoogleApiClient gac = null;
    boolean big = false;
    AsyncTask<Void,Void,Void> async;
    Handler mainHandle;
    Snackbar snack;
    View top;
    Character tmp = null;

    public GMFragment() {}

    public static GMFragment newInstance(GoogleApiClient gac) {
        GMFragment fragment = new GMFragment();
        fragment.gac = gac;
        return fragment;
    }

    public static GMFragment newInstance(GoogleApiClient gac, Character tmp) {
        GMFragment fragment = new GMFragment();
        fragment.gac = gac;
        fragment.tmp = tmp;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_gm, container, false);
        this.top = top;
        final LinearLayout chars = (LinearLayout)top.findViewById(R.id.character_list);
        big = !(top.findViewById(R.id.fragment_holder).getVisibility() == View.GONE ||
                top.findViewById(R.id.char_list_scroll).getVisibility() == View.GONE);
        mainHandle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                if (in.obj instanceof Character){
                    if (!big)
                        getFragmentManager().beginTransaction().replace(R.id.content_navigation,CharacterEditMain.newInstance((Character)in.obj,gac, true))
                                .addToBackStack("GMEdit").commit();
                    else
                        getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder,CharacterEditMain.newInstance((Character)in.obj,gac)).commit();
                }else if(in.obj instanceof ArrayList){
                    chars.removeAllViews();
                    ArrayList<Character> tmp = (ArrayList<Character>)in.obj;
                    for (Character chara:tmp){
                        chars.addView(new CharacterCard().getCard(GMFragment.this,chara,mainHandle,gac,true));
                    }
                }
                if(in.arg1 == 100){
                    chars.removeAllViews();
                    if (top.getContext()!= null)
                        snack = Snackbar.make(top,R.string.loading_snack,Snackbar.LENGTH_INDEFINITE);
                    snack.show();
                }else if(in.arg1 == -100){
                    if (snack != null && snack.isShownOrQueued()){
                        snack.dismiss();
                    }
                }
            }
        };
        if (big && tmp != null){
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder,CharacterEditMain.newInstance(tmp,gac)).commit();
        }
        return top;
    }

    public void onStart(){
        super.onStart();
        if (top.findViewById(R.id.fragment_holder).getVisibility() != View.GONE){
            big = true;
        }else{
            big = false;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
        }else {
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
                                DriveLoadChars dlc = new DriveLoadChars(GMFragment.this.getContext(), gac);
                                dlc.saveToFile(GMFragment.this.getContext(), gac);
                                System.out.println("Loaded");
                            } else {
                                Message timed = mainHandle.obtainMessage();
                                timed.arg1 = -1;
                                mainHandle.sendMessage(timed);
                            }
                        }
                        LoadChars lc = new LoadChars(GMFragment.this.getContext());
                        Message tmp = mainHandle.obtainMessage();
                        tmp.obj = lc.chars;
                        tmp.arg1 = -100;
                        mainHandle.sendMessage(tmp);
                    }catch(IllegalStateException ignored){}
                    async = null;
                    return null;
                }
            };
            async.execute();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGMInteractionListener) {
            mListener = (OnGMInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        if (top.findViewById(R.id.fragment_holder).getVisibility() != View.GONE){
            big = true;
        }else{
            big = false;
        }
        super.onDetach();
        mListener = null;
    }

    public interface OnGMInteractionListener {
        void OnGMInteraction();
    }
}
