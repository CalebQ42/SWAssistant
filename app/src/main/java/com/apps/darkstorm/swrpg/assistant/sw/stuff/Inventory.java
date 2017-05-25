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

public class Inventory implements JsonSavable {
    Item[] inv;
    public Inventory(){
        inv = new Item[0];
    }
    public void add(Item i){
        inv = Arrays.copyOf(inv,inv.length+1);
        inv[inv.length-1] = i;
    }
    public int remove(Item it){
        int i =-1;
        for (int j = 0;j<inv.length;j++){
            if (inv[j].equals(it)){
                i = j;
                break;
            }
        }
        if (i!=-1) {
            Item[] newInv = new Item[inv.length - 1];
            for (int j = 0; j < i; j++)
                newInv[j] = inv[j];
            for (int j = i + 1; j < inv.length; j++)
                newInv[j - 1] = inv[j];
            inv = newInv;
        }
        return i;
    }
    public Item get(int i){
        return inv[i];
    }
    public int size(){
        return inv.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Item i:inv){
            tmp.add(i.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Item> out = new ArrayList<>();
        for (Object o:tmp){
            Item i = new Item();
            i.loadFromObject(o);
            out.add(i);
        }
        inv = out.toArray(inv);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Inventory))
            return false;
        Inventory in = (Inventory)obj;
        if(in.inv.length != inv.length)
            return false;
        for (int i = 0;i<inv.length;i++){
            if (!in.inv[i].equals(inv[i]))
                return false;
        }
        return true;
    }
    public Inventory clone(){
        Inventory out = new Inventory();
        out.inv = new Item[inv.length];
        for(int i = 0;i<inv.length;i++)
            out.inv[i] = inv[i].clone();
        return out;
    }
    public int totalEncum(){
        int total =0;
        for (Item it:inv)
            total += it.encum;
        return total;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.name("Inventory").beginArray();
        for(Item i:inv)
            i.saveJson(jw);
        jw.endArray();
    }

    public void loadJson(JsonReader jr) throws IOException{
        ArrayList<Item> out = new ArrayList<>();
        jr.beginArray();
        while(jr.hasNext()){
            Item tmp = new Item();
            tmp.loadJson(jr);
            out.add(tmp);
        }
        jr.endArray();
        inv = out.toArray(inv);
    }

    public static class InventoryAdap extends RecyclerView.Adapter<InventoryAdap.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ac.getLayoutInflater().inflate(R.layout.item_simple,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if(c instanceof Character) {
                final Inventory inv = ((Character)c).inv;
                ((TextView) holder.v.findViewById(R.id.name)).setText(inv.get(holder.getAdapterPosition()).name);
                holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Item.editItem(ac, c, holder.getAdapterPosition(), false, new Skill.onSave() {
                            public void save() {
                                InventoryAdap.this.notifyItemChanged(holder.getAdapterPosition());
                                os.save();
                            }

                            public void delete() {
                                int ind = inv.remove(inv.get(holder.getAdapterPosition()));
                                InventoryAdap.this.notifyItemRemoved(ind);
                                os.delete();
                            }

                            public void cancel() {
                                os.cancel();
                            }
                        });
                        return true;
                    }
                });
            }else if(c instanceof Minion){
                final Inventory inv = ((Minion)c).inv;
                ((TextView) holder.v.findViewById(R.id.name)).setText(inv.get(holder.getAdapterPosition()).name);
                holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Item.editItem(ac, c, holder.getAdapterPosition(), false, new Skill.onSave() {
                            public void save() {
                                InventoryAdap.this.notifyItemChanged(holder.getAdapterPosition());
                            }

                            public void delete() {
                                int ind = inv.remove(inv.get(holder.getAdapterPosition()));
                                InventoryAdap.this.notifyItemRemoved(ind);
                            }

                            public void cancel() {
                            }
                        });
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if(c instanceof Character)
                return ((Character)c).inv.size();
            else if(c instanceof Minion)
                return ((Minion)c).inv.size();
            return 0;
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
        public InventoryAdap(Editable c, Skill.onSave os, Activity ac){
            this.c = c;
            this.ac = ac;
            this.os = os;
        }
    }
}
