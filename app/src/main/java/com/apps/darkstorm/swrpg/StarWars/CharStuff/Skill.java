package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;

public class Skill{
    //Version 1 0-3
    public String name;
    public int val;
    public int baseChar;
    public boolean career;
    public int costNext(){
        int out = 5*(val+1);
        if (!career){
            out += 5;
        }
        return out;
    }
    public String getNameString(){
        String tmp;
        if (career){
            tmp = "*";
        }else
            tmp = " ";
        tmp += name;
        tmp += ":";
        return tmp;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(val);
        tmp.add(baseChar);
        tmp.add(career);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 4:
                name = (String)tmp[0];
                val = (int)tmp[1];
                baseChar = (int)tmp[2];
                career = (boolean)tmp[3];
        }
    }
}