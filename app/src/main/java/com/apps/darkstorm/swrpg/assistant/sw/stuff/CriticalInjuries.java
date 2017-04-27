package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import java.util.ArrayList;
import java.util.Arrays;

public class CriticalInjuries{
    CriticalInjury[] critInj;
    public CriticalInjuries(){
        critInj = new CriticalInjury[0];
    }
    public void add(CriticalInjury ci){
        CriticalInjury[] newInj = Arrays.copyOf(critInj,critInj.length+1);
        newInj[critInj.length] = ci;
        critInj = newInj;
    }
    public CriticalInjury get(int i){
        return critInj[i];
    }
    public int size(){
        return critInj.length;
    }
    public int remove(CriticalInjury ci){
        int i =-1;
        for (int j =0;j<critInj.length;j++){
            if (critInj[j].equals(ci)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            CriticalInjury[] newInj = new CriticalInjury[critInj.length - 1];
            for (int j = 0; j < i; j++)
                newInj[j] = critInj[j];
            for (int j = i + 1; j < critInj.length; j++)
                newInj[j - 1] = critInj[j];
            critInj = newInj;
        }
        return i;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (CriticalInjury ci:critInj){
            tmp.add(ci.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<CriticalInjury> out = new ArrayList<>();
        for (Object o:tmp){
            CriticalInjury ci = new CriticalInjury();
            ci.loadFromObject(o);
            out.add(ci);
        }
        critInj = out.toArray(critInj);
    }
    public boolean equals(Object obj){
        if (obj instanceof CriticalInjuries){
            CriticalInjuries in = (CriticalInjuries)obj;
            if (in.critInj.length!=critInj.length){
                return false;
            }
            for (int i = 0;i<critInj.length;i++){
                if (!critInj[i].equals(in.critInj[i]))
                    return false;
            }
            return true;
        }else{
            return false;
        }
    }
    public CriticalInjuries clone(){
        CriticalInjuries out = new CriticalInjuries();
        out.critInj = new CriticalInjury[critInj.length];
        for(int i = 0;i<critInj.length;i++)
            out.critInj[i] = critInj[i].clone();
        return out;
    }
}
