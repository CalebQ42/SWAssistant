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
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Dutys implements JsonSavable {
    Duty[] d;
    public Dutys(){
        d = new Duty[0];
    }
    public void add(Duty du){
        d = Arrays.copyOf(d,d.length+1);
        d[d.length-1] = du;
    }
    public int remove(Duty dy){
        int i =-1;
        for (int j = 0;j<d.length;j++){
            if (d[j].equals(dy)){
                i = j;
                break;
            }
        }
        if (i!= -1) {
            Duty[] newD = new Duty[d.length - 1];
            for (int j = 0; j < i; j++)
                newD[j] = d[j];
            for (int j = i + 1; j < d.length; j++)
                newD[j - 1] = d[j];
            d = newD;
        }
        return i;
    }
    public Duty get(int i){
        return d[i];
    }
    public int size(){
        return d.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Duty du:d){
            tmp.add(du.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Duty> out = new ArrayList<>();
        for (Object o:tmp){
            Duty du = new Duty();
            du.loadFromObject(o);
            out.add(du);
        }
        d = out.toArray(d);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Dutys))
            return false;
        Dutys in = (Dutys)obj;
        if(in.d.length != d.length)
            return false;
        for (int i = 0;i<d.length;i++){
            if (!in.d[i].equals(d[i]))
                return false;
        }
        return true;
    }
    public Dutys clone(){
        Dutys out = new Dutys();
        out.d = new Duty[d.length];
        for (int i = 0;i<d.length;i++){
            out.d[i] = d[i].clone();
        }
        return out;
    }

    public void saveJson(JsonWriter jw) throws IOException {
        jw.name("Dutys").beginArray();
        for(Duty dt:d)
            dt.saveJson(jw);
        jw.endArray();
    }

    public void loadJson(JsonReader jw) throws IOException {
        ArrayList<Duty> out = new ArrayList<>();
        jw.beginArray();
        while(jw.hasNext()){
            Duty du = new Duty();
            du.loadJson(jw);
            out.add(du);
        }
        jw.endArray();
        d = out.toArray(d);
    }

    public static class DutysAdap extends RecyclerView.Adapter<DutysAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
                ((TextView) holder.v.findViewById(R.id.name)).setText(c.duty.get(holder.getAdapterPosition()).name);
                holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Duty.editDuty(ac, c, holder.getAdapterPosition(), false, new Skill.onSave() {
                            public void save() {
                                DutysAdap.this.notifyItemChanged(holder.getAdapterPosition());
                            }

                            public void delete() {
                                int ind = c.duty.remove(c.duty.get(holder.getAdapterPosition()));
                                DutysAdap.this.notifyItemRemoved(ind);
                            }

                            public void cancel() {}
                        });
                        return true;
                    }
                });
        }

        @Override
        public int getItemCount() {
            return c.duty.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            View v;
            ViewHolder(View v){
                super(v);
                this.v = v;
            }
        }
        Character c;
        Activity ac;
        public DutysAdap(Character c, Activity ac){
            this.c = c;
            this.ac = ac;
        }
    }
}
