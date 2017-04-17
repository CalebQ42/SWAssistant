package com.apps.darkstorm.swrpg.assistant;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DiceRollFragment extends Fragment {

    private OnDiceRollFragmentInteraction mListener;

    public DiceRollFragment() {}

    public static DiceRollFragment newInstance() {
        return new DiceRollFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dice_roll, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDiceRollFragmentInteraction) {
            mListener = (OnDiceRollFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //<editor-fold desc="setColor">
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.color_dice_key),true)){
            view.findViewById(R.id.ability_card).setBackgroundColor(getResources().getColor(R.color.ability_card));
            ConstraintLayout abilityLay = (ConstraintLayout)view.findViewById(R.id.ability_lay);
            for (int i = 0;i<abilityLay.getChildCount();i++){
                if (abilityLay.getChildAt(i) instanceof TextView){
                    ((TextView)abilityLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.ability_text));
                }else if (abilityLay.getChildAt(i) instanceof Button){
                    ((Button)abilityLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.ability_text));
                }
            }
            view.findViewById(R.id.proficiency_card).setBackgroundColor(getResources().getColor(R.color.proficiency_card));
            ConstraintLayout proficiencyLay = (ConstraintLayout)view.findViewById(R.id.proficiency_lay);
            for (int i = 0;i<proficiencyLay.getChildCount();i++){
                if (proficiencyLay.getChildAt(i) instanceof TextView){
                    ((TextView)proficiencyLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.proficiency_text));
                }else if (proficiencyLay.getChildAt(i) instanceof Button){
                    ((Button)proficiencyLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.proficiency_text));
                }
            }
            view.findViewById(R.id.boost_card).setBackgroundColor(getResources().getColor(R.color.boost_card));
            ConstraintLayout boostLay = (ConstraintLayout)view.findViewById(R.id.boost_lay);
            for (int i = 0;i<boostLay.getChildCount();i++){
                if (boostLay.getChildAt(i) instanceof TextView){
                    ((TextView)boostLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.boost_text));
                }else if (boostLay.getChildAt(i) instanceof Button){
                    ((Button)boostLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.boost_text));
                }
            }
            view.findViewById(R.id.difficulty_card).setBackgroundColor(getResources().getColor(R.color.difficulty_card));
            ConstraintLayout difficultyLay = (ConstraintLayout)view.findViewById(R.id.difficulty_lay);
            for (int i = 0;i<difficultyLay.getChildCount();i++){
                if (difficultyLay.getChildAt(i) instanceof TextView){
                    ((TextView)difficultyLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.difficulty_text));
                }else if (difficultyLay.getChildAt(i) instanceof Button){
                    ((Button)difficultyLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.difficulty_text));
                }
            }
            view.findViewById(R.id.challenge_card).setBackgroundColor(getResources().getColor(R.color.challenge_card));
            ConstraintLayout challengeLay = (ConstraintLayout)view.findViewById(R.id.challenge_lay);
            for (int i = 0;i<challengeLay.getChildCount();i++){
                if (challengeLay.getChildAt(i) instanceof TextView){
                    ((TextView)challengeLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.challenge_text));
                }else if (challengeLay.getChildAt(i) instanceof Button){
                    ((Button)challengeLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.challenge_text));
                }
            }
            view.findViewById(R.id.setback_card).setBackgroundColor(getResources().getColor(R.color.setback_card));
            ConstraintLayout setbackLay = (ConstraintLayout)view.findViewById(R.id.setback_lay);
            for (int i = 0;i<setbackLay.getChildCount();i++){
                if (setbackLay.getChildAt(i) instanceof TextView){
                    ((TextView)setbackLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.setback_text));
                }else if (setbackLay.getChildAt(i) instanceof Button){
                    ((Button)setbackLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.setback_text));
                }
            }
            view.findViewById(R.id.force_card).setBackgroundColor(getResources().getColor(R.color.force_card));
            ConstraintLayout forceLay = (ConstraintLayout)view.findViewById(R.id.force_lay);
            for (int i = 0;i<forceLay.getChildCount();i++){
                if (forceLay.getChildAt(i) instanceof TextView){
                    ((TextView)forceLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.force_text));
                }else if (forceLay.getChildAt(i) instanceof Button){
                    ((Button)forceLay.getChildAt(i)).setTextColor(getResources().getColor(R.color.force_text));
                }
            }
        }
        //</editor-fold>
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDiceRollFragmentInteraction {
    }
}
