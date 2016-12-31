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
import com.apps.darkstorm.swrpg.StarWars.Minion;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Item;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Skill;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Talent;
import com.apps.darkstorm.swrpg.StarWars.Stuff.WeapChar;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Weapon;
import com.apps.darkstorm.swrpg.UI.Char.SkillLayout;
import com.apps.darkstorm.swrpg.UI.Char.TalentLayout;

public class SetupMinionAttr {
    private int ability,proficiency,difficulty,challenge,boost,setback,force;
    public void setup(final View top, final Minion minion){
        //<editor-fold desc="name_card">
        ((TextView)top.findViewById(R.id.name_text)).setText(minion.name);
        top.findViewById(R.id.name_card).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.name_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                val.setText(minion.name);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        minion.name = val.getText().toString();
                        ((TextView)top.findViewById(R.id.name_text)).setText(minion.name);
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
        //<editor-fold desc="min_num_card">
        final TextView woundVal = (TextView)top.findViewById(R.id.wound_text);
        final TextView minNum = (TextView)top.findViewById(R.id.min_num_text);
        minNum.setText(String.valueOf(minion.getMinNum()));
        top.findViewById(R.id.min_num_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.setMinNum(minion.getMinNum()+1);
                minNum.setText(String.valueOf(minion.getMinNum()));
                woundVal.setText(String.valueOf(minion.getWound()));
            }
        });
        top.findViewById(R.id.min_num_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minion.getMinNum() > 0) {
                    minion.setMinNum(minion.getMinNum() - 1);
                    minNum.setText(String.valueOf(minion.getMinNum()));
                    woundVal.setText(String.valueOf(minion.getWound()));
                }
            }
        });
        top.findViewById(R.id.min_num_five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.setMinNum(minion.getMinNum()+5);
                minNum.setText(String.valueOf(minion.getMinNum()));
                woundVal.setText(String.valueOf(minion.getWound()));
            }
        });
        top.findViewById(R.id.min_num_ten).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.setMinNum(minion.getMinNum()+10);
                minNum.setText(String.valueOf(minion.getMinNum()));
                woundVal.setText(String.valueOf(minion.getWound()));
            }
        });
        //</editor-fold>
        //<editor-fold desc="wound_strain_card">
        ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(minion.soak));
        top.findViewById(R.id.soak_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.soak_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.soak));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.soak = Integer.parseInt(val.getText().toString());
                            ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(minion.soak));
                        }else{
                            minion.soak = 0;
                            ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(minion.soak));
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
        woundVal.setText(String.valueOf(minion.getWound()));
        top.findViewById(R.id.wound_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (minion.getWound() >0 ){
                    minion.setWound(minion.getWound()-1);
                    woundVal.setText(String.valueOf(minion.getWound()));
                    minNum.setText(String.valueOf(minion.getMinNum()));
                }
            }
        });
        top.findViewById(R.id.wound_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minion.getWound() < minion.woundThresh){
                    minion.setWound(minion.getWound()+1);
                    woundVal.setText(String.valueOf(minion.getWound()));
                    minNum.setText(String.valueOf(minion.getMinNum()));
                }
            }
        });
        top.findViewById(R.id.wound_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.setWound(minion.woundThresh);
                woundVal.setText(String.valueOf(minion.getWound()));
            }
        });
        ((TextView)top.findViewById(R.id.wound_ind_text)).setText(String.valueOf(minion.getWoundInd()));
        top.findViewById(R.id.wound_ind_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.wound_ind_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.getWoundInd()));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.setWoundInd(Integer.parseInt(val.getText().toString()));
                            ((TextView)top.findViewById(R.id.wound_ind_text)).setText(String.valueOf(minion.getMinNum()));
                            woundVal.setText(String.valueOf(minion.getWound()));
                        }else{
                            minion.setWoundInd(0);
                            ((TextView)top.findViewById(R.id.wound_ind_text)).setText(String.valueOf(minion.getMinNum()));
                            woundVal.setText(String.valueOf(minion.getWound()));
                        }
                        if (minion.getWound()>minion.woundThresh){
                            minion.setWound(minion.woundThresh);
                            woundVal.setText(String.valueOf(minion.getWound()));
                        }
                        ((TextView)top.findViewById(R.id.wound_ind_text)).setText(String.valueOf(minion.getWoundInd()));
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
        brawnText.setText(String.valueOf(minion.charVals[0]));
        top.findViewById(R.id.brawn_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.brawn_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[0]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.charVals[0] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[0] = 0;
                        }
                        brawnText.setText(String.valueOf(minion.charVals[0]));
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
                ability = minion.charVals[0];
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
        agilityText.setText(String.valueOf(minion.charVals[1]));
        top.findViewById(R.id.agility_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.agility_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[1]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.charVals[1] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[1] = 0;
                        }
                        agilityText.setText(String.valueOf(minion.charVals[1]));
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
                ability = minion.charVals[1];
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
        intellectText.setText(String.valueOf(minion.charVals[2]));
        top.findViewById(R.id.intellect_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.intellect_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[2]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.charVals[2] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[2] = 0;
                        }
                        intellectText.setText(String.valueOf(minion.charVals[2]));
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
                ability = minion.charVals[2];
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
        cunningText.setText(String.valueOf(minion.charVals[3]));
        top.findViewById(R.id.cunning_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.cunning_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[3]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.charVals[3] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[3] = 0;
                        }
                        cunningText.setText(String.valueOf(minion.charVals[3]));
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
                ability = minion.charVals[3];
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
        willpowerText.setText(String.valueOf(minion.charVals[4]));
        top.findViewById(R.id.willpower_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.willpower_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[4]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.charVals[4] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[4] = 0;
                        }
                        willpowerText.setText(String.valueOf(minion.charVals[4]));
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
                ability = minion.charVals[4];
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
        presenceText.setText(String.valueOf(minion.charVals[5]));
        top.findViewById(R.id.presence_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.presence_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[5]));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.charVals[5] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[5] = 0;
                        }
                        presenceText.setText(String.valueOf(minion.charVals[5]));
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
                ability = minion.charVals[5];
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
        for (int i = 0;i<minion.skills.size();i++){
            ((LinearLayout)top.findViewById(R.id.skill_layout)).addView(new SkillLayout()
                    .SkillLayout(top.getContext(),((LinearLayout)top.findViewById(R.id.skill_layout)),minion,
                            minion.skills.get(i)));
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
                dia.findViewById(R.id.skill_minion_hide).setVisibility(View.GONE);
                dia.findViewById(R.id.skill_career_switch).setVisibility(View.GONE);
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
                        if (skillsSpinner.getSelectedItemPosition()!= skills.length-1){
                            tmp.name = skills[skillsSpinner.getSelectedItemPosition()];
                        }else{
                            tmp.name = ((EditText)dia.findViewById(R.id.skill_other))
                                    .getText().toString();
                        }
                        minion.skills.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.skill_layout)).addView(new SkillLayout()
                                .SkillLayout(top.getContext(),((LinearLayout)top.findViewById(R.id.skill_layout)),minion,minion.skills.get(minion.skills.size()-1)));
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
        defMelee.setText(String.valueOf(minion.defMelee));
        top.findViewById(R.id.melee_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.melee_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.defMelee));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.defMelee = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.defMelee = 0;
                        }
                        defMelee.setText(String.valueOf(minion.defMelee));
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
        defranged.setText(String.valueOf(minion.defRanged));
        top.findViewById(R.id.ranged_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.ranged_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.defRanged));
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!val.getText().toString().equals("")){
                            minion.defRanged = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.defRanged = 0;
                        }
                        defranged.setText(String.valueOf(minion.defRanged));
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
        for (int i = 0;i<minion.weapons.size();i++)
            ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                    .WeaponLayout(top.getContext(),((LinearLayout)top.findViewById(R.id.weapons_layout)),minion,minion.weapons.get(i)));
        top.findViewById(R.id.weapons_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context main = top.getContext();
                final Weapon tmp = new Weapon();
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_weapon_edit);
                dia.findViewById(R.id.minion_hide).setVisibility(View.GONE);
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
                        minion.weapons.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                                .WeaponLayout(main,((LinearLayout)top.findViewById(R.id.weapons_layout)),minion,minion.weapons.get(minion.weapons.size()-1)));
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
        //<editor-fold desc="talents_card">
        for (int i =0;i<minion.talents.size();i++)
            ((LinearLayout)top.findViewById(R.id.talents_layout)).addView
                    (new TalentLayout().TalentLayout(top.getContext(),((LinearLayout)
                            top.findViewById(R.id.talents_layout)),minion,minion.talents.get(i)));
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
                        minion.talents.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.talents_layout)).addView
                                (new TalentLayout().TalentLayout(top.getContext(),((LinearLayout)
                                        top.findViewById(R.id.talents_layout)),minion,minion.talents.get(minion.talents.size()-1)));
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
        //<editor-fold desc="inventory_card">
        final LinearLayout invLay = (LinearLayout)top.findViewById(R.id.inventory_layout);
        for (int i = 0;i<minion.inv.size();i++)
            invLay.addView(new ItemLayout().ItemLayout(top.getContext(),invLay,minion,minion.inv.get(i)));
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
                dia.findViewById(R.id.minion_hide).setVisibility(View.GONE);
                dia.findViewById(R.id.item_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = item.getText().toString();
                        tmp.desc = desc.getText().toString();
                        if (!num.getText().toString().equals(""))
                            tmp.count = Integer.parseInt(num.getText().toString());
                        else
                            tmp.count = 0;
                        minion.inv.add(tmp);
                        invLay.addView(new ItemLayout().ItemLayout(top.getContext(),invLay,minion,minion.inv.get(minion.inv.size()-1)));
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
        //<editor-fold desc="desc_card">
        ((TextView)top.findViewById(R.id.desc_main)).setText(minion.desc);
        top.findViewById(R.id.desc_card).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.description_text);
                final EditText desc = (EditText)dia.findViewById(R.id.edit_val);
                desc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                desc.setText(minion.desc);
                desc.setSingleLine(false);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minion.desc = desc.getText().toString();
                        ((TextView)top.findViewById(R.id.desc_main)).setText(minion.desc);
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
