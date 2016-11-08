package com.apps.darkstorm.swrpg.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.Dice.DiceRoll;
import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.CriticalInjury;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Duty;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.ForcePower;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Item;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Obligation;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Skill;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Talent;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.WeapChar;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Weapon;
import com.apps.darkstorm.swrpg.StarWars.Character;

public class SetupCharAttr {
    private int ability,proficiency,difficulty,challenge,boost,setback,force;
    public void setup(final View top,final Character chara){
        //<editor-fold desc="name_card">
        ((TextView)top.findViewById(R.id.name_text)).setText(chara.name);
        top.findViewById(R.id.name_card).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.name_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                val.setText(chara.name);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        chara.name = val.getText().toString();
                        ((TextView)top.findViewById(R.id.name_text)).setText(chara.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
        //<editor-fold desc="species_career_card">
        ((TextView)top.findViewById(R.id.species_text)).setText(chara.species);
        ((TextView)top.findViewById(R.id.career_text)).setText(chara.career);
        TextView motivation = (TextView)top.findViewById(R.id.motivation_text);
        motivation.setText(chara.motivation);
        TextView age = (TextView)top.findViewById(R.id.age_text);
        age.setText(String.valueOf(chara.age));
        top.findViewById(R.id.motivation_layout).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.motivation_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(chara.motivation);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        chara.motivation = val.getText().toString();
                        ((TextView)top.findViewById(R.id.motivation_text)).setText(chara.motivation);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.age_layout).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.age_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(chara.age));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        if (!val.getText().toString().equals(""))
                            chara.age = Integer.parseInt(val.getText().toString());
                        else
                            chara.age = 0;
                        ((TextView)top.findViewById(R.id.age_text)).setText(String.valueOf(chara.age));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.species_layout).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.species_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(chara.species);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        chara.species = val.getText().toString();
                        ((TextView)top.findViewById(R.id.species_text)).setText(chara.species);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.career_layout).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.career_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(chara.career);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        chara.career = val.getText().toString();
                        ((TextView)top.findViewById(R.id.career_text)).setText(chara.career);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
        //<editor-fold desc="wound_strain_card">
        ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(chara.soak));
        top.findViewById(R.id.soak_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.soak_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.soak));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.soak = Integer.parseInt(val.getText().toString());
                            ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(chara.soak));
                        }else{
                            chara.soak = 0;
                            ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(chara.soak));
                        }
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView woundVal = (TextView)top.findViewById(R.id.wound_text);
        woundVal.setText(String.valueOf(chara.woundCur));
        final TextView strainVal = (TextView)top.findViewById(R.id.strain_text);
        strainVal.setText(String.valueOf(chara.strainCur));
        top.findViewById(R.id.wound_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (chara.woundCur >0 ){
                    chara.woundCur--;
                    woundVal.setText(String.valueOf(chara.woundCur));
                }
            }
        });
        top.findViewById(R.id.wound_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chara.woundCur < chara.woundThresh){
                    chara.woundCur++;
                    woundVal.setText(String.valueOf(chara.woundCur));
                }
            }
        });
        top.findViewById(R.id.wound_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chara.woundCur = chara.woundThresh;
                woundVal.setText(String.valueOf(chara.woundCur));
            }
        });
        top.findViewById(R.id.wound_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.wound_thresh_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.woundThresh));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.woundThresh = Integer.parseInt(val.getText().toString());
                            if (chara.woundThresh < chara.woundCur){
                                chara.woundCur = chara.woundThresh;
                                woundVal.setText(String.valueOf(chara.woundCur));
                            }
                        }else{
                            chara.woundThresh = 0;
                            chara.woundCur = chara.woundThresh;
                            woundVal.setText(String.valueOf(chara.woundCur));
                        }
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.strain_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (chara.strainCur >0 ){
                    chara.strainCur--;
                    strainVal.setText(String.valueOf(chara.strainCur));
                }
            }
        });
        top.findViewById(R.id.strain_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chara.strainCur < chara.strainThresh){
                    chara.strainCur++;
                    strainVal.setText(String.valueOf(chara.strainCur));
                }
            }
        });
        top.findViewById(R.id.strain_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chara.strainCur = chara.strainThresh;
                strainVal.setText(String.valueOf(chara.strainCur));
            }
        });
        top.findViewById(R.id.strain_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.strain_thresh_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.strainThresh));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.strainThresh = Integer.parseInt(val.getText().toString());
                            if (chara.strainThresh < chara.strainCur){
                                chara.strainCur = chara.strainThresh;
                                strainVal.setText(String.valueOf(chara.strainCur));
                            }
                        }else{
                            chara.strainThresh = 0;
                            chara.strainCur = chara.strainThresh;
                            strainVal.setText(String.valueOf(chara.strainCur));
                        }
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
        //<editor-fold desc="characteristics_card">
        final TextView brawnText = (TextView)top.findViewById(R.id.brawn_text);
        brawnText.setText(String.valueOf(chara.charVals[0]));
        top.findViewById(R.id.brawn_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.brawn_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[0]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.charVals[0] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[0] = 0;
                        }
                        brawnText.setText(String.valueOf(chara.charVals[0]));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.brawn_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_char_dice_roll);
                dia.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        difficulty = 0;
                        challenge = 0;
                        boost = 0;
                        setback = 0;
                        force = 0;
                    }
                });
                ability = chara.charVals[0];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ability>0){
                            ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (proficiency>0){
                            proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (difficulty>0){
                            difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (challenge>0){
                            challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (boost>0){
                            boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (setback>0){
                            setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (force>0){
                            force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                    }
                });
                dia.findViewById(R.id.char_dice_roll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(ability,proficiency,difficulty,challenge,boost,setback,force)
                                .showDialog(top.getContext());
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.char_dice_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        final TextView agilityText = (TextView)top.findViewById(R.id.agility_text);
        agilityText.setText(String.valueOf(chara.charVals[1]));
        top.findViewById(R.id.agility_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.agility_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[1]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.charVals[1] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[1] = 0;
                        }
                        agilityText.setText(String.valueOf(chara.charVals[1]));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.agility_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_char_dice_roll);
                dia.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ability = 0;
                        proficiency = 0;
                        difficulty = 0;
                        challenge = 0;
                        boost = 0;
                        setback = 0;
                        force = 0;
                    }
                });
                ability = chara.charVals[1];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ability>0){
                            ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (proficiency>0){
                            proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (difficulty>0){
                            difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (challenge>0){
                            challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (boost>0){
                            boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (setback>0){
                            setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (force>0){
                            force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                    }
                });
                dia.findViewById(R.id.char_dice_roll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(ability,proficiency,difficulty,challenge,boost,setback,force)
                                .showDialog(top.getContext());
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.char_dice_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        final TextView intellectText = (TextView)top.findViewById(R.id.intellect_text);
        intellectText.setText(String.valueOf(chara.charVals[2]));
        top.findViewById(R.id.intellect_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.intellect_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[2]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.charVals[2] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[2] = 0;
                        }
                        intellectText.setText(String.valueOf(chara.charVals[2]));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.intellect_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_char_dice_roll);
                dia.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ability = 0;
                        proficiency = 0;
                        difficulty = 0;
                        challenge = 0;
                        boost = 0;
                        setback = 0;
                        force = 0;
                    }
                });
                ability = chara.charVals[2];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ability>0){
                            ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (proficiency>0){
                            proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (difficulty>0){
                            difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (challenge>0){
                            challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (boost>0){
                            boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (setback>0){
                            setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (force>0){
                            force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                    }
                });
                dia.findViewById(R.id.char_dice_roll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(ability,proficiency,difficulty,challenge,boost,setback,force)
                                .showDialog(top.getContext());
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.char_dice_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        final TextView cunningText = (TextView)top.findViewById(R.id.cunning_text);
        cunningText.setText(String.valueOf(chara.charVals[3]));
        top.findViewById(R.id.cunning_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.cunning_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[3]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.charVals[3] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[3] = 0;
                        }
                        cunningText.setText(String.valueOf(chara.charVals[3]));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.cunning_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_char_dice_roll);
                dia.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ability = 0;
                        proficiency = 0;
                        difficulty = 0;
                        challenge = 0;
                        boost = 0;
                        setback = 0;
                        force = 0;
                    }
                });
                ability = chara.charVals[3];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ability>0){
                            ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (proficiency>0){
                            proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (difficulty>0){
                            difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (challenge>0){
                            challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (boost>0){
                            boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (setback>0){
                            setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (force>0){
                            force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                    }
                });
                dia.findViewById(R.id.char_dice_roll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(ability,proficiency,difficulty,challenge,boost,setback,force)
                                .showDialog(top.getContext());
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.char_dice_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        final TextView willpowerText = (TextView)top.findViewById(R.id.willpower_text);
        willpowerText.setText(String.valueOf(chara.charVals[4]));
        top.findViewById(R.id.willpower_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.willpower_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[4]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.charVals[4] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[4] = 0;
                        }
                        willpowerText.setText(String.valueOf(chara.charVals[4]));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.willpower_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_char_dice_roll);
                dia.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ability = 0;
                        proficiency = 0;
                        difficulty = 0;
                        challenge = 0;
                        boost = 0;
                        setback = 0;
                        force = 0;
                    }
                });
                ability = chara.charVals[4];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ability>0){
                            ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (proficiency>0){
                            proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (difficulty>0){
                            difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (challenge>0){
                            challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (boost>0){
                            boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (setback>0){
                            setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (force>0){
                            force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                    }
                });
                dia.findViewById(R.id.char_dice_roll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(ability,proficiency,difficulty,challenge,boost,setback,force)
                                .showDialog(top.getContext());
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.char_dice_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        final TextView presenceText = (TextView)top.findViewById(R.id.presence_text);
        presenceText.setText(String.valueOf(chara.charVals[5]));
        top.findViewById(R.id.presence_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.presence_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[5]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.charVals[5] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[5] = 0;
                        }
                        presenceText.setText(String.valueOf(chara.charVals[5]));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.presence_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_char_dice_roll);
                dia.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ability = 0;
                        proficiency = 0;
                        difficulty = 0;
                        challenge = 0;
                        boost = 0;
                        setback = 0;
                        force = 0;
                    }
                });
                ability = chara.charVals[5];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ability>0){
                            ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (proficiency>0){
                            proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (difficulty>0){
                            difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (challenge>0){
                            challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (boost>0){
                            boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (setback>0){
                            setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (force>0){
                            force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                    }
                });
                dia.findViewById(R.id.char_dice_roll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(ability,proficiency,difficulty,challenge,boost,setback,force)
                                .showDialog(top.getContext());
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.char_dice_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="skills_card">
        for (int i = 0;i<chara.skills.size();i++){
            ((LinearLayout)top.findViewById(R.id.skill_layout)).addView(new SkillLayout()
                    .SkillLayout(top.getContext(),((LinearLayout)top.findViewById(R.id.skill_layout)),chara,
                            chara.skills.get(i)));
        }
        top.findViewById(R.id.skill_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] skills = top.getContext().getResources().getStringArray(R.array.skills_list);
                final int[] skillChar = top.getContext().getResources().getIntArray(R.array.skill_characteristic);
                final Skill tmp = new Skill();
                tmp.name = skills[0];
                tmp.baseChar = skillChar[0];
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_skill_edit);
                ((Switch)dia.findViewById(R.id.skill_career_switch)).setChecked(tmp.career);
                final EditText val = (EditText)dia.findViewById(R.id.skill_value);
                val.setText(String.valueOf(tmp.val));
                final Spinner skillsSpinner = (Spinner)dia.findViewById(R.id.skill_spinner);
                ArrayAdapter<CharSequence> skillAdapter = ArrayAdapter.createFromResource(top.getContext(),R.array.skills_list,R.layout.spinner_base);
                skillsSpinner.setAdapter(skillAdapter);
                final Spinner charSpinner = (Spinner)dia.findViewById(R.id.skill_characteristic_spinner);
                ArrayAdapter<CharSequence> chars = ArrayAdapter.createFromResource(top.getContext(),R.array.base_characteristics,R.layout.spinner_base);
                charSpinner.setAdapter(chars);
                int ind = -1;
                for (int i = 0;i<skills.length;i++){
                    if (tmp.name.equals(skills[i])){
                        ind = i;
                        break;
                    }
                }
                if (ind == -1){
                    ind = skills.length-1;
                    dia.findViewById(R.id.skill_other).setVisibility(View.VISIBLE);
                    ((EditText)dia.findViewById(R.id.skill_other)).setText(tmp.name);
                }
                skillsSpinner.setSelection(ind);
                charSpinner.setSelection(tmp.baseChar);
                skillsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != skills.length-1){
                            charSpinner.setSelection(skillChar[position]);
                            dia.findViewById(R.id.skill_other).setVisibility(View.GONE);
                        }else{
                            dia.findViewById(R.id.skill_other).setVisibility(View.VISIBLE);
                            ((EditText)dia.findViewById(R.id.skill_other)).setText("");
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent){}
                });
                dia.findViewById(R.id.skill_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.career = ((Switch)dia.findViewById(R.id.skill_career_switch))
                                .isChecked();
                        tmp.baseChar = charSpinner.getSelectedItemPosition();
                        if (!val.getText().toString().equals("")){
                            tmp.val = Integer.parseInt(val.getText().toString());
                        }else{
                            tmp.val = 0;
                        }
                        if (skillsSpinner.getSelectedItemPosition()!= skills.length-1){
                            tmp.name = skills[skillsSpinner.getSelectedItemPosition()];
                        }else{
                            tmp.name = ((EditText)dia.findViewById(R.id.skill_other))
                                    .getText().toString();
                        }
                        chara.skills.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.skill_layout)).addView(new SkillLayout()
                                .SkillLayout(top.getContext(),((LinearLayout)top.findViewById(R.id.skill_layout)),chara,chara.skills.get(chara.skills.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.skill_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="defense_card">
        final TextView defMelee = (TextView)top.findViewById(R.id.melee_defense_text);
        defMelee.setText(String.valueOf(chara.defMelee));
        top.findViewById(R.id.melee_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.melee_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.defMelee));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.defMelee = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.defMelee = 0;
                        }
                        defMelee.setText(String.valueOf(chara.defMelee));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView defranged = (TextView)top.findViewById(R.id.ranged_defense_text);
        defranged.setText(String.valueOf(chara.defRanged));
        top.findViewById(R.id.ranged_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.ranged_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.defRanged));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.defRanged = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.defRanged = 0;
                        }
                        defranged.setText(String.valueOf(chara.defRanged));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
        //<editor-fold desc="weapons_card">
        for (int i = 0;i<chara.weapons.size();i++)
            ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                    .WeaponLayout(top.getContext(),((LinearLayout)top.findViewById(R.id.weapons_layout)),chara,chara.weapons.get(i)));
        top.findViewById(R.id.weapons_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context main = top.getContext();
                final Weapon tmp = new Weapon();
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_weapon_edit);
                final EditText nameVal = (EditText)dia.findViewById(R.id.weapon_edit_name);
                nameVal.setText(tmp.name);
                final EditText dmg = (EditText)dia.findViewById(R.id.weapon_edit_damage);
                dmg.setText(String.valueOf(tmp.dmg));
                final EditText crit = (EditText)dia.findViewById(R.id.weapon_edit_critical);
                crit.setText(String.valueOf(tmp.crit));
                final EditText hp = (EditText)dia.findViewById(R.id.weapon_edit_hp);
                hp.setText(String.valueOf(tmp.hp));
                final Spinner state = (Spinner)dia.findViewById(R.id.weapon_edit_weapon_state);
                ArrayAdapter<CharSequence> stateAdap = ArrayAdapter.createFromResource(main,R.array.gear_damage_levels,R.layout.spinner_base);
                state.setAdapter(stateAdap);
                state.setSelection(tmp.itemState);
                final Spinner range = (Spinner)dia.findViewById(R.id.weapon_edit_range_spinner);
                ArrayAdapter<CharSequence> rangeAdap = ArrayAdapter.createFromResource(main,R.array.range_bands,R.layout.spinner_base);
                range.setAdapter(rangeAdap);
                range.setSelection(tmp.range);
                final Spinner skill = (Spinner)dia.findViewById(R.id.weapon_edit_skill_spinner);
                ArrayAdapter<CharSequence> skillAdap = ArrayAdapter.createFromResource(main,R.array.weapon_skills,R.layout.spinner_base);
                skill.setAdapter(skillAdap);
                skill.setSelection(tmp.skill);
                final Spinner skillChar = (Spinner)dia.findViewById(R.id.weapon_edit_skill_char_spinner);
                ArrayAdapter<CharSequence> charAdap = ArrayAdapter.createFromResource(main,R.array.base_characteristics,R.layout.spinner_base);
                skillChar.setAdapter(charAdap);
                skillChar.setSelection(tmp.skillBase);
                skill.post(new Runnable() {
                    @Override
                    public void run() {
                        skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                skillChar.setSelection(main.getResources().getIntArray(R.array.weapon_skill_bases)[position]);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                });
                final LinearLayout spec = (LinearLayout)dia.findViewById(R.id.weapon_edit_special_layout);
                for (int i = 0;i<tmp.chars.size();i++)
                    spec.addView(new WeapCharLayout().WeapCharLayout(main,spec,tmp,tmp.chars.get(i)));
                dia.findViewById(R.id.weapon_edit_special_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final WeapChar wr = new WeapChar();
                        final Dialog dia = new Dialog(main);
                        dia.setContentView(R.layout.dialog_weapon_char);
                        final EditText name = (EditText)dia.findViewById(R.id.weapon_char_name);
                        name.setText(wr.name);
                        final EditText val = (EditText)dia.findViewById(R.id.weapon_char_value);
                        val.setText(String.valueOf(wr.val));
                        final EditText adv = (EditText)dia.findViewById(R.id.weapon_char_adv);
                        adv.setText(String.valueOf(wr.adv));
                        dia.findViewById(R.id.weapon_char_save).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wr.name = name.getText().toString();
                                if (!val.getText().toString().equals("")){
                                    wr.val = Integer.parseInt(val.getText().toString());
                                }else{
                                    wr.val = 0;
                                }
                                if (!adv.getText().toString().equals("")){
                                    wr.adv = Integer.parseInt(adv.getText().toString());
                                }else{
                                    wr.adv = 0;
                                }
                                tmp.chars.add(wr);
                                spec.addView(new WeapCharLayout().WeapCharLayout(main,spec,tmp,tmp.chars.get(tmp.chars.size()-1)));
                                dia.cancel();
                            }
                        });
                        dia.findViewById(R.id.weapon_char_cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dia.cancel();
                            }
                        });
                        dia.show();
                    }
                });
                final Switch addBrawn = (Switch)dia.findViewById(R.id.weapon_edit_add_brawn);
                addBrawn.setChecked(tmp.addBrawn);
                final Switch loaded = (Switch)dia.findViewById(R.id.weapon_edit_loaded);
                loaded.setChecked(tmp.loaded);
                final Switch slug = (Switch)dia.findViewById(R.id.weapon_edit_slug);
                slug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.VISIBLE);
                        }else{
                            dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.GONE);
                        }
                    }
                });
                slug.setChecked(tmp.slug);
                if (tmp.slug){
                    dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.VISIBLE);
                }else{
                    dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.GONE);
                }
                final TextView ammo = (TextView)dia.findViewById(R.id.weapon_edit_ammo_val);
                ammo.setText(String.valueOf(tmp.ammo));
                dia.findViewById(R.id.weapon_edit_ammo_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.ammo>0)
                            tmp.ammo--;
                        ammo.setText(String.valueOf(tmp.ammo));
                    }
                });
                dia.findViewById(R.id.weapon_edit_ammo_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.ammo++;
                        ammo.setText(String.valueOf(tmp.ammo));
                    }
                });
                dia.findViewById(R.id.weapon_edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = nameVal.getText().toString();
                        if (!dmg.getText().toString().equals("")){
                            tmp.dmg = Integer.parseInt(dmg.getText().toString());
                        }else{
                            tmp.dmg = 0;
                        }
                        if (!crit.getText().toString().equals(""))
                            tmp.crit = Integer.parseInt(crit.getText().toString());
                        else
                            tmp.crit = 0;
                        if (!hp.getText().toString().equals("")){
                            tmp.hp = Integer.parseInt(hp.getText().toString());
                        }else
                            tmp.hp = 0;
                        tmp.itemState = state.getSelectedItemPosition();
                        tmp.range = range.getSelectedItemPosition();
                        tmp.skill = skill.getSelectedItemPosition();
                        tmp.skillBase = skillChar.getSelectedItemPosition();
                        tmp.chars = tmp.chars.clone();
                        tmp.addBrawn = addBrawn.isChecked();
                        tmp.loaded = loaded.isChecked();
                        tmp.slug = slug.isChecked();
                        chara.weapons.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                                .WeaponLayout(main,((LinearLayout)top.findViewById(R.id.weapons_layout)),chara,chara.weapons.get(chara.weapons.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.weapon_edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="critical_injuries_card">
        final LinearLayout critLay = (LinearLayout)top.findViewById(R.id.critical_injuries_layout);
        for (int i = 0;i<chara.critInjuries.size();i++)
            critLay.addView(new CritInjLayout().CritInjLayout(top.getContext(),critLay,chara,chara.critInjuries.get(i)));
        top.findViewById(R.id.critical_injuries_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CriticalInjury tmp = new CriticalInjury();
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_critical_injury_edit);
                final EditText name = (EditText)dia.findViewById(R.id.crit_name);
                name.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(tmp.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(top.getContext(),R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(tmp.severity);
                dia.findViewById(R.id.crit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = name.getText().toString();
                        tmp.desc = desc.getText().toString();
                        tmp.severity = sev.getSelectedItemPosition();
                        chara.critInjuries.add(tmp);
                        critLay.addView(new CritInjLayout().CritInjLayout(top.getContext(),critLay,chara,
                                chara.critInjuries.get(chara.critInjuries.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.crit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="specialization_card">
        final LinearLayout specLay = (LinearLayout)top.findViewById(R.id.specialization_layout);
        for (int i = 0;i<chara.specializations.size();i++)
            specLay.addView(new SpecializationLayout().SpecializationLayout(
                    top.getContext(),specLay,chara,chara.specializations.get(i)));
        top.findViewById(R.id.specialization_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.spec_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText("");
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chara.specializations.add(val.getText().toString());
                        ((LinearLayout)top.findViewById(R.id.specialization_layout)).addView(new SpecializationLayout().SpecializationLayout(
                                top.getContext(),specLay,chara,
                                chara.specializations.get(chara.specializations.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="talents_card">
        for (int i =0;i<chara.talents.size();i++)
            ((LinearLayout)top.findViewById(R.id.talents_layout)).addView
                    (new TalentLayout().TalentLayout(top.getContext(),((LinearLayout)
                            top.findViewById(R.id.talents_layout)),chara,chara.talents.get(i)));
        top.findViewById(R.id.talents_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                final Talent tmp = new Talent();
                dia.setContentView(R.layout.dialog_talent_edit);
                final EditText name = (EditText)dia.findViewById(R.id.talent_name);
                name.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.talent_description);
                desc.setText(tmp.desc);
                final TextView val = (TextView)dia.findViewById(R.id.talent_val);
                val.setText(String.valueOf(tmp.val));
                dia.findViewById(R.id.talent_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.val>0)
                            tmp.val--;
                        val.setText(String.valueOf(tmp.val));

                    }
                });
                dia.findViewById(R.id.talent_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.val++;
                        val.setText(String.valueOf(tmp.val));
                    }
                });
                dia.findViewById(R.id.talent_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = name.getText().toString();
                        tmp.desc = desc.getText().toString();
                        chara.talents.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.talents_layout)).addView
                                (new TalentLayout().TalentLayout(top.getContext(),((LinearLayout)
                                        top.findViewById(R.id.talents_layout)),chara,chara.talents.get(chara.talents.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.talent_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="force_powers_card">
        final TextView rat = (TextView)top.findViewById(R.id.force_rating_text);
        rat.setText(String.valueOf(chara.force));
        top.findViewById(R.id.force_rating_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources()
                        .getString(R.string.force_rating_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(chara.force));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.force = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.force = 0;
                        }
                        rat.setText(String.valueOf(chara.force));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final LinearLayout fpLay = (LinearLayout)top.findViewById(R.id.force_powers_layout);
        for (int i =0;i<chara.forcePowers.size();i++)
            fpLay.addView(new ForceLayout().ForceLayout(top.getContext(),fpLay,chara,chara.forcePowers.get(i)));
        top.findViewById(R.id.force_powers_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ForcePower tmp = new ForcePower();
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                dia.findViewById(R.id.edit_desc_main).setVisibility(View.VISIBLE);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.force_power_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.edit_desc_val);
                desc.setText(tmp.desc);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = val.getText().toString();
                        tmp.desc = desc.getText().toString();
                        chara.forcePowers.add(tmp);
                        fpLay.addView(new ForceLayout().ForceLayout(top.getContext(),fpLay,chara,chara.forcePowers.get(chara.forcePowers.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="xp_card">
        final TextView xpCur = (TextView)top.findViewById(R.id.xp_current_text);
        final TextView xpTot = (TextView)top.findViewById(R.id.xp_total_text);
        xpCur.setText(String.valueOf(chara.xpCur));
        xpTot.setText(String.valueOf(chara.xpTot));
        top.findViewById(R.id.xp_total_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.total_xp_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(String.valueOf(chara.xpTot)));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals(""))
                            chara.xpTot = Integer.parseInt(val.getText().toString());
                        else
                            chara.xpTot = 0;
                        xpTot.setText(String.valueOf(chara.xpTot));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.xp_current_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.current_xp_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(String.valueOf(chara.xpCur)));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals(""))
                            chara.xpCur = Integer.parseInt(val.getText().toString());
                        else
                            chara.xpCur = 0;
                        xpCur.setText(String.valueOf(chara.xpCur));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.xp_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.xp_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText("");
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")) {
                            int tmp = Integer.parseInt(val.getText().toString());
                            chara.xpCur += tmp;
                            chara.xpTot += tmp;
                            xpCur.setText(String.valueOf(chara.xpCur));
                            xpTot.setText(String.valueOf(chara.xpTot));
                        }
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="inventory_card">
        final TextView creds = (TextView)top.findViewById(R.id.credits_val);
        creds.setText(String.valueOf(chara.credits));
        top.findViewById(R.id.credits_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.credits_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.credits));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            chara.credits = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.credits = 0;
                        }
                        creds.setText(String.valueOf(chara.credits));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final LinearLayout invLay = (LinearLayout)top.findViewById(R.id.inventory_layout);
        for (int i = 0;i<chara.inv.size();i++)
            invLay.addView(new ItemLayout().ItemLayout(top.getContext(),invLay,chara,chara.inv.get(i)));
        top.findViewById(R.id.inventory_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Item tmp = new Item();
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_item_edit);
                final EditText item = (EditText)dia.findViewById(R.id.item_name);
                item.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.item_desc);
                desc.setText(tmp.desc);
                final EditText num = (EditText)dia.findViewById(R.id.item_num);
                num.setText(String.valueOf(tmp.count));
                dia.findViewById(R.id.item_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = item.getText().toString();
                        tmp.desc = desc.getText().toString();
                        if (!num.getText().toString().equals(""))
                            tmp.count = Integer.parseInt(num.getText().toString());
                        else
                            tmp.count = 0;
                        chara.inv.add(tmp);
                        invLay.addView(new ItemLayout().ItemLayout(top.getContext(),invLay,chara,chara.inv.get(chara.inv.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.item_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="emotions_card">
        final TextView morality = (TextView)top.findViewById(R.id.morality_text);
        morality.setText(String.valueOf(chara.morality));
        final TextView conflict = (TextView)top.findViewById(R.id.conflict_text);
        conflict.setText(String.valueOf(chara.conflict));
        top.findViewById(R.id.morality_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.morality_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.morality));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals(""))
                            chara.morality = Integer.parseInt(val.getText().toString());
                        else
                            chara.morality = 0;
                        morality.setText(String.valueOf(chara.morality));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.conflict_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.conflict_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                val.setText(String.valueOf(chara.conflict));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals(""))
                            chara.conflict = Integer.parseInt(val.getText().toString());
                        else
                            chara.conflict = 0;
                        conflict.setText(String.valueOf(chara.conflict));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.findViewById(R.id.resolve_conflict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chara.resolveConflict();
                morality.setText(String.valueOf(chara.morality));
                conflict.setText(String.valueOf(chara.conflict));
            }
        });
        Switch dkSide = (Switch)top.findViewById(R.id.dark_side_switch);
        dkSide.setChecked(chara.darkSide);
        dkSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chara.darkSide = isChecked;
            }
        });
        final TextView strength = (TextView)top.findViewById(R.id.emotional_strength_text);
        strength.setText(chara.emotionalStr[0]);
        top.findViewById(R.id.emotional_strength_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.strength_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(chara.emotionalStr[0]);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chara.emotionalStr[0] = val.getText().toString();
                        strength.setText(chara.emotionalStr[0]);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView weakness = (TextView)top.findViewById(R.id.emotional_weakness_text);
        weakness.setText(chara.emotionalWeak[0]);
        top.findViewById(R.id.emotional_weakness_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.weakness_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(chara.emotionalWeak[0]);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chara.emotionalWeak[0] = val.getText().toString();
                        weakness.setText(chara.emotionalWeak[0]);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
        //<editor-fold desc="duty_card">
        final LinearLayout dutyLay = (LinearLayout)top.findViewById(R.id.duty_layout);
        for (int i = 0;i<chara.duty.size();i++)
            dutyLay.addView(new DutyLayout().DutyLayout(top.getContext(),dutyLay,chara,chara.duty.get(i)));
        top.findViewById(R.id.duty_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Duty tmp = new Duty();
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_dutobli);
                ((TextView) dia.findViewById(R.id.dutobli_name)).setText(top.getResources().getString(R.string.obligation_text));
                final EditText obligation = (EditText) dia.findViewById(R.id.dutobli_edit);
                obligation.setText(tmp.name);
                final EditText val = (EditText) dia.findViewById(R.id.dutobli_val);
                val.setText(String.valueOf(tmp.val));
                dia.findViewById(R.id.dutobli_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.dutobli_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = obligation.getText().toString();
                        if (!val.getText().toString().equals(""))
                            tmp.val = Integer.parseInt(val.getText().toString());
                        else
                            tmp.val = 0;
                        chara.duty.add(tmp);
                        dutyLay.addView(new DutyLayout().DutyLayout(top.getContext(),dutyLay,chara,chara.duty.get(chara.duty.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.dutobli_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        final LinearLayout obligationLay = (LinearLayout)top.findViewById(R.id.obligation_layout);
        for (int i = 0;i<chara.obligation.size();i++)
            obligationLay.addView(new ObligationLayout().ObligationLayout(top.getContext(),obligationLay,chara,
                    chara.obligation.get(i)));
        top.findViewById(R.id.obligation_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Obligation tmp = new Obligation();
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_dutobli);
                ((TextView) dia.findViewById(R.id.dutobli_name)).setText(top.getResources().getString(R.string.obligation_text));
                final EditText obligation = (EditText) dia.findViewById(R.id.dutobli_edit);
                obligation.setText(tmp.name);
                final EditText val = (EditText) dia.findViewById(R.id.dutobli_val);
                val.setText(String.valueOf(tmp.val));
                dia.findViewById(R.id.dutobli_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.dutobli_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = obligation.getText().toString();
                        if (!val.getText().toString().equals(""))
                            tmp.val = Integer.parseInt(val.getText().toString());
                        else
                            tmp.val = 0;
                        chara.obligation.add(tmp);
                        obligationLay.addView(new ObligationLayout().ObligationLayout(top.getContext(),obligationLay,chara,
                                chara.obligation.get(chara.obligation.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.dutobli_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="desc_card">
        ((TextView)top.findViewById(R.id.desc_main)).setText(chara.desc);
        top.findViewById(R.id.desc_card).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.description_text);
                final EditText desc = (EditText)dia.findViewById(R.id.edit_val);
                desc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                desc.setText(chara.desc);
                desc.setSingleLine(false);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chara.desc = desc.getText().toString();
                        ((TextView)top.findViewById(R.id.desc_main)).setText(chara.desc);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
    }
}