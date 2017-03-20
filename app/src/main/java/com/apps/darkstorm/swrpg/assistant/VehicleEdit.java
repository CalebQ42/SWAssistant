package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.apps.darkstorm.swrpg.assistant.ui.vehicle.SetupVehicEdit;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

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
        return inflater.inflate(R.layout.fragment_vehicle_edit, container, false);
    }

    DirectoryChooserFragment dcf;

    public void onViewCreated(final View top,Bundle saved){
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        Handler handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(getActivity()!= null && vh!=null) {
                    SetupVehicEdit.setup(((LinearLayout) top.findViewById(R.id.main_lay)), getActivity(), vh);
                    vh.showHideCards(top);
                    top.findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                                    .initialDirectory(((SWrpg) getActivity().getApplication()).prefs.getString(getString(R.string.local_location_key),
                                            ((SWrpg) getActivity().getApplication()).defaultLoc)).allowReadOnlyDirectory(false)
                                    .newDirectoryName("SWrpg")
                                    .allowNewDirectoryNameModification(true).build();
                            dcf = DirectoryChooserFragment.newInstance(config);
                            dcf.setTargetFragment(VehicleEdit.this, 0);
                            dcf.setDirectoryChooserListener(new DirectoryChooserFragment.OnFragmentInteractionListener() {
                                @Override
                                public void onSelectDirectory(@NonNull String path) {
                                    vh.save(path + "/" + vh.name+".vhcl");
                                    dcf.dismiss();
                                }
                                @Override
                                public void onCancelChooser() {
                                    dcf.dismiss();
                                }
                            });
                            dcf.setShowsDialog(true);
                            dcf.show(getFragmentManager(), null);
                        }
                    });
                }
            }
        };
        handle.sendEmptyMessage(0);
        top.setFocusableInTouchMode(true);
        top.requestFocus();
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
        if(getActivity()!=null) {
            if (pref.getBoolean(getString(R.string.google_drive_key), false) && ((SWrpg) getActivity().getApplication()).gac != null
                    && vh!=null && ((SWrpg) getActivity().getApplication()).vehicFold != null) {
                vh.startEditing(getActivity(), ((SWrpg) getActivity().getApplication()).vehicFold.getDriveId());
            } else {
                vh.startEditing(getActivity());
            }
        }
    }

    public void onPause(){
        super.onPause();
        vh.stopEditing();
    }

    public interface OnVehicleEditInteractionListener {
    }
}
