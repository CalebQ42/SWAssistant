package com.apps.darkstorm.swrpg.assistant.sw;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Inventory;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Item;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skill;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skills;
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

public class Minion extends Editable{
    //Version 1 0-15
    //public int ID;
    //public String name = "";
    //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
    public int[] charVals = new int[6];
    public Skills skills = new Skills();
    public Talents talents = new Talents();
    public Inventory inv = new Inventory();
    //public Weapons weapons = new Weapons();
    private int woundThreshInd = 1;
    public int woundThresh;
    private int woundCur;
    public int defMelee,defRanged;
    public int soak;
    private int minNum;
    //public String desc = "";
    private boolean[] showCards = new boolean[9];
    //Version 2 16-18
    //public CriticalInjuries critInjuries = new CriticalInjuries();
    public Inventory origInv = new Inventory();
    public Weapons origWeapons = new Weapons();
    //Version 3 19-20
    //public String category = "";
    //public Notes nts (From Editable)


    private boolean editing = false;
    private boolean saving = false;
    private String loc = "";
    public boolean external = false;

    public Minion(){
        for (int i = 0; i< showCards.length; i++)
            showCards[i] = true;
    }
    public Minion(int ID){
        this.ID = ID;
        for (int i = 0; i< showCards.length; i++)
            showCards[i] = true;
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
                        Minion tmpChar = Minion.this.clone();
                        Minion.this.save(Minion.this.getFileLocation(main));
                        if(((SWrpg)main.getApplication()).vehicFold!=null)
                            cloudSave(((SWrpg) main.getApplication()).gac, getFileId(main), false);
                        do {
                            if (!saving) {
                                saving = true;
                                if (!Minion.this.equals(tmpChar)) {
                                    if (!tmpChar.name.equals(Minion.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                        if (((SWrpg) main.getApplication()).hasShortcut(Minion.this)) {
                                            ((SWrpg) main.getApplication()).updateShortcut(Minion.this, main);
                                        } else {
                                            ((SWrpg) main.getApplication()).addShortcut(Minion.this, main);
                                        }
                                    }
                                    Minion.this.save(Minion.this.getFileLocation(main));
                                    if(((SWrpg)main.getApplication()).vehicFold!=null)
                                        cloudSave(((SWrpg) main.getApplication()).gac, getFileId(main), false);
                                    tmpChar = Minion.this.clone();
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
                            if (!Minion.this.equals(tmpChar)) {
                                if (!tmpChar.name.equals(Minion.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                    if (((SWrpg) main.getApplication()).hasShortcut(Minion.this)) {
                                        ((SWrpg) main.getApplication()).updateShortcut(Minion.this, main);
                                    } else {
                                        ((SWrpg) main.getApplication()).addShortcut(Minion.this, main);
                                    }
                                }
                                Minion.this.save(Minion.this.getFileLocation(main));
                                if(((SWrpg)main.getApplication()).vehicFold!=null)
                                    cloudSave(((SWrpg) main.getApplication()).gac, getFileId(main), false);
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
                    Minion tmpChar = Minion.this.clone();
                    Minion.this.save(Minion.this.getFileLocation(main));
                    do{
                        if (!saving) {
                            saving = true;
                            if (!Minion.this.equals(tmpChar)) {
                                if(!tmpChar.name.equals(Minion.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                                    if(((SWrpg)main.getApplication()).hasShortcut(Minion.this)) {
                                        ((SWrpg) main.getApplication()).updateShortcut(Minion.this, main);
                                    }else{
                                        ((SWrpg)main.getApplication()).addShortcut(Minion.this,main);
                                    }
                                }
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
                            if(!tmpChar.name.equals(Minion.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                                if(((SWrpg)main.getApplication()).hasShortcut(Minion.this)) {
                                    ((SWrpg) main.getApplication()).updateShortcut(Minion.this, main);
                                }else{
                                    ((SWrpg)main.getApplication()).addShortcut(Minion.this,main);
                                }
                            }
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
        if(main!= null) {
            String loc = ((SWrpg) main.getApplication()).prefs.getString(main.getString(R.string.local_location_key),
                    ((SWrpg) main.getApplication()).defaultLoc);
            File location = new File(loc);
            if (!location.exists()) {
                if (!location.mkdir()) {
                    return "";
                }
            }
            String def = location.getAbsolutePath() + "/" + Integer.toString(ID) + ".minion";
            if(external)
                return this.loc;
            return def;
        }else{
            return "";
        }
    }

    @Override
    public int cardNumber() {
        return 10;
    }

    public void save(String filename){
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
        sl.addSave(showCards);
        sl.addSave(critInjuries.serialObject());
        sl.addSave(origInv.serialObject());
        sl.addSave(origWeapons.serialObject());
        sl.addSave(category);
        sl.addSave(nts.serialObject());
        sl.save();
    }
    public void reLoad(String filename){
        SaveLoad sl = new SaveLoad(filename);
        Object[] obj = sl.load();
        switch(obj.length){
            case 21:
                nts.loadFromObject(obj[20]);
                category = (String)obj[19];
            case 19:
                origWeapons.loadFromObject(obj[18]);
                origInv.loadFromObject(obj[17]);
                critInjuries.loadFromObject(obj[16]);
            case 16:
                showCards = (boolean[])obj[15];
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
                else {
                    try {
                        ID = Integer.parseInt(title.substring(0, title.indexOf(".")));
                    }catch(NumberFormatException ignored){
                        ID = (int)obj[0];
                    }
                }
        }
    }
    public DriveId getFileId(Activity main){
        String name = Integer.toString(ID) + ".minion";
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
                    (((SWrpg)main.getApplication()).gac,new MetadataChangeSet.Builder().setTitle(name).build(),null).await()
                    .getDriveFile().getDriveId();
        }
        return fi;
    }
    public void cloudSave(GoogleApiClient gac, DriveId fil, boolean async){
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
            sl.addSave(showCards);
            sl.addSave(critInjuries.serialObject());
            sl.addSave(origInv.serialObject());
            sl.addSave(origWeapons.serialObject());
            sl.addSave(category);
            sl.addSave(nts.serialObject());
            sl.save(gac, async);
        }
    }
    public void reLoad(GoogleApiClient gac,DriveId fil){
        DriveSaveLoad sl = new DriveSaveLoad(fil);
        Object[] obj = sl.load(gac);
        switch(obj.length){
            case 21:
                nts.loadFromObject(obj[20]);
                category = (String)obj[19];
            case 19:
                origWeapons.loadFromObject(obj[18]);
                origInv.loadFromObject(obj[17]);
                critInjuries.loadFromObject(obj[16]);
            case 16:
                showCards = (boolean[])obj[15];
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
                else {
                    try {
                        ID = Integer.parseInt(title.substring(0, title.indexOf(".")));
                    }catch(NumberFormatException ignored){
                        ID = (int)obj[0];
                    }
                }
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
    public void setMinNum(int num){
        minNum = num;
        woundThresh = woundThreshInd*minNum;
        woundCur = woundThresh;
        if (minNum<=6) {
            for (int i = 0; i < skills.size(); i++)
                skills.get(i).val = minNum - 1;
        }else{
            for (int i = 0;i<skills.size();i++)
                skills.get(i).val = 5;
        }
    }
    public void setWoundInd(int wound){
        if (wound <= 0)
            wound = 1;
        woundThreshInd = wound;
        woundThresh = minNum*woundThreshInd;
        woundCur = woundThresh;
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
        min.category = category;
        min.nts = nts.clone();
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
                Arrays.equals(tmp.showCards, showCards) && woundThreshInd == tmp.woundThreshInd && minNum == tmp.minNum &&
                tmp.critInjuries.equals(critInjuries)&& tmp.category.equals(category)&& tmp.nts.equals(nts);
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
        File f = new File(folder+"/"+name+".minion");
        if(f.exists()){
            if(!f.delete())
                return;
        }
        save(folder+"/"+name+".minion");
    }

    public void setupCards(final Activity ac, final EditGeneral.EditableAdap ea, final CardView c, final int pos, final Handler parentHandle){
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
                //<editor-fold desc="Minion Numbers">
                case 1:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.min_num_text);
                    View num = ac.getLayoutInflater().inflate(R.layout.layout_minion_number,fl,false);
                    fl.addView(num);
                    final TextSwitcher value = (TextSwitcher)num.findViewById(R.id.num_switcher);
                    Animation in = AnimationUtils.loadAnimation(ac,android.R.anim.slide_in_left);
                    in.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
                    Animation out = AnimationUtils.loadAnimation(ac,android.R.anim.slide_out_right);
                    out.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
                    value.setInAnimation(in);
                    value.setOutAnimation(out);
                    value.setFactory(new ViewSwitcher.ViewFactory() {
                        @Override
                        public View makeView() {
                            return ac.getLayoutInflater().inflate(R.layout.template_num_text,value,false);
                        }
                    });
                    value.setText(String.valueOf(minNum));
                    num.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setMinNum(minNum+1);
                            value.setText(String.valueOf(minNum));
                            ea.notifyItemChanged(2);
                            ea.notifyItemChanged(4);
                        }
                    });
                    num.findViewById(R.id.plus).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            setMinNum(minNum+1);
                            value.setText(String.valueOf(minNum));
                            ea.notifyItemChanged(2);
                            ea.notifyItemChanged(4);
                            return false;
                        }
                    });
                    num.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(minNum>0){
                                setMinNum(minNum-1);
                                value.setText(String.valueOf(minNum));
                                ea.notifyItemChanged(2);
                                ea.notifyItemChanged(4);
                            }
                        }
                    });
                    num.findViewById(R.id.minus).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(minNum>0){
                                setMinNum(minNum-1);
                                value.setText(String.valueOf(minNum));
                                ea.notifyItemChanged(2);
                                ea.notifyItemChanged(4);
                            }
                            return false;
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Wound">
                case 2:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.wound_text);
                    View wnd = ac.getLayoutInflater().inflate(R.layout.layout_minion_wound,fl,false);
                    fl.addView(wnd);
                    final TextView soakText = (TextView)wnd.findViewById(R.id.soak_value);
                    soakText.setText(String.valueOf(soak));
                    final TextSwitcher valueWound = (TextSwitcher)wnd.findViewById(R.id.value_switch);
                    wnd.findViewById(R.id.soak_lay).setOnLongClickListener(new View.OnLongClickListener() {
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
                    final TextView woundPer = (TextView)wnd.findViewById(R.id.minion_wound_value);
                    woundPer.setText(String.valueOf(woundThreshInd));
                    wnd.findViewById(R.id.minion_wound_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(woundThreshInd));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.wound_ind_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(et.getText().toString().equals(""))
                                        setWoundInd(0);
                                    else
                                        setWoundInd(Integer.parseInt(et.getText().toString()));
                                    woundPer.setText(String.valueOf(woundThreshInd));
                                    valueWound.setText(String.valueOf(woundCur));
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
                    in = AnimationUtils.loadAnimation(ac,android.R.anim.slide_in_left);
                    in.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
                    out = AnimationUtils.loadAnimation(ac,android.R.anim.slide_out_right);
                    out.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
                    valueWound.setInAnimation(in);
                    valueWound.setOutAnimation(out);
                    valueWound.setFactory(new ViewSwitcher.ViewFactory() {
                        @Override
                        public View makeView() {
                            return ac.getLayoutInflater().inflate(R.layout.template_num_text,valueWound,false);
                        }
                    });
                    valueWound.setText(String.valueOf(woundCur));
                    wnd.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int tmp = minNum;
                            setWound(woundCur+1);
                            valueWound.setText(String.valueOf(woundCur));
                            if(tmp != minNum) {
                                ea.notifyItemChanged(1);
                                ea.notifyItemChanged(4);
                            }
                        }
                    });
                    wnd.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(woundCur>0) {
                                int tmp = minNum;
                                setWound(woundCur - 1);
                                valueWound.setText(String.valueOf(woundCur));
                                if (tmp != minNum) {
                                    ea.notifyItemChanged(1);
                                    ea.notifyItemChanged(4);
                                }
                            }
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
                                    charVals[0] = Integer.parseInt(et.getText().toString());
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
                                    charVals[1] = Integer.parseInt(et.getText().toString());
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
                                    charVals[2] = Integer.parseInt(et.getText().toString());
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
                                    charVals[3] = Integer.parseInt(et.getText().toString());
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
                                    charVals[4] = Integer.parseInt(et.getText().toString());
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
                                    charVals[5] = Integer.parseInt(et.getText().toString());
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
                            Skill.editSkill(ac, Minion.this,skills.size() - 1,true, new Skill.onSave() {
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
                                    defRanged = Integer.parseInt(et.getText().toString());
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
                                    defMelee = Integer.parseInt(et.getText().toString());
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
                    final Weapons.WeaponsAdap adapW = new Weapons.WeaponsAdap(this, new Skill.onSave() {public void save() {}public void delete() {}
                        public void cancel() {}}, ac);
                    r.setAdapter(adapW);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    weapon.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            weapons.add(new Weapon());
                            Weapon.editWeapon(ac, Minion.this,weapons.size() - 1,true, new Skill.onSave() {
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
                    weapon.findViewById(R.id.save).setVisibility(View.VISIBLE);
                    weapon.findViewById(R.id.load).setVisibility(View.VISIBLE);
                    weapon.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            origWeapons = weapons.clone();
                        }
                    });
                    weapon.findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            weapons = origWeapons.clone();
                            adapW.notifyDataSetChanged();
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Talents">
                case 7:
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
                            Talent.editTalent(ac, Minion.this,talents.size() - 1,true, new Skill.onSave() {
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
                //<editor-fold desc="Inv">
                case 8:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.inventory_text);
                    View invLay = ac.getLayoutInflater().inflate(R.layout.layout_inventory,fl,false);
                    fl.addView(invLay);
                    invLay.findViewById(R.id.soak_lay).setVisibility(View.GONE);
                    invLay.findViewById(R.id.encum_lay).setVisibility(View.GONE);
                    final Inventory.InventoryAdap invAdap = new Inventory.InventoryAdap(this, new Skill.onSave() {
                        public void save() {}public void delete() {}public void cancel() {}
                    }, ac);
                    r = (RecyclerView)invLay.findViewById(R.id.recycler);
                    r.setAdapter(invAdap);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    invLay.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inv.add(new Item());
                            Item.editItem(ac, Minion.this, inv.size() - 1, true, new Skill.onSave() {
                                @Override
                                public void save() {
                                    invAdap.notifyDataSetChanged();
                                }
                                public void delete() {
                                }
                                public void cancel() {
                                    inv.remove(inv.get(inv.size()-1));
                                }
                            });
                        }
                    });
                    invLay.findViewById(R.id.save).setVisibility(View.VISIBLE);
                    invLay.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            origInv = inv.clone();
                        }
                    });
                    invLay.findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inv = origInv.clone();
                            invAdap.notifyDataSetChanged();
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="Crit inj">
                case 9:
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
                            CriticalInjury.editCritical(ac, Minion.this,critInjuries.size() - 1,true, new Skill.onSave() {
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
                case 10:
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
                    et.setText(Minion.this.name);
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Minion.this.name = et.getText().toString();
                            ((TextView)c.findViewById(R.id.name)).setText(Minion.this.name);
                            if(parentHandle!=null){
                                AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException ignored) {}
                                        Message msg = parentHandle.obtainMessage();
                                        msg.arg2 = 5;
                                        parentHandle.sendMessage(msg);
                                        return null;
                                    }
                                };
                                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            c.findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(ac);
                    View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                    b.setView(in);
                    final EditText et = (EditText)in.findViewById(R.id.edit_text);
                    et.setText(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),
                            ((SWrpg)ac.getApplication()).defaultLoc));
                    ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.export_location));
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
                    Minion ch = Minion.this.clone();
                    ch.ID = ID;
                    ch.save(ch.getFileLocation(ac));
                    if(((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.google_drive_key),false))
                        ch.cloudSave(((SWrpg)ac.getApplication()).gac,ch.getFileId(ac),true);
                }
            });
        }
    }
}
