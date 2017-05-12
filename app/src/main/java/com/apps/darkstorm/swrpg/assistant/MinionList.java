package com.apps.darkstorm.swrpg.assistant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
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
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;

public class MinionList extends Fragment {

    public MinionList() {}

    public static MinionList newInstance() {
        return new MinionList();
    }
    Handler parentHandle = null;
    public static MinionList newInstance(Handler parentHandle){
        MinionList cl = new MinionList();
        cl.parentHandle = parentHandle;
        return cl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cat_list, container, false);
    }

    ArrayList<Minion> minions;
    ArrayList<CharSequence> cats;
    ArrayList<ArrayList<Minion>> minionCats;

    StaggeredGridLayoutManager sgl;

    Spinner sp;
    SwipeRefreshLayout srl;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true)) {
            AdView ads = (AdView)view.findViewById(R.id.adView);
            ads.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").addKeyword("Tabletop Roleplay").addKeyword("RPG").build();
            ads.loadAd(adRequest);
        }
        if(parentHandle==null) {
            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setImageResource(R.drawable.add);
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Integer> IDs = new ArrayList<>();
                    for (Minion c : minions) {
                        IDs.add(c.ID);
                    }
                    int ID = 0;
                    while (IDs.contains(ID)) {
                        ID++;
                    }
                    Minion ch = new Minion(ID);
                    getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(ch))
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("Minion Edit").commit();
                    ((FloatingActionButton) getActivity().findViewById(R.id.fab)).hide();
                }
            });
        }
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.minions);
        srl = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        sp = (Spinner)view.findViewById(R.id.cat_spinner);
        cats = new ArrayList<>();
        minionCats = new ArrayList<>();
        final NameCardAdap adap = new NameCardAdap();
        RecyclerView r = (RecyclerView)view.findViewById(R.id.recycler);
        r.setAdapter(adap);
        sgl = new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL);
        r.setLayoutManager(sgl);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adap.minionsAdap = minionCats.get(position);
                adap.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMinions();
            }
        });
        loadMinions();
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


    public void loadMinions(){
        srl.setRefreshing(true);
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!((SWrpg)getActivity().getApplication()).driveFail&&((SWrpg)getActivity().getApplication()).charsFold==null){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(((SWrpg)getActivity().getApplication()).driveFail) {
                        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                        b.setMessage(R.string.drive_fail);
                        b.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadMinions();
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
                    final Load.Minions ch = new Load.Minions();
                    ch.setOnFinish(new Load.onFinish() {
                        @Override
                        public void finish() {
                            ch.saveLocal(getActivity());
                            minions = ch.minions;
                            cats.clear();
                            minionCats.clear();
                            cats.add("All");
                            minionCats.add(new ArrayList<Minion>());
                            for (Minion c:ch.minions){
                                if(cats.contains(c.category)){
                                    minionCats.get(cats.indexOf(c.category)).add(c);
                                }else if(!c.category.equals("")){
                                    cats.add(c.category);
                                    minionCats.add(new ArrayList<Minion>());
                                    minionCats.get(minionCats.size()-1).add(c);
                                }
                                minionCats.get(0).add(c);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<CharSequence> apAdap = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cats);
                                    sp.setAdapter(apAdap);
                                    sp.setSelection(0);
                                    srl.setRefreshing(false);
                                }
                            });
                        }
                    });
                    ch.load(getActivity());
                }
            });
            th.run();
        }else{
            minions = new ArrayList<>();
            minions.addAll(Arrays.asList(LoadLocal.minions(getActivity())));
            cats.clear();
            minionCats.clear();
            cats.add("All");
            minionCats.add(new ArrayList<Minion>());
            for (Minion c:minions){
                if(cats.contains(c.category)){
                    minionCats.get(cats.indexOf(c.category)).add(c);
                }else if(!c.category.equals("")){
                    cats.add(c.category);
                    minionCats.add(new ArrayList<Minion>());
                    minionCats.get(minionCats.size()-1).add(c);
                }
                minionCats.get(0).add(c);
            }
            ArrayAdapter<CharSequence> apAdap = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cats);
            sp.setAdapter(apAdap);
            sp.setSelection(0);
            srl.setRefreshing(false);
        }
        if(parentHandle!= null){
            Message msg = parentHandle.obtainMessage();
            msg.obj = minions;
            msg.arg1 = 0;
            parentHandle.sendMessage(msg);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            if(((SWrpg)getActivity().getApplication()).gac==null ||!((SWrpg)getActivity().getApplication()).gac.isConnected())
                ((MainDrawer)getActivity()).gacMaker();
        }
    }

    class NameCardAdap extends RecyclerView.Adapter<NameCardAdap.NameCard> {

        ArrayList<Minion> minionsAdap = new ArrayList<>();

        @Override
        public NameCard onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView c = (CardView)getActivity().getLayoutInflater().inflate(R.layout.card_name,parent,false);
            final NameCard n = new NameCard(c);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(parentHandle== null) {
                        getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(n.minion))
                                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("Editing").commit();
                        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).hide();
                    }else{
                        Message msg = parentHandle.obtainMessage();
                        msg.obj = n.minion;
                        parentHandle.sendMessage(msg);
                    }
                }
            });
            c.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    b.setMessage(R.string.minion_delete);
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            minions.remove(n.minion);
                            int ind = minionsAdap.indexOf(n.minion);
                            if (ind != -1){
                                minionsAdap.remove(ind);
                                NameCardAdap.this.notifyItemRemoved(ind);
                            }
                            for (ArrayList<Minion> al:minionCats){
                                al.remove(n.minion);
                            }
                            n.minion.delete(getActivity());
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
            ((TextView)holder.c.findViewById(R.id.name)).setText(minionsAdap.get(position).name);
            holder.c.findViewById(R.id.subname).setVisibility(View.GONE);
            holder.minion = minionsAdap.get(position);
        }

        @Override
        public int getItemCount() {
            return minionsAdap.size();
        }

        class NameCard extends RecyclerView.ViewHolder {
            CardView c;
            Minion minion;
            NameCard(CardView c) {
                super(c);
                this.c = c;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnMinionListInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnMinionListInteractionListener");
        }
    }

    interface OnMinionListInteractionListener {}
}
