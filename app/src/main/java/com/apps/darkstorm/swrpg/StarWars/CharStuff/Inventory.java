package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;
import java.util.Arrays;

public class Inventory{
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
}
