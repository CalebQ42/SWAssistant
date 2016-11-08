package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.UI.NoteCard;


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
        final FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NoteCard().newNote(top,top.getContext(),((LinearLayout)top.findViewById(R.id.notes_main)),((LinearLayout)top.findViewById(R.id.notes_edit)),chara,fab);
            }
        });
//        top.setFocusableInTouchMode(true);
//        top.requestFocus();
        for (int i =0;i<chara.nts.size();i++) {
            ((LinearLayout)top.findViewById(R.id.notes_main)).addView(new NoteCard().NoteCard(top,top.getContext(),
                    ((LinearLayout)top.findViewById(R.id.notes_main)),((LinearLayout)top.findViewById(R.id.notes_edit)),
                    chara,chara.nts.get(i),fab));
        }
        fab.show();
        top.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    System.out.println("StartingNotes!");
                    chara.startEditing(top.getContext());
                }else{
                    System.out.println("HelloNotes!");
                    chara.stopEditing();
                }
            }
        });
        return top;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onNoteInteraction();
        }
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
    public interface OnNoteInteractionListener {
        void onNoteInteraction();
    }
}
