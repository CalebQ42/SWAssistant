package com.apps.darkstorm.swrpg.StarWars;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.apps.darkstorm.swrpg.CustVars.DriveSaveLoad;
import com.apps.darkstorm.swrpg.CustVars.SaveLoad;
import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Inventory;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Skills;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Talents;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Weapons;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.util.Arrays;

public class Minion {
    //Version 1 0-15
    public int ID;
    public String name = "";
    //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
    public int[] charVals = new int[6];
    public Skills skills = new Skills();
    public Talents talents = new Talents();
    public Inventory inv = new Inventory();
    public Weapons weapons = new Weapons();
    private int woundThreshInd = 1;
    public int woundThresh;
    private int woundCur;
    public int defMelee,defRanged;
    public int soak;
    private int minNum;
    public String desc = "";
    private boolean[] showCard = new boolean[9];


    private boolean editing = false;
    private boolean saving = false;

    public Minion(){
        for (int i = 0; i< showCard.length; i++)
            showCard[i] = true;
    }
    public Minion(int ID){
        this.ID = ID;
        for (int i = 0; i< showCard.length; i++)
            showCard[i] = true;
    }
    public void startEditing(final Activity main, final DriveId fold){
        if (!editing) {
            editing = true;
            AsyncTask<Void,Void,Void> blablah = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Minion tmpChar = Minion.this.clone();
                    Minion.this.save(Minion.this.getFileLocation(main));
                    cloudSave(((SWrpg)main.getApplication()).gac,getFileId(((SWrpg)main.getApplication()).gac,
                            fold),false);
                    do{
                        if (!saving) {
                            saving = true;
                            if (!Minion.this.equals(tmpChar)) {
                                Minion.this.save(Minion.this.getFileLocation(main));
                                cloudSave(((SWrpg)main.getApplication()).gac,
                                        getFileId(((SWrpg)main.getApplication()).gac,fold),false);
                                tmpChar = Minion.this.clone();
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
                        if (!Minion.this.equals(tmpChar)) {
                            Minion.this.save(Minion.this.getFileLocation(main));
                            cloudSave(((SWrpg)main.getApplication()).gac,getFileId(
                                    ((SWrpg)main.getApplication()).gac,fold),false);
                        }
                        saving = false;
                    }
                    return null;
                }
            };
            blablah.execute();
        }
    }
    public void startEditing(final Activity main){
        if (!editing) {
            editing = true;
            Thread tmp = new Thread(new Runnable() {
                @Override
                public void run() {
                    Minion tmpChar = Minion.this.clone();
                    Minion.this.save(Minion.this.getFileLocation(main));
                    do{
                        if (!saving) {
                            saving = true;
                            if (!Minion.this.equals(tmpChar)) {
                                System.out.println("saving...");
                                Minion.this.save(Minion.this.getFileLocation(main));
                                tmpChar = Minion.this.clone();
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
                        if (!Minion.this.equals(tmpChar)) {
                            Minion.this.save(Minion.this.getFileLocation(main));
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
        SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String def = location.getAbsolutePath();
        String loc = pref.getString(main.getString(R.string.local_location_key),def);
        location = new File(loc);
        if (!location.exists()){
            if (!location.mkdir()){
                return "";
            }
        }
        return location.getAbsolutePath() + "/" + Integer.toString(ID) + ".minion";
    }
    void save(String filename){
        SaveLoad sl = new SaveLoad(filename);
        sl.addSave(ID);
        sl.addSave(name);
        sl.addSave(charVals);
        sl.addSave(skills.serialObject());
        sl.addSave(talents.serialObject());
        sl.addSave(inv.serialObject());
        sl.addSave(weapons.serialObject());
        sl.addSave(woundThreshInd);
        sl.addSave(woundThresh);
        sl.addSave(woundCur);
        sl.addSave(defMelee);
        sl.addSave(defRanged);
        sl.addSave(soak);
        sl.addSave(minNum);
        sl.addSave(desc);
        sl.addSave(showCard);
        sl.save();
    }
    void reLoad(String filename){
        SaveLoad sl = new SaveLoad(filename);
        Object[] obj = sl.load();
        switch(obj.length){
            case 16:
                showCard = (boolean[])obj[15];
                desc = (String)obj[14];
                minNum = (int)obj[13];
                soak = (int)obj[12];
                defRanged = (int)obj[11];
                defMelee = (int)obj[10];
                woundCur = (int)obj[9];
                woundThresh = (int)obj[8];
                woundThreshInd = (int)obj[7];
                weapons.loadFromObject(obj[6]);
                inv.loadFromObject(obj[5]);
                talents.loadFromObject(obj[4]);
                skills.loadFromObject(obj[3]);
                charVals = (int[])obj[2];
                name = (String)obj[1];
                String title = filename.substring(filename.lastIndexOf("/")+1);
                if (title.substring(0,title.indexOf(".")).equals(""))
                    ID = (int)obj[0];
                else
                    ID = Integer.parseInt(title.substring(0,title.indexOf(".")));
        }
    }
    public DriveId getFileId(GoogleApiClient gac, DriveId fold){
        String name = Integer.toString(ID) + ".minion";
        DriveFolder folder = fold.asDriveFolder();
        DriveId fi = null;
        DriveApi.MetadataBufferResult res = folder.queryChildren(gac,new Query.Builder().addFilter(
                Filters.eq(SearchableField.TITLE,name)).build()).await();
        for (Metadata met:res.getMetadataBuffer()){
            if (!met.isTrashed()){
                fi = met.getDriveId();
                break;
            }
        }
        res.release();
        if (fi == null)
            fi = folder.createFile(gac,new MetadataChangeSet.Builder().setTitle(name).build(),null).await()
                    .getDriveFile().getDriveId();
        return fi;
    }
    void cloudSave(GoogleApiClient gac,DriveId fil, boolean async){
        if (fil != null){
            DriveSaveLoad sl = new DriveSaveLoad(fil);
            sl.addSave(ID);
            sl.addSave(name);
            sl.addSave(charVals);
            sl.addSave(skills.serialObject());
            sl.addSave(talents.serialObject());
            sl.addSave(inv.serialObject());
            sl.addSave(weapons.serialObject());
            sl.addSave(woundThreshInd);
            sl.addSave(woundThresh);
            sl.addSave(woundCur);
            sl.addSave(defMelee);
            sl.addSave(defRanged);
            sl.addSave(soak);
            sl.addSave(minNum);
            sl.addSave(desc);
            sl.addSave(showCard);
            sl.save(gac, async);
        }
    }
    void reLoad(GoogleApiClient gac,DriveId fil){
        DriveSaveLoad sl = new DriveSaveLoad(fil);
        Object[] obj = sl.load(gac);
        switch(obj.length){
            case 16:
                showCard = (boolean[])obj[15];
                desc = (String)obj[14];
                minNum = (int)obj[13];
                soak = (int)obj[12];
                defRanged = (int)obj[11];
                defMelee = (int)obj[10];
                woundCur = (int)obj[9];
                woundThresh = (int)obj[8];
                woundThreshInd = (int)obj[7];
                weapons.loadFromObject(obj[6]);
                inv.loadFromObject(obj[5]);
                talents.loadFromObject(obj[4]);
                skills.loadFromObject(obj[3]);
                charVals = (int[])obj[2];
                name = (String)obj[1];
                String title = fil.asDriveFile().getMetadata(gac).await().getMetadata().getTitle();
                if (title.substring(0,title.indexOf(".")).equals(""))
                    ID = (int)obj[0];
                else
                    ID = Integer.parseInt(title.substring(0,title.indexOf(".")));
        }
    }

    public void setWound(int wound){
        woundCur = wound;
        if (((woundCur/woundThreshInd)<minNum-1 || (woundCur/woundThreshInd == minNum-1 && woundCur%woundThreshInd == 0) )&& minNum != 0){
            int num = woundCur/woundThreshInd;
            if (woundCur%woundThreshInd>0)
                num++;
            setMinNum(num);
        }else if (woundCur/woundThreshInd > minNum ||
                (woundCur/woundThreshInd == minNum && woundCur%woundThreshInd > 0)){
            int num = woundCur/woundThreshInd;
            if (woundCur%woundThreshInd>0)
                num++;
            setMinNum(num);
        }
    }
    public int getWound(){
        return woundCur;
    }
    public void setMinNum(int num){
        minNum = num;
        woundThresh = woundThreshInd*minNum;
        woundCur = woundThresh;
        for (int i = 0;i<skills.size();i++)
            skills.get(i).val = minNum -1;
    }
    public int getMinNum(){
        return minNum;
    }
    public void setWoundInd(int wound){
        if (wound <= 0)
            wound = 1;
        woundThreshInd = wound;
        woundThresh = minNum*woundThreshInd;
        woundCur = woundThresh;
    }
    public int getWoundInd(){
        return woundThreshInd;
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
        min.showCard = showCard.clone();
        return min;
    }
    public void showHideCards(final View top){
        ((Switch)top.findViewById(R.id.min_num_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.min_num_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.min_num_main).setVisibility(View.GONE);
                }
                showCard[0] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.min_num_show)).setChecked(showCard[0]);
        if (showCard[0]) {
            top.findViewById(R.id.min_num_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.min_num_main).setVisibility(View.GONE);
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
        ((Switch)top.findViewById(R.id.inventory_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.inventory_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.inventory_main).setVisibility(View.GONE);
                }
                showCard[6] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.inventory_show)).setChecked(showCard[6]);
        if (showCard[6]) {
            top.findViewById(R.id.inventory_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.inventory_main).setVisibility(View.GONE);
        }
        ((Switch)top.findViewById(R.id.desc_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    top.findViewById(R.id.desc_main).setVisibility(View.VISIBLE);
                }else{
                    top.findViewById(R.id.desc_main).setVisibility(View.GONE);
                }
                showCard[7] = isChecked;
            }
        });
        ((Switch)top.findViewById(R.id.desc_show)).setChecked(showCard[7]);
        if (showCard[7]) {
            top.findViewById(R.id.desc_main).setVisibility(View.VISIBLE);
        }else{
            top.findViewById(R.id.desc_main).setVisibility(View.GONE);
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
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Minion))
            return false;
        Minion tmp = (Minion)obj;
        return tmp.name.equals(name) && tmp.ID == ID && Arrays.equals(tmp.charVals,charVals) &&
                tmp.skills.equals(skills) && tmp.talents.equals(talents) && tmp.inv.equals(inv) &&
                tmp.weapons.equals(weapons) && tmp.woundThresh == woundThresh && tmp.woundCur == woundCur &&
                tmp.defMelee == defMelee && tmp.defRanged == defRanged && tmp.soak == soak && tmp.desc.equals(desc) &&
                Arrays.equals(tmp.showCard, showCard) && woundThreshInd == tmp.woundThreshInd && minNum == tmp.minNum;
    }
}
