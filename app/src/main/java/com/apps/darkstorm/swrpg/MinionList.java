package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.StarWars.DriveLoadMinions;
import com.apps.darkstorm.swrpg.StarWars.LoadMinions;
import com.apps.darkstorm.swrpg.StarWars.Minion;
import com.apps.darkstorm.swrpg.UI.MinionCard;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.drive.Drive;

import java.util.ArrayList;

public class MinionList extends Fragment {

    private OnMinionListInteractionListener mListener;

    ArrayList<Minion> minions;
    AsyncTask<Void,Void,Void> async;
    Handler topHandle;
    boolean gm = false;

    public MinionList() {
    }

    public static MinionList newInstance(Handler top,boolean gm) {
        MinionList fragment = new MinionList();
        fragment.topHandle = top;
        fragment.gm = gm;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Handler mainHandle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_minion_list, container, false);
        minions = new ArrayList<>();
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout)top.findViewById(R.id.swipe_refresh);
        mainHandle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                fab.setEnabled(true);
                if (in.obj instanceof Minion) {
                    Minion tmp = (Minion)in.obj;
                    for (int i = 0; i < minions.size(); i++) {
                        if (minions.get(i).ID == tmp.ID) {
                            ((LinearLayout) top.findViewById(R.id.mainLay)).removeViewAt(i);
                            minions.remove(i);
                            break;
                        }
                    }
                }else if(in.obj instanceof ArrayList){
                    ArrayList<Minion> charsNew = (ArrayList<Minion>)in.obj;
                    minions = charsNew;
                    ((LinearLayout)top.findViewById(R.id.mainLay)).removeAllViews();
                    if (MinionList.this.getContext() != null) {
                        for (Minion minion : minions) {
                            ((LinearLayout) top.findViewById(R.id.mainLay)).addView(new MinionCard()
                                    .getCard(MinionList.this, minion, mainHandle, false,topHandle));
                        }
                    }
                    Message snack = mainHandle.obtainMessage();
                    snack.arg1 = -100;
                    mainHandle.sendMessage(snack);
                }
                if(in.arg1 == 100){
                    refresh.setRefreshing(true);
                    ((LinearLayout)top.findViewById(R.id.mainLay)).removeAllViews();
                    fab.setEnabled(false);
                }else if(in.arg1 == -100){
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
                                            while ((((SWrpg)getActivity().getApplication()).gac == null ||
                                                    !((SWrpg)getActivity().getApplication()).initConnect ||
                                                    !((SWrpg)getActivity().getApplication()).gac.hasConnectedApi(Drive.API) ||
                                                    ((SWrpg)getActivity().getApplication()).gac.isConnecting()) && timeout < 33) {
                                                try {
                                                    Thread.sleep(300);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                timeout++;
                                            }
                                            if (timeout < 33) {
                                                DriveLoadMinions dlc = new DriveLoadMinions(MinionList.this.getActivity());
                                                dlc.saveToFile(MinionList.this.getActivity());
                                                System.out.println("Loaded");
                                            } else {
                                                Message timed = mainHandle.obtainMessage();
                                                timed.arg1 = -1;
                                                mainHandle.sendMessage(timed);
                                            }
                                        }
                                        LoadMinions lc = new LoadMinions(MinionList.this.getActivity());
                                        Message tmp = mainHandle.obtainMessage();
                                        tmp.obj = lc.minions;
                                        mainHandle.sendMessage(tmp);
                                    }catch(IllegalStateException ignored){}
                                    async = null;
                                    return null;
                                }
                            };
                            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                                while ((((SWrpg)getActivity().getApplication()).gac == null ||
                                        !((SWrpg)getActivity().getApplication()).initConnect ||
                                        !((SWrpg)getActivity().getApplication()).gac.hasConnectedApi(Drive.API) ||
                                        ((SWrpg)getActivity().getApplication()).gac.isConnecting()) && timeout < 33) {
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    timeout++;
                                }
                                if (timeout < 33) {
                                    DriveLoadMinions dlc = new DriveLoadMinions(MinionList.this.getActivity());
                                    dlc.saveToFile(MinionList.this.getActivity());
                                    System.out.println("Loaded");
                                } else {
                                    Message timed = mainHandle.obtainMessage();
                                    timed.arg1 = -1;
                                    mainHandle.sendMessage(timed);
                                }
                            }
                            LoadMinions lc = new LoadMinions(MinionList.this.getActivity());
                            Message tmp = mainHandle.obtainMessage();
                            tmp.obj = lc.minions;
                            mainHandle.sendMessage(tmp);
                        }catch(IllegalStateException ignored){}
                        async = null;
                        return null;
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                        while (!((SWrpg)getActivity().getApplication()).initConnect && timeout < 33) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            timeout++;
                        }
                        if (timeout < 33) {
                            DriveLoadMinions dlc = new DriveLoadMinions(MinionList.this.getActivity());
                            dlc.saveToFile(MinionList.this.getActivity());
                        } else {
                            Message timed = mainHandle.obtainMessage();
                            timed.arg1 = -1;
                            mainHandle.sendMessage(timed);
                        }
                    }
                    LoadMinions lc = new LoadMinions(MinionList.this.getActivity());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.minions;
                    mainHandle.sendMessage(tmp);
                }catch(IllegalStateException ignored){}
                async = null;
                return null;
            }
        };
        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.ads_key),true) && !gm) {
            AdView ads = new AdView(getContext());
            ads.setAdSize(AdSize.BANNER);
            LinearLayout.LayoutParams adLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            adLayout.weight = 0;
            adLayout.topMargin = (int)(5*getResources().getDisplayMetrics().density);
            adLayout.gravity = Gravity.CENTER_HORIZONTAL;
            ads.setLayoutParams(adLayout);
            if (BuildConfig.APPLICATION_ID.equals("com.apps.darkstorm.swrpg"))
                ads.setAdUnitId(getString(R.string.free_banner_ad_id));
            else
                ads.setAdUnitId(getString(R.string.paid_banner_ad_id));
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
            ads.loadAd(adRequest);
            LinearLayout topLinLay = (LinearLayout)top.findViewById(R.id.top_lay);
            topLinLay.addView(ads,0);
        }
        return top;
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
        void onMinionListInteraction();
    }
}
