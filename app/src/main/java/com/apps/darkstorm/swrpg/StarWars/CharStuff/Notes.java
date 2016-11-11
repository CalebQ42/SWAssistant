package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;
import java.util.Arrays;

public class Notes{
    Note[] ns;
    public Notes(){
        ns = new Note[0];
    }
    public void add(Note nsb){
        ns = Arrays.copyOf(ns,ns.length+1);
        ns[ns.length-1] = nsb;
    }
    public int remove(Note n){
        int i = -1;
        for (int j = 0;j<ns.length;j++){
            if (ns[j].equals(n)){
                i = j;
                break;
            }
        }
        if (i!= -1) {
            Note[] newns = new Note[ns.length - 1];
            for (int j = 0; j < i; j++)
                newns[j] = ns[j];
            for (int j = i + 1; j < ns.length; j++)
                newns[j - 1] = ns[j];
            ns = newns;
        }
        return i;
    }
    public Note get(int i){
        return ns[i];
    }
    public int size(){
        return ns.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Note n :ns){
            tmp.add(n.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Note> out = new ArrayList<>();
        for (Object o:tmp){
            Note n = new Note();
            n.loadFromObject(o);
            out.add(n);
        }
        ns = out.toArray(ns);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Notes))
            return false;
        Notes in = (Notes)obj;
        if(in.ns.length != ns.length)
            return false;
        for (int i = 0;i<ns.length;i++){
            if (!in.ns[i].equals(ns[i]))
                return false;
        }
        return true;
    }
}
