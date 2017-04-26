package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NoteEdit extends Fragment {
    public NoteEdit() {}

    public static NoteEdit newInstance(com.apps.darkstorm.swrpg.assistant.sw.Editable c, int ind) {
        NoteEdit fragment = new NoteEdit();
        fragment.c = c;
        fragment.ind = ind;
        return fragment;
    }

    com.apps.darkstorm.swrpg.assistant.sw.Editable c;
    int ind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((FloatingActionButton)getActivity().findViewById(R.id.fab)).hide();
        EditText title = (EditText)view.findViewById(R.id.note_title_edit);
        title.setText(c.nts.get(ind).title);
        title.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                c.nts.get(ind).title = s.toString();
            }
        });
        EditText body = (EditText)view.findViewById(R.id.note_edit);
        body.setText(c.nts.get(ind).note);
        body.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                c.nts.get(ind).note = s.toString();
                System.out.println("Changed");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnNoteEditInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnNoteEditInteractionListener");
        }
    }

    public interface OnNoteEditInteractionListener {}
}
