package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;

import java.util.ArrayList;

public class CriticalInjury{
    //Version 1 0-2
    public String name = "";
    public String desc = "";
    public int severity;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        tmp.add(severity);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 3:
                name = (String)tmp[0];
                desc = (String)tmp[1];
                severity = (int)tmp[2];
        }
    }
    public boolean equals(Object obj){
        if (obj instanceof CriticalInjury){
            CriticalInjury in = (CriticalInjury)obj;
            return in.name.equals(name) && in.desc.equals(desc) && in.severity == severity;
        }else{
            return false;
        }
    }
    public CriticalInjury clone(){
        CriticalInjury out = new CriticalInjury();
        out.name = name;
        out.desc = desc;
        out.severity = severity;
        return out;
    }

    public static void editCritical(final Activity ac, final Editable c, final int pos, final boolean newCrit, final Skill.onSave os){
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        final View v = ac.getLayoutInflater().inflate(R.layout.dialog_critical_injury_edit,null);
        b.setView(v);
        final EditText name = (EditText)v.findViewById(R.id.name_edit);
        name.setText(c.critInjuries.get(pos).name);
        final Spinner severity = (Spinner)v.findViewById(R.id.severity_spin);
        severity.setSelection(c.critInjuries.get(pos).severity);
        final EditText desc = (EditText)v.findViewById(R.id.desc_edit);
        desc.setText(c.critInjuries.get(pos).desc);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                c.critInjuries.get(pos).name = name.getText().toString();
                c.critInjuries.get(pos).severity = severity.getSelectedItemPosition();
                c.critInjuries.get(pos).desc = desc.getText().toString();
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
        if(!newCrit){
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
