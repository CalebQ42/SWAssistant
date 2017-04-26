package com.apps.darkstorm.swrpg.assistant.sw;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.EditGeneral;
import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.custvars.SaveLoad;
import com.apps.darkstorm.swrpg.assistant.drive.DriveSaveLoad;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
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
    //public Notes nts (From Editable)
    //
    // Character V2 Start (35)
    //
    public int encumCapacity;
    //
    // Character v3 Start (36)
    //
    public String category = "";
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
                Arrays.equals(showCard, chara.showCard) && darkSide == chara.darkSide && age == chara.age && chara.nts.equals(nts) &&
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
        out.add(showCard);
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
                showCard = (boolean[])vals[31];
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
    public void setupCards(final Activity ac, EditGeneral.EditableAdap ea,final CardView c, int pos) {
        switch(pos){
            //Name Card
            case 0:
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
                        b.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
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
                        Character ch = new Character(ID);
                        ch.save(ch.getFileLocation(ac));
                        if(((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.color_dice_key),false))
                            ch.cloudSave(((SWrpg)ac.getApplication()).gac,ch.getFileId(ac),true);
                    }
                });
                break;
            //info card
            case 1:
                final FrameLayout fl = (FrameLayout) c.findViewById(R.id.holder);
                final View info = ac.getLayoutInflater().inflate(R.layout.layout_character_info,fl,false);
                fl.addView(info);
                Switch hide = (Switch)c.findViewById(R.id.hide);
                hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        showCard[0] = !isChecked;
                        if(isChecked){
                            fl.setVisibility(View.GONE);
                        }else{
                            fl.setVisibility(View.VISIBLE);
                        }
                    }
                });
                hide.setChecked(!showCard[0]);
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
                                age = Integer.parseInt(et.getText().toString());
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
            //wound/strain
            case 2:
                //TODO
                break;
            //Characteristics
            case 3:
                //TODO
                break;
            //Skill
            case 4:
                //TODO
                break;
            //Defense
            case 5:
                //TODO
                break;
            //Weapons
            case 6:
                //TODO
                break;
            //Crit inj
            case 7:
                //TODO
                break;
            //Specializations
            case 8:
                //TODO
                break;
            //Talents
            case 9:
                //TODO
                break;
            //Force Powers
            case 10:
                //TODO
                break;
            //XP
            case 11:
                //TODO
                break;
            //Inv
            case 12:
                //TODO
                break;
            //morality
            case 13:
                //TODO
                break;
            //duty
            case 14:
                //TODO
                break;
            //obligation
            case 15:
                //TODO
                break;
            //desc
            case 16:
                //TODO
                break;
        }
    }
}
