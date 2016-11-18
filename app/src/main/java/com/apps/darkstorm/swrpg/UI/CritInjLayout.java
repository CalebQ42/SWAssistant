package com.apps.darkstorm.swrpg.UI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.CriticalInjury;
import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.StarWars.Vehicle;

public class CritInjLayout {
    public LinearLayout CritInjLayout(final Context main,final LinearLayout critLay,final Character chara,final CriticalInjury ci){
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
        name.setText(ci.name);
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_critical_injury_edit);
                final EditText nameVal = (EditText)dia.findViewById(R.id.crit_name);
                nameVal.setText(ci.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(ci.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(main,R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(ci.severity);
                dia.findViewById(R.id.crit_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.crit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ci.name = nameVal.getText().toString();
                        ci.desc = desc.getText().toString();
                        ci.severity = sev.getSelectedItemPosition();
                        name.setText(ci.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.crit_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tmp = chara.critInjuries.remove(ci);
                        critLay.removeViewAt(tmp);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.crit_cancel).setOnClickListener(new View.OnClickListener() {
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
    public LinearLayout CritInjLayout(final Context main, final LinearLayout critLay, final Vehicle vh, final CriticalInjury ci){
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
        name.setText(ci.name);
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_critical_injury_edit);
                final EditText nameVal = (EditText)dia.findViewById(R.id.crit_name);
                nameVal.setText(ci.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(ci.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(main,R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(ci.severity);
                dia.findViewById(R.id.crit_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.crit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ci.name = nameVal.getText().toString();
                        ci.desc = desc.getText().toString();
                        ci.severity = sev.getSelectedItemPosition();
                        name.setText(ci.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.crit_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tmp = vh.crits.remove(ci);
                        critLay.removeViewAt(tmp);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.crit_cancel).setOnClickListener(new View.OnClickListener() {
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
