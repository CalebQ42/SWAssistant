package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.view.View;
import android.widget.EditText;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;

import java.io.IOException;
import java.util.ArrayList;

public class Item implements JsonSavable {
    //Version 1 0-2
    public String name = "";
    public String desc = "";
    public int count;
    //Version 2 3
    public int encum;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        tmp.add(count);
        tmp.add(encum);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 4:
                encum = (int)tmp[3];
            case 3:
                name = (String)tmp[0];
                desc = (String)tmp[1];
                count = (int)tmp[2];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Item))
            return false;
        Item in = (Item)obj;
        return in.name.equals(name) && in.desc.equals(desc) && in.count == count && in.encum == encum;
    }
    public Item clone(){
        Item out = new Item();
        out.name = name;
        out.desc = desc;
        out.count = count;
        return out;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.beginObject();
        jw.name("name").value(name);
        jw.name("description").value(desc);
        jw.name("count").value(count);
        jw.name("encumbrance").value(encum);
        jw.endObject();
    }

    public void loadJson(JsonReader jr) throws IOException{
        jr.beginObject();
        while(jr.hasNext()){
            if (jr.peek().equals(JsonToken.NAME)) {
                switch (jr.nextName()) {
                    case "name":
                        name = jr.nextString();
                        break;
                    case "description":
                        desc = jr.nextString();
                        break;
                    case "count":
                        count = jr.nextInt();
                        break;
                    case "encumbrance":
                        encum = jr.nextInt();
                        break;
                }
            }
        }
        jr.endObject();
    }

    public static void editItem(final Activity ac, final Editable c, final int pos, final boolean newItem, final Skill.onSave os){
        final Inventory inv;
        if(c instanceof Character)
            inv = ((Character)c).inv;
        else if(c instanceof Minion)
            inv = ((Minion)c).inv;
        else
            return;
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        View v = ac.getLayoutInflater().inflate(R.layout.dialog_item_edit,null);
        b.setView(v);
        final EditText name = (EditText)v.findViewById(R.id.name_edit);
        name.setText(inv.get(pos).name);
        final EditText count = (EditText)v.findViewById(R.id.count_edit);
        count.setText(String.valueOf(inv.get(pos).count));
        final EditText encum;
        if (c instanceof Character) {
            encum = (EditText) v.findViewById(R.id.encum_edit);
            encum.setText(String.valueOf(inv.get(pos).encum));
        }else {
            v.findViewById(R.id.encum_lay).setVisibility(View.GONE);
            encum = new EditText(ac);
        }
        final EditText desc = (EditText)v.findViewById(R.id.desc_edit);
        desc.setText(inv.get(pos).desc);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inv.get(pos).name = name.getText().toString();
                if(count.getText().toString().equals(""))
                    inv.get(pos).count = Integer.parseInt(count.getText().toString());
                else
                    inv.get(pos).count = 0;
                if(c instanceof Character) {
                    if (encum.getText().toString().equals(""))
                        inv.get(pos).encum = Integer.parseInt(encum.getText().toString());
                    else
                        inv.get(pos).encum = 0;
                }
                inv.get(pos).desc = desc.getText().toString();
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
        if(!newItem){
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
