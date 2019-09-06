package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ForcePowers implements JsonSavable {
    ForcePower[] fp;
    public ForcePowers(){
        fp = new ForcePower[0];
    }
    public void add(ForcePower f){
        fp = Arrays.copyOf(fp,fp.length+1);
        fp[fp.length-1] = f;
    }
    public int remove(ForcePower f){
        int i =-1;
        for (int j = 0;j<fp.length;j++){
            if (fp[j].equals(f)){
                i = j;
                break;
            }
        }
        if (i!=-1) {
            ForcePower[] newFp = new ForcePower[fp.length - 1];
            for (int j = 0; j < i; j++)
                newFp[j] = fp[j];
            for (int j = i + 1; j < fp.length; j++)
                newFp[j - 1] = fp[j];
            fp = newFp;
        }
        return i;
    }
    public ForcePower get(int i){
        return fp[i];
    }
    public int size(){
        return fp.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (ForcePower f:fp){
            tmp.add(f.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<ForcePower> out = new ArrayList<>();
        for (Object o:tmp){
            ForcePower f = new ForcePower();
            f.loadFromObject(o);
            out.add(f);
        }
        fp = out.toArray(fp);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof ForcePowers))
            return false;
        ForcePowers in = (ForcePowers)obj;
        if(in.fp.length != fp.length)
            return false;
        for (int i = 0;i<fp.length;i++){
            if (!in.fp[i].equals(fp[i]))
                return false;
        }
        return true;
    }
    public ForcePowers clone(){
        ForcePowers out = new ForcePowers();
        out.fp = new ForcePower[fp.length];
        for (int i = 0;i<fp.length;i++)
            out.fp[i] = fp[i].clone();
        return out;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.name("Force Powers").beginArray();
        for(ForcePower f:fp)
            f.saveJson(jw);
        jw.endArray();
    }

    public void loadJson(JsonReader jr) throws IOException{
        ArrayList<ForcePower> out = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()){
            ForcePower tmp = new ForcePower();
            tmp.loadJson(jr);
            out.add(tmp);
        }
        jr.endArray();
        fp = out.toArray(fp);
    }
    
    public static class ForcePowersAdap extends RecyclerView.Adapter<ForcePowers.ForcePowersAdap.ViewHolder>{
        @Override
        public ForcePowers.ForcePowersAdap.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ForcePowers.ForcePowersAdap.ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ForcePowers.ForcePowersAdap.ViewHolder holder, final int position) {
            ((TextView)holder.v.findViewById(R.id.name)).setText(c.forcePowers.get(holder.getAdapterPosition()).name);
            holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ForcePower.editForcePower(ac, c, holder.getAdapterPosition(), false, new Skill.onSave() {
                        public void save() {
                            ForcePowers.ForcePowersAdap.this.notifyItemChanged(holder.getAdapterPosition());
                        }
                        public void delete() {
                            int ind = c.forcePowers.remove(c.forcePowers.get(holder.getAdapterPosition()));
                            ForcePowers.ForcePowersAdap.this.notifyItemRemoved(ind);
                        }
                        public void cancel() {
                        }
                    });
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return c.forcePowers.size();
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
        public ForcePowersAdap(Character c, Activity ac){
            this.c = c;
            this.ac = ac;
        }
    }
}
