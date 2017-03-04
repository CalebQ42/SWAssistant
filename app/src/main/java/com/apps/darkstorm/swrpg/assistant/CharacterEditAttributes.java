package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.ui.character.SetupCharAttr;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

public class CharacterEditAttributes extends Fragment{
    private OnCharEditInteractionListener mListener;
    public Character chara;
    Handler handle;
    public CharacterEditAttributes() {}

    public static CharacterEditAttributes newInstance(Character chara){
        CharacterEditAttributes fragment = new CharacterEditAttributes();
        fragment.chara = chara;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_edit_attributes, container, false);
    }

    DirectoryChooserFragment dcf;

    public void onViewCreated(final View top,Bundle saved){
        final LinearLayout linLay = (LinearLayout)top.findViewById(R.id.main_lay);
        handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(getActivity()!= null&&chara!=null) {
                    SetupCharAttr.setup(linLay, getActivity(), chara);
                    chara.showHideCards(top);
                    top.findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                                    .initialDirectory(((SWrpg) getActivity().getApplication()).prefs.getString(getString(R.string.local_location_key),
                                            ((SWrpg) getActivity().getApplication()).defaultLoc)).allowReadOnlyDirectory(false)
                                    .allowNewDirectoryNameModification(true).newDirectoryName("SWrpg").build();
                            dcf = DirectoryChooserFragment.newInstance(config);
                            dcf.setTargetFragment(CharacterEditAttributes.this, 0);
                            dcf.setDirectoryChooserListener(new DirectoryChooserFragment.OnFragmentInteractionListener() {
                                @Override
                                public void onSelectDirectory(@NonNull String path) {
                                    chara.save(path + "/" + chara.name+".chara");
                                    dcf.dismiss();
                                }
                                @Override
                                public void onCancelChooser() {
                                    dcf.dismiss();
                                }
                            });
                            dcf.setShowsDialog(true);
                            dcf.show(getFragmentManager(), null);
                            System.out.println("Hello");
                        }
                    });
                }
            }
        };
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        handle.sendEmptyMessage(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCharEditInteractionListener) {
            mListener = (OnCharEditInteractionListener) context;
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

    public interface OnCharEditInteractionListener {}
}
