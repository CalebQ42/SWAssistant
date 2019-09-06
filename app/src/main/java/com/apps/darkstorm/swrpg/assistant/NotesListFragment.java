package com.apps.darkstorm.swrpg.assistant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesListFragment extends Fragment {

    public NotesListFragment() {}

    public static NotesListFragment newInstance(Editable c) {
        NotesListFragment fragment = new NotesListFragment();
        fragment.c = c;
        return fragment;
    }

    Editable c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_general, container, false);
    }

    StaggeredGridLayoutManager sgl;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note n = new Note();
                c.nts.add(n);
                getFragmentManager().beginTransaction().replace(R.id.content_notes,NoteEdit.newInstance(c,c.nts.size()-1))
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Note Edit").commit();
            }
        });
        RecyclerView r = (RecyclerView)view.findViewById(R.id.recycler);
        final NotesAdap adap = new NotesAdap();
        r.setAdapter(adap);
        sgl = new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL);
        r.setLayoutManager(sgl);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            sgl.setSpanCount(3);
        }else if (((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_LARGE)||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            sgl.setSpanCount(2);
        }else{
            sgl.setSpanCount(1);
        }
    }

    class NotesAdap extends RecyclerView.Adapter<NotesAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final CardView card = (CardView)getActivity().getLayoutInflater().inflate(R.layout.card_name,parent,false);
            final ViewHolder vh = new ViewHolder(card);
            card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    b.setMessage(R.string.noted_delete);
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int ind = c.nts.remove(vh.nt);
                            if (ind != -1)
                                NotesAdap.this.notifyItemRemoved(ind);
                            dialog.cancel();
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    b.show();
                    return true;
                }
            });
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().replace(R.id.content_notes,NoteEdit.newInstance(c,c.nts.indexOf(vh.nt)))
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Note Edit").commit();
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((TextView)holder.c.findViewById(R.id.name)).setText(c.nts.get(position).title);
            holder.c.findViewById(R.id.subname).setVisibility(View.GONE);
            holder.nt = c.nts.get(position);
        }

        @Override
        public int getItemCount() {
            if (c!= null && c.nts!=null)
                return c.nts.size();
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            CardView c;
            Note nt;
            ViewHolder(CardView c){
                super(c);
                this.c = c;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            sgl.setSpanCount(3);
        }else if (((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_LARGE)||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            sgl.setSpanCount(2);
        }else{
            sgl.setSpanCount(1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnEditInteractionListener");
        }
    }

    interface OnFragmentInteractionListener {}
}
