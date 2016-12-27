package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.animation.Animator;
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
import android.view.KeyEvent;
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

    AsyncTask<Void,Void,Void> async;
    Handler mainHandle;
    Snackbar snack;
    View top;

    public GMFragment() {}

    public static GMFragment newInstance() {
        GMFragment fragment = new GMFragment();
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
        top.setFocusableInTouchMode(true);
        top.requestFocus();
        final LinearLayout chars = (LinearLayout)top.findViewById(R.id.character_list);
        mainHandle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                if (in.obj instanceof Character){
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder,CharacterEditMain.newInstance((Character)in.obj)).commit();
                }else if(in.obj instanceof ArrayList){
                    chars.removeAllViews();
                    ArrayList<Character> tmp = (ArrayList<Character>)in.obj;
                    for (Character chara:tmp){
                        chars.addView(new CharacterCard().getCard(GMFragment.this,chara,mainHandle,true));
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
        return top;
    }

    public void onStart(){
        super.onStart();
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
                            while ((((SWrpg)getActivity().getApplication()).gac == null ||
                                    !((SWrpg)getActivity().getApplication()).gac.hasConnectedApi(Drive.API) ||
                                    ((SWrpg)getActivity().getApplication()).gac.isConnecting()) && timeout < 33 ||
                                    !((SWrpg)getActivity().getApplication()).initConnect) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                timeout++;
                            }
                            if (timeout < 33) {
                                DriveLoadChars dlc = new DriveLoadChars(GMFragment.this.getActivity());
                                dlc.saveToFile(GMFragment.this.getActivity());
                                System.out.println("Loaded");
                            } else {
                                Message timed = mainHandle.obtainMessage();
                                timed.arg1 = -1;
                                mainHandle.sendMessage(timed);
                            }
                        }
                        LoadChars lc = new LoadChars(GMFragment.this.getActivity());
                        Message tmp = mainHandle.obtainMessage();
                        tmp.obj = lc.chars;
                        tmp.arg1 = -100;
                        mainHandle.sendMessage(tmp);
                    }catch(IllegalStateException ignored){}
                    async = null;
                    return null;
                }
            };
            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        super.onDetach();
        mListener = null;
    }

    public interface OnGMInteractionListener {
        void OnGMInteraction();
    }
}
