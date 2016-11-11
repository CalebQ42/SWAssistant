package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;
import java.util.Arrays;

public class Skills{
    Skill[] sk;
    public Skills(){
        sk = new Skill[0];
    }
    public void add(Skill s){
        sk = Arrays.copyOf(sk,sk.length+1);
        sk[sk.length-1] = s;
    }
    public int remove(Skill s){
        int i = -1;
        for (int j =0;j<sk.length;j++){
            if (sk[j].equals(s)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            Skill[] newSk = new Skill[sk.length - 1];
            for (int j = 0; j < i; j++)
                newSk[j] = sk[j];
            for (int j = i + 1; j < sk.length; j++)
                newSk[j - 1] = sk[j];
            sk = newSk;
        }
        return i;
    }
    public Skill get(int i){
        return sk[i];
    }
    public int size(){
        return sk.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Skill k:sk){
            tmp.add(k.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Skill> out = new ArrayList<>();
        for (Object o:tmp){
            Skill s = new Skill();
            s.loadFromObject(o);
            out.add(s);
        }
        sk = out.toArray(sk);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Skills))
            return false;
        Skills in = (Skills)obj;
        if(in.sk.length != sk.length)
            return false;
        for (int i = 0;i<sk.length;i++){
            if (!in.sk[i].equals(sk[i]))
                return false;
        }
        return true;
    }
}
