package com.apps.darkstorm.swrpg.assistant.sw;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
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

import com.apps.darkstorm.swrpg.assistant.DiceRollFragment;
import com.apps.darkstorm.swrpg.assistant.EditGeneral;
import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.custvars.SaveLoad;
import com.apps.darkstorm.swrpg.assistant.dice.DiceHolder;
import com.apps.darkstorm.swrpg.assistant.drive.DriveSaveLoad;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjury;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Duty;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Dutys;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.ForcePower;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.ForcePowers;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Inventory;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Item;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Notes;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Obligation;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Obligations;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skill;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Skills;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Specializations;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Talent;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Talents;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapon;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapons;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    private String species = "";
    public String career = "";
    public Specializations specializations = new Specializations();
    public Talents talents = new Talents();
    public Inventory inv = new Inventory();
    //public Weapons weapons = new Weapons();
    public ForcePowers forcePowers = new ForcePowers();
    private String motivation = "";
    //public CriticalInjuries critInjuries = new CriticalInjuries();
    private String[] emotionalStr = {""};
    private String[] emotionalWeak = {""};
    public Dutys duty = new Dutys();
    public Obligations obligation = new Obligations();
    private int woundThresh, woundCur;
    private int strainThresh,strainCur;
    private int xpTot,xpCur;
    private int defMelee,defRanged;
    private int soak;
    public int force;
    private int credits;
    private int morality,conflict;
    //public String desc = "";
    private boolean[] showCards = new boolean[16];
    private boolean darkSide;
    private int age;
    //public Notes nts (From Editable)
    //
    // Character V2 Start (35)
    //
    private int encumCapacity;
    //
    // Character v3 Start (36)
    //
    //public String category = "";
    //
    //  ^                 ^
    //  |  Character End  |
    //  |                 |
    //

    public static String fileExtension = ".swcharacter";

    @Override
    public void saveJson(JsonWriter jw) throws IOException {
        jw.name("ID").value(ID);
        jw.name("name").value(name);
        jw.name("characteristics").beginArray();
        for(int cv:charVals)
            jw.value(cv);
        jw.endArray();
        skills.saveJson(jw);
        jw.name("species").value(species);
        jw.name("career").value(career);
        specializations.saveJson(jw);
        talents.saveJson(jw);
        inv.saveJson(jw);
        weapons.saveJson(jw);
        forcePowers.saveJson(jw);
        jw.name("motivation").value(motivation);
        critInjuries.saveJson(jw);
        if(emotionalStr[0]==null)
            emotionalStr[0]= "";
        jw.name("emotional strength").value(emotionalStr[0]);
        if(emotionalWeak[0]==null)
            emotionalWeak[0]= "";
        jw.name("emotional weakness").value(emotionalWeak[0]);
        duty.saveJson(jw);
        obligation.saveJson(jw);
        jw.name("wound threshold").value(woundThresh);
        jw.name("wound current").value(woundCur);
        jw.name("strain threshold").value(strainThresh);
        jw.name("strain current").value(strainCur);
        jw.name("xp total").value(xpTot);
        jw.name("xp current").value(xpCur);
        jw.name("melee defense").value(defMelee);
        jw.name("ranged defense").value(defRanged);
        jw.name("soak").value(soak);
        jw.name("force rating").value(force);
        jw.name("credits").value(credits);
        jw.name("morality").value(morality);
        jw.name("conflict").value(conflict);
        jw.name("description").value(desc);
        jw.name("show cards").beginArray();
        for(boolean b:showCards)
            jw.value(b);
        jw.endArray();
        jw.name("dark side").value(darkSide);
        jw.name("age").value(age);
        nts.saveJson(jw);
        jw.name("encumbrance capacity").value(encumCapacity);
        jw.name("category").value(category);
    }

    public void loadJson(JsonReader jr){
        try {
            while (jr.hasNext()) {
                if(!jr.peek().equals(JsonToken.NAME)){
                    jr.skipValue();
                    continue;
                }
                switch (jr.nextName()) {
                    case "ID":
                        ID = jr.nextInt();
                        break;
                    case "name":
                        name = jr.nextString();
                        break;
                    case "characteristics":
                        jr.beginArray();
                        for (int i = 0; i < charVals.length; i++)
                            charVals[i] = jr.nextInt();
                        jr.endArray();
                        break;
                    case "Skills":
                        skills.loadJson(jr);
                        break;
                    case "species":
                        species = jr.nextString();
                        break;
                    case "career":
                        career = jr.nextString();
                        break;
                    case "Specializations":
                        specializations.loadJson(jr);
                        break;
                    case "Talents":
                        talents.loadJson(jr);
                        break;
                    case "Inventory":
                        inv.loadJson(jr);
                        break;
                    case "Weapons":
                        weapons.loadJson(jr);
                        break;
                    case "Force Powers":
                        forcePowers.loadJson(jr);
                        break;
                    case "motivation":
                        motivation = jr.nextString();
                        break;
                    case "Critical Injuries":
                        critInjuries.loadJson(jr);
                        break;
                    case "emotional strength":
                        emotionalStr[0] = jr.nextString();
                        break;
                    case "emotional weakness":
                        emotionalWeak[0] = jr.nextString();
                        break;
                    case "Dutys":
                        duty.loadJson(jr);
                        break;
                    case "Obligations":
                        obligation.loadJson(jr);
                        break;
                    case "wound threshold":
                        woundThresh = jr.nextInt();
                        break;
                    case "wound current":
                        woundCur = jr.nextInt();
                        break;
                    case "strain threshold":
                        strainThresh = jr.nextInt();
                        break;
                    case "strain current":
                        strainCur = jr.nextInt();
                        break;
                    case "xp total":
                        xpTot = jr.nextInt();
                        break;
                    case "xp current":
                        xpCur = jr.nextInt();
                        break;
                    case "melee defense":
                        defMelee = jr.nextInt();
                        break;
                    case "ranged defense":
                        defRanged = jr.nextInt();
                        break;
                    case "soak":
                        soak = jr.nextInt();
                        break;
                    case "force rating":
                        force = jr.nextInt();
                        break;
                    case "credits":
                        credits = jr.nextInt();
                        break;
                    case "morality":
                        morality = jr.nextInt();
                        break;
                    case "conflict":
                        conflict = jr.nextInt();
                        break;
                    case "description":
                        desc = jr.nextString();
                        break;
                    case "show cards":
                        jr.beginArray();
                        for (int i = 0; i < showCards.length; i++)
                            showCards[i] = jr.nextBoolean();
                        jr.endArray();
                        break;
                    case "dark side":
                        darkSide = jr.nextBoolean();
                        break;
                    case "age":
                        age = jr.nextInt();
                        break;
                    case "Notes":
                        nts.loadJson(jr);
                        break;
                    case "encumbrance capacity":
                        encumCapacity = jr.nextInt();
                        break;
                    case "category":
                        category = jr.nextString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public void reLoadLegacy(String filename){
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
    public void reLoadLegacy(GoogleApiClient gac, DriveId fil){
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
    public Editable clone(){
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
    private void resolveConflict(){
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
                morality == chara.morality && conflict == chara.conflict && chara.desc.equals(desc) &&
                Arrays.equals(showCards, chara.showCards) && darkSide == chara.darkSide && age == chara.age && chara.nts.equals(nts) &&
                encumCapacity == chara.encumCapacity && category.equals(chara.category);
    }
    public String getFileExtension() {
        return ".swcharacter";
    }

    public int cardNumber(){
        return 17;
    }

    @Override
    public void setupCards(final Activity ac, final EditGeneral.EditableAdap ea, final CardView c, final int pos, final Handler parentHandle) {
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
                                    if(!et.getText().toString().equals("") && !et.getText().toString().equals("-"))
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
                    chars.findViewById(R.id.brawn_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ac);
                            final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll, null);
                            b.setView(view);
                            view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                            view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                            view.findViewById(R.id.fab_space).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_label).setVisibility(View.GONE);
                            final DiceHolder dh = new DiceHolder();
                            dh.ability = charVals[0];
                            final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac, dh);
                            RecyclerView r = (RecyclerView) view.findViewById(R.id.dice_recycler);
                            r.setAdapter(dl);
                            r.setLayoutManager(new LinearLayoutManager(ac));
                            b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dh.roll().showDialog(ac);
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
                    chars.findViewById(R.id.agility_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ac);
                            final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll, null);
                            b.setView(view);
                            view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                            view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                            view.findViewById(R.id.fab_space).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_label).setVisibility(View.GONE);
                            final DiceHolder dh = new DiceHolder();
                            dh.ability = charVals[1];
                            final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac, dh);
                            RecyclerView r = (RecyclerView) view.findViewById(R.id.dice_recycler);
                            r.setAdapter(dl);
                            r.setLayoutManager(new LinearLayoutManager(ac));
                            b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dh.roll().showDialog(ac);
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
                    chars.findViewById(R.id.agility_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ac);
                            final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll, null);
                            b.setView(view);
                            view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                            view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                            view.findViewById(R.id.fab_space).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_label).setVisibility(View.GONE);
                            final DiceHolder dh = new DiceHolder();
                            dh.ability = charVals[1];
                            final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac, dh);
                            RecyclerView r = (RecyclerView) view.findViewById(R.id.dice_recycler);
                            r.setAdapter(dl);
                            r.setLayoutManager(new LinearLayoutManager(ac));
                            b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dh.roll().showDialog(ac);
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
                    chars.findViewById(R.id.cunning_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ac);
                            final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll, null);
                            b.setView(view);
                            view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                            view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                            view.findViewById(R.id.fab_space).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_label).setVisibility(View.GONE);
                            final DiceHolder dh = new DiceHolder();
                            dh.ability = charVals[3];
                            final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac, dh);
                            RecyclerView r = (RecyclerView) view.findViewById(R.id.dice_recycler);
                            r.setAdapter(dl);
                            r.setLayoutManager(new LinearLayoutManager(ac));
                            b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dh.roll().showDialog(ac);
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
                    chars.findViewById(R.id.willpower_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ac);
                            final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll, null);
                            b.setView(view);
                            view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                            view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                            view.findViewById(R.id.fab_space).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_label).setVisibility(View.GONE);
                            final DiceHolder dh = new DiceHolder();
                            dh.ability = charVals[4];
                            final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac, dh);
                            RecyclerView r = (RecyclerView) view.findViewById(R.id.dice_recycler);
                            r.setAdapter(dl);
                            r.setLayoutManager(new LinearLayoutManager(ac));
                            b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dh.roll().showDialog(ac);
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
                    chars.findViewById(R.id.presence_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ac);
                            final View view = ac.getLayoutInflater().inflate(R.layout.fragment_dice_roll, null);
                            b.setView(view);
                            view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                            view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                            view.findViewById(R.id.fab_space).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
                            view.findViewById(R.id.dice_label).setVisibility(View.GONE);
                            final DiceHolder dh = new DiceHolder();
                            dh.ability = charVals[5];
                            final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(ac, dh);
                            RecyclerView r = (RecyclerView) view.findViewById(R.id.dice_recycler);
                            r.setAdapter(dl);
                            r.setLayoutManager(new LinearLayoutManager(ac));
                            b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dh.roll().showDialog(ac);
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
                                    specializations.remove(specializations.get(specializations.size()-1));
                                }
                                public void cancel() {
                                    specializations.remove(specializations.get(specializations.size()-1));
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
                    invLay.findViewById(R.id.soak_lay).setOnLongClickListener(new View.OnLongClickListener() {
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
                //<editor-fold desc="morality">
                case 13:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.morality_text);
                    View mLay = ac.getLayoutInflater().inflate(R.layout.layout_morality,fl,false);
                    fl.addView(mLay);
                    final TextView moralityText = (TextView)mLay.findViewById(R.id.morality_value);
                    moralityText.setText(String.valueOf(morality));
                    mLay.findViewById(R.id.morality_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(morality));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.morality_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        morality = Integer.parseInt(et.getText().toString());
                                    else
                                        morality = 0;
                                    moralityText.setText(String.valueOf(morality));
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
                    final TextView conflictText = (TextView)mLay.findViewById(R.id.conflict_value);
                    conflictText.setText(String.valueOf(conflict));
                    mLay.findViewById(R.id.conflict_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(String.valueOf(conflict));
                            et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.conflict_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!et.getText().toString().equals(""))
                                        conflict = Integer.parseInt(et.getText().toString());
                                    else
                                        conflict = 0;
                                    conflictText.setText(String.valueOf(conflict));
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
                    mLay.findViewById(R.id.resolve).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resolveConflict();
                            conflictText.setText(String.valueOf(conflict));
                            moralityText.setText(String.valueOf(morality));
                        }
                    });
                    Switch darkSide = (Switch)mLay.findViewById(R.id.dark_side_switch);
                    darkSide.setChecked(this.darkSide);
                    darkSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Character.this.darkSide = isChecked;
                        }
                    });
                    final TextView strength = (TextView)mLay.findViewById(R.id.strength_value);
                    strength.setText(emotionalStr[0]);
                    mLay.findViewById(R.id.strength_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(emotionalStr[0]);
                            et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.strength_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    emotionalStr[0] = et.getText().toString();
                                    strength.setText(emotionalStr[0]);
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
                    final TextView weakness = (TextView)mLay.findViewById(R.id.weakness_value);
                    weakness.setText(emotionalWeak[0]);
                    mLay.findViewById(R.id.weakness_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder b = new AlertDialog.Builder(ac);
                            View in = ac.getLayoutInflater().inflate(R.layout.dialog_one_string,null);
                            b.setView(in);
                            final EditText et = (EditText)in.findViewById(R.id.edit_text);
                            et.setText(emotionalWeak[0]);
                            et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS|InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE|InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                            ((TextInputLayout)in.findViewById(R.id.edit_layout)).setHint(ac.getString(R.string.weakness_text));
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    emotionalWeak[0] = et.getText().toString();
                                    weakness.setText(emotionalWeak[0]);
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
                //<editor-fold desc="duty">
                case 14:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.duty_text);
                    View dutLay = ac.getLayoutInflater().inflate(R.layout.layout_list,fl,false);
                    fl.addView(dutLay);
                    r = (RecyclerView)dutLay.findViewById(R.id.recycler);
                    final Dutys.DutysAdap dutyAdap = new Dutys.DutysAdap(this,ac);
                    r.setAdapter(dutyAdap);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    dutLay.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            duty.add(new Duty());
                            Duty.editDuty(ac, Character.this, duty.size() - 1, true, new Skill.onSave() {
                                @Override
                                public void save() {
                                    dutyAdap.notifyDataSetChanged();
                                }

                                @Override
                                public void delete() {

                                }

                                @Override
                                public void cancel() {
                                    duty.remove(duty.get(duty.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
                //<editor-fold desc="obligation">
                case 15:
                    ((TextView)c.findViewById(R.id.title)).setText(R.string.obligation_text);
                    View obliLay = ac.getLayoutInflater().inflate(R.layout.layout_list,fl,false);
                    fl.addView(obliLay);
                    r = (RecyclerView)obliLay.findViewById(R.id.recycler);
                    final Obligations.ObligationsAdap obligationAdap = new Obligations.ObligationsAdap(this,ac);
                    r.setAdapter(obligationAdap);
                    r.setLayoutManager(new LinearLayoutManager(ac));
                    obliLay.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            obligation.add(new Obligation());
                            Obligation.editObligation(ac, Character.this, obligation.size() - 1, true, new Skill.onSave() {
                                public void save() {
                                    obligationAdap.notifyDataSetChanged();
                                }
                                public void delete() {}
                                public void cancel() {
                                    obligation.remove(obligation.get(obligation.size()-1));
                                }
                            });
                        }
                    });
                    break;
                //</editor-fold>
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
                    final Character ch = (Character)Character.this.clone();
                    ch.ID = ID;
                    ch.save(ch.getFileLocation(ac));
                    if(((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.google_drive_key),false)) {
                        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                ch.save(((SWrpg) ac.getApplication()).gac, ch.getFileId(ac),false);
                                return null;
                            }
                        };
                        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            });
        }
    }
}
