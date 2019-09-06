package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Specializations  implements JsonSavable {
    String[] specs;
    public Specializations(){
        specs = new String[0];
    }
    public void add(String sp){
        specs = Arrays.copyOf(specs,specs.length+1);
        specs[specs.length-1] = sp;
    }
    public int remove(String sp){
        int i = -1;
        for (int j = 0;j<specs.length;j++){
            if (specs[j].equals(sp)){
                i = j;
                break;
            }
        }
        if (i != -1){
            String[] s = new String[specs.length-1];
            for (int j = 0;j<i;j++){
                s[j] = specs[j];
            }
            for (int j = i+1;j<specs.length;j++){
                s[j-1] = specs[j];
            }
            specs = s;
        }
        return i;
    }
    public String get(int i){
        return specs[i];
    }
    public int size(){
        return specs.length;
    }
    public Object serialObject(){
        return specs;
    }
    public void loadFromObject(Object o){
        specs = (String[])o;
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Specializations))
            return false;
        Specializations in = (Specializations)obj;
        if(in.specs.length != specs.length)
            return false;
        for (int i = 0;i<specs.length;i++){
            if (!in.specs[i].equals(specs[i]))
                return false;
        }
        return true;
    }
    public Specializations clone(){
        Specializations out = new Specializations();
        out.specs = specs.clone();
        return out;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.name("Specializations").beginArray();
        for(String s:specs)
            jw.value(s);
        jw.endArray();
    }

    public void loadJson(JsonReader jr) throws IOException{
        ArrayList<String> out = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()){
            out.add(jr.nextString());
        }
        jr.endArray();
        specs = out.toArray(specs);
    }

    public static class SpecializationsAdapter extends RecyclerView.Adapter<SpecializationsAdapter.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ((TextView)holder.v.findViewById(R.id.name)).setText(s.specializations.get(position));
            holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    editSpecialization(ac, s, holder.getAdapterPosition(),false , new Skill.onSave() {
                        public void save() {
                            SpecializationsAdapter.this.notifyDataSetChanged();
                        }
                        public void delete() {
                            s.specializations.remove(s.specializations.get(holder.getAdapterPosition()));
                            SpecializationsAdapter.this.notifyDataSetChanged();
                        }
                        public void cancel() {}
                    });
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return s.specializations.size();
        }

        Activity ac;
        Character s;

        public SpecializationsAdapter(Character s,Activity ac){
            this.ac = ac;
            this.s = s;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            View v;
            ViewHolder(View v){
                super(v);
                this.v = v;
            }
        }
    }

    public static void editSpecialization(Activity ac, final Character c, final int pos, boolean newSpecial, final Skill.onSave os){
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        View v = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
        b.setView(v);
        TextInputLayout ti = (TextInputLayout)v.findViewById(R.id.edit_layout);
        ti.setHint(ac.getString(R.string.specializations_text));
        final EditText e = (EditText)v.findViewById(R.id.edit_text);
        e.setText(c.specializations.get(pos));
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                c.specializations.specs[pos] = e.getText().toString();
                os.save();
                dialog.cancel();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                os.cancel();
                dialog.cancel();
            }
        });
        if(!newSpecial){
            b.setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    os.delete();
                    dialog.cancel();
                }
            });
        }
        b.show();
    }
}
