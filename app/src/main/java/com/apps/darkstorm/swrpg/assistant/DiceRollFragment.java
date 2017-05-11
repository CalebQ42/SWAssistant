package com.apps.darkstorm.swrpg.assistant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.apps.darkstorm.swrpg.assistant.dice.DiceHolder;
import com.apps.darkstorm.swrpg.assistant.ui.upDownCard;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;

public class DiceRollFragment extends Fragment {

    private DiceHolder dice;

    public DiceRollFragment() {}

    public static DiceRollFragment newInstance() {
        return new DiceRollFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dice = new DiceHolder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dice_roll, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnDiceRollFragmentInteraction)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingInterfactionInterface");
        }
    }

    RecyclerView r,i;
    StaggeredGridLayoutManager rl,il;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.ads_key),true)) {
            AdView ads = (AdView)view.findViewById(R.id.adView);
            ads.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
            ads.loadAd(adRequest);
        }
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice.roll().showDialog(getActivity());
            }
        });
        fab.setImageResource(R.drawable.die_icon);
        r = (RecyclerView)view.findViewById(R.id.dice_recycler);
        final DiceList d = new DiceList(getActivity(),dice);
        d.setHasStableIds(true);
        r.setAdapter(d);
        i = (RecyclerView)view.findViewById(R.id.instant_recycler);
        final InstantList inst = new InstantList();
        inst.setHasStableIds(true);
        i.setAdapter(inst);
        view.findViewById(R.id.dice_reset).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dice.reset();
                d.notifyDataSetChanged();
            }
        });
        il = new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL);
        i.setLayoutManager(il);
        rl = new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL);
        r.setLayoutManager(rl);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_LARGE)||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            il.setSpanCount(3);
            rl.setSpanCount(3);
        }else if (((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_LARGE)||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            il.setSpanCount(2);
            rl.setSpanCount(2);
        }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) !=Configuration.SCREENLAYOUT_SIZE_SMALL){
            il.setSpanCount(2);
            rl.setSpanCount(2);
        }else{
            il.setSpanCount(1);
            rl.setSpanCount(1);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_LARGE)||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            il.setSpanCount(3);
            rl.setSpanCount(3);
        }else if (((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_LARGE)||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE)){
            il.setSpanCount(2);
            rl.setSpanCount(2);
        }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) !=Configuration.SCREENLAYOUT_SIZE_SMALL){
            il.setSpanCount(2);
            rl.setSpanCount(2);
        }else{
            il.setSpanCount(1);
            rl.setSpanCount(1);
        }
    }

    class InstantList extends RecyclerView.Adapter<InstantList.InstantCardHolder>{
        class InstantCardHolder extends RecyclerView.ViewHolder{
            CardView c;
            InstantCardHolder(CardView c){
                super(c);
                this.c = c;
            }
        }
        public InstantCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView c = (CardView)getActivity().getLayoutInflater().inflate(R.layout.card_instant_dice,parent,false);
            final TextSwitcher ts = (TextSwitcher)c.findViewById(R.id.instant_switcher);
            Animation in = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_in_left);
            in.setInterpolator(getActivity(),android.R.anim.anticipate_overshoot_interpolator);
            Animation out = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_out_right);
            out.setInterpolator(getActivity(),android.R.anim.anticipate_overshoot_interpolator);
            ts.setInAnimation(in);
            ts.setOutAnimation(out);
            ts.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    return getActivity().getLayoutInflater().inflate(R.layout.template_num_text,ts,false);
                }
            });
            ts.setText("");
            return new InstantCardHolder(c);
        }
        public void onBindViewHolder(final InstantCardHolder i, int position) {
            switch(position){
                case 0:
                    ((TextView)i.c.findViewById(R.id.instant_label)).setText(R.string.dhun_text);
                    i.c.findViewById(R.id.instant_roll).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TextSwitcher)i.c.findViewById(R.id.instant_switcher)).setText(String.valueOf(new Random().nextInt(99)+1));
                        }
                    });
                    break;
                case 1:
                    ((TextView)i.c.findViewById(R.id.instant_label)).setText(R.string.dten_text);
                    i.c.findViewById(R.id.instant_roll).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((TextSwitcher)i.c.findViewById(R.id.instant_switcher)).setText(String.valueOf(new Random().nextInt(9)));
                        }
                    });
                    break;
            }
        }
        public int getItemCount() {
            return 2;
        }
    }

    public static class DiceList extends RecyclerView.Adapter<DiceList.DiceCardHolder>{
        class DiceCardHolder extends RecyclerView.ViewHolder{
            CardView c;
            DiceCardHolder(CardView c) {
                super(c);
                this.c = c;
            }
        }
        public DiceCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final CardView c = (CardView)ac.getLayoutInflater().inflate(R.layout.card_up_down,parent,false);
            final TextSwitcher ts = (TextSwitcher)c.findViewById(R.id.up_down_switcher);
            Animation in = AnimationUtils.loadAnimation(ac,android.R.anim.slide_in_left);
            in.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
            Animation out = AnimationUtils.loadAnimation(ac,android.R.anim.slide_out_right);
            out.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
            ts.setInAnimation(in);
            ts.setOutAnimation(out);
            ts.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    return ac.getLayoutInflater().inflate(R.layout.template_num_text,ts,false);
                }
            });
            return new DiceCardHolder(c);
        }
        public void onBindViewHolder(DiceCardHolder d, int position) {
            switch(position){
                case 0:
                    upDownCard.setUp(d.c, ac.getString(R.string.ability_dice), new upDownCard.upDown() {
                        public void up() {
                            dice.ability++;
                        }
                        public void down() {
                            if (dice.ability>0){
                                dice.ability--;
                            }
                        }
                        public String labelValue() {
                            return String.valueOf(dice.ability);
                        }
                    });
                    if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.color_dice_key),true)){
                        upDownCard.setColors(d.c,R.color.ability_card,R.color.ability_text);
                    }
                    break;
                case 1:
                    upDownCard.setUp(d.c, ac.getString(R.string.proficiency_dice), new upDownCard.upDown() {
                        public void up() {
                            dice.proficiency++;
                        }
                        public void down() {
                            if (dice.proficiency>0){
                                dice.proficiency--;
                            }
                        }
                        public String labelValue() {
                            return String.valueOf(dice.proficiency);
                        }
                    });
                    if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.color_dice_key),true)){
                        upDownCard.setColors(d.c,R.color.proficiency_card,R.color.proficiency_text);
                    }
                    break;
                case 2:
                    upDownCard.setUp(d.c, ac.getString(R.string.boost_dice), new upDownCard.upDown() {
                        public void up() {
                            dice.boost++;
                        }
                        public void down() {
                            if (dice.boost>0){
                                dice.boost--;
                            }
                        }
                        public String labelValue() {
                            return String.valueOf(dice.boost);
                        }
                    });
                    if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.color_dice_key),true)){
                        upDownCard.setColors(d.c,R.color.boost_card,R.color.boost_text);
                    }
                    break;
                case 3:
                    upDownCard.setUp(d.c, ac.getString(R.string.difficulty_dice), new upDownCard.upDown() {
                        public void up() {
                            dice.difficulty++;
                        }
                        public void down() {
                            if (dice.difficulty>0){
                                dice.difficulty--;
                            }
                        }
                        public String labelValue() {
                            return String.valueOf(dice.difficulty);
                        }
                    });
                    if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.color_dice_key),true)){
                        upDownCard.setColors(d.c,R.color.difficulty_card,R.color.difficulty_text);
                    }
                    break;
                case 4:
                    upDownCard.setUp(d.c, ac.getString(R.string.challenge_dice), new upDownCard.upDown() {
                        public void up() {
                            dice.challenge++;
                        }
                        public void down() {
                            if (dice.challenge>0){
                                dice.challenge--;
                            }
                        }
                        public String labelValue() {
                            return String.valueOf(dice.challenge);
                        }
                    });
                    if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.color_dice_key),true)){
                        upDownCard.setColors(d.c,R.color.challenge_card,R.color.challenge_text);
                    }
                    break;
                case 5:
                    upDownCard.setUp(d.c, ac.getString(R.string.setback_dice), new upDownCard.upDown() {
                        public void up() {
                            dice.setback++;
                        }
                        public void down() {
                            if (dice.setback>0){
                                dice.setback--;
                            }
                        }
                        public String labelValue() {
                            return String.valueOf(dice.setback);
                        }
                    });
                    if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.color_dice_key),true)){
                        upDownCard.setColors(d.c,R.color.setback_card,R.color.setback_text);
                    }
                    break;
                case 6:
                    upDownCard.setUp(d.c, ac.getString(R.string.force_dice), new upDownCard.upDown() {
                        public void up() {
                            dice.force++;
                        }
                        public void down() {
                            if (dice.force>0){
                                dice.force--;
                            }
                        }
                        public String labelValue() {
                            return String.valueOf(dice.force);
                        }
                    });
                    if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.color_dice_key),true)){
                        upDownCard.setColors(d.c,R.color.force_card,R.color.force_text);
                    }
                    break;
            }
        }
        public int getItemCount() {
            return 7;
        }
        Activity ac;
        DiceHolder dice;
        public DiceList(Activity ac,DiceHolder dice){
            this.ac = ac;
            this.dice = dice;
        }
    }

    interface OnDiceRollFragmentInteraction {
    }
}
