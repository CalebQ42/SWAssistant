package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.ui.character.SetupCharAttr;

import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooserDialog;

public class CharacterEditAttributes extends Fragment {
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
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_edit_attributes, container, false);
    }

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
                            FileChooserDialog.Builder build = new FileChooserDialog.Builder(FileChooserDialog.ChooserType.DIRECTORY_CHOOSER,
                                    new FileChooserDialog.ChooserListener() {
                                @Override
                                public void onSelect(String path) {
                                    chara.save(path+"/"+chara.name+".char");
                                }
                            }).setTitle(getString(R.string.export))
                                    .setSelectDirectoryButtonText(getString(R.string.select));
                            try {
                                build.build().show(getChildFragmentManager(), null);
                            } catch (ExternalStorageNotAvailableException e) {
                                e.printStackTrace();
                            }
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
