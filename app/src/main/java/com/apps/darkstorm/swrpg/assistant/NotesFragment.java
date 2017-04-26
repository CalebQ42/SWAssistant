package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.darkstorm.swrpg.assistant.sw.Editable;

public class NotesFragment extends Fragment {
    public NotesFragment() {}

    public static NotesFragment newInstance(Editable c) {
        NotesFragment fragment = new NotesFragment();
        fragment.c = c;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    Editable c;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getChildFragmentManager().beginTransaction().replace(R.id.content_notes,NotesListFragment.newInstance(c)).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnCharacterEditInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {}
}
