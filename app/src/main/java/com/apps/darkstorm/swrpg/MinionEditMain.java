package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.ui.SetupMinionAttr;

public class MinionEditMain extends Fragment {

    private OnMinionEditInteractionListener mListener;
    private Minion minion;
    SWrpg app;

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
        app = (SWrpg)getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_minion_edit_main, container, false);
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        SetupMinionAttr.setup((LinearLayout)top.findViewById(R.id.main_lay),getActivity(),minion);
        minion.showHideCards(top);
        top.setFocusableInTouchMode(true);
        top.requestFocus();
        return top;
    }

    public void onResume(){
        super.onResume();
        if (app.prefs.getBoolean(getString(R.string.cloud_key),false) && ((SWrpg)getActivity().getApplication()).gac != null){
            minion.startEditing(getActivity(),app.charsFold.getDriveId());
        }else{
            minion.startEditing(getActivity());
        }
    }

    public void onPause(){
        super.onPause();
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
