package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.Dice.DiceRoll;

public class DiceFragment extends Fragment {
    private OnDiceInteractionListener mListener;

    public DiceFragment() {}

    public static DiceFragment newInstance() {
        return new DiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    int ability, proficiency, difficulty, challenge, boost, setback, force;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_dice, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.preference_key),Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.color_dice_key),true)){
            top.findViewById(R.id.ability_card).setBackgroundColor(getResources().getColor(R.color.ability_card));
            ((TextView)top.findViewById(R.id.ability_label)).setTextColor(getResources().getColor(R.color.ability_text));
            ((TextView)top.findViewById(R.id.ability_num)).setTextColor(getResources().getColor(R.color.ability_text));
            ((Button)top.findViewById(R.id.ability_plus)).setTextColor(getResources().getColor(R.color.ability_text));
            ((Button)top.findViewById(R.id.ability_minus)).setTextColor(getResources().getColor(R.color.ability_text));
            top.findViewById(R.id.proficiency_card).setBackgroundColor(getResources().getColor(R.color.proficiency_card));
            ((TextView)top.findViewById(R.id.proficiency_label)).setTextColor(getResources().getColor(R.color.proficiency_text));
            ((TextView)top.findViewById(R.id.proficiency_num)).setTextColor(getResources().getColor(R.color.proficiency_text));
            ((Button)top.findViewById(R.id.proficiency_plus)).setTextColor(getResources().getColor(R.color.proficiency_text));
            ((Button)top.findViewById(R.id.proficiency_minus)).setTextColor(getResources().getColor(R.color.proficiency_text));
            top.findViewById(R.id.difficulty_card).setBackgroundColor(getResources().getColor(R.color.difficulty_card));
            ((TextView)top.findViewById(R.id.difficulty_label)).setTextColor(getResources().getColor(R.color.difficulty_text));
            ((TextView)top.findViewById(R.id.difficulty_num)).setTextColor(getResources().getColor(R.color.difficulty_text));
            ((Button)top.findViewById(R.id.difficulty_plus)).setTextColor(getResources().getColor(R.color.difficulty_text));
            ((Button)top.findViewById(R.id.difficulty_minus)).setTextColor(getResources().getColor(R.color.difficulty_text));
            top.findViewById(R.id.challenge_card).setBackgroundColor(getResources().getColor(R.color.challenge_card));
            ((TextView)top.findViewById(R.id.challenge_label)).setTextColor(getResources().getColor(R.color.challenge_text));
            ((TextView)top.findViewById(R.id.challenge_num)).setTextColor(getResources().getColor(R.color.challenge_text));
            ((Button)top.findViewById(R.id.challenge_plus)).setTextColor(getResources().getColor(R.color.challenge_text));
            ((Button)top.findViewById(R.id.challenge_minus)).setTextColor(getResources().getColor(R.color.challenge_text));
            top.findViewById(R.id.boost_card).setBackgroundColor(getResources().getColor(R.color.boost_card));
            ((TextView)top.findViewById(R.id.boost_label)).setTextColor(getResources().getColor(R.color.boost_text));
            ((TextView)top.findViewById(R.id.boost_num)).setTextColor(getResources().getColor(R.color.boost_text));
            ((Button)top.findViewById(R.id.boost_plus)).setTextColor(getResources().getColor(R.color.boost_text));
            ((Button)top.findViewById(R.id.boost_minus)).setTextColor(getResources().getColor(R.color.boost_text));
            top.findViewById(R.id.setback_card).setBackgroundColor(getResources().getColor(R.color.setback_card));
            ((TextView)top.findViewById(R.id.setback_label)).setTextColor(getResources().getColor(R.color.setback_text));
            ((TextView)top.findViewById(R.id.setback_num)).setTextColor(getResources().getColor(R.color.setback_text));
            ((Button)top.findViewById(R.id.setback_plus)).setTextColor(getResources().getColor(R.color.setback_text));
            ((Button)top.findViewById(R.id.setback_minus)).setTextColor(getResources().getColor(R.color.setback_text));
            top.findViewById(R.id.force_card).setBackgroundColor(getResources().getColor(R.color.force_card));
            ((TextView)top.findViewById(R.id.force_label)).setTextColor(getResources().getColor(R.color.force_text));
            ((TextView)top.findViewById(R.id.force_num)).setTextColor(getResources().getColor(R.color.force_text));
            ((Button)top.findViewById(R.id.force_plus)).setTextColor(getResources().getColor(R.color.force_text));
            ((Button)top.findViewById(R.id.force_minus)).setTextColor(getResources().getColor(R.color.force_text));
        }
        ability = 0;
        proficiency = 0;
        difficulty = 0;
        challenge = 0;
        boost = 0;
        setback = 0;
        force = 0;
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.universeFab);
        fab.show();
        fab.setImageResource(R.drawable.ic_die_icon);
        ((TextView)top.findViewById(R.id.ability_num)).setText(String.valueOf(ability));
        ((TextView)top.findViewById(R.id.proficiency_num)).setText(String.valueOf(proficiency));
        ((TextView)top.findViewById(R.id.difficulty_num)).setText(String.valueOf(difficulty));
        ((TextView)top.findViewById(R.id.challenge_num)).setText(String.valueOf(challenge));
        ((TextView)top.findViewById(R.id.boost_num)).setText(String.valueOf(boost));
        ((TextView)top.findViewById(R.id.setback_num)).setText(String.valueOf(setback));
        ((TextView)top.findViewById(R.id.force_num)).setText(String.valueOf(force));
        top.findViewById(R.id.dice_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ability = 0;
                ((TextView)top.findViewById(R.id.ability_num)).setText(String.valueOf(ability));
                proficiency = 0;
                ((TextView)top.findViewById(R.id.proficiency_num)).setText(String.valueOf(proficiency));
                difficulty = 0;
                ((TextView)top.findViewById(R.id.difficulty_num)).setText(String.valueOf(difficulty));
                challenge = 0;
                ((TextView)top.findViewById(R.id.challenge_num)).setText(String.valueOf(challenge));
                boost = 0;
                ((TextView)top.findViewById(R.id.boost_num)).setText(String.valueOf(boost));
                setback = 0;
                ((TextView)top.findViewById(R.id.setback_num)).setText(String.valueOf(setback));
                force = 0;
                ((TextView)top.findViewById(R.id.force_num)).setText(String.valueOf(force));
            }
        });
        top.findViewById(R.id.ability_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (ability>0){
                    ability--;
                    ((TextView)top.findViewById(R.id.ability_num)).setText(String.valueOf(ability));
                }
            }
        });
        top.findViewById(R.id.proficiency_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (proficiency>0){
                    proficiency--;
                    ((TextView)top.findViewById(R.id.proficiency_num)).setText(String.valueOf(proficiency));
                }
            }
        });
        top.findViewById(R.id.difficulty_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (difficulty>0){
                    difficulty--;
                    ((TextView)top.findViewById(R.id.difficulty_num)).setText(String.valueOf(difficulty));
                }
            }
        });
        top.findViewById(R.id.challenge_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (challenge>0){
                    challenge--;
                    ((TextView)top.findViewById(R.id.challenge_num)).setText(String.valueOf(challenge));
                }
            }
        });
        top.findViewById(R.id.boost_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (boost>0){
                    boost--;
                    ((TextView)top.findViewById(R.id.boost_num)).setText(String.valueOf(boost));
                }
            }
        });
        top.findViewById(R.id.setback_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (setback>0){
                    setback--;
                    ((TextView)top.findViewById(R.id.setback_num)).setText(String.valueOf(setback));
                }
            }
        });
        top.findViewById(R.id.force_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (force>0){
                    force--;
                    ((TextView)top.findViewById(R.id.force_num)).setText(String.valueOf(force));
                }
            }
        });
        top.findViewById(R.id.ability_plus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ability++;
                ((TextView)top.findViewById(R.id.ability_num)).setText(String.valueOf(ability));
            }
        });
        top.findViewById(R.id.proficiency_plus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                proficiency++;
                ((TextView)top.findViewById(R.id.proficiency_num)).setText(String.valueOf(proficiency));
            }
        });
        top.findViewById(R.id.difficulty_plus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                difficulty++;
                ((TextView)top.findViewById(R.id.difficulty_num)).setText(String.valueOf(difficulty));
            }
        });
        top.findViewById(R.id.challenge_plus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                challenge++;
                ((TextView)top.findViewById(R.id.challenge_num)).setText(String.valueOf(challenge));
            }
        });
        top.findViewById(R.id.boost_plus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                boost++;
                ((TextView)top.findViewById(R.id.boost_num)).setText(String.valueOf(boost));
            }
        });
        top.findViewById(R.id.setback_plus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setback++;
                ((TextView)top.findViewById(R.id.setback_num)).setText(String.valueOf(setback));
            }
        });
        top.findViewById(R.id.force_plus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                force++;
                ((TextView)top.findViewById(R.id.force_num)).setText(String.valueOf(force));
            }
        });
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new DiceRoll().rollDice(ability,proficiency,difficulty,challenge,boost,setback,force).showDialog(getContext());
            }
        });
        top.findViewById(R.id.dhun_roll).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ((TextView)top.findViewById(R.id.dhun_res)).setText(String.valueOf((int)(Math.random()*100)+1));
            }
        });
        top.findViewById(R.id.dten_roll).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ((TextView)top.findViewById(R.id.dten_res)).setText(String.valueOf((int)(Math.random()*10)+1));
            }
        });
        return top;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDiceInteractionListener) {
            mListener = (OnDiceInteractionListener) context;
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

    public interface OnDiceInteractionListener {
        void onDiceInteraction();
    }
}
