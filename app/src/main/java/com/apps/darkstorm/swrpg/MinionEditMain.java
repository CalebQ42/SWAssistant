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

import com.apps.darkstorm.swrpg.StarWars.Minion;
import com.apps.darkstorm.swrpg.UI.SetupMinionAttr;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.drive.DriveId;

public class MinionEditMain extends Fragment {

    private OnMinionEditInteractionListener mListener;
    private Minion minion;

    public MinionEditMain() {
    }
    public static MinionEditMain newInstance(Minion minion) {
        MinionEditMain fragment = new MinionEditMain();
        fragment.minion = minion;
        return fragment;
    }

    public static MinionEditMain newInstance(int ID) {
        MinionEditMain fragment = new MinionEditMain();
        fragment.minion = new Minion(ID);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_minion_edit_main, container, false);
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.universeFab);
        fab.hide();
        minion.showHideCards(top);
        new SetupMinionAttr().setup(top,minion);
        if (getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.ads_key),true)) {
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
        top.setFocusableInTouchMode(true);
        top.requestFocus();
        return top;
    }

    public void onStart(){
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.cloud_key),false) && ((SWrpg)getActivity().getApplication()).gac != null){
            minion.startEditing(getActivity(),
                    DriveId.decodeFromString(pref.getString(getString(R.string.swchars_id_key),"")));
        }else{
            minion.startEditing(getActivity());
        }
    }

    public void onPause(){
        super.onPause();
        minion.stopEditing();
    }

    public void onStop(){
        super.onStop();
        minion.stopEditing();
    }

    public void onDestroy(){
        super.onDestroy();
        minion.stopEditing();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMinionEditInteractionListener) {
            mListener = (OnMinionEditInteractionListener) context;
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

    public interface OnMinionEditInteractionListener {
        void OnMinionEditInteractionListener();
    }
}
