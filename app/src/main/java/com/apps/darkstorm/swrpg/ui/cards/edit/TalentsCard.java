package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.sw.stuff.Talent;
import com.apps.darkstorm.swrpg.ui.character.TalentLayout;

public class TalentsCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_talents,root,false);
        for (int i =0;i<chara.talents.size();i++)
            ((LinearLayout)top.findViewById(R.id.talents_layout)).addView
                    (new TalentLayout().TalentLayout(main,((LinearLayout)
                            top.findViewById(R.id.talents_layout)),chara,chara.talents.get(i)));
        top.findViewById(R.id.talents_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_talent_edit,null);
                build.setView(dia);
                final Talent tmp = new Talent();
                final EditText name = (EditText)dia.findViewById(R.id.talent_name);
                name.setText(tmp.name);
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
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = name.getText().toString();
                        tmp.desc = desc.getText().toString();
                        chara.talents.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.talents_layout)).addView
                                (new TalentLayout().TalentLayout(main,((LinearLayout)
                                        top.findViewById(R.id.talents_layout)),chara,chara.talents.get(chara.talents.size()-1)));
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
        final View top = main.getLayoutInflater().inflate(R.layout.edit_talents,root);
        for (int i =0;i<minion.talents.size();i++)
            ((LinearLayout)top.findViewById(R.id.talents_layout)).addView
                    (new TalentLayout().TalentLayout(main,((LinearLayout)
                            top.findViewById(R.id.talents_layout)),minion,minion.talents.get(i)));
        top.findViewById(R.id.talents_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_talent_edit,null);
                build.setView(dia);
                final Talent tmp = new Talent();
                final EditText name = (EditText)dia.findViewById(R.id.talent_name);
                name.setText(tmp.name);
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
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = name.getText().toString();
                        tmp.desc = desc.getText().toString();
                        minion.talents.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.talents_layout)).addView
                                (new TalentLayout().TalentLayout(main,((LinearLayout)
                                        top.findViewById(R.id.talents_layout)),minion,minion.talents.get(minion.talents.size()-1)));
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
