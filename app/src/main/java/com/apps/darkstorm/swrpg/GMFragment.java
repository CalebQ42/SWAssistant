package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.content.Context;
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

import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.StarWars.Minion;

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
        mainHandle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                if (in.obj instanceof Character){
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder,CharacterEditMain.newInstance((Character)in.obj)).commit();
                }else if(in.obj instanceof Minion){
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_holder,MinionEditMain.newInstance((Minion)in.obj)).commit();
                }
            }
        };
        getChildFragmentManager().beginTransaction().replace(R.id.character_list,MinCharList.newInstance(mainHandle)).commit();
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
