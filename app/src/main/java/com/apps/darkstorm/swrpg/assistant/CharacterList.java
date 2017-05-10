package com.apps.darkstorm.swrpg.assistant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.drive.Load;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Character;

import java.util.ArrayList;
import java.util.Arrays;

public class CharacterList extends Fragment {

    public CharacterList() {}

    public static CharacterList newInstance() {
        return new CharacterList();
    }

    Handler parentHandle = null;
    public static CharacterList newInstance(Handler parentHandle){
        CharacterList cl = new CharacterList();
        cl.parentHandle = parentHandle;
        return cl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cat_list, container, false);
    }

    ArrayList<Character> characters;
    ArrayList<CharSequence> cats;
    ArrayList<ArrayList<Character>> characterCats;

    StaggeredGridLayoutManager sgl;

    Spinner sp;
    SwipeRefreshLayout srl;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        if(parentHandle == null) {
            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setImageResource(R.drawable.add);
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Integer> IDs = new ArrayList<>();
                    for (Character c : characters) {
                        IDs.add(c.ID);
                    }
                    int ID = 0;
                    while (IDs.contains(ID)) {
                        ID++;
                    }
                    Character ch = new Character(ID);
                    getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(ch))
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("Character Edit").commit();
                    ((FloatingActionButton) getActivity().findViewById(R.id.fab)).hide();
                }
            });
        }
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.characters);
        srl = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        sp = (Spinner)view.findViewById(R.id.cat_spinner);
        cats = new ArrayList<>();
        characterCats = new ArrayList<>();
        final NameCardAdap adap = new NameCardAdap();
        RecyclerView r = (RecyclerView)view.findViewById(R.id.recycler);
        r.setAdapter(adap);
        sgl = new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL);
        r.setLayoutManager(sgl);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adap.charactersAdap = characterCats.get(position);
                adap.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCharacters();
            }
        });
        loadCharacters();
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

    public void loadCharacters(){
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    srl.setRefreshing(true);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    while(!((SWrpg)getActivity().getApplication()).driveFail&&((SWrpg)getActivity().getApplication()).charsFold==null){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if(((SWrpg)getActivity().getApplication()).driveFail) {
                        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                        b.setMessage(R.string.drive_fail);
                        b.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadCharacters();
                                dialog.cancel();
                            }
                        }).setNegativeButton(R.string.dice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFragmentManager().beginTransaction().replace(R.id.content_main,DiceRollFragment.newInstance())
                                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                                dialog.cancel();
                            }
                        });
                        b.setCancelable(false);
                        srl.setRefreshing(false);
                        return;
                    }
                    final Load.Characters ch = new Load.Characters();
                    ch.setOnFinish(new Load.onFinish() {
                        @Override
                        public void finish() {
                            ch.saveLocal(getActivity());
                            characters = ch.characters;
                            cats.clear();
                            characterCats.clear();
                            cats.add("All");
                            characterCats.add(new ArrayList<Character>());
                            for (Character c:ch.characters){
                                if(cats.contains(c.category)){
                                    characterCats.get(cats.indexOf(c.category)).add(c);
                                }else if(!c.category.equals("")){
                                    cats.add(c.category);
                                    characterCats.add(new ArrayList<Character>());
                                    characterCats.get(characterCats.size()-1).add(c);
                                }
                                characterCats.get(0).add(c);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<CharSequence> apAdap = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cats);
                                    sp.setAdapter(apAdap);
                                    srl.setRefreshing(false);
                                    sp.setSelection(0);
                                }
                            });
                        }
                    });
                    ch.load(getActivity());
                }
            };
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            srl.setRefreshing(true);
            characters = new ArrayList<>();
            characters.addAll(Arrays.asList(LoadLocal.characters(getActivity())));
            cats.clear();
            characterCats.clear();
            cats.add("All");
            characterCats.add(new ArrayList<Character>());
            for (Character c:characters){
                if(cats.contains(c.category)){
                    characterCats.get(cats.indexOf(c.category)).add(c);
                }else if(!c.category.equals("")){
                    cats.add(c.category);
                    characterCats.add(new ArrayList<Character>());
                    characterCats.get(characterCats.size()-1).add(c);
                }
                characterCats.get(0).add(c);
            }
            ArrayAdapter<CharSequence> apAdap = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cats);
            sp.setAdapter(apAdap);
            srl.setRefreshing(false);
            sp.setSelection(0);
        }
        if(parentHandle!= null){
            Message msg = parentHandle.obtainMessage();
            msg.obj = characters;
            msg.arg1 = 0;
            parentHandle.sendMessage(msg);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            if(((SWrpg)getActivity().getApplication()).gac==null ||!((SWrpg)getActivity().getApplication()).gac.isConnected()) {
                ((MainDrawer) getActivity()).gacMaker();
            }
        }
    }

    class NameCardAdap extends RecyclerView.Adapter<NameCardAdap.NameCard> {

        ArrayList<Character> charactersAdap = new ArrayList<>();

        @Override
        public NameCard onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView c = (CardView)getActivity().getLayoutInflater().inflate(R.layout.card_name,parent,false);
            final NameCard n = new NameCard(c);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(parentHandle== null) {
                        getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(n.character))
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("Editing").commit();
                        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).hide();
                    }else{
                        Message msg = parentHandle.obtainMessage();
                        msg.obj = n.character;
                        parentHandle.sendMessage(msg);
                    }
                }
            });
            c.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    b.setMessage(R.string.character_delete);
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            characters.remove(n.character);
                            int ind = charactersAdap.indexOf(n.character);
                            if (ind != -1){
                                charactersAdap.remove(ind);
                                NameCardAdap.this.notifyItemRemoved(ind);
                            }
                            for (ArrayList<Character> al:characterCats){
                                al.remove(n.character);
                            }
                            n.character.delete(getActivity());
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
            return n;
        }

        @Override
        public void onBindViewHolder(NameCard holder, int position) {
            ((TextView)holder.c.findViewById(R.id.name)).setText(charactersAdap.get(position).name);
            if (charactersAdap.get(position).career.equals(""))
                holder.c.findViewById(R.id.subname).setVisibility(View.GONE);
            else
                ((TextView) holder.c.findViewById(R.id.subname)).setText(charactersAdap.get(position).career);
            holder.character = charactersAdap.get(position);
        }

        @Override
        public int getItemCount() {
            return charactersAdap.size();
        }

        class NameCard extends RecyclerView.ViewHolder {
            CardView c;
            Character character;
            NameCard(CardView c) {
                super(c);
                this.c = c;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnCharacterListInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnCharacterListInteractionListener");
        }
    }

    interface OnCharacterListInteractionListener {}
}
