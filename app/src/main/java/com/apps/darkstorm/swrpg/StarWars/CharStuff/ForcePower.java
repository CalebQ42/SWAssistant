package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;

public class ForcePower{
    //Version 1 0-1
    public String name,desc;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 2:
                name = (String)tmp[0];
                desc = (String)tmp[1];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof ForcePower))
            return false;
        ForcePower in = (ForcePower)obj;
        return in.name.equals(name) && in.desc.equals(desc);
    }
}
