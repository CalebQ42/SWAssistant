package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Character;

public class EmotionsCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_emotions,root);
        final TextView morality = (TextView)top.findViewById(R.id.morality_text);
        morality.setText(String.valueOf(chara.morality));
        final TextView conflict = (TextView)top.findViewById(R.id.conflict_text);
        conflict.setText(String.valueOf(chara.conflict));
        top.findViewById(R.id.morality_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.morality_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.morality));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals(""))
                            chara.morality = Integer.parseInt(val.getText().toString());
                        else
                            chara.morality = 0;
                        morality.setText(String.valueOf(chara.morality));
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
        top.findViewById(R.id.conflict_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.conflict_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.conflict));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals(""))
                            chara.conflict = Integer.parseInt(val.getText().toString());
                        else
                            chara.conflict = 0;
                        conflict.setText(String.valueOf(chara.conflict));
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
        top.findViewById(R.id.resolve_conflict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chara.resolveConflict();
                morality.setText(String.valueOf(chara.morality));
                conflict.setText(String.valueOf(chara.conflict));
            }
        });
        Switch dkSide = (Switch)top.findViewById(R.id.dark_side_switch);
        dkSide.setChecked(chara.darkSide);
        dkSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chara.darkSide = isChecked;
            }
        });
        final TextView strength = (TextView)top.findViewById(R.id.emotional_strength_text);
        strength.setText(chara.emotionalStr[0]);
        top.findViewById(R.id.emotional_strength_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.strength_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setText(chara.emotionalStr[0]);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chara.emotionalStr[0] = val.getText().toString();
                        strength.setText(chara.emotionalStr[0]);
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
        final TextView weakness = (TextView)top.findViewById(R.id.emotional_weakness_text);
        weakness.setText(chara.emotionalWeak[0]);
        top.findViewById(R.id.emotional_weakness_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.weakness_text);
                final EditText val = (EditText)dia.findViewById(R.id.editText);
                val.setText(chara.emotionalWeak[0]);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chara.emotionalWeak[0] = val.getText().toString();
                        weakness.setText(chara.emotionalWeak[0]);
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
        return top;
    }
}
