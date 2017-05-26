package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class WeapChars implements JsonSavable {
    WeapChar[] wc;
    public WeapChars(){
        wc = new WeapChar[0];
    }
    public void add(WeapChar w){
        wc = Arrays.copyOf(wc,wc.length+1);
        wc[wc.length-1] = w;
    }
    public int remove(WeapChar w){
        int i = -1;
        for (int j = 0;j<wc.length;j++){
            if (wc[j].equals(w)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            WeapChar[] newWc = new WeapChar[wc.length - 1];
            for (int j = 0; j < i; j++)
                newWc[j] = wc[j];
            for (int j = i + 1; j < wc.length; j++)
                newWc[j - 1] = wc[j];
            wc = newWc;
        }
        return i;
    }
    public WeapChar get(int i){
        return wc[i];
    }
    public int size(){
        return wc.length;
    }
    public WeapChars clone(){
        WeapChars tmp = new WeapChars();
        tmp.wc = new WeapChar[wc.length];
        for(int i = 0;i<wc.length;i++)
            tmp.wc[i] = wc[i];
        return tmp;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (WeapChar w:wc){
            tmp.add(w.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<WeapChar> out = new ArrayList<>();
        for (Object o:tmp){
            WeapChar w = new WeapChar();
            w.loadFromObject(o);
            out.add(w);
        }
        wc = out.toArray(wc);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof WeapChars))
            return false;
        WeapChars in = (WeapChars)obj;
        if(in.wc.length != wc.length)
            return false;
        for (int i = 0;i<wc.length;i++){
            if (!in.wc[i].equals(wc[i]))
                return false;
        }
        return true;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.name("Weapon Characteristics").beginArray();
        for(WeapChar w:wc)
            w.saveJson(jw);
        jw.endArray();
    }

    public void loadJson(JsonReader jr) throws IOException{
        ArrayList<WeapChar> out = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()){
            WeapChar tmp = new WeapChar();
            tmp.loadJson(jr);
            out.add(tmp);
        }
        jr.endArray();
        wc = out.toArray(wc);
    }

    public static class WeapCharsAdap extends RecyclerView.Adapter<WeapCharsAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ((TextView)holder.v.findViewById(R.id.name)).setText(c.chars.get(position).name);
            holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    WeapChar.editWeapChar(ac,c,holder.getAdapterPosition(),false,new Skill.onSave(){
                        public void save() {
                            WeapCharsAdap.this.notifyItemChanged(holder.getAdapterPosition());
                        }
                        public void delete() {
                            int ind = c.chars.remove(c.chars.get(holder.getAdapterPosition()));
                            WeapCharsAdap.this.notifyItemRemoved(ind);
                        }
                        public void cancel() {}
                    });
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return c.chars.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            View v;
            ViewHolder(View v){
                super(v);
                this.v = v;
            }
        }
        Weapon c;
        Activity ac;
        public WeapCharsAdap(Weapon c,Activity ac){
            this.c = c;
            this.ac = ac;
        }
    }
}
