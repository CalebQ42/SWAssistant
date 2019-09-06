package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.view.View;
import android.widget.EditText;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;

public class Obligation implements JsonSavable {
    //Version 1 0-1
    public String name = "";
    public int val;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(val);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 2:
                name = (String)tmp[0];
                val = (int)tmp[1];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Obligation))
            return false;
        Obligation in = (Obligation)obj;
        return in.name.equals(name) && in.val == val;
    }
    public Obligation clone(){
        Obligation out = new Obligation();
        out.name = name;
        out.val = val;
        return out;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.beginObject();
        jw.name("name").value(name);
        jw.name("value").value(val);
        jw.endObject();
    }

    public void loadJson(JsonReader jr) throws IOException{
        jr.beginObject();
        while(jr.hasNext()){
            if(!jr.peek().equals(JsonToken.NAME)){
                jr.skipValue();
                continue;
            }
            switch(jr.nextName()){
                case "name":
                    name = jr.nextString();
                    break;
                case "value":
                    val = jr.nextInt();
            }
        }
        jr.endObject();
    }

    public static void editObligation(final Activity ac, final Character c, final int pos, final boolean newObligation, final Skill.onSave os){
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        View v = ac.getLayoutInflater().inflate(R.layout.dialog_two_strings,null);
        b.setView(v);
        TextInputLayout nameLay = (TextInputLayout)v.findViewById(R.id.first_lay);
        nameLay.setHint(ac.getString(R.string.name_text));
        final EditText name = (EditText)v.findViewById(R.id.first_edit);
        name.setText(c.obligation.get(pos).name);
        TextInputLayout valLay = (TextInputLayout)v.findViewById(R.id.second_lay);
        valLay.setHint(ac.getString(R.string.value_text));
        final EditText val = (EditText)v.findViewById(R.id.second_edit);
        val.setInputType(InputType.TYPE_CLASS_NUMBER);
        val.setText(String.valueOf(c.obligation.get(pos).val));
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                c.obligation.get(pos).name = name.getText().toString();
                if(!val.getText().toString().equals(""))
                    c.obligation.get(pos).val = Integer.parseInt(val.getText().toString());
                else
                    c.obligation.get(pos).val = 0;
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
        if(!newObligation){
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
