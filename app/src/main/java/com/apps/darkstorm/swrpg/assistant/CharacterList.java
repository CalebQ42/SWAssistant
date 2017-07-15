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
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
    NameCardAdap adap;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        if(parentHandle == null) {
            if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true)) {
                AdView ads = (AdView)view.findViewById(R.id.adView);
                ads.setVisibility(View.VISIBLE);
                AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").addKeyword("Tabletop Roleplay").addKeyword("RPG").build();
                ads.loadAd(adRequest);
            }
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
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.characters);
        }
        srl = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        sp = (Spinner)view.findViewById(R.id.cat_spinner);
        cats = new ArrayList<>();
        cats.add("All");
        characterCats = new ArrayList<>();
        characterCats.add(new ArrayList<Character>());
        characters = new ArrayList<>();
        adap = new NameCardAdap();
        adap.setHasStableIds(true);
        RecyclerView r = (RecyclerView)view.findViewById(R.id.recycler);
        r.setAdapter(adap);
        sgl = new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL);
        r.setLayoutManager(sgl);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adap.cat = position;
                adap.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        sp.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cats));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCharacters();
            }
        });
        loadCharacters();
        if(parentHandle==null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                    ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)) {
                sgl.setSpanCount(3);
            } else if (((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) ||
                    ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)) {
                sgl.setSpanCount(2);
            } else {
                sgl.setSpanCount(1);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(parentHandle==null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                    ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)) {
                sgl.setSpanCount(3);
            } else if (((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) ||
                    ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)) {
                sgl.setSpanCount(2);
            } else {
                sgl.setSpanCount(1);
            }
        }
    }

    public void loadCharacters(){
        characterCats.get(0).clear();
        if(sp.getSelectedItemPosition()==0)
            adap.notifyDataSetChanged();
        else
            sp.setSelection(0);
        if(cats.size()>1) {
            cats.removeAll(cats.subList(1, cats.size() - 1));
            characterCats.removeAll(characterCats.subList(1,characterCats.size()-1));
        }
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            if(getActivity()!=null) {
                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        srl.setRefreshing(true);
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        if (getActivity() == null)
                            return null;
                        while (!((SWrpg) getActivity().getApplication()).driveFail && ((SWrpg) getActivity().getApplication()).charsFold == null) {
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
                        if (getActivity() == null)
                            return;
                        if (((SWrpg) getActivity().getApplication()).driveFail) {
                            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                            b.setMessage(R.string.drive_fail);
                            b.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((SWrpg) getActivity().getApplication()).driveFail = false;
                                    ((MainDrawer) getActivity()).gacMaker();
                                    loadCharacters();
                                    dialog.cancel();
                                }
                            }).setNegativeButton(R.string.dice, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance())
                                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).commit();
                                    dialog.cancel();
                                }
                            });
                            b.setCancelable(false);
                            srl.setRefreshing(false);
                            b.show();
                            return;
                        }
                        final Load.Characters ch = new Load.Characters();
                        ch.setOnFinish(new Load.OnLoad() {
                            @Override
                            public void onStart() {
                                srl.setRefreshing(false);
                                if (parentHandle == null)
                                    getActivity().findViewById(R.id.fab).setEnabled(false);
                            }

                            @Override
                            public boolean onLoad(final Editable ed) {
                                if (cats.contains(ed.category))
                                    characterCats.get(cats.indexOf(ed.category)).add((Character) ed);
                                else if (!ed.category.equals("")) {
                                    cats.add(ed.category);
                                    characterCats.add(new ArrayList<Character>());
                                    characterCats.get(cats.size() - 1).add((Character) ed);
                                }
                                characterCats.get(0).add((Character) ed);
                                if(getActivity()!=null) {
                                    if (sp.getSelectedItemPosition() == cats.indexOf(ed.category) || sp.getSelectedItemPosition() == 0) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (sp.getSelectedItemPosition() != 0)
                                                    adap.notifyItemInserted(characterCats.get(cats.indexOf(ed.category)).size() - 1);
                                                else
                                                    adap.notifyItemInserted(characterCats.get(0).size() - 1);
                                            }
                                        });
                                    }
                                }
                                return false;
                            }

                            @Override
                            public void onFinish(ArrayList<Editable> characters) {
                                CharacterList.this.characters.clear();
                                for (Editable ed : characters)
                                    CharacterList.this.characters.add((Character) ed);
                                if(getActivity()!=null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (parentHandle == null)
                                                getActivity().findViewById(R.id.fab).setEnabled(true);
                                        }
                                    });
                                    ch.saveLocal(getActivity());
                                }
                            }
                        });
                        ch.load(getActivity());
                    }
                };
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
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
            srl.setRefreshing(false);
        }
        if(parentHandle!= null){
            Message msg = parentHandle.obtainMessage();
            msg.obj = characters;
            msg.arg1 = 0;
            parentHandle.sendMessage(msg);
        }
    }

    class NameCardAdap extends RecyclerView.Adapter<NameCardAdap.NameCard> {

        int cat = 0;

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
                            int ind = characterCats.get(cat).indexOf(n.character);
                            if (ind != -1){
                                characterCats.get(cat).remove(ind);
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
            ((TextView)holder.c.findViewById(R.id.name)).setText(characterCats.get(cat).get(position).name);
            if (characterCats.get(cat).get(position).career.equals(""))
                holder.c.findViewById(R.id.subname).setVisibility(View.GONE);
            else
                ((TextView) holder.c.findViewById(R.id.subname)).setText(characterCats.get(cat).get(position).career);
            holder.character = characterCats.get(cat).get(position);
        }

        @Override
        public int getItemCount() {
            return characterCats.get(cat).size();
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
    public void onResume() {
        super.onResume();
        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            if(((SWrpg)getActivity().getApplication()).gac==null ||!((SWrpg)getActivity().getApplication()).gac.isConnected()) {
                ((MainDrawer) getActivity()).gacMaker();
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
