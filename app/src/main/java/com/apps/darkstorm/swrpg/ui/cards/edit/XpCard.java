package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Character;

public class XpCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_xp,root,false);
        final TextView xpCur = (TextView)top.findViewById(R.id.xp_current_text);
        final TextView xpTot = (TextView)top.findViewById(R.id.xp_total_text);
        xpCur.setText(String.valueOf(chara.xpCur));
        xpTot.setText(String.valueOf(chara.xpTot));
        top.findViewById(R.id.xp_total_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.total_xp_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(String.valueOf(chara.xpTot)));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals(""))
                            chara.xpTot = Integer.parseInt(val.getText().toString());
                        else
                            chara.xpTot = 0;
                        xpTot.setText(String.valueOf(chara.xpTot));
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
        top.findViewById(R.id.xp_current_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.current_xp_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(String.valueOf(chara.xpCur)));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals(""))
                            chara.xpCur = Integer.parseInt(val.getText().toString());
                        else
                            chara.xpCur = 0;
                        xpCur.setText(String.valueOf(chara.xpCur));
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
        top.findViewById(R.id.xp_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.xp_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText("");
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")) {
                            int tmp = Integer.parseInt(val.getText().toString());
                            chara.xpCur += tmp;
                            chara.xpTot += tmp;
                            xpCur.setText(String.valueOf(chara.xpCur));
                            xpTot.setText(String.valueOf(chara.xpTot));
                        }
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
