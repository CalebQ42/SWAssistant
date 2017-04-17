package com.apps.darkstorm.swrpg.assistant.sw;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.drive.DriveSaveLoad;
import com.apps.darkstorm.swrpg.assistant.custvars.SaveLoad;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Dutys;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.ForcePowers;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Inventory;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Notes;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Obligations;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skills;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Specializations;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Talents;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapons;
import com.apps.darkstorm.swrpg.assistant.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Character {

    //
    //  |  Char v1 Start  |
    //  |                 |
    //  V      0-34       V
    //
    public int ID;
    public String name = "";
    //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
    public int[] charVals = new int[6];
    public Skills skills = new Skills();
    public String species = "";
    public String career = "";
    public Specializations specializations = new Specializations();
    public Talents talents = new Talents();
    public Inventory inv = new Inventory();
    public Weapons weapons = new Weapons();
    public ForcePowers forcePowers = new ForcePowers();
    public String motivation = "";
    public CriticalInjuries critInjuries = new CriticalInjuries();
    public String[] emotionalStr = new String[1];
    public String[] emotionalWeak = new String[1];
    public Dutys duty = new Dutys();
    public Obligations obligation = new Obligations();
    public int woundThresh, woundCur;
    public int strainThresh,strainCur;
    public int xpTot,xpCur;
    public int defMelee,defRanged;
    public int soak;
    public int force;
    public int credits;
    public int morality,conflict;
    public String desc = "";
    private boolean[] showCard = new boolean[16];
    public boolean darkSide;
    public int age;
    public Notes nts = new Notes();
    //
    // Character V2 Start (35)
    //
    public int encumCapacity;
    //
    //  ^                 ^
    //  |  Character End  |
    //  |                 |
    //

    private boolean editing = false;
    private boolean saving = false;
    private String loc = "";
    public boolean external = false;

    public Character(){
        morality = 50;
        for (int i = 0;i<showCard.length;i++){
            showCard[i] = true;
        }
    }
    public Character(int ID){
        this.ID = ID;
        morality = 50;
        for (int i = 0;i<showCard.length;i++){
            showCard[i] = true;
        }
    }
    public void startEditing(final Activity main, final DriveId fold){
        if(external){
            startEditing(main);
        }else {
            if (!editing) {
                editing = true;
                AsyncTask<Void, Void, Void> blablah = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Character tmpChar = Character.this.clone();
                        Character.this.save(Character.this.getFileLocation(main));
                        if(((SWrpg)main.getApplication()).vehicFold!=null)
                            cloudSave(((SWrpg) main.getApplication()).gac, getFileId(main), false);
                        do {
                            if (!saving) {
                                saving = true;
                                if (!Character.this.equals(tmpChar)) {
                                    if (!tmpChar.name.equals(Character.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                        if (((SWrpg) main.getApplication()).hasShortcut(Character.this)) {
                                            ((SWrpg) main.getApplication()).updateShortcut(Character.this, main);
                                        } else {
                                            ((SWrpg) main.getApplication()).addShortcut(Character.this, main);
                                        }
                                    }
                                    Character.this.save(Character.this.getFileLocation(main));
                                    if(((SWrpg)main.getApplication()).vehicFold!=null)
                                        cloudSave(((SWrpg) main.getApplication()).gac,
                                            getFileId(main), false);
                                    tmpChar = Character.this.clone();
                                }
                                saving = false;
                            }
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (editing);
                        if (!saving) {
                            saving = true;
                            if (!Character.this.equals(tmpChar)) {
                                if (!tmpChar.name.equals(Character.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                    if (((SWrpg) main.getApplication()).hasShortcut(Character.this)) {
                                        ((SWrpg) main.getApplication()).updateShortcut(Character.this, main);
                                    } else {
                                        ((SWrpg) main.getApplication()).addShortcut(Character.this, main);
                                    }
                                }
                                Character.this.save(Character.this.getFileLocation(main));
                                if(((SWrpg)main.getApplication()).vehicFold!=null)
                                    cloudSave(((SWrpg) main.getApplication()).gac, getFileId(
                                        main), false);
                            }
                            saving = false;
                        }
                        return null;
                    }
                };
                blablah.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }
    public void startEditing(final Activity main){
        if (!editing) {
            editing = true;
            Thread tmp = new Thread(new Runnable() {
                @Override
                public void run() {
                    Character tmpChar = Character.this.clone();
                    Character.this.save(Character.this.getFileLocation(main));
                    do{
                        if (!saving) {
                            saving = true;
                            if (!Character.this.equals(tmpChar)) {
                                if(!tmpChar.name.equals(Character.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                                    if(((SWrpg)main.getApplication()).hasShortcut(Character.this)) {
                                        ((SWrpg) main.getApplication()).updateShortcut(Character.this, main);
                                    }else{
                                        ((SWrpg)main.getApplication()).addShortcut(Character.this,main);
                                    }
                                }
                                Character.this.save(Character.this.getFileLocation(main));
                                tmpChar = Character.this.clone();
                            }
                            saving = false;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }while (editing);
                    if (!saving) {
                        saving = true;
                        if (!Character.this.equals(tmpChar)) {
                            if(!tmpChar.name.equals(Character.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                                if(((SWrpg)main.getApplication()).hasShortcut(Character.this)) {
                                    ((SWrpg) main.getApplication()).updateShortcut(Character.this, main);
                                }else{
                                    ((SWrpg)main.getApplication()).addShortcut(Character.this,main);
                                }
                            }
                            Character.this.save(Character.this.getFileLocation(main));
                        }
                        saving = false;
                    }
                }
            });
            tmp.start();
        }
    }
    public void stopEditing(){
        editing = false;
    }
    public String getFileLocation(Activity main){
        if(main!= null) {
            String loc = ((SWrpg) main.getApplication()).prefs.getString(main.getString(R.string.local_location_key),
                    ((SWrpg) main.getApplication()).defaultLoc);
            File location = new File(loc);
            if (!location.exists()) {
                if (!location.mkdir()) {
                    return "";
                }
            }
            String def = location.getAbsolutePath() + "/" + Integer.toString(ID) + ".char";
            if(external)
                return this.loc;
            return def;
        }else{
            return "";
        }
    }
    public void save(String filename){
        SaveLoad sl = new SaveLoad(filename);
        sl.addSave(ID);
        sl.addSave(name);
        sl.addSave(charVals);
        sl.addSave(skills.serialObject());
        sl.addSave(species);
        sl.addSave(career);
        sl.addSave(specializations.serialObject());
        sl.addSave(talents.serialObject());
        sl.addSave(inv.serialObject());
        sl.addSave(weapons.serialObject());
        sl.addSave(forcePowers.serialObject());
        sl.addSave(motivation);
        sl.addSave(critInjuries.serialObject());
        sl.addSave(emotionalStr);
        sl.addSave(emotionalWeak);
        sl.addSave(duty.serialObject());
        sl.addSave(obligation.serialObject());
        sl.addSave(woundThresh);
        sl.addSave(woundCur);
        sl.addSave(strainThresh);
        sl.addSave(strainCur);
        sl.addSave(xpTot);
        sl.addSave(xpCur);
        sl.addSave(defMelee);
        sl.addSave(defRanged);
        sl.addSave(soak);
        sl.addSave(force);
        sl.addSave(credits);
        sl.addSave(morality);
        sl.addSave(conflict);
        sl.addSave(desc);
        sl.addSave(showCard);
        sl.addSave(darkSide);
        sl.addSave(age);
        sl.addSave(nts.serialObject());
        sl.addSave(encumCapacity);
        sl.save();
    }
    public void reLoad(String filename){
        SaveLoad sl = new SaveLoad(filename);
        Object[] vals = sl.load();
        switch (vals.length){
            //later versions go here and fallthrough
            case 36:
                encumCapacity = (int)vals[35];
            case 35:
                String title = filename.substring(filename.lastIndexOf("/")+1);
                if (title.substring(0,title.indexOf(".")).equals(""))
                    ID = (int)vals[0];
                else {
                    try {
                        ID = Integer.parseInt(title.substring(0, title.indexOf(".")));
                    }catch(NumberFormatException ignored){
                        ID = (int)vals[0];
                    }
                }
                name = (String)vals[1];
                charVals = (int[])vals[2];
                skills.loadFromObject(vals[3]);
                species = (String)vals[4];
                career = (String)vals[5];
                specializations.loadFromObject(vals[6]);
                talents.loadFromObject(vals[7]);
                inv.loadFromObject(vals[8]);
                weapons.loadFromObject(vals[9]);
                forcePowers.loadFromObject(vals[10]);
                motivation = (String)vals[11];
                critInjuries.loadFromObject(vals[12]);
                emotionalStr = (String[])vals[13];
                emotionalWeak = (String[])vals[14];
                duty.loadFromObject(vals[15]);
                obligation.loadFromObject(vals[16]);
                woundThresh = (int)vals[17];
                woundCur = (int)vals[18];
                strainThresh = (int)vals[19];
                strainCur = (int)vals[20];
                xpTot = (int)vals[21];
                xpCur = (int)vals[22];
                defMelee = (int)vals[23];
                defRanged = (int)vals[24];
                soak = (int)vals[25];
                force = (int)vals[26];
                credits = (int)vals[27];
                morality = (int)vals[28];
                conflict = (int)vals[29];
                desc = (String)vals[30];
                showCard = (boolean[])vals[31];
                darkSide = (boolean)vals[32];
                age = (int)vals[33];
                nts.loadFromObject(vals[34]);
                if (nts == null){
                    nts = new Notes();
                }
        }
    }
    public DriveId getFileId(Activity main){
        String name = Integer.toString(ID) + ".char";
        DriveId fi = null;
        DriveApi.MetadataBufferResult res = ((SWrpg)main.getApplication())
                .charsFold.queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder().addFilter(
                Filters.eq(SearchableField.TITLE,name)).build()).await();
        for (Metadata met:res.getMetadataBuffer()){
            if (!met.isTrashed()){
                fi = met.getDriveId();
                break;
            }
        }
        res.release();
        if (fi == null){
            fi = ((SWrpg)main.getApplication()).charsFold.createFile
                    (((SWrpg)main.getApplication())
                            .gac,new MetadataChangeSet.Builder().setTitle(name).build(),null).await()
                    .getDriveFile().getDriveId();
        }
        return fi;
    }
    public void cloudSave(GoogleApiClient gac,DriveId fil, boolean async){
        if (fil != null) {
            DriveSaveLoad sl = new DriveSaveLoad(fil);
            sl.addSave(ID);
            sl.addSave(name);
            sl.addSave(charVals);
            sl.addSave(skills.serialObject());
            sl.addSave(species);
            sl.addSave(career);
            sl.addSave(specializations.serialObject());
            sl.addSave(talents.serialObject());
            sl.addSave(inv.serialObject());
            sl.addSave(weapons.serialObject());
            sl.addSave(forcePowers.serialObject());
            sl.addSave(motivation);
            sl.addSave(critInjuries.serialObject());
            sl.addSave(emotionalStr);
            sl.addSave(emotionalWeak);
            sl.addSave(duty.serialObject());
            sl.addSave(obligation.serialObject());
            sl.addSave(woundThresh);
            sl.addSave(woundCur);
            sl.addSave(strainThresh);
            sl.addSave(strainCur);
            sl.addSave(xpTot);
            sl.addSave(xpCur);
            sl.addSave(defMelee);
            sl.addSave(defRanged);
            sl.addSave(soak);
            sl.addSave(force);
            sl.addSave(credits);
            sl.addSave(morality);
            sl.addSave(conflict);
            sl.addSave(desc);
            sl.addSave(showCard);
            sl.addSave(darkSide);
            sl.addSave(age);
            sl.addSave(nts.serialObject());
            sl.addSave(encumCapacity);
            sl.save(gac,async);
        }
    }
    public void reLoad(GoogleApiClient gac,DriveId fil){
        DriveSaveLoad sl = new DriveSaveLoad(fil);
        Object[] vals = sl.load(gac);
        switch (vals.length){
            //later versions go here and fallthrough
            case 36:
                encumCapacity = (int)vals[35];
            case 35:
                String title = fil.asDriveFile().getMetadata(gac).await().getMetadata().getTitle();
                if (title.substring(0,title.indexOf(".")).equals(""))
                    ID = (int)vals[0];
                else {
                    try {
                        ID = Integer.parseInt(title.substring(0, title.indexOf(".")));
                    }catch(NumberFormatException ignored){
                        ID = (int)vals[0];
                    }
                }
                name = (String)vals[1];
                charVals = (int[])vals[2];
                skills.loadFromObject(vals[3]);
                species = (String)vals[4];
                career = (String)vals[5];
                specializations.loadFromObject(vals[6]);
                talents.loadFromObject(vals[7]);
                inv.loadFromObject(vals[8]);
                weapons.loadFromObject(vals[9]);
                forcePowers.loadFromObject(vals[10]);
                motivation = (String)vals[11];
                critInjuries.loadFromObject(vals[12]);
                emotionalStr = (String[])vals[13];
                emotionalWeak = (String[])vals[14];
                duty.loadFromObject(vals[15]);
                obligation.loadFromObject(vals[16]);
                woundThresh = (int)vals[17];
                woundCur = (int)vals[18];
                strainThresh = (int)vals[19];
                strainCur = (int)vals[20];
                xpTot = (int)vals[21];
                xpCur = (int)vals[22];
                defMelee = (int)vals[23];
                defRanged = (int)vals[24];
                soak = (int)vals[25];
                force = (int)vals[26];
                credits = (int)vals[27];
                morality = (int)vals[28];
                conflict = (int)vals[29];
                desc = (String)vals[30];
                showCard = (boolean[])vals[31];
                darkSide = (boolean)vals[32];
                age = (int)vals[33];
                nts.loadFromObject(vals[34]);
                if (nts == null){
                    nts = new Notes();
                }
        }
    }
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Character clone(){
        Character tmp = new Character();
        tmp.ID = ID;
        tmp.name = name;
        tmp.charVals = charVals.clone();
        tmp.skills = skills.clone();
        tmp.species = species;
        tmp.career = career;
        tmp.specializations = specializations.clone();
        tmp.talents = talents.clone();
        tmp.inv = inv.clone();
        tmp.weapons = weapons.clone();
        tmp.forcePowers = forcePowers.clone();
        tmp.motivation = motivation;
        if (tmp.motivation == null)
            tmp.motivation = "";
        if (motivation == null)
            motivation = "";
        tmp.critInjuries = critInjuries.clone();
        tmp.emotionalStr = emotionalStr.clone();
        tmp.emotionalWeak = emotionalWeak.clone();
        tmp.duty = duty.clone();
        tmp.obligation = obligation.clone();
        tmp.woundThresh = woundThresh;
        tmp.woundCur = woundCur;
        tmp.strainThresh = strainThresh;
        tmp.strainCur = strainCur;
        tmp.xpTot = xpTot;
        tmp.xpCur = xpCur;
        tmp.defMelee = defMelee;
        tmp.defRanged = defRanged;
        tmp.soak = soak;
        tmp.force = force;
        tmp.credits = credits;
        tmp.morality = morality;
        tmp.conflict = conflict;
        tmp.desc = desc;
        tmp.showCard = showCard.clone();
        tmp.darkSide = darkSide;
        tmp.age = age;
        tmp.nts = nts.clone();
        tmp.encumCapacity = encumCapacity;
        return tmp;
    }
    public void resolveConflict(){
        if (!darkSide)
            morality += ((int)(Math.random()*10)+1)-conflict;
        else
            morality -= ((int)(Math.random()*10)+1)-conflict;
        if (morality > 100)
            morality = 100;
        else if (morality <0)
            morality = 0;
        conflict = 0;
    }
    public boolean equals(Object obj) {
        if (!(obj instanceof Character))
            return false;
        Character chara = (Character)obj;
        return chara.name.equals(name) && chara.ID == ID && Arrays.equals(chara.charVals, charVals) &&
                chara.skills.equals(skills) && chara.species.equals(species) && chara.career.equals(career) &&
                chara.specializations.equals(specializations) && chara.talents.equals(talents) && chara.inv.equals(inv) &&
                chara.weapons.equals(weapons) && chara.forcePowers.equals(forcePowers) && chara.motivation.equals(motivation) &&
                chara.critInjuries.equals(critInjuries) && Arrays.equals(emotionalStr, chara.emotionalStr) &&
                Arrays.equals(chara.emotionalWeak, emotionalWeak) && chara.duty.equals(duty) && chara.obligation.equals(obligation) &&
                woundThresh == chara.woundThresh && woundCur == chara.woundCur && strainThresh == chara.strainThresh &&
                strainCur == chara.strainCur && xpCur == chara.xpCur && xpTot == chara.xpTot && defMelee == chara.defMelee &&
                defRanged == chara.defRanged && soak == chara.soak && force == chara.force && credits == chara.credits &&
                morality == chara.morality && conflict == chara.conflict && Objects.equals(chara.desc, desc) &&
                Arrays.equals(showCard, chara.showCard) && darkSide == chara.darkSide && age == chara.age && chara.nts.equals(nts) &&
                encumCapacity == chara.encumCapacity;
    }
    public boolean isOverEncum(){
        return (encumCapacity < inv.totalEncum() + weapons.totalEncum());
    }
    public void delete(final Activity main){
        File tmp = new File(getFileLocation(main));
        tmp.delete();
        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false)){
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    getFileId(main).asDriveResource().delete(((SWrpg)main.getApplication()).gac).await();
                    return null;
                }
            };
            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ((SWrpg)main.getApplication()).deleteShortcut(Character.this,main);
        }
    }
    
    public Object serialObject(){
        ArrayList<Object> out = new ArrayList<>();
        out.add(ID);
        out.add(name);
        out.add(charVals);
        out.add(skills.serialObject());
        out.add(species);
        out.add(career);
        out.add(specializations.serialObject());
        out.add(talents.serialObject());
        out.add(inv.serialObject());
        out.add(weapons.serialObject());
        out.add(forcePowers.serialObject());
        out.add(motivation);
        out.add(critInjuries.serialObject());
        out.add(emotionalStr);
        out.add(emotionalWeak);
        out.add(duty.serialObject());
        out.add(obligation.serialObject());
        out.add(woundThresh);
        out.add(woundCur);
        out.add(strainThresh);
        out.add(strainCur);
        out.add(xpTot);
        out.add(xpCur);
        out.add(defMelee);
        out.add(defRanged);
        out.add(soak);
        out.add(force);
        out.add(credits);
        out.add(morality);
        out.add(conflict);
        out.add(desc);
        out.add(showCard);
        out.add(darkSide);
        out.add(age);
        out.add(nts.serialObject());
        out.add(encumCapacity);
        return out.toArray();
    }
    public void loadFromObject(Object in){
        Object[] vals = (Object[])in;encumCapacity = (int)vals[35];
        ID = (int)vals[0];
        name = (String)vals[1];
        charVals = (int[])vals[2];
        skills.loadFromObject(vals[3]);
        species = (String)vals[4];
        career = (String)vals[5];
        specializations.loadFromObject(vals[6]);
        talents.loadFromObject(vals[7]);
        inv.loadFromObject(vals[8]);
        weapons.loadFromObject(vals[9]);
        forcePowers.loadFromObject(vals[10]);
        motivation = (String)vals[11];
        critInjuries.loadFromObject(vals[12]);
        emotionalStr = (String[])vals[13];
        emotionalWeak = (String[])vals[14];
        duty.loadFromObject(vals[15]);
        obligation.loadFromObject(vals[16]);
        woundThresh = (int)vals[17];
        woundCur = (int)vals[18];
        strainThresh = (int)vals[19];
        strainCur = (int)vals[20];
        xpTot = (int)vals[21];
        xpCur = (int)vals[22];
        defMelee = (int)vals[23];
        defRanged = (int)vals[24];
        soak = (int)vals[25];
        force = (int)vals[26];
        credits = (int)vals[27];
        morality = (int)vals[28];
        conflict = (int)vals[29];
        desc = (String)vals[30];
        showCard = (boolean[])vals[31];
        darkSide = (boolean)vals[32];
        age = (int)vals[33];
        nts.loadFromObject(vals[34]);
        if (nts == null){
            nts = new Notes();
        }
    }
}