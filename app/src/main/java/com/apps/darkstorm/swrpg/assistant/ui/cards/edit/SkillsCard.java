package com.apps.darkstorm.swrpg.assistant.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skill;
import com.apps.darkstorm.swrpg.assistant.ui.character.SkillLayout;
import com.apps.darkstorm.swrpg.assistant.R;

public class SkillsCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_skills,root,false);
        for (int i = 0;i<chara.skills.size();i++){
            ((LinearLayout)top.findViewById(R.id.skill_layout)).addView(new SkillLayout()
                    .SkillLayout(main,((LinearLayout)top.findViewById(R.id.skill_layout)),chara,
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
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_skill_edit,null);
                build.setView(dia);
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
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
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
                                .SkillLayout(main,((LinearLayout)top.findViewById(R.id.skill_layout)),chara,chara.skills.get(chara.skills.size()-1)));
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
        final View top = main.getLayoutInflater().inflate(R.layout.edit_skills,root,false);
        for (int i = 0;i<minion.skills.size();i++){
            ((LinearLayout)top.findViewById(R.id.skill_layout)).addView(new SkillLayout()
                    .SkillLayout(main,((LinearLayout)top.findViewById(R.id.skill_layout)),minion,
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
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_skill_edit,null);
                build.setView(dia);
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
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
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
                        minion.skills.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.skill_layout)).addView(new SkillLayout()
                                .SkillLayout(main,((LinearLayout)top.findViewById(R.id.skill_layout)),minion,minion.skills.get(minion.skills.size()-1)));
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
