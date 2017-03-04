package com.apps.darkstorm.swrpg.assistant.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjury;
import com.apps.darkstorm.swrpg.assistant.R;

public class CritInjLayout {
    public LinearLayout CritInjLayout(final Activity main, final LinearLayout critLay, final Character chara, final CriticalInjury ci){
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
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_critical_injury_edit,null);
                build.setView(dia);
                final EditText nameVal = (EditText)dia.findViewById(R.id.crit_name);
                nameVal.setText(ci.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(ci.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(main,R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(ci.severity);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ci.name = nameVal.getText().toString();
                        ci.desc = desc.getText().toString();
                        ci.severity = sev.getSelectedItemPosition();
                        name.setText(ci.name);
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = chara.critInjuries.remove(ci);
                        critLay.removeViewAt(tmp);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.addView(name);
        return top;
    }
    public LinearLayout CritInjLayout(final Activity main, final LinearLayout critLay, final Vehicle vh, final CriticalInjury ci){
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
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_critical_injury_edit,null);
                build.setView(dia);
                final EditText nameVal = (EditText)dia.findViewById(R.id.crit_name);
                nameVal.setText(ci.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(ci.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(main,R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(ci.severity);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ci.name = nameVal.getText().toString();
                        ci.desc = desc.getText().toString();
                        ci.severity = sev.getSelectedItemPosition();
                        name.setText(ci.name);
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = vh.crits.remove(ci);
                        critLay.removeViewAt(tmp);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.addView(name);
        return top;
    }
    public LinearLayout CritInjLayout(final Activity main, final LinearLayout critLay, final Minion minion, final CriticalInjury ci){
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
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_critical_injury_edit,null);
                build.setView(dia);
                final EditText nameVal = (EditText)dia.findViewById(R.id.crit_name);
                nameVal.setText(ci.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(ci.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(main,R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(ci.severity);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ci.name = nameVal.getText().toString();
                        ci.desc = desc.getText().toString();
                        ci.severity = sev.getSelectedItemPosition();
                        name.setText(ci.name);
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = minion.critInjuries.remove(ci);
                        critLay.removeViewAt(tmp);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        top.addView(name);
        return top;
    }

}
