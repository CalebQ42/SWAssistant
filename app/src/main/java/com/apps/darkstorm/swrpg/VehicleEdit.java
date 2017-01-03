package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.StarWars.Vehicle;
import com.apps.darkstorm.swrpg.UI.Vehic.SetupVehicEdit;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.drive.DriveId;

public class VehicleEdit extends Fragment {
    private OnVehicleEditInteractionListener mListener;

    Vehicle vh;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_vehicle_edit, container, false);
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        fab.hide();
        SetupVehicEdit.setup(top,vh);
        vh.showHideCards(top);
        top.setFocusableInTouchMode(true);
        top.requestFocus();
        if (getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.ads_key),true)) {
//            AdView ads = new AdView(getContext());
//            ads.setAdSize(AdSize.BANNER);
//            LinearLayout.LayoutParams adLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//            adLayout.weight = 0;
//            adLayout.topMargin = (int)(5*getResources().getDisplayMetrics().density);
//            adLayout.gravity = Gravity.CENTER_HORIZONTAL;
//            ads.setLayoutParams(adLayout);
//            if (BuildConfig.APPLICATION_ID.equals("com.apps.darkstorm.swrpg"))
//                ads.setAdUnitId(getString(R.string.free_banner_ad_id));
//            else
//                ads.setAdUnitId(getString(R.string.paid_banner_ad_id));
//            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
//            ads.loadAd(adRequest);
//            LinearLayout topLinLay = (LinearLayout)top.findViewById(R.id.top_lay);
//            topLinLay.addView(ads,0);
        }
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
        if (pref.getBoolean(getString(R.string.cloud_key),false) && ((SWrpg)getActivity().getApplication()).gac != null){
            vh.startEditing(getActivity(),
                    DriveId.decodeFromString(pref.getString(getString(R.string.ships_id_key),"")));
        }else{
            vh.startEditing(getActivity());
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
