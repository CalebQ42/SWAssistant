package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import java.util.ArrayList;

public class Obligation{
    //Version 1 0-1
    public String name;
    public int val;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(val);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 2:
                name = (String)tmp[0];
                val = (int)tmp[1];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Obligation))
            return false;
        Obligation in = (Obligation)obj;
        return in.name.equals(name) && in.val == val;
    }
    public Obligation clone(){
        Obligation out = new Obligation();
        out.name = name;
        out.val = val;
        return out;
    }
}
