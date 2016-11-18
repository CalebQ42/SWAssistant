package com.apps.darkstorm.swrpg.UI;

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
import com.apps.darkstorm.swrpg.StarWars.Stuff.WeapChar;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Weapon;

public class WeapCharLayout {
    public LinearLayout WeapCharLayout(final Context main, final LinearLayout charLayout, final Weapon weap, final WeapChar wc){
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
        LinearLayout.LayoutParams namelp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setText(wc.name + " " + String.valueOf(wc.val));
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_weapon_char);
                final EditText nameVal = (EditText)dia.findViewById(R.id.weapon_char_name);
                nameVal.setText(wc.name);
                final EditText val = (EditText)dia.findViewById(R.id.weapon_char_value);
                val.setText(String.valueOf(wc.val));
                final EditText adv = (EditText)dia.findViewById(R.id.weapon_char_adv);
                adv.setText(String.valueOf(wc.adv));
                dia.findViewById(R.id.weapon_char_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.weapon_char_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wc.name = nameVal.getText().toString();
                        if (!val.getText().toString().equals("")){
                            wc.val = Integer.parseInt(val.getText().toString());
                        }else{
                            wc.val = 0;
                        }
                        if (!adv.getText().toString().equals("")){
                            wc.adv = Integer.parseInt(adv.getText().toString());
                        }else{
                            wc.adv = 0;
                        }
                        name.setText(wc.name + " " + String.valueOf(wc.val));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.weapon_char_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tmp = weap.chars.remove(wc);
                        charLayout.removeViewAt(tmp);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.weapon_char_cancel).setOnClickListener(new View.OnClickListener() {
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
