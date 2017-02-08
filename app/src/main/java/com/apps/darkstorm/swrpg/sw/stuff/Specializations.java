package com.apps.darkstorm.swrpg.sw.stuff;

import java.util.Arrays;

public class Specializations {
    String[] specs;
    public Specializations(){
        specs = new String[0];
    }
    public void add(String sp){
        specs = Arrays.copyOf(specs,specs.length+1);
        specs[specs.length-1] = sp;
    }
    public int remove(String sp){
        int i = -1;
        for (int j = 0;j<specs.length;j++){
            if (specs[j].equals(sp)){
                i = j;
                break;
            }
        }
        if (i != -1){
            String[] s = new String[specs.length-1];
            for (int j = 0;j<i;j++){
                s[j] = specs[j];
            }
            for (int j = i+1;j<specs.length;j++){
                s[j-1] = specs[j];
            }
            specs = s;
        }
        return i;
    }
    public String get(int i){
        return specs[i];
    }
    public int size(){
        return specs.length;
    }
    public Object serialObject(){
        return specs;
    }
    public void loadFromObject(Object o){
        specs = (String[])o;
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Specializations))
            return false;
        Specializations in = (Specializations)obj;
        if(in.specs.length != specs.length)
            return false;
        for (int i = 0;i<specs.length;i++){
            if (!in.specs[i].equals(specs[i]))
                return false;
        }
        return true;
    }
    public Specializations clone(){
        Specializations out = new Specializations();
        out.specs = specs.clone();
        return out;
    }
}
