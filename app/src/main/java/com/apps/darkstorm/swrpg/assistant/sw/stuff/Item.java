package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import java.util.ArrayList;

public class Item{
    //Version 1 0-2
    public String name;
    public String desc;
    public int count;
    //Version 2 3
    public int encum;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        tmp.add(count);
        tmp.add(encum);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 4:
                encum = (int)tmp[3];
            case 3:
                name = (String)tmp[0];
                desc = (String)tmp[1];
                count = (int)tmp[2];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Item))
            return false;
        Item in = (Item)obj;
        return in.name.equals(name) && in.desc.equals(desc) && in.count == count && in.encum == encum;
    }
}
