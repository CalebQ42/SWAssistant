package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;

import java.io.IOException;
import java.util.ArrayList;

public class Weapon implements JsonSavable {
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
    public boolean limitedAmmo;
    //0-Good,1-Minor,2-Moderate,3-Major
    public int itemState;
    public int ammo;
    //Version 2 13
    public String firingArc = "";
    //Version 3 14
    public int encum;
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
        tmp.limitedAmmo = limitedAmmo;
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
        tmp.add(limitedAmmo);
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
                limitedAmmo = (boolean)tmp[10];
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
                && in.skillBase == skillBase && in.chars.equals(chars) && in.addBrawn == addBrawn && in.loaded == loaded && in.limitedAmmo == limitedAmmo
                && in.itemState == itemState && in.ammo == ammo && in.firingArc.equals(firingArc) && in.encum == encum;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.beginObject();
        jw.name("name").value(name);
        jw.name("damage").value(dmg);
        jw.name("critical rating").value(crit);
        jw.name("hard points").value(hp);
        jw.name("range").value(range);
        jw.name("skill").value(skill);
        jw.name("base").value(skillBase);
        chars.saveJson(jw);
        jw.name("add brawn").value(addBrawn);
        jw.name("loaded").value(loaded);
        jw.name("limited ammo").value(limitedAmmo);
        jw.name("item state").value(itemState);
        jw.name("ammo").value(ammo);
        jw.name("firing arc").value(firingArc);
        jw.name("encumbrance").value(encum);
        jw.endObject();
    }

    public void loadJson(JsonReader jr) throws IOException{
        jr.beginObject();
        while(jr.hasNext()){
            if(!jr.peek().equals(JsonToken.NAME)){
                jr.skipValue();
                continue;
            }
            switch (jr.nextName()){
                case "name":
                    name = jr.nextString();
                    break;
                case "damage":
                    dmg = jr.nextInt();
                    break;
                case "critical rating":
                    crit = jr.nextInt();
                    break;
                case "hard points":
                    hp = jr.nextInt();
                    break;
                case "range":
                    range = jr.nextInt();
                    break;
                case "skill":
                    skill =jr.nextInt();
                    break;
                case "base":
                    skillBase = jr.nextInt();
                    break;
                case "Weapon Characteristics":
                    chars.loadJson(jr);
                    break;
                case "WeapChars":
                    chars.loadJson(jr);
                case "add brawn":
                    addBrawn = jr.nextBoolean();
                    break;
                case "loaded":
                    loaded = jr.nextBoolean();
                    break;
                case "limited ammo":
                    limitedAmmo = jr.nextBoolean();
                    break;
                case "item state":
                    itemState = jr.nextInt();
                    break;
                case "ammo":
                    ammo = jr.nextInt();
                    break;
                case "firing arc":
                    firingArc = jr.nextString();
                    break;
                case "encumbrance":
                    encum = jr.nextInt();
            }
        }
        jr.endObject();
    }

    public static void editWeapon(final Activity ac, final Editable c, final int pos, final boolean newWeapon, final Skill.onSave os){
        final Weapons w = c.weapons.clone();
        AlertDialog.Builder b = new AlertDialog.Builder(ac);
        final View ed = ac.getLayoutInflater().inflate(R.layout.dialog_weapon_edit,null);
        b.setView(ed);
        if(c instanceof Character){
            ed.findViewById(R.id.encum_lay).setVisibility(View.VISIBLE);
            ed.findViewById(R.id.add_brawn_switch).setVisibility(View.VISIBLE);
        }else if(c instanceof Minion)
            ed.findViewById(R.id.add_brawn_switch).setVisibility(View.VISIBLE);
        else if(c instanceof Vehicle)
            ed.findViewById(R.id.arc_lay).setVisibility(View.VISIBLE);
        final EditText name = (EditText)ed.findViewById(R.id.name_edit);
        name.setText(w.get(pos).name);
        final EditText dmg = (EditText)ed.findViewById(R.id.damage_edit);
        dmg.setText(String.valueOf(w.get(pos).dmg));
        final EditText crit = (EditText)ed.findViewById(R.id.crit_edit);
        crit.setText(String.valueOf(w.get(pos).crit));
        final EditText hp = (EditText)ed.findViewById(R.id.hp_edit);
        hp.setText(String.valueOf(w.get(pos).hp));
        final EditText arc = (EditText)ed.findViewById(R.id.arc_edit);
        arc.setText(w.get(pos).firingArc);
        final EditText encum = (EditText)ed.findViewById(R.id.encum_edit);
        encum.setText(String.valueOf(w.get(pos).encum));
        final Spinner range = (Spinner)ed.findViewById(R.id.range_spin);
        range.setSelection(w.get(pos).range);
        final Spinner state = (Spinner)ed.findViewById(R.id.state_spin);
        state.setSelection(w.get(pos).itemState);
        final Spinner skill = (Spinner)ed.findViewById(R.id.skill_spin);
        skill.setSelection(w.get(pos).skill);
        final Spinner base = (Spinner)ed.findViewById(R.id.base_spin);
        base.setSelection(w.get(pos).skillBase);
        final int[] skillBase = ac.getResources().getIntArray(R.array.weapon_skill_bases);
        skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                base.setSelection(skillBase[position]);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        RecyclerView chars = (RecyclerView)ed.findViewById(R.id.char_recycler);
        final WeapChars.WeapCharsAdap adap = new WeapChars.WeapCharsAdap(w.get(pos),ac);
        chars.setAdapter(adap);
        chars.setLayoutManager(new LinearLayoutManager(ac));
        ed.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w.get(pos).chars.add(new WeapChar());
                WeapChar.editWeapChar(ac, w.get(pos), w.get(pos).chars.size() - 1, true, new Skill.onSave() {
                    public void save() {
                        adap.notifyDataSetChanged();
                    }
                    public void delete() {
                        w.get(pos).chars.remove(w.get(pos).chars.get(w.get(pos).chars.size()-1));
                    }
                    public void cancel() {
                        w.get(pos).chars.remove(w.get(pos).chars.get(w.get(pos).chars.size()-1));
                    }
                });
            }
        });
        final Switch brawn = (Switch)ed.findViewById(R.id.add_brawn_switch);
        brawn.setSelected(w.get(pos).addBrawn);
        final Switch loaded = (Switch)ed.findViewById(R.id.loaded_switch);
        loaded.setSelected(w.get(pos).loaded);
        final Switch limited = (Switch)ed.findViewById(R.id.limited_ammo_switch);
        final int[] ammo = {w.get(pos).ammo};
        limited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ed.findViewById(R.id.ammo_lay).setVisibility(View.VISIBLE);
                }else
                    ed.findViewById(R.id.ammo_lay).setVisibility(View.GONE);
            }
        });
        limited.setSelected(w.get(pos).limitedAmmo);
        Animation in = AnimationUtils.loadAnimation(ac,android.R.anim.slide_in_left);
        in.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
        Animation out = AnimationUtils.loadAnimation(ac,android.R.anim.slide_out_right);
        out.setInterpolator(ac,android.R.anim.anticipate_overshoot_interpolator);
        final TextSwitcher ammoText = (TextSwitcher)ed.findViewById(R.id.ammo_switch);
        ammoText.setInAnimation(in);
        ammoText.setOutAnimation(out);
        ammoText.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return ac.getLayoutInflater().inflate(R.layout.template_num_text,ammoText,false);
            }
        });
        ammoText.setText(String.valueOf(ammo[0]));
        ed.findViewById(R.id.ammo_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ammo[0]++;
                ammoText.setText(String.valueOf(ammo[0]));
            }
        });
        ed.findViewById(R.id.ammo_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ammo[0]>0){
                    ammo[0]--;
                    ammoText.setText(String.valueOf(ammo[0]));
                }
            }
        });
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                w.get(pos).name = name.getText().toString();
                if (!dmg.getText().toString().equals(""))
                    w.get(pos).dmg = Integer.parseInt(dmg.getText().toString());
                else
                    w.get(pos).dmg = 0;
                if (!crit.getText().toString().equals(""))
                    w.get(pos).crit = Integer.parseInt(crit.getText().toString());
                else
                    w.get(pos).crit = 0;
                if (!hp.getText().toString().equals(""))
                    w.get(pos).hp = Integer.parseInt(hp.getText().toString());
                else
                    w.get(pos).hp = 0;
                w.get(pos).firingArc = arc.getText().toString();
                if (!encum.getText().toString().equals(""))
                    w.get(pos).encum = Integer.parseInt(encum.getText().toString());
                else
                    w.get(pos).encum = 0;
                w.get(pos).range = range.getSelectedItemPosition();
                w.get(pos).itemState = state.getSelectedItemPosition();
                w.get(pos).skill = skill.getSelectedItemPosition();
                w.get(pos).skillBase = base.getSelectedItemPosition();
                w.get(pos).addBrawn = brawn.isChecked();
                w.get(pos).loaded = loaded.isChecked();
                w.get(pos).ammo = ammo[0];
                w.get(pos).limitedAmmo = limited.isChecked();
                c.weapons = w.clone();
                os.save();
                dialog.cancel();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                os.cancel();
                dialog.cancel();
            }
        });
        if(!newWeapon){
            b.setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    os.delete();
                    dialog.cancel();
                }
            });
        }
        b.show();
    }
}
