package com.apps.darkstorm.swrpg.assistant.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.load.DriveLoadCharacters;
import com.apps.darkstorm.swrpg.assistant.load.DriveLoadMinions;
import com.apps.darkstorm.swrpg.assistant.load.DriveLoadVehicles;
import com.apps.darkstorm.swrpg.assistant.load.LoadCharacters;
import com.apps.darkstorm.swrpg.assistant.load.LoadMinions;
import com.apps.darkstorm.swrpg.assistant.load.LoadVehicles;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.apps.darkstorm.swrpg.assistant.R;

import java.io.Serializable;
import java.util.ArrayList;

public class NameCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_name,root,false);
        ((TextView)top.findViewById(R.id.name_text)).setText(chara.name);
        top.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.name_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                val.setText(chara.name);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chara.name = val.getText().toString();
                        ((TextView)top.findViewById(R.id.name_text)).setText(chara.name);
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
        top.findViewById(R.id.clone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main,R.string.cloning_text,Toast.LENGTH_SHORT).show();
                AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false)){
                            DriveLoadCharacters dlc = new DriveLoadCharacters(main);
                            dlc.saveLocal(main);
                        }
                        LoadCharacters lc = new LoadCharacters(main);
                        ArrayList<Integer> taken = new ArrayList<>();
                        for(Character chara:lc.characters)
                            taken.add(chara.ID);
                        int id = 0;
                        for (int i = 0;i<taken.size();i++){
                            if(taken.get(i)==id){
                                id++;
                                i = -1;
                            }
                        }
                        Character clone = chara.clone();
                        clone.ID = id;
                        clone.save(clone.getFileLocation(main));
                        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false))
                            clone.cloudSave(((SWrpg)main.getApplication()).gac,clone.getFileId(main),false);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Toast.makeText(main,R.string.cloning_done_text,Toast.LENGTH_SHORT).show();
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Minion minion){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_name,root,false);
        ((TextView)top.findViewById(R.id.name_text)).setText(minion.name);
        top.findViewById(R.id.name_card).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.name_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                val.setText(minion.name);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        minion.name = val.getText().toString();
                        ((TextView)top.findViewById(R.id.name_text)).setText(minion.name);
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
        top.findViewById(R.id.clone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main,R.string.cloning_text,Toast.LENGTH_SHORT).show();
                AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false)){
                            DriveLoadMinions dlc = new DriveLoadMinions(main);
                            dlc.saveLocal(main);
                        }
                        LoadMinions lc = new LoadMinions(main);
                        ArrayList<Integer> taken = new ArrayList<>();
                        for(Minion chara:lc.minions)
                            taken.add(chara.ID);
                        int id = 0;
                        for (int i = 0;i<taken.size();i++){
                            if(taken.get(i)==id){
                                id++;
                                i = -1;
                            }
                        }
                        Minion clone = minion.clone();
                        clone.ID = id;
                        clone.save(clone.getFileLocation(main));
                        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false))
                            clone.cloudSave(((SWrpg)main.getApplication()).gac,clone.getFileId(main),false);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Toast.makeText(main,R.string.cloning_done_text,Toast.LENGTH_SHORT).show();
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        top.findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra("Mini", (Serializable) minion);
                intent.setType("resource/folder");
                main.startActivityForResult(intent,20);
            }
        });
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Vehicle vehic){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_name,root,false);
        ((TextView)top.findViewById(R.id.name_text)).setText(vehic.name);
        top.findViewById(R.id.name_card).setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.name_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                val.setText(vehic.name);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vehic.name = val.getText().toString();
                        ((TextView)top.findViewById(R.id.name_text)).setText(vehic.name);
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
        top.findViewById(R.id.clone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main,R.string.cloning_text,Toast.LENGTH_SHORT).show();
                AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false)){
                            DriveLoadVehicles dlc = new DriveLoadVehicles(main);
                            dlc.saveLocal(main);
                        }
                        LoadVehicles lc = new LoadVehicles(main);
                        ArrayList<Integer> taken = new ArrayList<>();
                        for(Vehicle chara:lc.vehicles)
                            taken.add(chara.ID);
                        int id = 0;
                        for (int i = 0;i<taken.size();i++){
                            if(taken.get(i)==id){
                                id++;
                                i = -1;
                            }
                        }
                        Vehicle clone = vehic.clone();
                        clone.ID = id;
                        clone.save(clone.getFileLocation(main));
                        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false))
                            clone.cloudSave(((SWrpg)main.getApplication()).gac,clone.getFileId(main),false);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Toast.makeText(main,R.string.cloning_done_text,Toast.LENGTH_SHORT).show();
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        top.findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra("Vhcl", (Serializable) vehic);
                intent.setType("resource/folder");
                main.startActivityForResult(intent,20);
            }
        });
        return top;
    }
//    public static View getCard(final Activity main, ViewGroup root, final Character chara){
//        final View top = main.getLayoutInflater().inflate(R.layout.edit_wound_strain_character,root);
//        return top;
//    }
}
