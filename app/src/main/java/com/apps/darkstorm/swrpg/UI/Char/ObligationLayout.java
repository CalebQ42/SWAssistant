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
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Obligation;
import com.apps.darkstorm.swrpg.StarWars.Character;

public class ObligationLayout {
    public LinearLayout ObligationLayout(final Context main, final LinearLayout obligationLay, final Character chara, final Obligation o) {
        LinearLayout top = new LinearLayout(main);
        LinearLayout.LayoutParams toplp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        toplp.setMargins(4, 4, 4, 4);
        toplp.setMarginEnd(4);
        toplp.setMarginStart(4);
        top.setLayoutParams(toplp);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, outVal, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            top.setForeground(main.getDrawable(outVal.resourceId));
        else
            top.setBackgroundResource(outVal.resourceId);
        final TextView name = new TextView(main);
        LinearLayout.LayoutParams namelp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        name.setText(o.name);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_dutobli);
                ((TextView) dia.findViewById(R.id.dutobli_name)).setText(main.getResources().getString(R.string.obligation_text));
                final EditText obligation = (EditText) dia.findViewById(R.id.dutobli_edit);
                obligation.setText(o.name);
                final EditText val = (EditText) dia.findViewById(R.id.dutobli_val);
                val.setText(String.valueOf(o.val));
                dia.findViewById(R.id.dutobli_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.dutobli_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        o.name = obligation.getText().toString();
                        if (!val.getText().toString().equals(""))
                            o.val = Integer.parseInt(val.getText().toString());
                        else
                            o.val = 0;
                        name.setText(o.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.dutobli_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tmp = chara.obligation.remove(o);
                        obligationLay.removeViewAt(tmp);
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
