package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.stuff.ForcePower;
import com.apps.darkstorm.swrpg.ui.character.ForceLayout;

public class ForcePowerCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_force_powers,root);
        final TextView rat = (TextView)top.findViewById(R.id.force_rating_text);
        rat.setText(String.valueOf(chara.force));
        top.findViewById(R.id.force_rating_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_edit,null);
                build.setView(dia);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources()
                        .getString(R.string.force_rating_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(chara.force));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            chara.force = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.force = 0;
                        }
                        rat.setText(String.valueOf(chara.force));
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
        final LinearLayout fpLay = (LinearLayout)top.findViewById(R.id.force_powers_layout);
        for (int i =0;i<chara.forcePowers.size();i++)
            fpLay.addView(new ForceLayout().ForceLayout(main,fpLay,chara,chara.forcePowers.get(i)));
        top.findViewById(R.id.force_powers_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ForcePower tmp = new ForcePower();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_edit,null);
                build.setView(dia);
                dia.findViewById(R.id.edit_desc_main).setVisibility(View.VISIBLE);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.force_power_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.edit_desc_val);
                desc.setText(tmp.desc);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = val.getText().toString();
                        tmp.desc = desc.getText().toString();
                        chara.forcePowers.add(tmp);
                        fpLay.addView(new ForceLayout().ForceLayout(main,fpLay,chara,chara.forcePowers.get(chara.forcePowers.size()-1)));
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
