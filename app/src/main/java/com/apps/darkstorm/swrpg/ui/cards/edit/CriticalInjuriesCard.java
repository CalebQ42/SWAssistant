package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.sw.Vehicle;
import com.apps.darkstorm.swrpg.sw.stuff.CriticalInjury;
import com.apps.darkstorm.swrpg.ui.CritInjLayout;

public class CriticalInjuriesCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_critical_injuries,root,false);
        final LinearLayout critLay = (LinearLayout)top.findViewById(R.id.critical_injuries_layout);
        for (int i = 0;i<chara.critInjuries.size();i++)
            critLay.addView(new CritInjLayout().CritInjLayout(main,critLay,chara,chara.critInjuries.get(i)));
        top.findViewById(R.id.critical_injuries_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CriticalInjury tmp = new CriticalInjury();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_critical_injury_edit,null);
                build.setView(dia);
                final EditText name = (EditText)dia.findViewById(R.id.crit_name);
                name.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(tmp.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(top.getContext(),R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(tmp.severity);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        tmp.name = name.getText().toString();
                        tmp.desc = desc.getText().toString();
                        tmp.severity = sev.getSelectedItemPosition();
                        chara.critInjuries.add(tmp);
                        critLay.addView(new CritInjLayout().CritInjLayout(main,critLay,chara,
                                chara.critInjuries.get(chara.critInjuries.size()-1)));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Minion minion){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_critical_injuries,root);
        final LinearLayout critLay = (LinearLayout)top.findViewById(R.id.critical_injuries_layout);
        for (int i = 0;i<minion.critInjuries.size();i++)
            critLay.addView(new CritInjLayout().CritInjLayout(main,critLay,minion,minion.critInjuries.get(i)));
        top.findViewById(R.id.critical_injuries_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CriticalInjury tmp = new CriticalInjury();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_critical_injury_edit,null);
                build.setView(dia);
                final EditText name = (EditText)dia.findViewById(R.id.crit_name);
                name.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(tmp.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(top.getContext(),R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(tmp.severity);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        tmp.name = name.getText().toString();
                        tmp.desc = desc.getText().toString();
                        tmp.severity = sev.getSelectedItemPosition();
                        minion.critInjuries.add(tmp);
                        critLay.addView(new CritInjLayout().CritInjLayout(main,critLay,minion,
                                minion.critInjuries.get(minion.critInjuries.size()-1)));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Vehicle vh){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_critical_injuries,root);
        final LinearLayout critLay = (LinearLayout)top.findViewById(R.id.critical_injuries_layout);
        for (int i = 0;i<vh.crits.size();i++)
            critLay.addView(new CritInjLayout().CritInjLayout(main,critLay,vh,vh.crits.get(i)));
        top.findViewById(R.id.critical_injuries_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CriticalInjury tmp = new CriticalInjury();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_critical_injury_edit,null);
                build.setView(dia);
                final EditText name = (EditText)dia.findViewById(R.id.crit_name);
                name.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(tmp.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(top.getContext(),R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(tmp.severity);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        tmp.name = name.getText().toString();
                        tmp.desc = desc.getText().toString();
                        tmp.severity = sev.getSelectedItemPosition();
                        vh.crits.add(tmp);
                        critLay.addView(new CritInjLayout().CritInjLayout(main,critLay,vh,
                                vh.crits.get(vh.crits.size()-1)));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        return top;
    }
}
