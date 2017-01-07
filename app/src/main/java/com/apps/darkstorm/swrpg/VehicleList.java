package com.apps.darkstorm.swrpg;

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

import com.apps.darkstorm.swrpg.load.DriveLoadVehicles;
import com.apps.darkstorm.swrpg.load.LoadVehicles;
import com.apps.darkstorm.swrpg.sw.Vehicle;
import com.apps.darkstorm.swrpg.ui.cards.VehicleCard;

import java.util.ArrayList;

public class VehicleList extends Fragment {
    private OnVehicleListInteractionListener mListener;
    SWrpg app;
    Handler handle;
    ArrayList<Vehicle> vehicles = new ArrayList<>();

    public VehicleList() {}

    public static VehicleList newInstance() {
        VehicleList fragment = new VehicleList();
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
                loadVehicles();
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
                    ArrayList<Vehicle> chars = (ArrayList<Vehicle>)msg.obj;
                    if(!chars.equals(vehicles)) {
                        linLay.removeAllViews();
                        vehicles = chars;
                        for(Vehicle chara:chars){
                            linLay.addView(VehicleCard.getCard(getActivity(),linLay,chara,handle));
                        }
                    }
                }
                if (msg.obj instanceof Vehicle){
                    if (msg.arg1==-1){
                        int ind = vehicles.indexOf(msg.obj);
                        if(ind != -1){
                            linLay.removeViewAt(ind);
                            vehicles.remove(ind);
                        }
                    }else{
                        getFragmentManager().beginTransaction().replace(R.id.content_main,
                                VehicleEdit.newInstance((Vehicle)msg.obj))
                                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack("").commit();
                    }
                }
            }
        };
        return top;
    }

    public void onResume(){
        super.onResume();
        loadVehicles();
    }

    public void loadVehicles(){
        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Message dal = handle.obtainMessage();
                dal.arg1 = 20;
                handle.sendMessage(dal);
                if(ContextCompat.checkSelfPermission(VehicleList.this.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    while(app.askingPerm){
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(ContextCompat.checkSelfPermission(VehicleList.this.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    if(app.prefs.getBoolean(getString(R.string.cloud_key),false)){
                        DriveLoadVehicles dlc = new DriveLoadVehicles(getActivity());
                        if(dlc.vehicles!=null){
                            dlc.saveLocal(getActivity());
                        }
                    }
                    LoadVehicles lc = new LoadVehicles(getActivity());
                    Message out = handle.obtainMessage();
                    out.obj = lc.vehicles;
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

    public interface OnVehicleListInteractionListener {
    }
}
