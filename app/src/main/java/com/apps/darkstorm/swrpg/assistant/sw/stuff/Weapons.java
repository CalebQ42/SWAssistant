package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.DiceRollFragment;
import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.dice.DiceHolder;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Weapons implements JsonSavable {
    Weapon[] w;
    public Weapons(){
        w = new Weapon[0];
    }
    public void add(Weapon weap){
        w = Arrays.copyOf(w,w.length+1);
        w[w.length-1] = weap;
    }
    public int remove(Weapon wn){
        int i = -1;
        for (int j = 0;j<w.length;j++){
            if (w[j].equals(wn)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            Weapon[] newW = new Weapon[w.length - 1];
            for (int j = 0; j < i; j++)
                newW[j] = w[j];
            for (int j = i + 1; j < w.length; j++)
                newW[j - 1] = w[j];
            w = newW;
        }
        return i;
    }
    public Weapon get(int i){
        return w[i];
    }
    public int size(){
        return w.length;
    }
    public void set(Weapon old, Weapon newW){
        int i = -1;
        for (int j = 0;j<w.length;j++){
            if (w[j].equals(old)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            w[i] = newW;
        }
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Weapon wn: w)
            tmp.add(wn.serialObject());
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Weapon> out = new ArrayList<>();
        for (Object o : tmp) {
            Weapon wn = new Weapon();
            wn.loadFromObject(o);
            out.add(wn);
        }
        w = out.toArray(w);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Weapons))
            return false;
        Weapons in = (Weapons)obj;
        if(in.w.length != w.length)
            return false;
        for (int i = 0;i<w.length;i++){
            if (!in.w[i].equals(w[i]))
                return false;
        }
        return true;
    }
    public Weapons clone(){
        Weapons out = new Weapons();
        out.w = new Weapon[w.length];
        for (int i = 0;i<w.length;i++)
            out.w[i] = w[i].clone();
        return out;
    }
    public int totalEncum(){
        int total = 0;
        for (Weapon we:w)
            total += we.encum;
        return total;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.name("Weapons").beginArray();
        for(Weapon wp:w)
            wp.saveJson(jw);
        jw.endArray();
    }

    public void loadJson(JsonReader jr) throws IOException{
        ArrayList<Weapon> out = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()){
            Weapon tmp = new Weapon();
            tmp.loadJson(jr);
            out.add(tmp);
        }
        jr.endArray();
        w = out.toArray(w);
    }

    public static class WeaponsAdap extends RecyclerView.Adapter<WeaponsAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ((TextView)holder.v.findViewById(R.id.name)).setText(c.weapons.get(position).name);
            holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Weapon.editWeapon(ac,c,holder.getAdapterPosition(),false,new Skill.onSave(){
                        public void save() {
                            WeaponsAdap.this.notifyItemChanged(holder.getAdapterPosition());
                            os.save();
                        }
                        public void delete() {
                            int ind = c.weapons.remove(c.weapons.get(holder.getAdapterPosition()));
                            WeaponsAdap.this.notifyItemRemoved(ind);
                            os.delete();
                        }
                        public void cancel() {
                            os.cancel();
                        }
                    });
                    return true;
                }
            });
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(ac);
                    final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll,null);
                    b.setView(view);
                    view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                    view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                    view.findViewById(R.id.fab_space).setVisibility(View.GONE);
                    view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
                    view.findViewById(R.id.dice_label).setVisibility(View.GONE);
                    final DiceHolder dh = new DiceHolder();
                    if(c instanceof Character) {
                        Character ch = (Character)c;
                        String[] weapSkills = ac.getResources().getStringArray(R.array.weapon_skills);
                        int skillVal = 0;
                        for (int i = 0; i < ch.skills.size(); i++) {
                            if (ch.skills.get(i).name.equals(weapSkills[ch.weapons.get(holder.getAdapterPosition()).skill])) {
                                skillVal = ch.skills.get(i).val;
                                break;
                            }
                        }
                        if (ch.charVals[ch.weapons.get(holder.getAdapterPosition()).skillBase] > skillVal) {
                            dh.ability = ch.charVals[ch.weapons.get(holder.getAdapterPosition()).skillBase] - skillVal;
                            dh.proficiency = skillVal;
                        } else {
                            dh.ability = skillVal - ch.charVals[ch.weapons.get(holder.getAdapterPosition()).skillBase];
                            dh.proficiency = ch.charVals[ch.weapons.get(holder.getAdapterPosition()).skillBase];
                        }
                    }else if (c instanceof Minion){
                        Minion ch = (Minion)c;
                        String[] weapSkills = ac.getResources().getStringArray(R.array.weapon_skills);
                        int skillVal = 0;
                        for (int i = 0; i < ch.skills.size(); i++) {
                            if (ch.skills.get(i).name.equals(weapSkills[ch.weapons.get(holder.getAdapterPosition()).skill])) {
                                skillVal = ch.skills.get(i).val;
                                break;
                            }
                        }
                        if (ch.charVals[ch.weapons.get(holder.getAdapterPosition()).skillBase] > skillVal) {
                            dh.ability = ch.charVals[ch.weapons.get(holder.getAdapterPosition()).skillBase] - skillVal;
                            dh.proficiency = skillVal;
                        } else {
                            dh.ability = skillVal - ch.charVals[ch.weapons.get(holder.getAdapterPosition()).skillBase];
                            dh.proficiency = ch.charVals[ch.weapons.get(holder.getAdapterPosition()).skillBase];
                        }
                    }
                    final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac,dh);
                    RecyclerView r = (RecyclerView)view.findViewById(R.id.dice_recycler);
                    r.setAdapter(dl);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dh.roll().showWeaponDialog(ac,c.weapons.get(holder.getAdapterPosition()));
                            dialog.cancel();
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    b.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return c.weapons.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            View v;
            ViewHolder(View v){
                super(v);
                this.v = v;
            }
        }
        Editable c;
        Activity ac;
        Skill.onSave os;
        public WeaponsAdap(Editable c,Skill.onSave os, Activity ac){
            this.c = c;
            this.ac = ac;
            this.os = os;
        }
    }
}
