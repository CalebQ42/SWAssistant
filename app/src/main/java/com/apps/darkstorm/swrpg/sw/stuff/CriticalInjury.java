package com.apps.darkstorm.swrpg.sw.stuff;

import java.util.ArrayList;

public class CriticalInjury{
    //Version 1 0-2
    public String name;
    public String desc;
    public int severity;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        tmp.add(severity);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 3:
                name = (String)tmp[0];
                desc = (String)tmp[1];
                severity = (int)tmp[2];
        }
    }
    public boolean equals(Object obj){
        if (obj instanceof CriticalInjury){
            CriticalInjury in = (CriticalInjury)obj;
            return in.name.equals(name) && in.desc.equals(desc) && in.severity == severity;
        }else{
            return false;
        }
    }
}
