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
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Duty;
import com.apps.darkstorm.swrpg.StarWars.Character;

public class DutyLayout {
    public LinearLayout DutyLayout(final Context main, final LinearLayout dutyLay, final Character chara, final Duty d){
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
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        name.setText(d.name);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_dutobli);
                ((TextView)dia.findViewById(R.id.dutobli_name)).setText(main.getResources().getString(R.string.duty_text));
                final EditText duty = (EditText)dia.findViewById(R.id.dutobli_edit);
                duty.setText(d.name);
                final EditText val = (EditText)dia.findViewById(R.id.dutobli_val);
                val.setText(String.valueOf(d.val));
                dia.findViewById(R.id.dutobli_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.dutobli_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.name = duty.getText().toString();
                        if (!val.getText().toString().equals(""))
                            d.val = Integer.parseInt(val.getText().toString());
                        else
                            d.val = 0;
                        name.setText(d.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.dutobli_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tmp = chara.duty.remove(d);
                        dutyLay.removeViewAt(tmp);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.dutobli_cancel).setOnClickListener(new View.OnClickListener() {
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
