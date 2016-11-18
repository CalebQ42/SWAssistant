package com.apps.darkstorm.swrpg.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.Dice.DiceResults;
import com.apps.darkstorm.swrpg.Dice.DiceRoll;
import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.Character;
import com.apps.darkstorm.swrpg.StarWars.Stuff.WeapChar;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Weapon;
import com.apps.darkstorm.swrpg.StarWars.Vehicle;

public class WeaponLayout {
    int ability,proficiency,difficulty,challenge,boost,setback,force;
    public LinearLayout WeaponLayout(final Context main,final LinearLayout weapLay,final Character chara,final Weapon w){
        LinearLayout top = new LinearLayout(main);
        LinearLayout.LayoutParams toplp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        toplp.setMargins(4,4,4,4);
        toplp.setMarginEnd(4);
        toplp.setMarginStart(4);
        top.setLayoutParams(toplp);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless,outVal,true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            top.setForeground(main.getDrawable(outVal.resourceId));
        else
            top.setBackgroundResource(outVal.resourceId);
        final TextView name = new TextView(main);
        LinearLayout.LayoutParams namelp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setTextSize(16);
        name.setTypeface(null,Typeface.BOLD);
        name.setText(w.name);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (w.loaded && (!w.slug ||
                        w.ammo >0)) {
                    final Dialog dia = new Dialog(main);
                    dia.setContentView(R.layout.dialog_char_dice_roll);
                    dia.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ability = 0;
                            proficiency = 0;
                            difficulty = 0;
                            challenge = 0;
                            boost = 0;
                            setback = 0;
                            force = 0;
                        }
                    });
                    int skillNum =0;
                    String[] weapSkil = main.getResources().getStringArray(R.array.weapon_skills);
                    for (int i = 0; i < chara.skills.size(); i++) {
                        if (chara.skills.get(i).name.equals(weapSkil[w.skill])) {
                            skillNum = chara.skills.get(i).val;
                            break;
                        }
                    }
                    ability = Math.abs(skillNum - chara.charVals[w.skillBase]);
                    ((TextView) dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                    if (chara.charVals[w.skillBase] > skillNum)
                        proficiency = skillNum;
                    else
                        proficiency = chara.charVals[w.skillBase];
                    ((TextView) dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                    dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ability > 0) {
                                ability--;
                                ((TextView) dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ability++;
                            ((TextView) dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                        }
                    });
                    dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (proficiency > 0) {
                                proficiency--;
                                ((TextView) dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            proficiency++;
                            ((TextView) dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                        }
                    });
                    dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (difficulty > 0) {
                                difficulty--;
                                ((TextView) dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            difficulty++;
                            ((TextView) dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                        }
                    });
                    dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (challenge > 0) {
                                challenge--;
                                ((TextView) dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            challenge++;
                            ((TextView) dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                        }
                    });
                    dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (boost > 0) {
                                boost--;
                                ((TextView) dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boost++;
                            ((TextView) dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                        }
                    });
                    dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (setback > 0) {
                                setback--;
                                ((TextView) dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setback++;
                            ((TextView) dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                        }
                    });
                    dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (force > 0) {
                                force--;
                                ((TextView) dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            force++;
                            ((TextView) dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                        }
                    });
                    dia.findViewById(R.id.char_dice_roll).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DiceRoll dr = new DiceRoll();
                            DiceResults res = dr.rollDice(ability
                                    , proficiency, difficulty, challenge, boost, setback, force);
                            dia.cancel();
                            Dialog dia = new Dialog(main);
                            dia.setContentView(R.layout.dialog_weapon_damage);
                            if (w.slug)
                                w.ammo--;
                            if (res.suc <= res.fail) {
                                dia.findViewById(R.id.weapon_miss).setVisibility(View.VISIBLE);
                            } else {
                                int dmg = res.suc - res.fail + w.dmg;
                                if (w.addBrawn)
                                    dmg += chara.charVals[0];
                                dia.findViewById(R.id.weapon_damage_main).setVisibility(View.VISIBLE);
                                ((TextView) dia.findViewById(R.id.weapon_damage_val)).setText(String.valueOf(dmg));
                            }
                            if (res.adv > res.thr){
                                dia.findViewById(R.id.weapon_advantage_main).setVisibility(View.VISIBLE);
                                dia.findViewById(R.id.weapon_char_topper).setVisibility(View.VISIBLE);
                                ((TextView)dia.findViewById(R.id.weapon_advantage_val))
                                        .setText(String.valueOf(res.adv-res.thr));
                                dia.findViewById(R.id.weapon_char_scroll).setVisibility(View.VISIBLE);
                                ((ScrollView)dia.findViewById(R.id.weapon_char_scroll)).addView(WeapCharList(
                                        main,chara,w));
                            }else if (res.thr > res.adv){
                                dia.findViewById(R.id.weapon_threat_main).setVisibility(View.VISIBLE);
                                ((TextView)dia.findViewById(R.id.weapon_threat_val)).setText(
                                        String.valueOf(res.thr-res.adv));
                            }else if (res.tri > 0){
                                dia.findViewById(R.id.weapon_triumph_main).setVisibility(View.VISIBLE);
                                ((TextView)dia.findViewById(R.id.weapon_triumph_val)).setText(String.valueOf(res.tri));
                            }else if (res.desp >0){
                                dia.findViewById(R.id.weapon_despair_main).setVisibility(View.VISIBLE);
                                ((TextView)dia.findViewById(R.id.weapon_despair_val)).setText(String.valueOf(res.desp));
                            }
                            dia.show();
                        }
                    });
                    dia.findViewById(R.id.char_dice_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dia.cancel();
                        }
                    });
                    dia.show();
                }else if(!w.loaded){
                    Dialog dia = new Dialog(main);
                    dia.setContentView(R.layout.dialog_weapon_damage);
                    dia.findViewById(R.id.weapon_not_loaded).setVisibility(View.VISIBLE);
                    dia.show();
                }else if(w.slug && w.ammo ==0){
                    Dialog dia = new Dialog(main);
                    dia.setContentView(R.layout.dialog_weapon_damage);
                    dia.findViewById(R.id.weapon_no_ammo).setVisibility(View.VISIBLE);
                    dia.show();
                }
            }
        });
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Weapon tmp = w.clone();
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_weapon_edit);
                final EditText nameVal = (EditText)dia.findViewById(R.id.weapon_edit_name);
                nameVal.setText(tmp.name);
                final EditText dmg = (EditText)dia.findViewById(R.id.weapon_edit_damage);
                dmg.setText(String.valueOf(tmp.dmg));
                final EditText crit = (EditText)dia.findViewById(R.id.weapon_edit_critical);
                crit.setText(String.valueOf(tmp.crit));
                final EditText hp = (EditText)dia.findViewById(R.id.weapon_edit_hp);
                hp.setText(String.valueOf(tmp.hp));
                final Spinner state = (Spinner)dia.findViewById(R.id.weapon_edit_weapon_state);
                ArrayAdapter<CharSequence> stateAdap = ArrayAdapter.createFromResource(main,R.array.gear_damage_levels,R.layout.spinner_base);
                state.setAdapter(stateAdap);
                state.setSelection(tmp.itemState);
                final Spinner range = (Spinner)dia.findViewById(R.id.weapon_edit_range_spinner);
                ArrayAdapter<CharSequence> rangeAdap = ArrayAdapter.createFromResource(main,R.array.range_bands,R.layout.spinner_base);
                range.setAdapter(rangeAdap);
                range.setSelection(tmp.range);
                final Spinner skill = (Spinner)dia.findViewById(R.id.weapon_edit_skill_spinner);
                ArrayAdapter<CharSequence> skillAdap = ArrayAdapter.createFromResource(main,R.array.weapon_skills,R.layout.spinner_base);
                skill.setAdapter(skillAdap);
                skill.setSelection(tmp.skill);
                final Spinner skillChar = (Spinner)dia.findViewById(R.id.weapon_edit_skill_char_spinner);
                ArrayAdapter<CharSequence> charAdap = ArrayAdapter.createFromResource(main,R.array.base_characteristics,R.layout.spinner_base);
                skillChar.setAdapter(charAdap);
                skillChar.setSelection(tmp.skillBase);
                skill.post(new Runnable() {
                    @Override
                    public void run() {
                        skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                skillChar.setSelection(main.getResources().getIntArray(R.array.weapon_skill_bases)[position]);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                });
                final LinearLayout spec = (LinearLayout)dia.findViewById(R.id.weapon_edit_special_layout);
                for (int i = 0;i<tmp.chars.size();i++)
                    spec.addView(new WeapCharLayout().WeapCharLayout(main,spec,tmp,tmp.chars.get(i)));
                dia.findViewById(R.id.weapon_edit_special_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final WeapChar wr = new WeapChar();
                        final Dialog dia = new Dialog(main);
                        dia.setContentView(R.layout.dialog_weapon_char);
                        final EditText name = (EditText)dia.findViewById(R.id.weapon_char_name);
                        name.setText(wr.name);
                        final EditText val = (EditText)dia.findViewById(R.id.weapon_char_value);
                        val.setText(String.valueOf(wr.val));
                        final EditText adv = (EditText)dia.findViewById(R.id.weapon_char_adv);
                        adv.setText(String.valueOf(wr.adv));
                        dia.findViewById(R.id.weapon_char_save).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wr.name = name.getText().toString();
                                if (!val.getText().toString().equals("")){
                                    wr.val = Integer.parseInt(val.getText().toString());
                                }else{
                                    wr.val = 0;
                                }
                                if (!adv.getText().toString().equals("")){
                                    wr.adv = Integer.parseInt(adv.getText().toString());
                                }else{
                                    wr.adv = 0;
                                }
                                tmp.chars.add(wr);
                                spec.addView(new WeapCharLayout().WeapCharLayout(main,spec,tmp,tmp.chars.get(tmp.chars.size()-1)));
                                dia.cancel();
                            }
                        });
                        dia.findViewById(R.id.weapon_char_cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dia.cancel();
                            }
                        });
                        dia.show();
                    }
                });
                final Switch addBrawn = (Switch)dia.findViewById(R.id.weapon_edit_add_brawn);
                addBrawn.setChecked(tmp.addBrawn);
                final Switch loaded = (Switch)dia.findViewById(R.id.weapon_edit_loaded);
                loaded.setChecked(tmp.loaded);
                final Switch slug = (Switch)dia.findViewById(R.id.weapon_edit_slug);
                slug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.VISIBLE);
                        }else{
                            dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.GONE);
                        }
                    }
                });
                slug.setChecked(tmp.slug);
                if (tmp.slug){
                    dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.VISIBLE);
                }else{
                    dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.GONE);
                }
                final TextView ammo = (TextView)dia.findViewById(R.id.weapon_edit_ammo_val);
                ammo.setText(String.valueOf(tmp.ammo));
                dia.findViewById(R.id.weapon_edit_ammo_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.ammo>0)
                            tmp.ammo--;
                        ammo.setText(String.valueOf(tmp.ammo));
                    }
                });
                dia.findViewById(R.id.weapon_edit_ammo_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.ammo++;
                        ammo.setText(String.valueOf(tmp.ammo));
                    }
                });
                dia.findViewById(R.id.weapon_edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        w.name = nameVal.getText().toString();
                        if (!dmg.getText().toString().equals("")){
                            w.dmg = Integer.parseInt(dmg.getText().toString());
                        }else{
                            w.dmg = 0;
                        }
                        if (!crit.getText().toString().equals(""))
                            w.crit = Integer.parseInt(crit.getText().toString());
                        else
                            w.crit = 0;
                        if (!hp.getText().toString().equals("")){
                            w.hp = Integer.parseInt(hp.getText().toString());
                        }else
                            w.hp = 0;
                        w.itemState = state.getSelectedItemPosition();
                        w.range = range.getSelectedItemPosition();
                        w.skill = skill.getSelectedItemPosition();
                        w.skillBase = skillChar.getSelectedItemPosition();
                        w.chars = tmp.chars.clone();
                        w.addBrawn = addBrawn.isChecked();
                        w.loaded = loaded.isChecked();
                        w.slug = slug.isChecked();
                        w.ammo = tmp.ammo;
                        name.setText(w.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.weapon_edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.weapon_edit_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.weapon_edit_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int wea = chara.weapons.remove(w);
                        weapLay.removeViewAt(wea);
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.addView(name);
        return top;
    }
    public LinearLayout WeaponLayout(final Context main, final LinearLayout weapLay, final Vehicle vh, final Weapon w){
        LinearLayout top = new LinearLayout(main);
        LinearLayout.LayoutParams toplp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        toplp.setMargins(4,4,4,4);
        toplp.setMarginEnd(4);
        toplp.setMarginStart(4);
        top.setLayoutParams(toplp);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless,outVal,true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            top.setForeground(main.getDrawable(outVal.resourceId));
        else
            top.setBackgroundResource(outVal.resourceId);
        final TextView name = new TextView(main);
        LinearLayout.LayoutParams namelp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setTextSize(16);
        name.setTypeface(null,Typeface.BOLD);
        name.setText(w.name);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (w.loaded && (!w.slug ||
                        w.ammo >0)) {
                    final Dialog dia = new Dialog(main);
                    dia.setContentView(R.layout.dialog_char_dice_roll);
                    dia.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ability = 0;
                            proficiency = 0;
                            difficulty = 0;
                            challenge = 0;
                            boost = 0;
                            setback = 0;
                            force = 0;
                        }
                    });
                    ((TextView) dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                    dia.findViewById(R.id.char_dice_ability_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ability > 0) {
                                ability--;
                                ((TextView) dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_ability_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ability++;
                            ((TextView) dia.findViewById(R.id.char_dice_ability_val)).setText(String.valueOf(ability));
                        }
                    });
                    dia.findViewById(R.id.char_dice_proficiency_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (proficiency > 0) {
                                proficiency--;
                                ((TextView) dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_proficiency_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            proficiency++;
                            ((TextView) dia.findViewById(R.id.char_dice_proficiency_val)).setText(String.valueOf(proficiency));
                        }
                    });
                    dia.findViewById(R.id.char_dice_difficulty_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (difficulty > 0) {
                                difficulty--;
                                ((TextView) dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_difficulty_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            difficulty++;
                            ((TextView) dia.findViewById(R.id.char_dice_difficulty_val)).setText(String.valueOf(difficulty));
                        }
                    });
                    dia.findViewById(R.id.char_dice_challenge_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (challenge > 0) {
                                challenge--;
                                ((TextView) dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_challenge_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            challenge++;
                            ((TextView) dia.findViewById(R.id.char_dice_challenge_val)).setText(String.valueOf(challenge));
                        }
                    });
                    dia.findViewById(R.id.char_dice_boost_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (boost > 0) {
                                boost--;
                                ((TextView) dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_boost_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boost++;
                            ((TextView) dia.findViewById(R.id.char_dice_boost_val)).setText(String.valueOf(boost));
                        }
                    });
                    dia.findViewById(R.id.char_dice_setback_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (setback > 0) {
                                setback--;
                                ((TextView) dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_setback_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setback++;
                            ((TextView) dia.findViewById(R.id.char_dice_setback_val)).setText(String.valueOf(setback));
                        }
                    });
                    dia.findViewById(R.id.char_dice_force_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (force > 0) {
                                force--;
                                ((TextView) dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                            }
                        }
                    });
                    dia.findViewById(R.id.char_dice_force_plus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            force++;
                            ((TextView) dia.findViewById(R.id.char_dice_force_val)).setText(String.valueOf(force));
                        }
                    });
                    dia.findViewById(R.id.char_dice_roll).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DiceRoll dr = new DiceRoll();
                            DiceResults res = dr.rollDice(ability
                                    , proficiency, difficulty, challenge, boost, setback, force);
                            dia.cancel();
                            Dialog dia = new Dialog(main);
                            dia.setContentView(R.layout.dialog_weapon_damage);
                            if (w.slug)
                                w.ammo--;
                            if (res.suc <= res.fail) {
                                dia.findViewById(R.id.weapon_miss).setVisibility(View.VISIBLE);
                            } else {
                                int dmg = res.suc - res.fail + w.dmg;
                                dia.findViewById(R.id.weapon_damage_main).setVisibility(View.VISIBLE);
                                ((TextView) dia.findViewById(R.id.weapon_damage_val)).setText(String.valueOf(dmg));
                            }
                            if (res.adv > res.thr){
                                dia.findViewById(R.id.weapon_advantage_main).setVisibility(View.VISIBLE);
                                dia.findViewById(R.id.weapon_char_topper).setVisibility(View.VISIBLE);
                                ((TextView)dia.findViewById(R.id.weapon_advantage_val))
                                        .setText(String.valueOf(res.adv-res.thr));
                                dia.findViewById(R.id.weapon_char_scroll).setVisibility(View.VISIBLE);
                                ((ScrollView)dia.findViewById(R.id.weapon_char_scroll)).addView(WeapCharList(
                                        main,vh,w));
                            }else if (res.thr > res.adv){
                                dia.findViewById(R.id.weapon_threat_main).setVisibility(View.VISIBLE);
                                ((TextView)dia.findViewById(R.id.weapon_threat_val)).setText(
                                        String.valueOf(res.thr-res.adv));
                            }else if (res.tri > 0){
                                dia.findViewById(R.id.weapon_triumph_main).setVisibility(View.VISIBLE);
                                ((TextView)dia.findViewById(R.id.weapon_triumph_val)).setText(String.valueOf(res.tri));
                            }else if (res.desp >0){
                                dia.findViewById(R.id.weapon_despair_main).setVisibility(View.VISIBLE);
                                ((TextView)dia.findViewById(R.id.weapon_despair_val)).setText(String.valueOf(res.desp));
                            }
                            dia.show();
                        }
                    });
                    dia.findViewById(R.id.char_dice_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dia.cancel();
                        }
                    });
                    dia.show();
                }else if(!w.loaded){
                    Dialog dia = new Dialog(main);
                    dia.setContentView(R.layout.dialog_weapon_damage);
                    dia.findViewById(R.id.weapon_not_loaded).setVisibility(View.VISIBLE);
                    dia.show();
                }else if(w.slug && w.ammo ==0){
                    Dialog dia = new Dialog(main);
                    dia.setContentView(R.layout.dialog_weapon_damage);
                    dia.findViewById(R.id.weapon_no_ammo).setVisibility(View.VISIBLE);
                    dia.show();
                }
            }
        });
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Weapon tmp = w.clone();
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_weapon_edit);
                final EditText nameVal = (EditText)dia.findViewById(R.id.weapon_edit_name);
                nameVal.setText(tmp.name);
                final EditText dmg = (EditText)dia.findViewById(R.id.weapon_edit_damage);
                dmg.setText(String.valueOf(tmp.dmg));
                final EditText crit = (EditText)dia.findViewById(R.id.weapon_edit_critical);
                crit.setText(String.valueOf(tmp.crit));
                final EditText hp = (EditText)dia.findViewById(R.id.weapon_edit_hp);
                hp.setText(String.valueOf(tmp.hp));
                final EditText arc = (EditText)dia.findViewById(R.id.weapon_edit_arc);
                arc.setText(tmp.firingArc);
                dia.findViewById(R.id.weapon_edit_arc_layout).setVisibility(View.VISIBLE);
                final Spinner state = (Spinner)dia.findViewById(R.id.weapon_edit_weapon_state);
                ArrayAdapter<CharSequence> stateAdap = ArrayAdapter.createFromResource(main,R.array.gear_damage_levels,R.layout.spinner_base);
                state.setAdapter(stateAdap);
                state.setSelection(tmp.itemState);
                final Spinner range = (Spinner)dia.findViewById(R.id.weapon_edit_range_spinner);
                ArrayAdapter<CharSequence> rangeAdap = ArrayAdapter.createFromResource(main,R.array.range_bands,R.layout.spinner_base);
                range.setAdapter(rangeAdap);
                range.setSelection(tmp.range);
                final Spinner skill = (Spinner)dia.findViewById(R.id.weapon_edit_skill_spinner);
                ArrayAdapter<CharSequence> skillAdap = ArrayAdapter.createFromResource(main,R.array.weapon_skills,R.layout.spinner_base);
                skill.setAdapter(skillAdap);
                skill.setSelection(tmp.skill);
                final Spinner skillChar = (Spinner)dia.findViewById(R.id.weapon_edit_skill_char_spinner);
                ArrayAdapter<CharSequence> charAdap = ArrayAdapter.createFromResource(main,R.array.base_characteristics,R.layout.spinner_base);
                skillChar.setAdapter(charAdap);
                skillChar.setSelection(tmp.skillBase);
                skill.post(new Runnable() {
                    @Override
                    public void run() {
                        skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                skillChar.setSelection(main.getResources().getIntArray(R.array.weapon_skill_bases)[position]);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                });
                final LinearLayout spec = (LinearLayout)dia.findViewById(R.id.weapon_edit_special_layout);
                for (int i = 0;i<tmp.chars.size();i++)
                    spec.addView(new WeapCharLayout().WeapCharLayout(main,spec,tmp,tmp.chars.get(i)));
                dia.findViewById(R.id.weapon_edit_special_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final WeapChar wr = new WeapChar();
                        final Dialog dia = new Dialog(main);
                        dia.setContentView(R.layout.dialog_weapon_char);
                        final EditText name = (EditText)dia.findViewById(R.id.weapon_char_name);
                        name.setText(wr.name);
                        final EditText val = (EditText)dia.findViewById(R.id.weapon_char_value);
                        val.setText(String.valueOf(wr.val));
                        final EditText adv = (EditText)dia.findViewById(R.id.weapon_char_adv);
                        adv.setText(String.valueOf(wr.adv));
                        dia.findViewById(R.id.weapon_char_save).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wr.name = name.getText().toString();
                                if (!val.getText().toString().equals("")){
                                    wr.val = Integer.parseInt(val.getText().toString());
                                }else{
                                    wr.val = 0;
                                }
                                if (!adv.getText().toString().equals("")){
                                    wr.adv = Integer.parseInt(adv.getText().toString());
                                }else{
                                    wr.adv = 0;
                                }
                                tmp.chars.add(wr);
                                spec.addView(new WeapCharLayout().WeapCharLayout(main,spec,tmp,tmp.chars.get(tmp.chars.size()-1)));
                                dia.cancel();
                            }
                        });
                        dia.findViewById(R.id.weapon_char_cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dia.cancel();
                            }
                        });
                        dia.show();
                    }
                });
                final Switch addBrawn = (Switch)dia.findViewById(R.id.weapon_edit_add_brawn);
                addBrawn.setChecked(tmp.addBrawn);
                addBrawn.setVisibility(View.GONE);
                final Switch loaded = (Switch)dia.findViewById(R.id.weapon_edit_loaded);
                loaded.setChecked(tmp.loaded);
                final Switch slug = (Switch)dia.findViewById(R.id.weapon_edit_slug);
                slug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.VISIBLE);
                        }else{
                            dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.GONE);
                        }
                    }
                });
                slug.setChecked(tmp.slug);
                if (tmp.slug){
                    dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.VISIBLE);
                }else{
                    dia.findViewById(R.id.weapon_edit_ammo_layout).setVisibility(View.GONE);
                }
                final TextView ammo = (TextView)dia.findViewById(R.id.weapon_edit_ammo_val);
                ammo.setText(String.valueOf(tmp.ammo));
                dia.findViewById(R.id.weapon_edit_ammo_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.ammo>0)
                            tmp.ammo--;
                        ammo.setText(String.valueOf(tmp.ammo));
                    }
                });
                dia.findViewById(R.id.weapon_edit_ammo_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.ammo++;
                        ammo.setText(String.valueOf(tmp.ammo));
                    }
                });
                dia.findViewById(R.id.weapon_edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        w.name = nameVal.getText().toString();
                        if (!dmg.getText().toString().equals("")){
                            w.dmg = Integer.parseInt(dmg.getText().toString());
                        }else{
                            w.dmg = 0;
                        }
                        if (!crit.getText().toString().equals(""))
                            w.crit = Integer.parseInt(crit.getText().toString());
                        else
                            w.crit = 0;
                        if (!hp.getText().toString().equals("")){
                            w.hp = Integer.parseInt(hp.getText().toString());
                        }else
                            w.hp = 0;
                        w.itemState = state.getSelectedItemPosition();
                        w.range = range.getSelectedItemPosition();
                        w.skill = skill.getSelectedItemPosition();
                        w.skillBase = skillChar.getSelectedItemPosition();
                        w.chars = tmp.chars.clone();
                        w.addBrawn = addBrawn.isChecked();
                        w.loaded = loaded.isChecked();
                        w.slug = slug.isChecked();
                        w.ammo = tmp.ammo;
                        w.firingArc = arc.getText().toString();
                        name.setText(w.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.weapon_edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.weapon_edit_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.weapon_edit_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int wea = vh.weapons.remove(w);
                        weapLay.removeViewAt(wea);
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.addView(name);
        return top;
    }
    public static LinearLayout WeapCharList(final Context main,final Character chara,final Weapon w){
        LinearLayout top = new LinearLayout(main);
        top.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams toplp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        top.setLayoutParams(toplp);
        LinearLayout crit = new LinearLayout(main);
        crit.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams critlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        critlp.setMargins(5, 5, 5, 5);
        crit.setLayoutParams(critlp);
        TextView critName = new TextView(main);
        LinearLayout.LayoutParams critnamelp = new LinearLayout.LayoutParams(
                ((int) main.getResources().getDisplayMetrics().density * 100),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        critnamelp.weight = 2;
        critName.setLayoutParams(critnamelp);
        critName.setTextSize(16);
        critName.setText(main.getResources().getString(R.string.critical_text));
        TextView critAdv = new TextView(main);
        LinearLayout.LayoutParams critadvlp = new LinearLayout.LayoutParams
                (((int) main.getResources().getDisplayMetrics().density * 25),
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        critadvlp.weight = 1;
        critAdv.setLayoutParams(critadvlp);
        critAdv.setTextSize(16);
        critAdv.setGravity(Gravity.CENTER_HORIZONTAL);
        critAdv.setText(String.valueOf(w.crit));
        crit.addView(critName);
        crit.addView(critAdv);
        top.addView(crit);
        for (int i =0;i<w.chars.size();i++) {
            LinearLayout tmp = new LinearLayout(main);
            tmp.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams tmplp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tmplp.setMargins(5,5,5,5);
            tmp.setLayoutParams(tmplp);
            TextView tmpName = new TextView(main);
            tmpName.setLayoutParams(critnamelp);
            tmpName.setTextSize(16);
            String stuff = w.chars.get(i).name + " " +
                    w.chars.get(i).val + ":";
            tmpName.setText(stuff);
            TextView tmpAdv = new TextView(main);
            tmpAdv.setLayoutParams(critadvlp);
            tmpAdv.setTextSize(16);
            tmpAdv.setGravity(Gravity.CENTER_HORIZONTAL);
            tmpAdv.setText(String.valueOf(w.chars.get(i).adv));
            tmp.addView(tmpName);
            tmp.addView(tmpAdv);
            top.addView(tmp);
        }
        return top;
    }
    public static LinearLayout WeapCharList(final Context main,final Vehicle vh,final Weapon w){
        LinearLayout top = new LinearLayout(main);
        top.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams toplp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        top.setLayoutParams(toplp);
        LinearLayout crit = new LinearLayout(main);
        crit.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams critlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        critlp.setMargins(5, 5, 5, 5);
        crit.setLayoutParams(critlp);
        TextView critName = new TextView(main);
        LinearLayout.LayoutParams critnamelp = new LinearLayout.LayoutParams(
                ((int) main.getResources().getDisplayMetrics().density * 100),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        critnamelp.weight = 2;
        critName.setLayoutParams(critnamelp);
        critName.setTextSize(16);
        critName.setText(main.getResources().getString(R.string.critical_text));
        TextView critAdv = new TextView(main);
        LinearLayout.LayoutParams critadvlp = new LinearLayout.LayoutParams
                (((int) main.getResources().getDisplayMetrics().density * 25),
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        critadvlp.weight = 1;
        critAdv.setLayoutParams(critadvlp);
        critAdv.setTextSize(16);
        critAdv.setGravity(Gravity.CENTER_HORIZONTAL);
        critAdv.setText(String.valueOf(w.crit));
        crit.addView(critName);
        crit.addView(critAdv);
        top.addView(crit);
        for (int i =0;i<w.chars.size();i++) {
            LinearLayout tmp = new LinearLayout(main);
            tmp.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams tmplp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tmplp.setMargins(5,5,5,5);
            tmp.setLayoutParams(tmplp);
            TextView tmpName = new TextView(main);
            tmpName.setLayoutParams(critnamelp);
            tmpName.setTextSize(16);
            String stuff = w.chars.get(i).name + " " +
                    w.chars.get(i).val + ":";
            tmpName.setText(stuff);
            TextView tmpAdv = new TextView(main);
            tmpAdv.setLayoutParams(critadvlp);
            tmpAdv.setTextSize(16);
            tmpAdv.setGravity(Gravity.CENTER_HORIZONTAL);
            tmpAdv.setText(String.valueOf(w.chars.get(i).adv));
            tmp.addView(tmpName);
            tmp.addView(tmpAdv);
            top.addView(tmp);
        }
        return top;
    }
}
