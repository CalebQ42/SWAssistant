package com.apps.darkstorm.swrpg.assistant.sw;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.apps.darkstorm.swrpg.assistant.EditGeneral;
import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.custvars.SaveLoad;
import com.apps.darkstorm.swrpg.assistant.drive.DriveSaveLoad;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjury;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Dutys;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.ForcePower;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.ForcePowers;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Inventory;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Item;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Notes;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Obligations;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skill;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skills;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Specializations;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Talent;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Talents;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapon;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapons;
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

public class Character extends Editable{

    //
    //  |  Char v1 Start  |
    //  |                 |
    //  V      0-34       V
    //
    //public int ID;
    //public String name = "";
    //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
    public int[] charVals = new int[6];
    public Skills skills = new Skills();
    public String species = "";
    public String career = "";
    public Specializations specializations = new Specializations();
    public Talents talents = new Talents();
    public Inventory inv = new Inventory();
    //public Weapons weapons = new Weapons();
    public ForcePowers forcePowers = new ForcePowers();
    public String motivation = "";
    //public CriticalInjuries critInjuries = new CriticalInjuries();
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
    //public String desc = "";
    private boolean[] showCards = new boolean[16];
    public boolean darkSide;
    public int age;
    //public Notes nts (From Editable)
    //
    // Character V2 Start (35)
    //
    public int encumCapacity;
    //
    // Character v3 Start (36)
    //
    //public String category = "";
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
        for (int i = 0; i< showCards.length; i++){
            showCards[i] = true;
        }
    }
    public Character(int ID){
        this.ID = ID;
        morality = 50;
        for (int i = 0; i< showCards.length; i++){
            showCards[i] = true;
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
        sl.addSave(showCards);
        sl.addSave(darkSide);
        sl.addSave(age);
        sl.addSave(nts.serialObject());
        sl.addSave(encumCapacity);
        sl.addSave(category);
        sl.save();
    }
    public void reLoad(String filename){
        SaveLoad sl = new SaveLoad(filename);
        Object[] vals = sl.load();
        switch (vals.length){
            //later versions go here and fallthrough
            case 37:
                category = (String)vals[36];
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
                showCards = (boolean[])vals[31];
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
            sl.addSave(showCards);
            sl.addSave(darkSide);
            sl.addSave(age);
            sl.addSave(nts.serialObject());
            sl.addSave(encumCapacity);
            sl.addSave(category);
            sl.save(gac,async);
        }
    }
    public void reLoad(GoogleApiClient gac,DriveId fil){
        DriveSaveLoad sl = new DriveSaveLoad(fil);
        Object[] vals = sl.load(gac);
        switch (vals.length){
            //later versions go here and fallthrough
            case 37:
                category = (String)vals[36];
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
                showCards = (boolean[])vals[31];
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
        tmp.showCards = showCards.clone();
        tmp.darkSide = darkSide;
        tmp.age = age;
        tmp.nts = nts.clone();
        tmp.encumCapacity = encumCapacity;
        tmp.category = category;
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
                Arrays.equals(showCards, chara.showCards) && darkSide == chara.darkSide && age == chara.age && chara.nts.equals(nts) &&
                encumCapacity == chara.encumCapacity && category.equals(chara.category);
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
        out.add(showCards);
        out.add(darkSide);
        out.add(age);
        out.add(nts.serialObject());
        out.add(encumCapacity);
        out.add(category);
        return out.toArray();
    }
    public void loadFromObject(Object in){
        Object[] vals = (Object[])in;
        switch (vals.length){
            //later versions go here and fallthrough
            case 37:
                category = (String)vals[36];
            case 36:
                encumCapacity = (int)vals[35];
            case 35:
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
                showCards = (boolean[])vals[31];
                darkSide = (boolean)vals[32];
                age = (int)vals[33];
                nts.loadFromObject(vals[34]);
                if (nts == null){
                    nts = new Notes();
                }
        }
    }
    public void exportTo(String folder){
        File fold = new File(folder);
        if(!fold.exists()) {
            if (!fold.mkdir())
                return;
        }
        File f = new File(folder+"/"+name+".char");
        if(f.exists()){
            if(!f.delete())
                return;
        }
        save(folder+"/"+name+".char");
    }

    public int cardNumber(){
        return 17;
    }

    @Override
    public void setupCards(final Activity ac, final EditGeneral.EditableAdap ea, final CardView c, final int pos) {
        if (pos!= 0){
            final FrameLayout fl = (FrameLayout) c.findViewById(R.id.holder);
            fl.removeAllViews();
            Switch hide = (Switch) c.findViewById(R.id.hide);
            hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    showCards[pos - 1] = isChecked;
                    if (isChecked)
                        fl.setVisibility(View.VISIBLE);
                    else
                        fl.setVisibility(View.GONE);
                }
            });
            hide.setChecked(showCards[pos-1]);
            if (showCards[pos-1])
                fl.setVisibility(View.VISIBLE);
            else
                fl.setVisibility(View.GONE);
            switch(pos){
                //<editor-fold desc="infoCard">
                case 1:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.basic_info_text);
                    final View info = ac.getLayoutInflater().inflate(R.layout.layout_character_info,fl,false);
                    fl.addView(info);
                    final TextView speciesText = (TextView)info.findViewById(R.id.species);
                    speciesText.setText(species);
                    info.findViewById(R.id.species_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(species);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.species_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    species = et.getText().toString();
                                    speciesText.setText(species);
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView ageText = (TextView)info.findViewById(R.id.age);
                    ageText.setText(String.valueOf(age));
                    info.findViewById(R.id.age_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(age));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.age_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        age = Integer.parseInt(et.getText().toString());
                                    else
                                        age = 0;
                                    ageText.setText(String.valueOf(age));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView motivationText = (TextView)info.findViewById(R.id.motivation);
                    motivationText.setText(motivation);
                    info.findViewById(R.id.motivation_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(motivation);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.motivation_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    motivation = et.getText().toString();
                                    motivationText.setText(motivation);
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView careerText = (TextView)info.findViewById(R.id.career);
                    careerText.setText(career);
                    info.findViewById(R.id.career_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(career);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.career_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    career = et.getText().toString();
                                    careerText.setText(career);
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView categoryText = (TextView)info.findViewById(R.id.category_text);
                    categoryText.setText(category);
                    info.findViewById(R.id.category_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(category);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.category_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    category = et.getText().toString();
                                    categoryText.setText(category);
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="wound/strain">
                case 2:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.wound_strain_text);
                    final View ws = ac.getLayoutInflater().inflate(R.layout.layout_wound_strain,fl,false);
                    fl.addView(ws);
                    final TextView soakText = (TextView)ws.findViewById(R.id.soak_value);
                    soakText.setText(String.valueOf(soak));
                    ws.findViewById(R.id.soak_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(soak));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.soak_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        soak = Integer.parseInt(et.getText().toString());
                                    else
                                        soak = 0;
                                    soakText.setText(String.valueOf(soak));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    Animation in = AnimationUtils.loadAnimation(ac,android.R.anim.slide_in_left);
                    in.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
                    Animation out = AnimationUtils.loadAnimation(ac,android.R.anim.slide_out_right);
                    out.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
                    final TextSwitcher wound = (TextSwitcher)ws.findViewById(R.id.wound_switcher);
                    wound.setInAnimation(in);
                    wound.setOutAnimation(out);
                    wound.setFactory(new ViewSwitcher.ViewFactory() {
                        @Override
                        public View makeView() {
                            return ac.getLayoutInflater().inflate(R.layout.template_num_text,wound,false);
                        }
                    });
                    wound.setText(String.valueOf(woundCur));
                    ws.findViewById(R.id.wound_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(woundCur<woundThresh) {
                                woundCur++;
                                wound.setText(String.valueOf(woundCur));
                            }
                        }
                    });
                    ws.findViewById(R.id.wound_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(woundCur>0) {
                                woundCur--;
                                wound.setText(String.valueOf(woundCur));
                            }
                        }
                    });
                    final TextSwitcher strain = (TextSwitcher)ws.findViewById(R.id.strain_switcher);
                    strain.setInAnimation(in);
                    strain.setOutAnimation(out);
                    strain.setFactory(new ViewSwitcher.ViewFactory() {
                        @Override
                        public View makeView() {
                            return ac.getLayoutInflater().inflate(R.layout.template_num_text,strain,false);
                        }
                    });
                    strain.setText(String.valueOf(strainCur));
                    ws.findViewById(R.id.strain_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(strainCur<strainThresh) {
                                strainCur++;
                                strain.setText(String.valueOf(strainCur));
                            }
                        }
                    });
                    ws.findViewById(R.id.strain_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(strainCur>0) {
                                strainCur--;
                                strain.setText(String.valueOf(strainCur));
                            }
                        }
                    });
                    ws.findViewById(R.id.wound_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(woundThresh));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.wound_thresh_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        woundThresh = Integer.parseInt(et.getText().toString());
                                    else
                                        woundThresh = 0;
                                    if(woundCur>woundThresh){
                                        woundCur = woundThresh;
                                        wound.setText(String.valueOf(woundCur));
                                    }
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    ws.findViewById(R.id.strain_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(strainThresh));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.strain_thresh_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        strainThresh = Integer.parseInt(et.getText().toString());
                                    else
                                        strainThresh = 0;
                                    if(strainCur>strainThresh){
                                        strainCur = strainThresh;
                                        strain.setText(String.valueOf(strainCur));
                                    }
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Characteristics">
                case 3:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.characteristics_text);
                    final View chars = ac.getLayoutInflater().inflate(R.layout.layout_characteristics,fl,false);
                    fl.addView(chars);
                    final TextView brawnVal = (TextView)chars.findViewById(R.id.brawn_num);
                    brawnVal.setText(String.valueOf(charVals[0]));
                    chars.findViewById(R.id.brawn_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(charVals[0]));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.brawn_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        charVals[0] = Integer.parseInt(et.getText().toString());
                                    else
                                        charVals[0] = 0;
                                    brawnVal.setText(String.valueOf(charVals[0]));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView agilityVal = (TextView)chars.findViewById(R.id.agility_num);
                    agilityVal.setText(String.valueOf(charVals[1]));
                    chars.findViewById(R.id.agility_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(charVals[1]));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.agility_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        charVals[1] = Integer.parseInt(et.getText().toString());
                                    else
                                        charVals[1] = 0;
                                    agilityVal.setText(String.valueOf(charVals[1]));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView intellectVal = (TextView)chars.findViewById(R.id.intellect_num);
                    intellectVal.setText(String.valueOf(charVals[2]));
                    chars.findViewById(R.id.intellect_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(charVals[2]));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.intellect_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        charVals[2] = Integer.parseInt(et.getText().toString());
                                    else
                                        charVals[2] = 0;
                                    intellectVal.setText(String.valueOf(charVals[2]));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView cunningVal = (TextView)chars.findViewById(R.id.cunning_num);
                    cunningVal.setText(String.valueOf(charVals[3]));
                    chars.findViewById(R.id.cunning_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(charVals[3]));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.cunning_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        charVals[3] = Integer.parseInt(et.getText().toString());
                                    else
                                        charVals[3] = 0;
                                    cunningVal.setText(String.valueOf(charVals[3]));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView willpowerVal = (TextView)chars.findViewById(R.id.willpower_num);
                    willpowerVal.setText(String.valueOf(charVals[4]));
                    chars.findViewById(R.id.willpower_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(charVals[4]));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.willpower_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        charVals[4] = Integer.parseInt(et.getText().toString());
                                    else
                                        charVals[4] = 0;
                                    willpowerVal.setText(String.valueOf(charVals[4]));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView presenceVal = (TextView)chars.findViewById(R.id.presence_num);
                    presenceVal.setText(String.valueOf(charVals[5]));
                    chars.findViewById(R.id.presence_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(charVals[5]));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.presence_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        charVals[5] = Integer.parseInt(et.getText().toString());
                                    else
                                        charVals[5] = 0;
                                    presenceVal.setText(String.valueOf(charVals[5]));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Skill">
                case 4:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.skill_text);
                    final View skill = ac.getLayoutInflater().inflate(R.layout.layout_list,fl,false);
                    fl.addView(skill);
                    RecyclerView r = (RecyclerView)skill.findViewById(R.id.recycler);
                    final Skills.SkillsAdap adap = new Skills.SkillsAdap(this,ac);
                    r.setAdapter(adap);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    skill.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            skills.add(new Skill());
                            Skill.editSkill(ac, Character.this,skills.size() - 1,true, new Skill.onSave() {
                                public void save() {
                                    adap.notifyDataSetChanged();
                                }
                                public void delete() {
                                    skills.remove(skills.get(skills.size()-1));
                                }
                                public void cancel() {
                                    skills.remove(skills.get(skills.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Defense">
                case 5:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.defense_text);
                    View def = ac.getLayoutInflater().inflate(R.layout.layout_defense,fl,false);
                    fl.addView(def);
                    final TextView rng = (TextView)def.findViewById(R.id.ranged_num);
                    final TextView mel = (TextView)def.findViewById(R.id.melee_num);
                    rng.setText(String.valueOf(defRanged));
                    mel.setText(String.valueOf(defMelee));
                    def.findViewById(R.id.ranged_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(defRanged));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.ranged_defense_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        defRanged = Integer.parseInt(et.getText().toString());
                                    else
                                        defRanged = 0;
                                    rng.setText(String.valueOf(defRanged));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    def.findViewById(R.id.melee_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(defMelee));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.melee_defense_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        defMelee = Integer.parseInt(et.getText().toString());
                                    else
                                        defMelee = 0;
                                    mel.setText(String.valueOf(defMelee));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    break;
                //</editor-fold>s
                //<editor-fold desc="Weapons">
                case 6:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.weapons_text);
                    final View weapon = ac.getLayoutInflater().inflate(R.layout.layout_list,fl,false);
                    fl.addView(weapon);
                    r = (RecyclerView)weapon.findViewById(R.id.recycler);
                    final Weapons.WeaponsAdap adapW = new Weapons.WeaponsAdap(this, new Skill.onSave() {
                        @Override
                        public void save() {
                            ea.notifyItemChanged(12);
                        }

                        @Override
                        public void delete() {
                            ea.notifyItemChanged(12);
                        }

                        @Override
                        public void cancel() {

                        }
                    }, ac);
                    r.setAdapter(adapW);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    weapon.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            weapons.add(new Weapon());
                            Weapon.editWeapon(ac, Character.this,weapons.size() - 1,true, new Skill.onSave() {
                                public void save() {
                                    adapW.notifyDataSetChanged();
                                    ea.notifyItemChanged(12);
                                }
                                public void delete() {
                                    weapons.remove(weapons.get(weapons.size()-1));
                                }
                                public void cancel() {
                                    weapons.remove(weapons.get(weapons.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Crit inj">
                case 7:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.critical_injuries_text);
                    final View crit = ac.getLayoutInflater().inflate(R.layout.layout_list,fl,false);
                    fl.addView(crit);
                    r = (RecyclerView)crit.findViewById(R.id.recycler);
                    final CriticalInjuries.CriticalInjuriesAdapChar adapCrit = new CriticalInjuries.CriticalInjuriesAdapChar(this,ac);
                    r.setAdapter(adapCrit);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    crit.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            critInjuries.add(new CriticalInjury());
                            CriticalInjury.editCritical(ac, Character.this,critInjuries.size() - 1,true, new Skill.onSave() {
                                public void save() {
                                    adapCrit.notifyDataSetChanged();
                                }
                                public void delete() {
                                    critInjuries.remove(critInjuries.get(critInjuries.size()-1));
                                }
                                public void cancel() {
                                    critInjuries.remove(critInjuries.get(critInjuries.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Specializations">
                case 8:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.specializations_text);
                    final View spec = ac.getLayoutInflater().inflate(R.layout.layout_list,fl,false);
                    fl.addView(spec);
                    r = (RecyclerView)spec.findViewById(R.id.recycler);
                    final Specializations.SpecializationsAdapter adapSpec = new Specializations.SpecializationsAdapter(this,ac);
                    r.setAdapter(adapSpec);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    spec.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            specializations.add("");
                            Specializations.editSpecialization(ac, Character.this,specializations.size() - 1,true, new Skill.onSave() {
                                public void save() {
                                    adapSpec.notifyDataSetChanged();
                                }
                                public void delete() {
                                    critInjuries.remove(critInjuries.get(critInjuries.size()-1));
                                }
                                public void cancel() {
                                    critInjuries.remove(critInjuries.get(critInjuries.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Talents">
                case 9:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.talents_text);
                    final View tal = ac.getLayoutInflater().inflate(R.layout.layout_list,fl,false);
                    fl.addView(tal);
                    r = (RecyclerView)tal.findViewById(R.id.recycler);
                    final Talents.TalentsAdap adapTal = new Talents.TalentsAdap(this,ac);
                    r.setAdapter(adapTal);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    tal.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            talents.add(new Talent());
                            Talent.editTalent(ac, Character.this,talents.size() - 1,true, new Skill.onSave() {
                                public void save() {
                                    adapTal.notifyDataSetChanged();
                                }
                                public void delete() {
                                    talents.remove(talents.get(talents.size()-1));
                                }
                                public void cancel() {
                                    talents.remove(talents.get(talents.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Force Powers">
                case 10:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.force_powers_text);
                    View powers = ac.getLayoutInflater().inflate(R.layout.layout_force_powers,fl,false);
                    fl.addView(powers);
                    final TextView rating = (TextView)powers.findViewById(R.id.fr_value);
                    rating.setText(String.valueOf(force));
                    powers.findViewById(R.id.soak_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(force));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.force_rating_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        force = Integer.parseInt(et.getText().toString());
                                    else
                                        force = 0;
                                    rating.setText(String.valueOf(force));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final ForcePowers.ForcePowersAdap fpAdap = new ForcePowers.ForcePowersAdap(this,ac);
                    r = (RecyclerView)powers.findViewById(R.id.recycler);
                    r.setAdapter(fpAdap);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    powers.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forcePowers.add(new ForcePower());
                            ForcePower.editForcePower(ac, Character.this, forcePowers.size() - 1, true, new Skill.onSave() {
                                @Override
                                public void save() {
                                    fpAdap.notifyDataSetChanged();
                                }

                                @Override
                                public void delete() {
                                    forcePowers.remove(forcePowers.get(forcePowers.size()-1));
                                }

                                @Override
                                public void cancel() {
                                    forcePowers.remove(forcePowers.get(forcePowers.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="XP">
                case 11:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.xp_text);
                    View xpLay = ac.getLayoutInflater().inflate(R.layout.layout_xp,fl,false);
                    fl.addView(xpLay);
                    final TextView cur = (TextView)xpLay.findViewById(R.id.current_value);
                    cur.setText(String.valueOf(xpCur));
                    xpLay.findViewById(R.id.current_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(xpCur));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.current_xp_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        xpCur = Integer.parseInt(et.getText().toString());
                                    else
                                        xpCur = 0;
                                    cur.setText(String.valueOf(xpCur));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    final TextView tot = (TextView)xpLay.findViewById(R.id.total_value);
                    tot.setText(String.valueOf(xpTot));
                    xpLay.findViewById(R.id.total_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(xpTot));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.total_xp_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        xpTot = Integer.parseInt(et.getText().toString());
                                    else
                                        xpTot = 0;
                                    tot.setText(String.valueOf(xpTot));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    xpLay.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.xp_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals("")) {
                                        int add = Integer.parseInt(et.getText().toString());
                                        xpTot += add;
                                        xpCur += add;
                                        tot.setText(String.valueOf(xpTot));
                                        cur.setText(String.valueOf(xpCur));
                                    }
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Inv">
                case 12:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.inventory_text);
                    View invLay = ac.getLayoutInflater().inflate(R.layout.layout_inventory,fl,false);
                    fl.addView(invLay);
                    final TextView creds = (TextView)invLay.findViewById(R.id.credits_value);
                    creds.setText(String.valueOf(credits));
                    final TextView encum = (TextView)invLay.findViewById(R.id.encum_value);
                    encum.setText(String.valueOf(encumCapacity));
                    invLay.findViewById(R.id.credits_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(credits));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.credits_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        credits = Integer.parseInt(et.getText().toString());
                                    else
                                        credits = 0;
                                    creds.setText(String.valueOf(credits));
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    invLay.findViewById(R.id.encum_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(encumCapacity));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.encumbrance_capacity_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        encumCapacity = Integer.parseInt(et.getText().toString());
                                    else
                                        encumCapacity = 0;
                                    encum.setText(String.valueOf(encumCapacity));
                                    ea.notifyItemChanged(12);
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    if(inv.totalEncum()+weapons.totalEncum()>encumCapacity)
                        invLay.findViewById(R.id.encum_warning).setVisibility(View.VISIBLE);
                    else
                        invLay.findViewById(R.id.encum_warning).setVisibility(View.GONE);
                    final Inventory.InventoryAdap invAdap = new Inventory.InventoryAdap(this, new Skill.onSave() {
                        @Override
                        public void save() {
                            ea.notifyItemChanged(12);
                        }

                        @Override
                        public void delete() {
                            ea.notifyItemChanged(12);
                        }

                        @Override
                        public void cancel() {

                        }
                    }, ac);
                    r = (RecyclerView)invLay.findViewById(R.id.recycler);
                    r.setAdapter(invAdap);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    invLay.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inv.add(new Item());
                            Item.editItem(ac, Character.this, inv.size() - 1, true, new Skill.onSave() {
                                @Override
                                public void save() {
                                    invAdap.notifyDataSetChanged();
                                    ea.notifyItemChanged(12);
                                }
                                public void delete() {
                                }
                                public void cancel() {
                                    inv.remove(inv.get(inv.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
                //morality
                case 13:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.morality_text);
                    //TODO
                    break;
                //duty
                case 14:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.duty_text);
                    //TODO
                    break;
                //obligation
                case 15:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.obligation_text);
                    //TODO
                    break;
                //<editor-fold desc="desc">
                case 16:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.description_text);
                    final TextView descText = (TextView)ac.getLayoutInflater().inflate(R.layout.layout_desc,fl,false);
                    fl.addView(descText);
                    descText.setText(desc);
                    descText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(desc);
                            et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE|
                            InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            et.setSingleLine(false);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.description_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    desc = et.getText().toString();
                                    descText.setText(desc);
                                    dialog.cancel();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            b.show();
                            return true;
                        }
                    });
                    break;
                //</editor-fold>
            }
        }else{
            ((TextView)c.findViewById(R.id.name)).setText(name);
            c.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(ac);
                    View vw = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                    b.setView(vw);
                    TextInputLayout t = (TextInputLayout)vw.findViewById(R.id.edit_layout);
                    t.setHint(ac.getString(R.string.name_text));
                    final EditText et = (EditText)vw.findViewById(R.id.edit_text);
                    et.setText(Character.this.name);
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Character.this.name = et.getText().toString();
                            ((TextView)c.findViewById(R.id.name)).setText(Character.this.name);
                            dialog.cancel();
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    b.show();
                    return true;
                }
            });
            c.findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(ac);
                    View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                    b.setView(in);
                    final EditText et = (EditText)in.findViewById(R.id.edit_text);
                    et.setText(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),
                            ((SWrpg)ac.getApplication()).defaultLoc));
                    //TODO: resource
                    ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint("Export Location");
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exportTo(et.getText().toString());
                            dialog.cancel();
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    b.show();
                }
            });
            c.findViewById(R.id.clone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Character[] characters = LoadLocal.characters(ac);
                    ArrayList<Integer> IDs = new ArrayList<>();
                    for (Character c:characters){
                        IDs.add(c.ID);
                    }
                    int ID = 0;
                    while(IDs.contains(ID)){
                        ID++;
                    }
                    Character ch = Character.this.clone();
                    ch.ID = ID;
                    ch.save(ch.getFileLocation(ac));
                    if(((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.google_drive_key),false))
                        ch.cloudSave(((SWrpg)ac.getApplication()).gac,ch.getFileId(ac),true);
                }
            });
        }
    }
}
