package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;

public class WeapChar{
    //Version 1 (0-2)
    public String name;
    public int val;
    public int adv;
    //Version 1 end
    public WeapChar clone(){
        WeapChar tmp;
        try {
            tmp = (WeapChar)super.clone();
        } catch (CloneNotSupportedException ignored) {
            tmp = new WeapChar();
        }
        tmp.name = name;
        tmp.val = val;
        tmp.adv = adv;
        return tmp;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(val);
        tmp.add(adv);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 3:
                name = (String)tmp[0];
                val = (int)tmp[1];
                adv = (int)tmp[2];
        }
    }
}
