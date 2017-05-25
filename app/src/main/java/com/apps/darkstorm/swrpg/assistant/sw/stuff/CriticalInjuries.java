package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CriticalInjuries implements JsonSavable {
    CriticalInjury[] critInj;
    public CriticalInjuries(){
        critInj = new CriticalInjury[0];
    }
    public void add(CriticalInjury ci){
        CriticalInjury[] newInj = Arrays.copyOf(critInj,critInj.length+1);
        newInj[critInj.length] = ci;
        critInj = newInj;
    }
    public CriticalInjury get(int i){
        return critInj[i];
    }
    public int size(){
        return critInj.length;
    }
    public int remove(CriticalInjury ci){
        int i =-1;
        for (int j =0;j<critInj.length;j++){
            if (critInj[j].equals(ci)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            CriticalInjury[] newInj = new CriticalInjury[critInj.length - 1];
            for (int j = 0; j < i; j++)
                newInj[j] = critInj[j];
            for (int j = i + 1; j < critInj.length; j++)
                newInj[j - 1] = critInj[j];
            critInj = newInj;
        }
        return i;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (CriticalInjury ci:critInj){
            tmp.add(ci.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<CriticalInjury> out = new ArrayList<>();
        for (Object o:tmp){
            CriticalInjury ci = new CriticalInjury();
            ci.loadFromObject(o);
            out.add(ci);
        }
        critInj = out.toArray(critInj);
    }
    public boolean equals(Object obj){
        if (obj instanceof CriticalInjuries){
            CriticalInjuries in = (CriticalInjuries)obj;
            if (in.critInj.length!=critInj.length){
                return false;
            }
            for (int i = 0;i<critInj.length;i++){
                if (!critInj[i].equals(in.critInj[i]))
                    return false;
            }
            return true;
        }else{
            return false;
        }
    }
    public CriticalInjuries clone(){
        CriticalInjuries out = new CriticalInjuries();
        out.critInj = new CriticalInjury[critInj.length];
        for(int i = 0;i<critInj.length;i++)
            out.critInj[i] = critInj[i].clone();
        return out;
    }

    public void saveJson(JsonWriter jw) throws IOException {
        jw.name("Critical Injuries").beginArray();
        for (CriticalInjury ci: critInj)
            ci.saveJson(jw);
        jw.endArray();
    }

    public void loadJson(JsonReader jr) throws IOException{
        ArrayList<CriticalInjury> out = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()){
            CriticalInjury tmp = new CriticalInjury();
            tmp.loadJson(jr);
            out.add(tmp);
        }
        jr.endArray();
        critInj = out.toArray(critInj);
    }

    public static class CriticalInjuriesAdapChar extends RecyclerView.Adapter<CriticalInjuriesAdapChar.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ((TextView)holder.v.findViewById(R.id.name)).setText(c.critInjuries.get(position).name);
            holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CriticalInjury.editCritical(ac,c,holder.getAdapterPosition(),false,new Skill.onSave(){
                        public void save() {
                            CriticalInjuriesAdapChar.this.notifyItemChanged(holder.getAdapterPosition());
                        }
                        public void delete() {
                            int ind = c.critInjuries.remove(c.critInjuries.get(holder.getAdapterPosition()));
                            CriticalInjuriesAdapChar.this.notifyItemRemoved(ind);
                        }
                        public void cancel() {}
                    });
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return c.critInjuries.size();
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
        public CriticalInjuriesAdapChar(Editable c,Activity ac){
            this.c = c;
            this.ac = ac;
        }
    }
}
