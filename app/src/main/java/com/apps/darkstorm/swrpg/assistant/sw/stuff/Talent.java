package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;

import java.util.ArrayList;

public class Talent{
    //Version 1 0-2
    public String name = "";
    public String desc = "";
    public int val;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        tmp.add(val);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 3:
                name = (String)tmp[0];
                desc = (String)tmp[1];
                val = (int)tmp[2];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Talent))
            return false;
        Talent in = (Talent)obj;
        return in.name.equals(name) && in.desc.equals(desc) && in.val == val;
    }
    public Talent clone(){
        Talent out = new Talent();
        out.name = name;
        out.desc = desc;
        out.val = val;
        return out;
    }

    public static void editTalent(final Activity ac, final Editable c, final int pos, final boolean newTalent, final Skill.onSave os){
        final Talents ts;
        if(c instanceof Character)
            ts = ((Character)c).talents;
        else if(c instanceof Minion)
            ts = ((Minion)c).talents;
        else
            return;
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        View v = ac.getLayoutInflater().inflate(R.layout.dialog_talent_edit,null);
        b.setView(v);
        final EditText name = (EditText)v.findViewById(R.id.name_edit);
        name.setText(ts.get(pos).name);
        Animation in = AnimationUtils.loadAnimation(ac,android.R.anim.slide_in_left);
        in.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
        Animation out = AnimationUtils.loadAnimation(ac,android.R.anim.slide_out_right);
        out.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
        final TextSwitcher numText = (TextSwitcher)v.findViewById(R.id.value_switch);
        numText.setInAnimation(in);
        numText.setOutAnimation(out);
        numText.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return ac.getLayoutInflater().inflate(R.layout.template_num_text,numText,false);
            }
        });
        final int[] val = new int[1];
        val[0] = ts.get(pos).val;
        numText.setText(String.valueOf(val[0]));
        v.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val[0]++;
                numText.setText(String.valueOf(val[0]));
            }
        });
        v.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(val[0]>0){
                    val[0]--;
                    numText.setText(String.valueOf(val[0]));
                }
            }
        });
        final EditText desc = (EditText)v.findViewById(R.id.desc_edit);
        desc.setText(ts.get(pos).desc);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ts.get(pos).name = name.getText().toString();
                ts.get(pos).val = val[0];
                ts.get(pos).desc = desc.getText().toString();
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
        if(!newTalent){
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
