package com.apps.darkstorm.swrpg.assistant.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.R;

public class SpeciesCareerCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_species_career,root,false);
        ((TextView)top.findViewById(R.id.species_text)).setText(chara.species);
        ((TextView)top.findViewById(R.id.career_text)).setText(chara.career);
        TextView motivation = (TextView)top.findViewById(R.id.motivation_text);
        motivation.setText(chara.motivation);
        TextView age = (TextView)top.findViewById(R.id.age_text);
        age.setText(String.valueOf(chara.age));
        top.findViewById(R.id.motivation_layout).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.motivation_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(chara.motivation);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chara.motivation = val.getText().toString();
                        ((TextView)top.findViewById(R.id.motivation_text)).setText(chara.motivation);
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
        top.findViewById(R.id.age_layout).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(chara.age));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals(""))
                            chara.age = Integer.parseInt(val.getText().toString());
                        else
                            chara.age = 0;
                        ((TextView)top.findViewById(R.id.age_text)).setText(String.valueOf(chara.age));
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
        top.findViewById(R.id.species_layout).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.species_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(chara.species);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chara.species = val.getText().toString();
                        ((TextView)top.findViewById(R.id.species_text)).setText(chara.species);
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
        top.findViewById(R.id.career_layout).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.career_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(chara.career);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chara.career = val.getText().toString();
                        ((TextView)top.findViewById(R.id.career_text)).setText(chara.career);
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
