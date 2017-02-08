package com.apps.darkstorm.swrpg.ui.character;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.dice.DiceRoll;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.sw.stuff.Skill;

public class SkillLayout {
    private int ability, proficiency,difficulty,challenge,boost,setback,force;
    public View SkillLayout(final Activity main, final LinearLayout skillLay, final Character chara, final Skill s){
        LinearLayout top = new LinearLayout(main);
        LinearLayout.LayoutParams toplp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        toplp.setMargins(15,4,4,4);
        toplp.setMarginEnd(4);
        toplp.setMarginStart(4);
        top.setLayoutParams(toplp);
        final TextView name = new TextView(main);
        LinearLayout.LayoutParams namelp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        namelp.weight = 1;
        name.setLayoutParams(namelp);
        name.setTextSize(16);
        name.setText(s.getNameString());
        name.setTypeface(null,Typeface.BOLD);
        final TextView val = new TextView(main);
        LinearLayout.LayoutParams vallp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        vallp.weight = 1;
        val.setLayoutParams(vallp);
        val.setTextSize(16);
        val.setTypeface(null,Typeface.BOLD);
        val.setText(String.valueOf(s.val));
        val.setGravity(Gravity.CENTER_HORIZONTAL);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless,outVal,true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            top.setForeground(main.getDrawable(outVal.resourceId));
        else
            top.setBackgroundResource(outVal.resourceId);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_skill_edit,null);
                build.setView(dia);
                ((Switch)dia.findViewById(R.id.skill_career_switch)).setChecked(s.career);
                final EditText valDia = (EditText)dia.findViewById(R.id.skill_value);
                valDia.setText(String.valueOf(s.val));
                final String[] skills = main.getResources().getStringArray(R.array.skills_list);
                final int[] skillChar = main.getResources().getIntArray(R.array.skill_characteristic);
                final Spinner skillsSpinner = (Spinner)dia.findViewById(R.id.skill_spinner);
                ArrayAdapter<CharSequence> skillAdapter = ArrayAdapter.createFromResource(main,R.array.skills_list,R.layout.spinner_base);
                skillsSpinner.setAdapter(skillAdapter);
                final Spinner charSpinner = (Spinner)dia.findViewById(R.id.skill_characteristic_spinner);
                ArrayAdapter<CharSequence> chars = ArrayAdapter.createFromResource(main,R.array.base_characteristics,R.layout.spinner_base);
                charSpinner.setAdapter(chars);
                int ind = -1;
                for (int i = 0;i<skills.length;i++){
                    if (s.name.equals(skills[i])){
                        ind = i;
                        break;
                    }
                }
                if (ind == -1){
                    ind = skills.length-1;
                    dia.findViewById(R.id.skill_other).setVisibility(View.VISIBLE);
                    ((EditText)dia.findViewById(R.id.skill_other)).setText(s.name);
                }
                skillsSpinner.setSelection(ind);
                skillsSpinner.post(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                });
                charSpinner.setSelection(s.baseChar);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s.career = ((Switch)dia.findViewById(R.id.skill_career_switch))
                                .isChecked();
                        s.baseChar = charSpinner.getSelectedItemPosition();
                        if (!valDia.getText().toString().equals("")){
                            s.val = Integer.parseInt(valDia.getText().toString());
                        }else{
                            s.val = 0;
                        }
                        if (skillsSpinner.getSelectedItemPosition() != skills.length-1){
                            s.name = skills[skillsSpinner.getSelectedItemPosition()];
                        }else{
                            s.name = ((EditText)dia.findViewById(R.id.skill_other))
                                    .getText().toString();
                        }
                        name.setText(s.getNameString());
                        val.setText(String.valueOf(s.val));
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = chara.skills.remove(s);
                        skillLay.removeViewAt(tmp);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                build.setOnCancelListener(new DialogInterface.OnCancelListener() {
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
                ability = Math.abs(s.val - chara.charVals[s.baseChar]);
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                if (chara.charVals[s.baseChar]>s.val)
                    proficiency = s.val;
                else
                    proficiency = chara.charVals[s.baseChar];
                ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
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
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(ability
                                ,proficiency,difficulty,challenge,boost,setback,force)
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
        top.addView(name);
        top.addView(val);
        return top;
    }
    public View SkillLayout(final Activity main, final LinearLayout skillLay, final Minion minion, final Skill s){
        LinearLayout top = new LinearLayout(main);
        LinearLayout.LayoutParams toplp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        toplp.setMargins(15,4,4,4);
        toplp.setMarginEnd(4);
        toplp.setMarginStart(4);
        top.setLayoutParams(toplp);
        final TextView name = new TextView(main);
        LinearLayout.LayoutParams namelp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        namelp.weight = 1;
        name.setLayoutParams(namelp);
        name.setTextSize(16);
        name.setText(s.name);
        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        name.setTypeface(null,Typeface.BOLD);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless,outVal,true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            top.setForeground(main.getDrawable(outVal.resourceId));
        else
            top.setBackgroundResource(outVal.resourceId);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_skill_edit,null);
                build.setView(dia);
                dia.findViewById(R.id.skill_career_switch).setVisibility(View.GONE);
                final EditText valDia = (EditText)dia.findViewById(R.id.skill_value);
                valDia.setText(String.valueOf(s.val));
                final String[] skills = main.getResources().getStringArray(R.array.skills_list);
                final int[] skillChar = main.getResources().getIntArray(R.array.skill_characteristic);
                dia.findViewById(R.id.skill_minion_hide).setVisibility(View.GONE);
                final Spinner skillsSpinner = (Spinner)dia.findViewById(R.id.skill_spinner);
                ArrayAdapter<CharSequence> skillAdapter = ArrayAdapter.createFromResource(main,R.array.skills_list,R.layout.spinner_base);
                skillsSpinner.setAdapter(skillAdapter);
                final Spinner charSpinner = (Spinner)dia.findViewById(R.id.skill_characteristic_spinner);
                ArrayAdapter<CharSequence> chars = ArrayAdapter.createFromResource(main,R.array.base_characteristics,R.layout.spinner_base);
                charSpinner.setAdapter(chars);
                int ind = -1;
                for (int i = 0;i<skills.length;i++){
                    if (s.name.equals(skills[i])){
                        ind = i;
                        break;
                    }
                }
                if (ind == -1){
                    ind = skills.length-1;
                    dia.findViewById(R.id.skill_other).setVisibility(View.VISIBLE);
                    ((EditText)dia.findViewById(R.id.skill_other)).setText(s.name);
                }
                skillsSpinner.setSelection(ind);
                skillsSpinner.post(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                });
                charSpinner.setSelection(s.baseChar);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s.career = ((Switch)dia.findViewById(R.id.skill_career_switch))
                                .isChecked();
                        s.baseChar = charSpinner.getSelectedItemPosition();
                        if (!valDia.getText().toString().equals("")){
                            s.val = Integer.parseInt(valDia.getText().toString());
                        }else{
                            s.val = 0;
                        }
                        if (skillsSpinner.getSelectedItemPosition() != skills.length-1){
                            s.name = skills[skillsSpinner.getSelectedItemPosition()];
                        }else{
                            s.name = ((EditText)dia.findViewById(R.id.skill_other))
                                    .getText().toString();
                        }
                        name.setText(s.getNameString());
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = minion.skills.remove(s);
                        skillLay.removeViewAt(tmp);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                build.show();
                return true;
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_char_dice_roll,null);
                build.setView(dia);
                build.setOnCancelListener(new DialogInterface.OnCancelListener() {
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
                ability = Math.abs(s.val - minion.charVals[s.baseChar]);
                ((TextView)dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                if (minion.charVals[s.baseChar]>s.val)
                    proficiency = s.val;
                else
                    proficiency = minion.charVals[s.baseChar];
                ((TextView)dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
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
                build.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DiceRoll dr = new DiceRoll();
                        dr.rollDice(ability
                                ,proficiency,difficulty,challenge,boost,setback,force)
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
        top.addView(name);
        return top;
    }
}
