package com.apps.darkstorm.swrpg.StarWars;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.apps.darkstorm.swrpg.CharacterList;
import com.apps.darkstorm.swrpg.CustVars.DriveSaveLoad;
import com.apps.darkstorm.swrpg.CustVars.SaveLoad;
import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Dutys;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.ForcePowers;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Inventory;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Notes;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Obligations;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Skills;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Specializations;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Talents;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Weapons;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.File;
import java.util.Arrays;

public class Character {
    //
    //  |  Char v1 Start  |
    //  |                 |
    //  V      0-34       V
    //
    public int ID;
    public String name;
    //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
    public int[] charVals = new int[6];
    public Skills skills = new Skills();
    public String species;
    public String career;
    public Specializations specializations = new Specializations();
    public Talents talents = new Talents();
    public Inventory inv = new Inventory();
    public Weapons weapons = new Weapons();
    public ForcePowers forcePowers = new ForcePowers();
    public String motivation;
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
    public String desc;
    boolean[] showCard = new boolean[16];
    public boolean darkSide;
    public int age;
    public Notes nts = new Notes();
    //
    //  ^                 ^
    //  |  Character End  |
    //  |                 |
    //

    private boolean editing = false;
    private boolean saving = false;

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
    public void startEditing(final Context main, final GoogleApiClient gac, final DriveId fold){
        if (!editing) {
            editing = true;
            AsyncTask<Void,Void,Void> blablah = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    System.out.println("Editing");
                    Character tmpChar = Character.this.clone();
                    do{
                        if (!saving) {
                            saving = true;
                            if (!equals(tmpChar)) {
                                Character.this.save(Character.this.getFileLocation(main));
                                if (fold != null && gac.isConnected())
                                    cloudSave(gac,getFileId(gac,fold),false);
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
                        if (!equals(tmpChar)) {
                            Character.this.save(Character.this.getFileLocation(main));
                            if (fold != null && gac.isConnected())
                                cloudSave(gac,getFileId(gac,fold),false);
                        }
                        saving = false;
                    }
                    return null;
                }
            };
            blablah.execute();
        }
    }
    public void startEditing(final Context main){
        if (!editing) {
            editing = true;
            Thread tmp = new Thread(new Runnable() {
                @Override
                public void run() {
                    Character tmpChar = Character.this.clone();
                    do{
                        if (!saving) {
                            saving = true;
                            if (!equals(tmpChar)) {
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
                        if (!equals(tmpChar)) {
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
        sl.save();
    }
    public void reLoad(String filename){
        SaveLoad sl = new SaveLoad(filename);
        Object[] vals = sl.load();
        switch (vals.length){
            //later versions go here and fallthrough
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
        }
    }
    public void reLoad(GoogleApiClient gac,DriveId fil){
        DriveSaveLoad sl = new DriveSaveLoad(fil);
        Object[] vals = sl.load(gac);
        switch (vals.length){
            //later versions go here and fallthrough
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
        }
    }
    public String getFileLocation(Context main){
        File location;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            File tmp = Environment.getExternalStorageDirectory();
            location = new File(tmp.getAbsolutePath() + "/SWChars");
            if (!location.exists()){
                if (!location.mkdir()){
                    return "";
                }
            }
        }else{
            File tmp = main.getFilesDir();
            location = new File(tmp.getAbsolutePath() + "/SWChars");
            if (!location.exists()){
                if (!location.mkdir()){
                    return "";
                }
            }
        }
        SharedPreferences pref = main.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        String def = location.getAbsolutePath();
        String loc = pref.getString(main.getString(R.string.local_location_key),def);
        location = new File(loc);
        if (!location.exists()){
            if (!location.mkdir()){
                return "";
            }
        }
        return location.getAbsolutePath() + "/" + Integer.toString(ID) + ".char";
    }
    public DriveId getFileId(GoogleApiClient gac,DriveId fold){
        String name = Integer.toString(ID) + ".char";
        DriveFolder folder = fold.asDriveFolder();
        Boolean create = true;
        DriveId fi = null;
        DriveApi.MetadataBufferResult res = folder.listChildren(gac).await();
        for (Metadata met:res.getMetadataBuffer()){
            if (!met.isFolder() && met.getTitle().equals(name)){
                create = false;
                fi = met.getDriveId();
                break;
            }
        }
        res.release();
        if (create){
            fi = folder.createFile(gac,new MetadataChangeSet.Builder().setTitle(name).build(),null).await()
                    .getDriveFile().getDriveId();
        }
        return fi;
    }
    public Character clone(){
        Character tmp;
        try {
            tmp = (Character)super.clone();
        }catch (CloneNotSupportedException e){
            tmp = new Character(ID);
        }
        tmp.name = name;
        tmp.charVals = charVals.clone();
        tmp.skills = skills;
        tmp.species = species;
        tmp.career = career;
        tmp.specializations = specializations;
        tmp.talents = talents;
        tmp.inv = inv;
        tmp.weapons = weapons;
        tmp.forcePowers = forcePowers;
        tmp.motivation = motivation;
        tmp.critInjuries = critInjuries;
        tmp.emotionalStr = emotionalStr.clone();
        tmp.emotionalWeak = emotionalWeak.clone();
        tmp.duty = duty;
        tmp.obligation = obligation;
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
        tmp.showCard = showCard;
        tmp.darkSide = darkSide;
        tmp.age = age;
        tmp.nts = nts;
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
    public void showHideCards(final View top){
        ((Switch)top.findViewById(R.id.species_career_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.species_career_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.species_career_main).setVisibility(View.GONE);
                }
                showCard[0] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.species_career_show)).setChecked(showCard[0]);
        if (showCard[0]) {
            top.findViewById(R.id.species_career_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.species_career_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.wound_strain_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.wound_strain_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.wound_strain_main).setVisibility(View.GONE);
                }
                showCard[1] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.wound_strain_show)).setChecked(showCard[1]);
        if (showCard[1]) {
            top.findViewById(R.id.wound_strain_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.wound_strain_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.characteristics_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.characteristics_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.characteristics_main).setVisibility(View.GONE);
                }
                showCard[2] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.characteristics_show)).setChecked(showCard[2]);
        if (showCard[2]) {
            top.findViewById(R.id.characteristics_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.characteristics_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.skill_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.skill_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.skill_main).setVisibility(View.GONE);
                }
                showCard[3] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.skill_show)).setChecked(showCard[3]);
        if (showCard[3]) {
            top.findViewById(R.id.skill_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.skill_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.defense_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.defense_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.defense_main).setVisibility(View.GONE);
                }
                showCard[4] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.defense_show)).setChecked(showCard[4]);
        if (showCard[4]) {
            top.findViewById(R.id.defense_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.defense_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.weapons_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.weapons_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.weapons_main).setVisibility(View.GONE);
                }
                showCard[5] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.weapons_show)).setChecked(showCard[5]);
        if (showCard[5]) {
            top.findViewById(R.id.weapons_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.weapons_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.critical_injuries_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.critical_injuries_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.critical_injuries_main).setVisibility(View.GONE);
                }
                showCard[6] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.critical_injuries_show)).setChecked(showCard[6]);
        if (showCard[6]) {
            top.findViewById(R.id.critical_injuries_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.critical_injuries_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.specialization_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.specialization_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.specialization_main).setVisibility(View.GONE);
                }
                showCard[7] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.specialization_show)).setChecked(showCard[7]);
        if (showCard[7]) {
            top.findViewById(R.id.specialization_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.specialization_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.talents_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.talents_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.talents_main).setVisibility(View.GONE);
                }
                showCard[8] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.talents_show)).setChecked(showCard[8]);
        if (showCard[8]) {
            top.findViewById(R.id.talents_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.talents_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.force_powers_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.force_powers_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.force_powers_main).setVisibility(View.GONE);
                }
                showCard[9] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.force_powers_show)).setChecked(showCard[9]);
        if (showCard[9]) {
            top.findViewById(R.id.force_powers_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.force_powers_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.xp_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.xp_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.xp_main).setVisibility(View.GONE);
                }
                showCard[10] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.xp_show)).setChecked(showCard[10]);
        if (showCard[10]) {
            top.findViewById(R.id.xp_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.xp_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.inventory_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.inventory_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.inventory_main).setVisibility(View.GONE);
                }
                showCard[11] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.inventory_show)).setChecked(showCard[11]);
        if (showCard[11]) {
            top.findViewById(R.id.inventory_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.inventory_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.emotions_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.emotions_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.emotions_main).setVisibility(View.GONE);
                }
                showCard[12] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.emotions_show)).setChecked(showCard[12]);
        if (showCard[12]) {
            top.findViewById(R.id.emotions_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.emotions_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.duty_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.duty_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.duty_main).setVisibility(View.GONE);
                }
                showCard[13] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.duty_show)).setChecked(showCard[13]);
        if (showCard[13]) {
            top.findViewById(R.id.duty_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.duty_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.obligation_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.obligation_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.obligation_main).setVisibility(View.GONE);
                }
                showCard[14] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.obligation_show)).setChecked(showCard[14]);
        if (showCard[14]) {
            top.findViewById(R.id.obligation_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.obligation_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.desc_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.desc_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.desc_main).setVisibility(View.GONE);
                }
                showCard[15] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.desc_show)).setChecked(showCard[15]);
        if (showCard[15]) {
            top.findViewById(R.id.desc_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.desc_main).setVisibility(View.GONE);
        }
    }
    public void cloudSave(GoogleApiClient gac,DriveId fil, boolean async){
        if (fil != null) {
            DriveFile file = fil.asDriveFile();
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
            sl.save(gac,async);
        }
    }
    public boolean equalsChar(Character chara){
        try {
            return chara.name.equals(name) ||
                    chara.ID == ID ||
                    Arrays.equals(chara.charVals, charVals) ||
                    chara.skills.equals(skills) ||
                    chara.species.equals(species) ||
                    chara.career.equals(career) ||
                    chara.specializations.equals(specializations) ||
                    chara.talents.equals(talents) ||
                    chara.inv.equals(inv) ||
                    chara.weapons.equals(weapons) ||
                    chara.forcePowers.equals(forcePowers) ||
                    chara.motivation.equals(motivation) ||
                    chara.critInjuries.equals(critInjuries) ||
                    Arrays.equals(emotionalStr, chara.emotionalStr) ||
                    Arrays.equals(chara.emotionalWeak, emotionalWeak) ||
                    chara.duty.equals(duty) ||
                    chara.obligation.equals(obligation) ||
                    woundThresh == chara.woundThresh ||
                    woundCur == chara.woundCur ||
                    strainThresh == chara.strainThresh ||
                    strainCur == chara.strainCur ||
                    xpCur == chara.xpCur ||
                    xpTot == chara.xpTot ||
                    defMelee == chara.defMelee ||
                    defRanged == chara.defRanged ||
                    soak == chara.soak ||
                    force == chara.force ||
                    credits == chara.credits ||
                    morality == chara.morality ||
                    conflict == chara.conflict ||
                    desc.equals(chara.desc) ||
                    Arrays.equals(showCard, chara.showCard) ||
                    darkSide == chara.darkSide ||
                    age == chara.age ||
                    nts.equals(chara.nts);
        }catch(java.lang.NullPointerException ignored){
            return false;
        }
    }
}
