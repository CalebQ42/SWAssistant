package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import java.util.ArrayList;

public class Talent{
    //Version 1 0-2
    public String name;
    public String desc;
    public int val;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        tmp.add(val);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 3:
                name = (String)tmp[0];
                desc = (String)tmp[1];
                val = (int)tmp[2];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Talent))
            return false;
        Talent in = (Talent)obj;
        return in.name.equals(name) && in.desc.equals(desc) && in.val == val;
    }
    public Talent clone(){
        Talent out = new Talent();
        out.name = name;
        out.desc = desc;
        out.val = val;
        return out;
    }
}
