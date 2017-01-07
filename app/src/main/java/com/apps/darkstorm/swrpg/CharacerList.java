package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.load.DriveLoadCharacters;
import com.apps.darkstorm.swrpg.load.LoadCharacters;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.ui.cards.CharacterCard;

import java.util.ArrayList;

public class CharacerList extends Fragment {

    private OnCharacterListInteractionListener mListener;
    private Handler handle;
    private Handler topHandle;
    ArrayList<Character> characters = new ArrayList<>();
    SWrpg app;


    public CharacerList() {}

    public static CharacerList newInstance(Handler topHandle) {
        CharacerList fragment = new CharacerList();
        fragment.topHandle = topHandle;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (SWrpg)getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.general_list, container, false);
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout)top.findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadChars();
            }
        });
        final LinearLayout linLay = (LinearLayout)top.findViewById(R.id.main_lay);
        handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==20){
                    refresh.setRefreshing(true);
                }else if(msg.arg1==-20){
                    refresh.setRefreshing(false);
                }
                if (msg.obj instanceof ArrayList){
                    ArrayList<Character> chars = (ArrayList<Character>)msg.obj;
                    if(!chars.equals(characters)) {
                        linLay.removeAllViews();
                        characters = chars;
                        for(Character chara:chars){
                            linLay.addView(CharacterCard.getCard(getActivity(),linLay,chara,handle));
                        }
                    }
                }
                if (msg.obj instanceof Character){
                    if (msg.arg1==-1){
                        int ind = characters.indexOf(msg.obj);
                        if(ind != -1){
                            linLay.removeViewAt(ind);
                            characters.remove(ind);
                        }
                    }else{
                        topHandle.sendMessage(msg);
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
                Message dal = handle.obtainMessage();
                dal.arg1 = 20;
                handle.sendMessage(dal);
                if(ContextCompat.checkSelfPermission(CharacerList.this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    while(app.askingPerm){
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(ContextCompat.checkSelfPermission(CharacerList.this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    if(app.prefs.getBoolean(getString(R.string.cloud_key),false)){
                        DriveLoadCharacters dlc = new DriveLoadCharacters(getActivity());
                        if(dlc.characters!=null){
                            dlc.saveLocal(getActivity());
                        }
                    }
                    LoadCharacters lc = new LoadCharacters(getActivity());
                    Message out = handle.obtainMessage();
                    out.obj = lc.characters;
                    handle.sendMessage(out);
                }
                Message out = handle.obtainMessage();
                out.arg1 = -20;
                handle.sendMessage(out);
                return null;
            }
        };
        async.execute();
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
