package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Skill implements JsonSavable {
    //Version 1 0-3
    public String name = "";
    public int val;
    int baseChar;
    public boolean career;
    public int costNext(){
        int out = 5*(val+1);
        if (!career){
            out += 5;
        }
        return out;
    }
    public String getNameString(){
        String tmp;
        if (career){
            tmp = "*";
        }else
            tmp = " ";
        tmp += name;
        tmp += ":";
        return tmp;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(val);
        tmp.add(baseChar);
        tmp.add(career);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 4:
                name = (String)tmp[0];
                val = (int)tmp[1];
                baseChar = (int)tmp[2];
                career = (boolean)tmp[3];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Skill))
            return false;
        Skill in = (Skill)obj;
        return in.name.equals(name) && in.val == val && in.baseChar == baseChar && in.career == career;
    }
    public Skill clone(){
        Skill sk = new Skill();
        sk.name = name;
        sk.career = career;
        sk.val = val;
        sk.baseChar = baseChar;
        return sk;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.beginObject();
        jw.name("name").value(name);
        jw.name("value").value(val);
        jw.name("base").value(baseChar);
        jw.name("career").value(career);
        jw.endObject();
    }

    public void loadJson(JsonReader jr) throws IOException{
        jr.beginObject();
        while(jr.hasNext()){
            switch(jr.nextName()){
                case "name":
                    name = jr.nextString();
                    break;
                case "value":
                    val = jr.nextInt();
                    break;
                case "base":
                    baseChar = jr.nextInt();
                    break;
                case "career":
                    career = jr.nextBoolean();
            }
        }
        jr.endObject();
    }

    public abstract static class onSave{
        public abstract void save();
        public abstract void delete();
        public abstract void cancel();
    }

    public static void editSkill(final Activity ac, final Editable c, final int pos, final boolean newSkill, final onSave os){
        final Skills s;
        if(c instanceof Character)
            s = ((Character)c).skills;
        else if(c instanceof Minion)
            s = ((Minion)c).skills;
        else
            s = new Skills();
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        final View ed = ac.getLayoutInflater().inflate(R.layout.dialog_skill_edit,null);
        b.setView(ed);
        if (c instanceof Minion) {
            ed.findViewById(R.id.value_lay).setVisibility(View.GONE);
            ed.findViewById(R.id.career).setVisibility(View.GONE);
        }else{
            ((Switch)ed.findViewById(R.id.career)).setChecked(s.get(pos).career);
            ((EditText)ed.findViewById(R.id.value_edit)).setText(String.valueOf(s.get(pos).val));
        }
        final ArrayList<String> skills = new ArrayList<>();
        skills.addAll(Arrays.asList(ac.getResources().getStringArray(R.array.skills_list)));
        final int[] tmp = ac.getResources().getIntArray(R.array.skill_characteristic);
        final Spinner skillSpin = (Spinner)ed.findViewById(R.id.skill_spinner);
        skillSpin.setAdapter(new ArrayAdapter<>(ac,android.R.layout.simple_spinner_item,skills));
        final Spinner charSpin = (Spinner)ed.findViewById(R.id.base_characteristic_spinner);
        charSpin.setSelection(s.get(pos).baseChar);
        skillSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==tmp.length){
                    ed.findViewById(R.id.skill_lay).setVisibility(View.VISIBLE);
                    ((EditText)ed.findViewById(R.id.skill_edit)).setText("");
                }else{
                    ed.findViewById(R.id.skill_lay).setVisibility(View.GONE);
                    charSpin.setSelection(tmp[position]);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        if (!newSkill) {
            int ind = skills.indexOf(s.get(pos).name);
            if (ind == -1) {
                skillSpin.setSelection(tmp.length);
                ed.findViewById(R.id.skill_lay).setVisibility(View.VISIBLE);
            } else {
                ed.findViewById(R.id.skill_lay).setVisibility(View.GONE);
                skillSpin.setSelection(ind);
            }
        }else{
            skillSpin.setSelection(0);
        }
        charSpin.setSelection(s.get(pos).baseChar);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s.get(pos).baseChar = charSpin.getSelectedItemPosition();
                if(skillSpin.getSelectedItemPosition()==tmp.length){
                    s.get(pos).name = ((EditText)ed.findViewById(R.id.skill_edit)).getText().toString();
                }else{
                    s.get(pos).name = skills.get(skillSpin.getSelectedItemPosition());
                }
                if(!(c instanceof Minion)){
                    if(!((EditText)ed.findViewById(R.id.value_edit)).getText().toString().equals(""))
                        s.get(pos).val = Integer.parseInt(((EditText)ed.findViewById(R.id.value_edit)).getText().toString());
                    else
                        s.get(pos).val = 0;
                }
                s.get(pos).career = ((Switch)ed.findViewById(R.id.career)).isChecked();
                os.save();
                dialog.cancel();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                os.cancel();
                dialog.cancel();
            }
        });
        if(!newSkill) {
            b.setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    os.delete();
                    dialog.cancel();
                }
            });
        }
        b.show();
    }
}