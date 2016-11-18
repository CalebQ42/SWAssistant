package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.StarWars.Vehicle;
import com.apps.darkstorm.swrpg.UI.Vehic.SetupVehicEdit;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

public class VehicleEdit extends Fragment {
    private OnVehicleEditInteractionListener mListener;

    Vehicle vh;
    GoogleApiClient gac = null;

    public VehicleEdit() {}

    public static VehicleEdit newInstance(Vehicle vh, GoogleApiClient gac) {
        VehicleEdit fragment = new VehicleEdit();
        fragment.vh = vh;
        fragment.gac = gac;
        return fragment;
    }

    public static VehicleEdit newInstance(int ID, GoogleApiClient gac) {
        VehicleEdit fragment = new VehicleEdit();
        fragment.vh = new Vehicle(ID);
        fragment.gac = gac;
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
        SetupVehicEdit.setup(top,vh);
        vh.showHideCards(top);
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

    public void onStart(){
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.cloud_key),false) && gac != null){
            vh.startEditing(getContext(),gac,
                    DriveId.decodeFromString(pref.getString(getString(R.string.swchars_id_key),"")));
        }else{
            vh.startEditing(getContext());
        }
    }

    public void onPause(){
        super.onPause();
        vh.stopEditing();
    }

    public void onStop(){
        super.onStop();
        vh.stopEditing();
    }

    public void onDestroy(){
        super.onDestroy();
        vh.stopEditing();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnVehicleEditInteractionListener {
        void onVehicleEditInteraction();
    }
}
