package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.sw.Vehicle;
import com.apps.darkstorm.swrpg.ui.vehicle.SetupVehicEdit;

public class VehicleEdit extends Fragment {
    private OnVehicleEditInteractionListener mListener;

    Vehicle vh;

    public VehicleEdit() {}

    public static VehicleEdit newInstance(Vehicle vh) {
        VehicleEdit fragment = new VehicleEdit();
        fragment.vh = vh;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if(!((SWrpg)getActivity().getApplication()).hasShortcut(vh))
                ((SWrpg)getActivity().getApplication()).addShortcut(vh,getActivity());
            else
                ((SWrpg)getActivity().getApplication()).updateShortcut(vh,getActivity());
        }
    }

    public static VehicleEdit newInstance(int ID) {
        VehicleEdit fragment = new VehicleEdit();
        fragment.vh = new Vehicle(ID);
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
        final View top = inflater.inflate(R.layout.fragment_vehicle_edit, container, false);
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        Handler handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(getActivity()!= null && vh!=null) {
                    SetupVehicEdit.setup(((LinearLayout) top.findViewById(R.id.main_lay)), getActivity(), vh);
                    vh.showHideCards(top);
                }
            }
        };
        handle.sendEmptyMessage(0);
        top.setFocusableInTouchMode(true);
        top.requestFocus();
        return top;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVehicleEditInteractionListener) {
            mListener = (OnVehicleEditInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onResume(){
        super.onResume();
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.google_drive_key),false) && ((SWrpg)getActivity().getApplication()).gac != null){
            vh.startEditing(getActivity(),((SWrpg)getActivity().getApplication()).vehicFold.getDriveId());
        }else{
            vh.startEditing(getActivity());
        }
    }

    public void onPause(){
        super.onPause();
        vh.stopEditing();
    }

    public interface OnVehicleEditInteractionListener {
    }
}
