package com.apps.darkstorm.swrpg.UI.Char;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Talent;

public class TalentLayout {
    public LinearLayout TalentLayout(final Context main, final LinearLayout talentLay, final Character chara, final Talent t){
        LinearLayout top = new LinearLayout(main);
        LinearLayout.LayoutParams toplp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        toplp.setMargins(4,4,4,4);
        toplp.setMarginEnd(4);
        toplp.setMarginStart(4);
        top.setLayoutParams(toplp);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless,outVal,true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            top.setForeground(main.getDrawable(outVal.resourceId));
        else
            top.setBackgroundResource(outVal.resourceId);
        final TextView name = new TextView(main);
        LinearLayout.LayoutParams namelp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextSize(16);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        name.setText(t.name);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(main);
                final Talent tmp = t;
                dia.setContentView(R.layout.dialog_talent_edit);
                final EditText nameVal = (EditText)dia.findViewById(R.id.talent_name);
                nameVal.setText(tmp.name);
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
                dia.findViewById(R.id.talent_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.talent_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = nameVal.getText().toString();
                        tmp.desc = desc.getText().toString();
                        chara.talents.set(tmp,t);
                        name.setText(tmp.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.talent_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tmp = chara.talents.remove(t);
                        talentLay.removeViewAt(tmp);
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
                return true;
            }
        });
        top.addView(name);
        return top;
    }
}
