package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
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

import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.ui.SetupMinionAttr;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if(!((SWrpg)getActivity().getApplication()).hasShortcut(minion))
                ((SWrpg)getActivity().getApplication()).addShortcut(minion,getActivity());
            else
                ((SWrpg)getActivity().getApplication()).updateShortcut(minion,getActivity());
        }
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
        return inflater.inflate(R.layout.fragment_minion_edit_main, container, false);
    }

    DirectoryChooserFragment dcf;

    public void onViewCreated(final View top,Bundle saved){
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        Handler handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(getActivity()!= null&& minion!=null) {
                    SetupMinionAttr.setup((LinearLayout) top.findViewById(R.id.main_lay), getActivity(), minion);
                    minion.showHideCards(top);
                    top.findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                                    .initialDirectory(((SWrpg) getActivity().getApplication()).prefs.getString(getString(R.string.local_location_key),
                                            ((SWrpg) getActivity().getApplication()).defaultLoc)).allowReadOnlyDirectory(false)
                                    .newDirectoryName("SWrpg")
                                    .allowNewDirectoryNameModification(true).build();
                            dcf = DirectoryChooserFragment.newInstance(config);
                            dcf.setTargetFragment(MinionEditMain.this, 0);
                            dcf.setDirectoryChooserListener(new DirectoryChooserFragment.OnFragmentInteractionListener() {
                                @Override
                                public void onSelectDirectory(@NonNull String path) {
                                    minion.save(path + "/" + minion.name+".minion");
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

    public void onResume(){
        super.onResume();
        if(getActivity()!=null) {
            if (((SWrpg) getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key), false) &&
                    ((SWrpg) getActivity().getApplication()).gac != null
                    && minion!=null && ((SWrpg) getActivity().getApplication()).charsFold!=null) {
                minion.startEditing(getActivity(), ((SWrpg) getActivity().getApplication()).charsFold.getDriveId());
            } else {
                minion.startEditing(getActivity());
            }
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
    }
}
