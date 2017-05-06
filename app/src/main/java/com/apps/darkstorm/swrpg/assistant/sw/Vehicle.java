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
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjury;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skill;
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

public class Vehicle extends Editable{
    //
    //  |  Version 1  |
    //  |             |
    //  V     0-19    V
    //
    //public int ID;
    //public String name = "";
    public int silhouette;
    public int speed;
    public int handling;
    public int armor;
    //0-Fore,1-Port,2-Starboard,3-Aft;
    public int[] defense = new int[4];
    public int totalDefense;
    public int hullTraumaThresh;
    public int hullTraumaCur;
    public int sysStressThresh;
    public int sysStressCur;
    public int encumCapacity;
    public int passengerCapacity;
    public int hp;
    //public Weapons weapons = new Weapons();
    //public CriticalInjuries crits = new CriticalInjuries();
    private boolean[] showCards = new boolean[6];
    //public String desc = "";
    public String model = "";
    //
    // Version 2 start (20-1)
    //
    //public String category = "";
    //Public Notes nts (From Editable)
    //
    //  ^               ^
    //  |  Vehicle End  |
    //  |               |
    //

    private boolean editing = false;
    private boolean saving = false;
    private String loc="";
    public boolean external = false;


    public Vehicle(){
        defense[1] = -1;
        defense[2] = -1;
        for (int i = 0;i<showCards.length;i++)
            showCards[i] = true;
    }
    public Vehicle(int ID){
        this.ID = ID;
        defense[1] = -1;
        defense[2] = -1;
        for (int i = 0;i<showCards.length;i++)
            showCards[i] = true;
    }
    public void setSilhouette(int sil){
        silhouette = sil;
        if (sil>4){
            if(defense[1]==-1)
                defense[1]=0;
            if(defense[2]==-1)
                defense[2] = 0;
        }else{
            if(defense[1]!=-1)
                defense[1] = -1;
            if(defense[2]!=-1)
                defense[2]=-1;
        }
    }
    public void stopEditing(){
        editing=false;
    }
    public Vehicle clone(){
        Vehicle tmp = new Vehicle();
        tmp.ID = ID;
        tmp.name = name;
        tmp.silhouette = silhouette;
        tmp.speed = speed;
        tmp.handling = handling;
        tmp.armor = armor;
        tmp.defense = defense.clone();
        tmp.totalDefense = totalDefense;
        tmp.hullTraumaCur = hullTraumaCur;
        tmp.hullTraumaThresh = hullTraumaThresh;
        tmp.sysStressCur = sysStressCur;
        tmp.sysStressThresh = sysStressThresh;
        tmp.encumCapacity = encumCapacity;
        tmp.passengerCapacity = passengerCapacity;
        tmp.hp = hp;
        tmp.weapons = weapons.clone();
        tmp.critInjuries = critInjuries.clone();
        tmp.showCards = showCards.clone();
        tmp.desc = desc;
        tmp.model = model;
        tmp.category = category;
        tmp.nts = nts.clone();
        return tmp;
    }
    public void startEditing(final Activity main, final DriveId fold){
        if(external){
            startEditing(main);
        }else {
            if (!editing) {
                editing = true;
                AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Vehicle old = Vehicle.this.clone();
                        if(((SWrpg)main.getApplication()).vehicFold!=null)
                            Vehicle.this.cloudSave(((SWrpg) main.getApplication()).gac, getFileId(main), false);
                        Vehicle.this.save(getFileLocation(main));
                        do {
                            if (!saving) {
                                saving = true;
                                if (!Vehicle.this.equals(old)) {
                                    if (!old.name.equals(Vehicle.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                        if (((SWrpg) main.getApplication()).hasShortcut(Vehicle.this)) {
                                            ((SWrpg) main.getApplication()).updateShortcut(Vehicle.this, main);
                                        } else {
                                            ((SWrpg) main.getApplication()).addShortcut(Vehicle.this, main);
                                        }
                                    }
                                    if(((SWrpg)main.getApplication()).vehicFold!=null)
                                        Vehicle.this.cloudSave(((SWrpg) main.getApplication()).gac, getFileId(main), false);
                                    Vehicle.this.save(getFileLocation(main));
                                    old = Vehicle.this.clone();
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
                            if (!Vehicle.this.equals(old)) {
                                if (!old.name.equals(Vehicle.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                    if (((SWrpg) main.getApplication()).hasShortcut(Vehicle.this)) {
                                        ((SWrpg) main.getApplication()).updateShortcut(Vehicle.this, main);
                                    } else {
                                        ((SWrpg) main.getApplication()).addShortcut(Vehicle.this, main);
                                    }
                                }
                                if(((SWrpg)main.getApplication()).vehicFold!=null)
                                    Vehicle.this.cloudSave(((SWrpg) main.getApplication()).gac,
                                        getFileId(main), false);
                                Vehicle.this.save(getFileLocation(main));
                            }
                            saving = false;
                        }
                        return null;
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }
    public void startEditing(final Activity main){
        if (!editing){
            editing = true;
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Vehicle old = Vehicle.this.clone();
                Vehicle.this.save(getFileLocation(main));
                do{
                    if (!saving) {
                        saving = true;
                        if (!Vehicle.this.equals(old)) {
                            if(!old.name.equals(Vehicle.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                                if(((SWrpg)main.getApplication()).hasShortcut(Vehicle.this)) {
                                    ((SWrpg) main.getApplication()).updateShortcut(Vehicle.this, main);
                                }else{
                                    ((SWrpg)main.getApplication()).addShortcut(Vehicle.this,main);
                                }
                            }
                            Vehicle.this.save(getFileLocation(main));
                            old = Vehicle.this.clone();
                        }
                        saving = false;
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while(editing);
                if (!saving) {
                    saving = true;
                    if (!Vehicle.this.equals(old)) {
                        if(!old.name.equals(Vehicle.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                            if(((SWrpg)main.getApplication()).hasShortcut(Vehicle.this)) {
                                ((SWrpg) main.getApplication()).updateShortcut(Vehicle.this, main);
                            }else{
                                ((SWrpg)main.getApplication()).addShortcut(Vehicle.this,main);
                            }
                        }
                        Vehicle.this.save(getFileLocation(main));
                    }
                    saving = false;
                }
                return null;
            }
        };
        async.execute();
    }
    }

    public void save(String filename){
        SaveLoad sl = new SaveLoad(filename);
        sl.addSave(ID);
        sl.addSave(name);
        sl.addSave(silhouette);
        sl.addSave(speed);
        sl.addSave(handling);
        sl.addSave(armor);
        sl.addSave(defense);
        sl.addSave(totalDefense);
        sl.addSave(hullTraumaCur);
        sl.addSave(hullTraumaThresh);
        sl.addSave(sysStressCur);
        sl.addSave(sysStressThresh);
        sl.addSave(encumCapacity);
        sl.addSave(passengerCapacity);
        sl.addSave(hp);
        sl.addSave(weapons.serialObject());
        sl.addSave(critInjuries.serialObject());
        sl.addSave(showCards);
        sl.addSave(desc);
        sl.addSave(model);
        sl.addSave(category);
        sl.addSave(nts.serialObject());
        sl.save();
    }
    public void cloudSave(GoogleApiClient gac, DriveId fil, boolean async){
        if(fil != null){
            DriveSaveLoad sl = new DriveSaveLoad(fil);
            sl.setMime("swrpg/vhcl");
            sl.addSave(ID);
            sl.addSave(name);
            sl.addSave(silhouette);
            sl.addSave(speed);
            sl.addSave(handling);
            sl.addSave(armor);
            sl.addSave(defense);
            sl.addSave(totalDefense);
            sl.addSave(hullTraumaCur);
            sl.addSave(hullTraumaThresh);
            sl.addSave(sysStressCur);
            sl.addSave(sysStressThresh);
            sl.addSave(encumCapacity);
            sl.addSave(passengerCapacity);
            sl.addSave(hp);
            sl.addSave(weapons.serialObject());
            sl.addSave(critInjuries.serialObject());
            sl.addSave(showCards);
            sl.addSave(desc);
            sl.addSave(model);
            sl.addSave(category);
            sl.addSave(nts.serialObject());
            sl.save(gac,async);
        }
    }
    public void reLoad(String filename){
        loc = filename;
        SaveLoad sl = new SaveLoad(filename);
        Object[] val = sl.load();
        switch (val.length){
            case 22:
                nts.loadFromObject(val[21]);
                category = (String)val[20];
            case 20:
                model = (String)val[19];
                desc = (String)val[18];
                showCards = (boolean[])val[17];
                critInjuries.loadFromObject(val[16]);
                weapons.loadFromObject(val[15]);
                hp = (int)val[14];
                passengerCapacity = (int)val[13];
                encumCapacity = (int)val[12];
                sysStressThresh = (int)val[11];
                sysStressCur = (int)val[10];
                hullTraumaThresh = (int)val[9];
                hullTraumaCur = (int)val[8];
                totalDefense = (int)val[7];
                defense = (int[])val[6];
                armor = (int)val[5];
                handling = (int)val[4];
                speed = (int)val[3];
                silhouette = (int)val[2];
                name = (String)val[1];
                String title = filename.substring(filename.lastIndexOf("/")+1);
                if (title.substring(0,title.indexOf(".")).equals(""))
                    ID = (int)val[0];
                else {
                    try {
                        ID = Integer.parseInt(title.substring(0, title.indexOf(".")));
                    }catch(NumberFormatException ignored){
                        ID = (int)val[0];
                    }
                }
        }
    }
    public void reLoad(GoogleApiClient gac, DriveId fil){
        DriveSaveLoad sl = new DriveSaveLoad(fil);
        Object[] val = sl.load(gac);
        switch (val.length){
            case 22:
                nts.loadFromObject(val[21]);
                category = (String)val[20];
            case 20:
                model = (String)val[19];
                desc = (String)val[18];
                showCards = (boolean[])val[17];
                critInjuries.loadFromObject(val[16]);
                weapons.loadFromObject(val[15]);
                hp = (int)val[14];
                passengerCapacity = (int)val[13];
                encumCapacity = (int)val[12];
                sysStressThresh = (int)val[11];
                sysStressCur = (int)val[10];
                hullTraumaThresh = (int)val[9];
                hullTraumaCur = (int)val[8];
                totalDefense = (int)val[7];
                defense = (int[])val[6];
                armor = (int)val[5];
                handling = (int)val[4];
                speed = (int)val[3];
                silhouette = (int)val[2];
                name = (String)val[1];
                String title = fil.asDriveFile().getMetadata(gac).await().getMetadata().getTitle();
                if (title.substring(0,title.indexOf(".")).equals(""))
                    ID = (int)val[0];
                else {
                    try {
                        ID = Integer.parseInt(title.substring(0, title.indexOf(".")));
                    }catch(NumberFormatException ignored){
                        ID = (int)val[0];
                    }
                }
        }
    }
    public String getFileLocation(Activity main){
        if(main!= null) {
            String loc = ((SWrpg) main.getApplication()).prefs.getString(main.getString(R.string.local_location_key),
                    ((SWrpg) main.getApplication()).defaultLoc + "/SWShips");
            File location = new File(loc);
            if (!location.exists()) {
                if (!location.mkdir()) {
                    return "";
                }
            }
            String def = location.getAbsolutePath() + "/" + Integer.toString(ID) + ".vhcl";
            if(external)
                return this.loc;
            return def;
        }else{
            return "";
        }
    }
    public DriveId getFileId(Activity main){
        String name = Integer.toString(ID) + ".vhcl";
        DriveId fi = null;
        DriveApi.MetadataBufferResult res =
                ((SWrpg)main.getApplication()).vehicFold.queryChildren
                        (((SWrpg)main.getApplication()).gac,new Query.Builder().addFilter(
                Filters.eq(SearchableField.TITLE,name)).build()).await();
        for (Metadata met:res.getMetadataBuffer()){
            if (!met.isTrashed()){
                fi = met.getDriveId();
                break;
            }
        }
        res.release();
        if (fi == null){
            fi = ((SWrpg)main.getApplication()).vehicFold.createFile
                    (((SWrpg)main.getApplication()).gac,new MetadataChangeSet.Builder().setTitle(name).build(),null).await()
                    .getDriveFile().getDriveId();
        }
        return fi;
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Vehicle))
            return false;
        Vehicle in = (Vehicle)obj;
        return in.ID == ID && in.name.equals(name) && in.silhouette == silhouette && in.speed == speed && in.handling == handling
                && in.armor == armor && Arrays.equals(in.defense,defense) && totalDefense == in.totalDefense && in.hullTraumaCur == hullTraumaCur
                && in.hullTraumaThresh == hullTraumaThresh && in.sysStressCur == sysStressCur && in.sysStressThresh == sysStressThresh
                && in.encumCapacity == encumCapacity && in.passengerCapacity == passengerCapacity && in.hp == hp && in.weapons.equals(weapons)
                && in.critInjuries.equals(critInjuries) && Arrays.equals(in.showCards,showCards) && in.desc.equals(desc) && in.model.equals(model)
                && in.category.equals(category);
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
            ((SWrpg)main.getApplication()).deleteShortcut(this,main);
        }
    }
    public void exportTo(String folder){
        File fold = new File(folder);
        if(!fold.exists()) {
            if (!fold.mkdir())
                return;
        }
        File f = new File(folder+"/"+name+".vhcl");
        if(f.exists()){
            if(!f.delete())
                return;
        }
        save(folder+"/"+name+".vhcl");
    }
    public int cardNumber() {
        return 7;
    }
    public void setupCards(final Activity ac, EditGeneral.EditableAdap ea, final CardView c, final  int pos){
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
                //Basic Info
                case 1:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.basic_info_text);
                    //TODO
                    break;
                //Defense
                case 2:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.defense_text);
                    //TODO
                    break;
                //Damage
                case 3:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.damage_text);
                    //TODO
                    break;
                //<editor-fold desc="Weapons">
                case 4:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.weapons_text);
                    final View weapon = ac.getLayoutInflater().inflate(R.layout.layout_list,fl,false);
                    fl.addView(weapon);
                    RecyclerView r = (RecyclerView)weapon.findViewById(R.id.recycler);
                    final Weapons.WeaponsAdap adapW = new Weapons.WeaponsAdap(this, new Skill.onSave() {public void save() {} public void delete() {}
                            public void cancel() {}}, ac);
                    r.setAdapter(adapW);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    weapon.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            weapons.add(new Weapon());
                            Weapon.editWeapon(ac, Vehicle.this,weapons.size() - 1,true, new Skill.onSave() {
                                public void save() {
                                    adapW.notifyDataSetChanged();
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
                case 5:
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
                            CriticalInjury.editCritical(ac, Vehicle.this,critInjuries.size() - 1,true, new Skill.onSave() {
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
                //<editor-fold desc="desc">
                case 6:
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
                    et.setText(Vehicle.this.name);
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Vehicle.this.name = et.getText().toString();
                            ((TextView)c.findViewById(R.id.name)).setText(Vehicle.this.name);
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
                    Vehicle ch = Vehicle.this.clone();
                    ch.ID = ID;
                    ch.save(ch.getFileLocation(ac));
                    if(((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.google_drive_key),false))
                        ch.cloudSave(((SWrpg)ac.getApplication()).gac,ch.getFileId(ac),true);
                }
            });
        }
    }
}
