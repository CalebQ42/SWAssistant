package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.widget.EditText;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;

import java.io.IOException;
import java.util.ArrayList;

public class ForcePower implements JsonSavable{
    //Version 1 0-1
    public String name = "",desc = "";
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 2:
                name = (String)tmp[0];
                desc = (String)tmp[1];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof ForcePower))
            return false;
        ForcePower in = (ForcePower)obj;
        return in.name.equals(name) && in.desc.equals(desc);
    }
    public ForcePower clone(){
        ForcePower out = new ForcePower();
        out.name = name;
        out.desc = desc;
        return out;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.beginObject();
        jw.name("name").value(name);
        jw.name("description").value(desc);
        jw.endObject();
    }

    public void loadJson(JsonReader jr) throws IOException{
        jr.beginObject();
        jr.skipValue();
        name = jr.nextString();
        jr.skipValue();
        desc = jr.nextString();
        jr.endObject();
    }

    public static void editForcePower(final Activity ac, final Character c, final int pos, final boolean newPower, final Skill.onSave os){
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        View v = ac.getLayoutInflater().inflate(R.layout.dialog_two_strings,null);
        b.setView(v);
        ((TextInputLayout)v.findViewById(R.id.first_lay)).setHint(ac.getString(R.string.name_text));
        ((TextInputLayout)v.findViewById(R.id.second_lay)).setHint(ac.getString(R.string.description_text));
        final EditText name = (EditText)v.findViewById(R.id.first_edit);
        name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        final EditText desc = (EditText)v.findViewById(R.id.second_edit);
        desc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT|
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        name.setText(c.forcePowers.get(pos).name);
        desc.setText(c.forcePowers.get(pos).desc);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                c.forcePowers.get(pos).name = name.getText().toString();
                c.forcePowers.get(pos).desc = desc.getText().toString();
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
        if(!newPower){
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
