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
import com.apps.darkstorm.swrpg.ui.NoteCard;


public class CharacterEditNotes extends Fragment {

    Character chara;

    private OnNoteInteractionListener mListener;

    public CharacterEditNotes() {}
    public static CharacterEditNotes newInstance(Character chara) {
        CharacterEditNotes fragment = new CharacterEditNotes();
        fragment.chara = chara;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_character_edit_notes, container, false);
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.setImageResource(R.drawable.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NoteCard().newNote(top,top.getContext(),((LinearLayout)top.findViewById(R.id.notes_main)),((LinearLayout)top.findViewById(R.id.notes_edit)),chara,fab);
            }
        });
        for (int i =0;i<chara.nts.size();i++) {
            ((LinearLayout)top.findViewById(R.id.notes_main)).addView(new NoteCard().NoteCard(top,top.getContext(),
                    ((LinearLayout)top.findViewById(R.id.notes_main)),((LinearLayout)top.findViewById(R.id.notes_edit)),
                    chara,chara.nts.get(i),fab));
        }
        return top;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteInteractionListener) {
            mListener = (OnNoteInteractionListener) context;
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
    public interface OnNoteInteractionListener {}
}
