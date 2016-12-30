package com.apps.darkstorm.swrpg.StarWars;

import com.apps.darkstorm.swrpg.StarWars.Stuff.Inventory;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Skills;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Talents;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Weapons;

import java.util.Arrays;

public class Minion {
    //Version 1 0-13
    public int ID;
    public String name;
    //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
    public int[] charVals = new int[6];
    public Skills skills = new Skills();
    public Talents talents = new Talents();
    public Inventory inv = new Inventory();
    public Weapons weapons = new Weapons();
    public int woundThresh, woundCur;
    public int defMelee,defRanged;
    public int soak;
    public String desc = "";
    private boolean[] showCards = new boolean[0];


    private boolean editing = false;
    private boolean saving = false;

    public Minion(){
        for (int i = 0;i<showCards.length;i++)
            showCards[i] = true;
    }
    public Minion(int ID){
        this.ID = ID;
        for (int i = 0;i<showCards.length;i++)
            showCards[i] = true;
    }
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Minion clone(){
        Minion min = new Minion();
        min.ID = ID;
        min.name = name;
        min.charVals = charVals.clone();
        min.skills = skills.clone();
        min.talents = talents.clone();
        min.inv = inv.clone();
        min.weapons = weapons.clone();
        min.woundThresh = woundThresh;
        min.woundCur = woundCur;
        min.defMelee = defMelee;
        min.defRanged = defRanged;
        min.soak = soak;
        min.desc = desc;
        min.showCards = showCards.clone();
        return min;
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Minion))
            return false;
        Minion tmp = (Minion)obj;
        return tmp.name.equals(name) && tmp.ID == ID && Arrays.equals(tmp.charVals,charVals) &&
                tmp.skills.equals(skills) && tmp.talents.equals(talents) && tmp.inv.equals(inv) &&
                tmp.weapons.equals(weapons) && tmp.woundThresh == woundThresh && tmp.woundCur == woundCur &&
                tmp.defMelee == defMelee && tmp.defRanged == defRanged && tmp.soak == soak && tmp.desc.equals(desc) &&
                Arrays.equals(tmp.showCards,showCards);
    }
}
