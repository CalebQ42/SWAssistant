package com.apps.darkstorm.swrpg;

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

import com.apps.darkstorm.swrpg.load.DriveLoadMinions;
import com.apps.darkstorm.swrpg.load.LoadMinions;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.ui.cards.MinionCard;

import java.util.ArrayList;

public class MinionList extends Fragment {

    private OnMinionListInteractionListener mListener;
    Handler topHandle;
    Handler handle;
    ArrayList<Minion> minions = new ArrayList<>();

    public MinionList() {}

    public static MinionList newInstance(Handler topHandle) {
        MinionList fragment = new MinionList();
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
                loadMinions();
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
                    Snackbar.make(top,R.string.cloud_fail,Snackbar.LENGTH_LONG).show();
                }
                if (msg.obj instanceof ArrayList){
                    ArrayList<Minion> chars = (ArrayList<Minion>)msg.obj;
                    if(!chars.equals(minions)||linLay.getChildCount()!=chars.size()) {
                        linLay.removeAllViews();
                        minions = chars;
                        for(Minion chara:chars){
                            if(getActivity()!=null)
                                linLay.addView(MinionCard.getCard(getActivity(),linLay,chara,handle));
                        }
                    }
                }
                if (msg.obj instanceof Minion){
                    if (msg.arg1==-1){
                        int ind = minions.indexOf(msg.obj);
                        Minion min = (Minion)msg.obj;
                        min.delete(getActivity());
                        if(ind != -1){
                            linLay.removeViewAt(ind);
                            minions.remove(ind);
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
    public void loadMinions(){
        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Message dal = handle.obtainMessage();
                dal.arg1 = 20;
                handle.sendMessage(dal);
                if(ContextCompat.checkSelfPermission(MinionList.this.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    while(((SWrpg)getActivity().getApplication()).askingPerm){
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(ContextCompat.checkSelfPermission(MinionList.this.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                        int timeout = 0;
                        while((((SWrpg)getActivity().getApplication()).gac == null ||
                                !((SWrpg)getActivity().getApplication()).gac.isConnected() ||
                                ((SWrpg)getActivity().getApplication()).charsFold==null)&& timeout< 50){
                            if(!((SWrpg)getActivity().getApplication()).gac.isConnecting())
                                ((MainActivity)getActivity()).gacMaker();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            timeout++;
                        }
                        if(timeout != 50) {
                            DriveLoadMinions dlc = new DriveLoadMinions(getActivity());
                            if (dlc.minions != null) {
                                dlc.saveLocal(getActivity());
                            }
                        }else{
                            Message out = handle.obtainMessage();
                            out.arg1 = 5;
                            handle.sendMessage(out);
                        }
                    }
                    LoadMinions lc = new LoadMinions(getActivity());
                    Message out = handle.obtainMessage();
                    out.obj = lc.minions;
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
        loadMinions();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMinionListInteractionListener) {
            mListener = (OnMinionListInteractionListener) context;
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

    public interface OnMinionListInteractionListener {
    }
}
