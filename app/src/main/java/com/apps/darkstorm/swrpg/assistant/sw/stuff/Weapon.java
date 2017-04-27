package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import java.util.ArrayList;

public class Weapon{
    //<editor-fold desc="Vars v1">
    //Version 1 0-12
    public String name = "";
    public int dmg;
    public int crit;
    public int hp;
    public int range;
    public int skill;
    public int skillBase;
    public WeapChars chars = new WeapChars();
    public boolean addBrawn;
    public boolean loaded = true;
    public boolean slug;
    //0-Good,1-Minor,2-Moderate,3-Major
    public int itemState;
    public int ammo;
    //Version 2 13
    public String firingArc = "";
    //Version 3 14
    public int encum;
    //</editor-fold>
    public void copyFrom(Weapon w){
        name = w.name;
        dmg = w.dmg;
        crit = w.crit;
        hp = w.hp;
        range = w.range;
        skill = w.skill;
        skillBase = w.skillBase;
        chars = w.chars.clone();
        addBrawn = w.addBrawn;
        loaded = w.loaded;
        slug = w.slug;
        itemState = w.itemState;
        ammo = w.ammo;
        firingArc = w.firingArc;
        encum = w.encum;
    }
    public Weapon clone(){
        Weapon tmp = new Weapon();
        tmp.name = name;
        tmp.dmg = dmg;
        tmp.crit = crit;
        tmp.hp = hp;
        tmp.range = range;
        tmp.skill = skill;
        tmp.skillBase = skillBase;
        tmp.chars = chars.clone();
        tmp.addBrawn = addBrawn;
        tmp.loaded = loaded;
        tmp.slug = slug;
        tmp.itemState = itemState;
        tmp.ammo = ammo;
        tmp.firingArc = firingArc;
        tmp.encum = encum;
        return tmp;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(dmg);
        tmp.add(crit);
        tmp.add(hp);
        tmp.add(range);
        tmp.add(skill);
        tmp.add(skillBase);
        tmp.add(chars.serialObject());
        tmp.add(addBrawn);
        tmp.add(loaded);
        tmp.add(slug);
        tmp.add(itemState);
        tmp.add(ammo);
        tmp.add(firingArc);
        tmp.add(encum);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 15:
                encum = (int)tmp[14];
            case 14:
                firingArc = (String)tmp[13];
                if(firingArc == null)
                    firingArc = "";
            case 13:
                name = (String)tmp[0];
                dmg = (int)tmp[1];
                crit = (int)tmp[2];
                hp = (int)tmp[3];
                range = (int)tmp[4];
                skill = (int)tmp[5];
                skillBase = (int)tmp[6];
                WeapChars wc = new WeapChars();
                wc.loadFromObject(tmp[7]);
                chars = wc;
                addBrawn = (boolean)tmp[8];
                loaded = (boolean)tmp[9];
                slug = (boolean)tmp[10];
                itemState = (int)tmp[11];
                ammo = (int)tmp[12];
        }
        if(firingArc == null)
            firingArc = "";
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Weapon))
            return false;
        Weapon in = (Weapon)obj;
        return in.name.equals(name) && in.dmg == dmg && in.crit == crit && in.hp == hp && in.range == range && in.skill == skill
                && in.skillBase == skillBase && in.chars.equals(chars) && in.addBrawn == addBrawn && in.loaded == loaded && in.slug == slug
                && in.itemState == itemState && in.ammo == ammo && in.firingArc.equals(firingArc) && in.encum == encum;
    }
}
