package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.ui.character.SetupCharAttr;

public class CharacterEditAttributes extends Fragment {
    private OnCharEditInteractionListener mListener;
    public Character chara;
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
        final View top = inflater.inflate(R.layout.fragment_character_edit_attributes, container, false);
        SetupCharAttr.setup((LinearLayout)top.findViewById(R.id.main_lay),getActivity(),chara);
        chara.showHideCards(top);
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        return top;
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
