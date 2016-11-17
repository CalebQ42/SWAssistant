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

public class SpecializationLayout {
    public LinearLayout SpecializationLayout(final Context main,final LinearLayout specLay,final Character chara,final String spec){
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
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        name.setText(spec);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(main.getResources().getString(R.string.spec_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(spec);
                dia.findViewById(R.id.edit_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chara.specializations.add(val.getText().toString());
                        name.setText(val.getText().toString());
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tmp = chara.specializations.remove(spec);
                        specLay.removeViewAt(tmp);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
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
