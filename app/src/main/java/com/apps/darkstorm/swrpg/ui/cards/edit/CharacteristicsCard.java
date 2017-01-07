package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.dice.DiceNumHolder;
import com.apps.darkstorm.swrpg.dice.DiceRoll;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;

public class CharacteristicsCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_characteristics,root);
        final TextView brawnText = (TextView)top.findViewById(R.id.brawn_text);
        brawnText.setText(String.valueOf(chara.charVals[0]));
        top.findViewById(R.id.brawn_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.brawn_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[0]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            chara.charVals[0] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[0] = 0;
                        }
                        brawnText.setText(String.valueOf(chara.charVals[0]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.brawn_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = chara.charVals[0];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        final TextView agilityText = (TextView)top.findViewById(R.id.agility_text);
        agilityText.setText(String.valueOf(chara.charVals[1]));
        top.findViewById(R.id.agility_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.agility_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[1]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            chara.charVals[1] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[1] = 0;
                        }
                        agilityText.setText(String.valueOf(chara.charVals[1]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.agility_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = chara.charVals[1];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });

        final TextView intellectText = (TextView)top.findViewById(R.id.intellect_text);
        intellectText.setText(String.valueOf(chara.charVals[2]));
        top.findViewById(R.id.intellect_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.intellect_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[2]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            chara.charVals[2] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[2] = 0;
                        }
                        intellectText.setText(String.valueOf(chara.charVals[2]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.intellect_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = chara.charVals[2];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });

        final TextView cunningText = (TextView)top.findViewById(R.id.cunning_text);
        cunningText.setText(String.valueOf(chara.charVals[3]));
        top.findViewById(R.id.cunning_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.cunning_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[3]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            chara.charVals[3] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[3] = 0;
                        }
                        cunningText.setText(String.valueOf(chara.charVals[3]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.cunning_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = chara.charVals[3];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });

        final TextView willpowerText = (TextView)top.findViewById(R.id.willpower_text);
        willpowerText.setText(String.valueOf(chara.charVals[4]));
        top.findViewById(R.id.willpower_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.willpower_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[4]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            chara.charVals[4] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[4] = 0;
                        }
                        willpowerText.setText(String.valueOf(chara.charVals[4]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.willpower_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = chara.charVals[4];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });

        final TextView presenceText = (TextView)top.findViewById(R.id.presence_text);
        presenceText.setText(String.valueOf(chara.charVals[5]));
        top.findViewById(R.id.presence_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.presence_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.charVals[5]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            chara.charVals[5] = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.charVals[5] = 0;
                        }
                        presenceText.setText(String.valueOf(chara.charVals[5]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.presence_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = chara.charVals[5];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Minion minion){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_characteristics,root);
        final TextView brawnText = (TextView)top.findViewById(R.id.brawn_text);
        brawnText.setText(String.valueOf(minion.charVals[0]));
        top.findViewById(R.id.brawn_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.brawn_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[0]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            minion.charVals[0] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[0] = 0;
                        }
                        brawnText.setText(String.valueOf(minion.charVals[0]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.brawn_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = minion.charVals[0];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        final TextView agilityText = (TextView)top.findViewById(R.id.agility_text);
        agilityText.setText(String.valueOf(minion.charVals[1]));
        top.findViewById(R.id.agility_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.agility_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[1]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            minion.charVals[1] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[1] = 0;
                        }
                        agilityText.setText(String.valueOf(minion.charVals[1]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.agility_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = minion.charVals[1];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });

        final TextView intellectText = (TextView)top.findViewById(R.id.intellect_text);
        intellectText.setText(String.valueOf(minion.charVals[2]));
        top.findViewById(R.id.intellect_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.intellect_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[2]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            minion.charVals[2] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[2] = 0;
                        }
                        intellectText.setText(String.valueOf(minion.charVals[2]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.intellect_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = minion.charVals[2];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });

        final TextView cunningText = (TextView)top.findViewById(R.id.cunning_text);
        cunningText.setText(String.valueOf(minion.charVals[3]));
        top.findViewById(R.id.cunning_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.cunning_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[3]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            minion.charVals[3] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[3] = 0;
                        }
                        cunningText.setText(String.valueOf(minion.charVals[3]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.cunning_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = minion.charVals[3];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });

        final TextView willpowerText = (TextView)top.findViewById(R.id.willpower_text);
        willpowerText.setText(String.valueOf(minion.charVals[4]));
        top.findViewById(R.id.willpower_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.willpower_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[4]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            minion.charVals[4] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[4] = 0;
                        }
                        willpowerText.setText(String.valueOf(minion.charVals[4]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.willpower_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = minion.charVals[4];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });

        final TextView presenceText = (TextView)top.findViewById(R.id.presence_text);
        presenceText.setText(String.valueOf(minion.charVals[5]));
        top.findViewById(R.id.presence_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.presence_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.charVals[5]));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            minion.charVals[5] = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.charVals[5] = 0;
                        }
                        presenceText.setText(String.valueOf(minion.charVals[5]));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.findViewById(R.id.presence_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DiceNumHolder dice = new DiceNumHolder();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                dice.ability = minion.charVals[5];
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.ability>0){
                            dice.ability--;
                            ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.ability++;
                        ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(dice.ability));
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.proficiency>0){
                            dice.proficiency--;
                            ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.proficiency++;
                        ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(dice.proficiency));
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.difficulty>0){
                            dice.difficulty--;
                            ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.difficulty++;
                        ((TextView)dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(dice.difficulty));
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.challenge>0){
                            dice.challenge--;
                            ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.challenge++;
                        ((TextView)dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(dice.challenge));
                    }
                });
                dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.boost>0){
                            dice.boost--;
                            ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.boost++;
                        ((TextView)dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(dice.boost));
                    }
                });
                dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.setback>0){
                            dice.setback--;
                            ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.setback++;
                        ((TextView)dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(dice.setback));
                    }
                });
                dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dice.force>0){
                            dice.force--;
                            ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                        }
                    }
                });
                dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dice.force++;
                        ((TextView)dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(dice.force));
                    }
                });
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(dice)
                                .showDialog(main);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        return top;
    }
}
