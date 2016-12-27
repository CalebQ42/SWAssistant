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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.StarWars.DriveLoadVehics;
import com.apps.darkstorm.swrpg.StarWars.LoadVehics;
import com.apps.darkstorm.swrpg.StarWars.Vehicle;
import com.apps.darkstorm.swrpg.UI.Vehic.VehicleCard;
import com.google.android.gms.drive.Drive;

import java.util.ArrayList;

public class VehicleList extends Fragment {

    ArrayList<Vehicle> vhs;
    Handler mainHandle;
    AsyncTask<Void,Void,Void> async;

    private OnVehicleListInteractionListener mListener;

    public VehicleList() {}

    public static VehicleList newInstance() {
        VehicleList fragment = new VehicleList();
        fragment.vhs = new ArrayList<>();
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
        final View top = inflater.inflate(R.layout.fragment_vehicle_list, container, false);
        top.setFocusableInTouchMode(true);
        top.requestFocus();
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout)top.findViewById(R.id.swipe_refresh);
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        final SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        fab.show();
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getBoolean(getString(R.string.cloud_key),false) && pref.getBoolean(getString(R.string.sync_key),true) &&
                        (((SWrpg)getActivity().getApplication()).gac == null || !((SWrpg)getActivity().getApplication()).gac.isConnected())){
                    Snackbar snackbar = Snackbar.make(top,R.string.cloud_fail,Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    ArrayList<Integer> has = new ArrayList<>();
                    for (Vehicle vh : vhs) {
                        has.add(vh.ID);
                    }
                    int max = 0;
                    while (has.contains(max)) {
                        max++;
                    }
                    getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.content_navigation, VehicleEdit.newInstance(max))
                            .addToBackStack("Creating a new Character").commit();
                    fab.hide();
                }
            }
        });
        mainHandle = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message in){
                if (in.obj instanceof ArrayList){
                    ArrayList<Vehicle> inList = (ArrayList<Vehicle>)in.obj;
                    System.out.println("Something " + String.valueOf(inList.size()));
                    vhs = inList;
                    ((LinearLayout)top.findViewById(R.id.main_list)).removeAllViews();
                    for (int i = 0;i<vhs.size();i++){
                        ((LinearLayout)top.findViewById(R.id.main_list)).addView(VehicleCard.card(VehicleList.this,vhs.get(i),mainHandle));
                    }
                    Message snack = mainHandle.obtainMessage();
                    snack.arg1 = -100;
                    mainHandle.sendMessage(snack);
                }else if (in.obj instanceof Vehicle){
                    for (int i = 0;i<vhs.size();i++){
                        if (vhs.get(i).equals(in.obj)){
                            vhs.remove(i);
                            ((LinearLayout)top.findViewById(R.id.main_list)).removeViewAt(i);
                        }
                    }
                }
                if(in.arg1 == 100){
                    refresh.setRefreshing(true);
                    ((LinearLayout)top.findViewById(R.id.main_list)).removeAllViews();
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
                                            while ((((SWrpg)getActivity().getApplication()).gac == null||
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
                                                DriveLoadVehics dlc = new DriveLoadVehics(VehicleList.this.getActivity());
                                                dlc.saveToFile(VehicleList.this.getActivity());
                                            } else {
                                                Message timed = mainHandle.obtainMessage();
                                                timed.arg1 = -1;
                                                mainHandle.sendMessage(timed);
                                            }
                                        }
                                        LoadVehics lc = new LoadVehics(VehicleList.this.getActivity());
                                        Message tmp = mainHandle.obtainMessage();
                                        tmp.obj = lc.vehics;
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
                                while ((((SWrpg)getActivity().getApplication()).gac == null||
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
                                    DriveLoadVehics dlc = new DriveLoadVehics(VehicleList.this.getActivity());
                                    dlc.saveToFile(VehicleList.this.getActivity());
                                } else {
                                    Message timed = mainHandle.obtainMessage();
                                    timed.arg1 = -1;
                                    mainHandle.sendMessage(timed);
                                }
                            }
                            LoadVehics lc = new LoadVehics(VehicleList.this.getActivity());
                            Message tmp = mainHandle.obtainMessage();
                            tmp.obj = lc.vehics;
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
                        while ((((SWrpg)getActivity().getApplication()).gac == null||
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
                            DriveLoadVehics dlc = new DriveLoadVehics(VehicleList.this.getActivity());
                            dlc.saveToFile(VehicleList.this.getActivity());
                        } else {
                            Message timed = mainHandle.obtainMessage();
                            timed.arg1 = -1;
                            mainHandle.sendMessage(timed);
                        }
                    }
                    LoadVehics lc = new LoadVehics(VehicleList.this.getActivity());
                    Message tmp = mainHandle.obtainMessage();
                    tmp.obj = lc.vehics;
                    mainHandle.sendMessage(tmp);
                }catch(IllegalStateException ignored){}
                async = null;
                return null;
            }
        };
        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        if (context instanceof OnVehicleListInteractionListener) {
            mListener = (OnVehicleListInteractionListener) context;
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
                            while ((((SWrpg)getActivity().getApplication()).gac == null||
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
                                DriveLoadVehics dlc = new DriveLoadVehics(VehicleList.this.getActivity());
                                dlc.saveToFile(VehicleList.this.getActivity());
                            } else {
                                Message timed = mainHandle.obtainMessage();
                                timed.arg1 = -1;
                                mainHandle.sendMessage(timed);
                            }
                        }
                        LoadVehics lc = new LoadVehics(VehicleList.this.getActivity());
                        Message tmp = mainHandle.obtainMessage();
                        tmp.obj = lc.vehics;
                        mainHandle.sendMessage(tmp);
                    }catch(IllegalStateException ignored){}
                    async = null;
                    return null;
                }
            };
            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public interface OnVehicleListInteractionListener {
        void onVehicleListInteraction();
    }
}
