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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.load.DriveLoadCharacters;
import com.apps.darkstorm.swrpg.load.LoadCharacters;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.ui.cards.CharacterCard;

import java.util.ArrayList;

public class CharacterList extends Fragment {

    private OnCharacterListInteractionListener mListener;
    private Handler handle;
    private Handler topHandle;
    ArrayList<Character> characters = new ArrayList<>();


    public CharacterList() {}

    public static CharacterList newInstance(Handler topHandle) {
        CharacterList fragment = new CharacterList();
        fragment.topHandle = topHandle;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.general_list, container, false);
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout)top.findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadChars();
            }
        });
        TypedValue primary = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary,primary,true);
        TypedValue accent = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent,accent,true);
        refresh.setColorSchemeResources(primary.resourceId,accent.resourceId);
        final LinearLayout linLay = (LinearLayout)top.findViewById(R.id.main_lay);
        handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==20){
                    refresh.setRefreshing(true);
                }else if(msg.arg1==-20){
                    refresh.setRefreshing(false);
                }else if(msg.arg1==5){
                    if (top != null)
                        Snackbar.make(top,R.string.cloud_fail,Snackbar.LENGTH_LONG).show();
                }
                if (msg.obj instanceof ArrayList){
                    ArrayList<Character> chars = (ArrayList<Character>)msg.obj;
                    if(!chars.equals(characters)||linLay.getChildCount()!=chars.size()) {
                        characters = chars;
                        linLay.removeAllViews();
                        for (int i = 0;i<chars.size();i++){
                            if(chars.get(i) == null)
                                chars.remove(i);
                        }
                        for(Character chara:chars)
                            linLay.addView(CharacterCard.getCard(getActivity(),linLay,chara,handle));
                    }
                }
                if (msg.obj instanceof Character){
                    if (msg.arg1==-1){
                        int ind = characters.indexOf(msg.obj);
                        Character min = (Character)msg.obj;
                        min.delete(getActivity());
                        if(ind != -1){
                            linLay.removeViewAt(ind);
                            characters.remove(ind);
                        }
                    }else{
                        Message out = topHandle.obtainMessage();
                        out.arg1 = msg.arg1;
                        out.obj = msg.obj;
                        topHandle.sendMessage(out);
                    }
                }
            }
        };
        return top;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCharacterListInteractionListener) {
            mListener = (OnCharacterListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void loadChars(){
        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if(handle!= null) {
                    Message dal = handle.obtainMessage();
                    dal.arg1 = 20;
                    handle.sendMessage(dal);
                }
                if(getContext()!= null) {
                    if (ContextCompat.checkSelfPermission(CharacterList.this.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        while (((SWrpg) getActivity().getApplication()).askingPerm) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (ContextCompat.checkSelfPermission(CharacterList.this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        if (((SWrpg) getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key), false)) {
                            int timeout = 0;
                            while ((((SWrpg) getActivity().getApplication()).gac == null ||
                                    !((SWrpg) getActivity().getApplication()).gac.isConnected()) && timeout < 50) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                timeout++;
                                if (getActivity() == null)
                                    break;
                            }
                            if (getActivity() != null) {
                                if (timeout != 50) {
                                    DriveLoadCharacters dlc = new DriveLoadCharacters(getActivity());
                                    if (dlc.characters != null) {
                                        dlc.saveLocal(getActivity());
                                    }
                                } else {
                                    Message out = handle.obtainMessage();
                                    out.arg1 = 5;
                                    handle.sendMessage(out);
                                }
                            }
                        }
                        if (getActivity() != null) {
                            LoadCharacters lc = new LoadCharacters(getActivity());
                            Message out = handle.obtainMessage();
                            out.obj = lc.characters;
                            handle.sendMessage(out);
                        }
                    }
                }
                if (handle!=null) {
                    Message out = handle.obtainMessage();
                    out.arg1 = -20;
                    handle.sendMessage(out);
                }
                return null;
            }
        };
        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onResume(){
        super.onResume();
        loadChars();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCharacterListInteractionListener {
    }
}
