package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;

import java.util.ArrayList;
import java.util.Arrays;

public class Obligations{
    Obligation[] o;
    public Obligations(){
        o = new Obligation[0];
    }
    public void add(Obligation ob){
        o = Arrays.copyOf(o,o.length+1);
        o[o.length-1] = ob;
    }
    public int remove(Obligation ob){
        int i =-1;
        for (int j = 0;j<o.length;j++){
            if (o[j].equals(ob)){
                i = j;
                break;
            }
        }
        if (i!=-1) {
            Obligation[] newO = new Obligation[o.length - 1];
            for (int j = 0; j < i; j++)
                newO[j] = o[j];
            for (int j = i + 1; j < o.length; j++)
                newO[j - 1] = o[j];
            o = newO;
        }
        return i;
    }
    public Obligation get(int i){
        return o[i];
    }
    public int size(){
        return o.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Obligation ob:o){
            tmp.add(ob.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Obligation> out = new ArrayList<>();
        for (Object ob:tmp){
            Obligation obl = new Obligation();
            obl.loadFromObject(ob);
            out.add(obl);
        }
        o = out.toArray(o);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Obligations))
            return false;
        Obligations in = (Obligations)obj;
        if(in.o.length != o.length)
            return false;
        for (int i = 0;i<o.length;i++){
            if (!in.o[i].equals(o[i]))
                return false;
        }
        return true;
    }
    public Obligations clone(){
        Obligations out = new Obligations();
        out.o = new Obligation[o.length];
        for(int i = 0;i<o.length;i++)
            out.o[i] = o[i].clone();
        return out;
    }

    public static class ObligationsAdap extends RecyclerView.Adapter<ObligationsAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ((TextView) holder.v.findViewById(R.id.name)).setText(c.obligation.get(holder.getAdapterPosition()).name);
            holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Obligation.editObligation(ac, c, holder.getAdapterPosition(), false, new Skill.onSave() {
                        public void save() {
                            ObligationsAdap.this.notifyItemChanged(holder.getAdapterPosition());
                        }

                        public void delete() {
                            int ind = c.obligation.remove(c.obligation.get(holder.getAdapterPosition()));
                            ObligationsAdap.this.notifyItemRemoved(ind);
                        }

                        public void cancel() {}
                    });
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return c.obligation.size();
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
        public ObligationsAdap(Character c, Activity ac){
            this.c = c;
            this.ac = ac;
        }
    }
}
