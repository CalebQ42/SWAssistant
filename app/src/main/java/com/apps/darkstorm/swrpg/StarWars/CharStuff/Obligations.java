package com.apps.darkstorm.swrpg.StarWars.CharStuff;

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
}
