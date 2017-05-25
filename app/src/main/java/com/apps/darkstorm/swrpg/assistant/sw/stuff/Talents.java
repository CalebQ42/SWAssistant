package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Talents implements JsonSavable {
    Talent[] tal;
    public Talents(){
        tal= new Talent[0];
    }
    public void add(Talent t){
        tal = Arrays.copyOf(tal,tal.length+1);
        tal[tal.length -1] = t;
    }
    public int remove(Talent t){
        int i = -1;
        for (int j = 0;j<tal.length;j++){
            if (tal[j].equals(t)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            Talent[] newTal = new Talent[tal.length - 1];
            for (int j = 0; j < i; j++)
                newTal[j] = tal[j];
            for (int j = i + 1; j < tal.length; j++)
                newTal[j - 1] = tal[j];
            tal = newTal;
        }
        return i;
    }
    public Talent get(int i){
        return tal[i];
    }
    public int size(){
        return tal.length;
    }
    public void set(Talent newT, Talent old){
        int i = -1;
        for (int j = 0;j<tal.length;j++){
            if (tal[j].equals(old)){
                i = j;
                break;
            }
        }
        if (i != -1)
            tal[i] = newT;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Talent t:tal){
            tmp.add(t.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Talent> out = new ArrayList<>();
        for (Object o:tmp){
            Talent t = new Talent();
            t.loadFromObject(o);
            out.add(t);
        }
        tal = out.toArray(tal);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Talents))
            return false;
        Talents in = (Talents)obj;
        if(in.tal.length != tal.length)
            return false;
        for (int i = 0;i<tal.length;i++){
            if (!in.tal[i].equals(tal[i]))
                return false;
        }
        return true;
    }
    public Talents clone(){
        Talents out = new Talents();
        out.tal = new Talent[tal.length];
        for(int i = 0;i<tal.length;i++)
            out.tal[i] = tal[i].clone();
        return out;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.name("Talents").beginArray();
        for(Talent t:tal)
            t.saveJson(jw);
        jw.endArray();
    }

    public void loadJson(JsonReader jr) throws IOException{
        ArrayList<Talent> out = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()){
            Talent tmp = new Talent();
            tmp.loadJson(jr);
            out.add(tmp);
        }
        jr.endArray();
        tal = out.toArray(tal);
    }

    public static class TalentsAdap extends RecyclerView.Adapter<TalentsAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if(c instanceof Character) {
                final Talents ts = ((Character) c).talents;
                ((TextView)holder.v.findViewById(R.id.name)).setText(ts.get(position).name);
                holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Talent.editTalent(ac,c,holder.getAdapterPosition(),false,new Skill.onSave(){
                            public void save() {
                                TalentsAdap.this.notifyItemChanged(holder.getAdapterPosition());
                            }
                            public void delete() {
                                int ind = ts.remove(ts.get(holder.getAdapterPosition()));
                                TalentsAdap.this.notifyItemRemoved(ind);
                            }
                            public void cancel() {}
                        });
                        return true;
                    }
                });
            }else if(c instanceof Minion) {
                final Talents ts = ((Minion) c).talents;
                ((TextView)holder.v.findViewById(R.id.name)).setText(ts.get(position).name);
                holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Talent.editTalent(ac,c,holder.getAdapterPosition(),false,new Skill.onSave(){
                            public void save() {
                                TalentsAdap.this.notifyItemChanged(holder.getAdapterPosition());
                            }
                            public void delete() {
                                int ind = ts.remove(ts.get(holder.getAdapterPosition()));
                                TalentsAdap.this.notifyItemRemoved(ind);
                            }
                            public void cancel() {}
                        });
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            Talents ts;
            if(c instanceof Character)
                ts = ((Character)c).talents;
            else if(c instanceof Minion)
                ts = ((Minion)c).talents;
            else
                return 0;
            return ts.size();
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
        public TalentsAdap(Editable c,Activity ac){
            this.c = c;
            this.ac = ac;
        }
    }
}
