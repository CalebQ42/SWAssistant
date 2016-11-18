package com.apps.darkstorm.swrpg.StarWars.Stuff;

import java.util.ArrayList;
import java.util.Arrays;

public class Talents{
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
    public void set(Talent newT,Talent old){
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
        out.tal = tal.clone();
        return out;
    }
}
