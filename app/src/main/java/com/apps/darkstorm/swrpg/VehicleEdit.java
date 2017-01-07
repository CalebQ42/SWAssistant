package com.apps.darkstorm.swrpg;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.sw.Vehicle;
import com.apps.darkstorm.swrpg.ui.vehicle.SetupVehicEdit;

public class VehicleEdit extends Fragment {
    private OnVehicleEditInteractionListener mListener;

    Vehicle vh;
    SWrpg app;

    public VehicleEdit() {}

    public static VehicleEdit newInstance(Vehicle vh) {
        VehicleEdit fragment = new VehicleEdit();
        fragment.vh = vh;
        return fragment;
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
        app = (SWrpg)getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_vehicle_edit, container, false);
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        SetupVehicEdit.setup(((LinearLayout)top.findViewById(R.id.main_lay)),getActivity(),vh);
        vh.showHideCards(top);
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
        if (pref.getBoolean(getString(R.string.cloud_key),false) && ((SWrpg)getActivity().getApplication()).gac != null){
            vh.startEditing(getActivity(),app.vehicFold.getDriveId());
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
