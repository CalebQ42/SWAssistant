package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.StarWars.Vehicle;
import com.apps.darkstorm.swrpg.UI.Vehic.SetupVehicEdit;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnVehicleEditInteractionListener {
        // TODO: Update argument type and name
        void onVehicleEditInteraction();
    }
}
