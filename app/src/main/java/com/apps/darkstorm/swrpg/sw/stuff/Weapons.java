package com.apps.darkstorm.swrpg.sw.stuff;

import java.util.ArrayList;
import java.util.Arrays;

public class Weapons{
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
    public void set(Weapon old,Weapon newW){
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
        out.w = w.clone();
        return out;
    }
    public int totalEncum(){
        int total = 0;
        for (Weapon we:w)
            total += we.encum;
        return total;
    }
}
