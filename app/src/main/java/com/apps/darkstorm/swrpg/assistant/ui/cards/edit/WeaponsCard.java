package com.apps.darkstorm.swrpg.assistant.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.WeapChar;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapon;
import com.apps.darkstorm.swrpg.assistant.ui.WeapCharLayout;
import com.apps.darkstorm.swrpg.assistant.ui.WeaponLayout;
import com.apps.darkstorm.swrpg.assistant.R;

public class WeaponsCard {
    public static View getCard(final Activity main, final ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_weapons,root,false);
        for (int i = 0;i<chara.weapons.size();i++)
            ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                    .WeaponLayout(root,main,((LinearLayout)top.findViewById(R.id.weapons_layout)),chara,chara.weapons.get(i)));
        top.findViewById(R.id.weapons_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Weapon tmp = new Weapon();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_weapon_edit,null);
                build.setView(dia);
                final EditText nameVal = (EditText)dia.findViewById(R.id.weapon_edit_name);
                nameVal.setText(tmp.name);
                final EditText dmg = (EditText)dia.findViewById(R.id.weapon_edit_damage);
                dmg.setText(String.valueOf(tmp.dmg));
                final EditText crit = (EditText)dia.findViewById(R.id.weapon_edit_critical);
                crit.setText(String.valueOf(tmp.crit));
                final EditText hp = (EditText)dia.findViewById(R.id.weapon_edit_hp);
                hp.setText(String.valueOf(tmp.hp));
                final EditText encum = (EditText)dia.findViewById(R.id.weapon_edit_encum);
                encum.setText(String.valueOf(tmp.encum));
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
                        AlertDialog.Builder build = new AlertDialog.Builder(main);
                        View dia = main.getLayoutInflater().inflate(R.layout.dialog_weapon_char,null);
                        build.setView(dia);
                        final EditText name = (EditText)dia.findViewById(R.id.weapon_char_name);
                        name.setText(wr.name);
                        final EditText val = (EditText)dia.findViewById(R.id.weapon_char_value);
                        val.setText(String.valueOf(wr.val));
                        final EditText adv = (EditText)dia.findViewById(R.id.weapon_char_adv);
                        adv.setText(String.valueOf(wr.adv));
                        build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                if (chara.isOverEncum())
                                    root.findViewById(R.id.encum_warning).setVisibility(View.VISIBLE);
                                else
                                    root.findViewById(R.id.encum_warning).setVisibility(View.GONE);
                                dialog.cancel();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        build.show();
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
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = nameVal.getText().toString();
                        if (!dmg.getText().toString().equals("")){
                            tmp.dmg = Integer.parseInt(dmg.getText().toString());
                        }else{
                            tmp.dmg = 0;
                        }
                        if (!crit.getText().toString().equals(""))
                            tmp.crit = Integer.parseInt(crit.getText().toString());
                        else
                            tmp.crit = 0;
                        if (!hp.getText().toString().equals("")){
                            tmp.hp = Integer.parseInt(hp.getText().toString());
                        }else
                            tmp.hp = 0;
                        if (!encum.getText().toString().equals(""))
                            tmp.encum = Integer.parseInt(encum.getText().toString());
                        else
                            tmp.encum = 0;
                        tmp.itemState = state.getSelectedItemPosition();
                        tmp.range = range.getSelectedItemPosition();
                        tmp.skill = skill.getSelectedItemPosition();
                        tmp.skillBase = skillChar.getSelectedItemPosition();
                        tmp.chars = tmp.chars.clone();
                        tmp.addBrawn = addBrawn.isChecked();
                        tmp.loaded = loaded.isChecked();
                        tmp.slug = slug.isChecked();
                        if (chara.isOverEncum())
                            root.findViewById(R.id.encum_warning).setVisibility(View.VISIBLE);
                        else
                            root.findViewById(R.id.encum_warning).setVisibility(View.GONE);
                        chara.weapons.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                                .WeaponLayout(root,main,((LinearLayout)top.findViewById(R.id.weapons_layout)),chara,chara.weapons.get(chara.weapons.size()-1)));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Minion minion){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_weapons,root,false);
        for (int i = 0;i<minion.weapons.size();i++)
            ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                    .WeaponLayout(main,((LinearLayout)top.findViewById(R.id.weapons_layout)),minion,minion.weapons.get(i)));
        top.findViewById(R.id.weapons_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Weapon tmp = new Weapon();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_weapon_edit,null);
                build.setView(dia);
                final EditText nameVal = (EditText)dia.findViewById(R.id.weapon_edit_name);
                nameVal.setText(tmp.name);
                final EditText dmg = (EditText)dia.findViewById(R.id.weapon_edit_damage);
                dmg.setText(String.valueOf(tmp.dmg));
                final EditText crit = (EditText)dia.findViewById(R.id.weapon_edit_critical);
                crit.setText(String.valueOf(tmp.crit));
                final EditText hp = (EditText)dia.findViewById(R.id.weapon_edit_hp);
                hp.setText(String.valueOf(tmp.hp));
                final EditText encum = (EditText)dia.findViewById(R.id.weapon_edit_encum);
                encum.setText(String.valueOf(tmp.encum));
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
                        AlertDialog.Builder build = new AlertDialog.Builder(main);
                        View dia = main.getLayoutInflater().inflate(R.layout.dialog_weapon_char,null);
                        build.setView(dia);
                        final EditText name = (EditText)dia.findViewById(R.id.weapon_char_name);
                        name.setText(wr.name);
                        final EditText val = (EditText)dia.findViewById(R.id.weapon_char_value);
                        val.setText(String.valueOf(wr.val));
                        final EditText adv = (EditText)dia.findViewById(R.id.weapon_char_adv);
                        adv.setText(String.valueOf(wr.adv));
                        build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                dialog.cancel();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        build.show();
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
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = nameVal.getText().toString();
                        if (!dmg.getText().toString().equals("")){
                            tmp.dmg = Integer.parseInt(dmg.getText().toString());
                        }else{
                            tmp.dmg = 0;
                        }
                        if (!crit.getText().toString().equals(""))
                            tmp.crit = Integer.parseInt(crit.getText().toString());
                        else
                            tmp.crit = 0;
                        if (!hp.getText().toString().equals("")){
                            tmp.hp = Integer.parseInt(hp.getText().toString());
                        }else
                            tmp.hp = 0;
                        if (!encum.getText().toString().equals(""))
                            tmp.encum = Integer.parseInt(encum.getText().toString());
                        else
                            tmp.encum = 0;
                        tmp.itemState = state.getSelectedItemPosition();
                        tmp.range = range.getSelectedItemPosition();
                        tmp.skill = skill.getSelectedItemPosition();
                        tmp.skillBase = skillChar.getSelectedItemPosition();
                        tmp.chars = tmp.chars.clone();
                        tmp.addBrawn = addBrawn.isChecked();
                        tmp.loaded = loaded.isChecked();
                        tmp.slug = slug.isChecked();
                        minion.weapons.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                                .WeaponLayout(main,((LinearLayout)top.findViewById(R.id.weapons_layout)),minion,minion.weapons.get(minion.weapons.size()-1)));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        top.findViewById(R.id.weap_save_load).setVisibility(View.VISIBLE);
        top.findViewById(R.id.weap_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.origWeapons = minion.weapons.clone();
            }
        });
        top.findViewById(R.id.weap_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.weapons = minion.origWeapons.clone();
                ((LinearLayout)top.findViewById(R.id.weapons_layout)).removeAllViews();
                for (int i = 0;i<minion.weapons.size();i++)
                    ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                            .WeaponLayout(main,((LinearLayout)top.findViewById(R.id.weapons_layout)),minion,minion.weapons.get(i)));
            }
        });
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Vehicle vh){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_weapons,root,false);
        for (int i = 0;i<vh.weapons.size();i++){
            ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout().WeaponLayout(main,
                    ((LinearLayout)top.findViewById(R.id.weapons_layout)),vh,vh.weapons.get(i)));
        }
        top.findViewById(R.id.weapons_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Weapon tmp = new Weapon();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_weapon_edit,null);
                build.setView(dia);
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
                dia.findViewById(R.id.weapon_edit_encum).setVisibility(View.GONE);
                dia.findViewById(R.id.encum_text).setVisibility(View.GONE);
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
                        AlertDialog.Builder build = new AlertDialog.Builder(main);
                        final View dia = main.getLayoutInflater().inflate(R.layout.dialog_weapon_char,null);
                        build.setView(dia);
                        final EditText name = (EditText)dia.findViewById(R.id.weapon_char_name);
                        name.setText(wr.name);
                        final EditText val = (EditText)dia.findViewById(R.id.weapon_char_value);
                        val.setText(String.valueOf(wr.val));
                        final EditText adv = (EditText)dia.findViewById(R.id.weapon_char_adv);
                        adv.setText(String.valueOf(wr.adv));
                        build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                dialog.cancel();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        build.show();
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
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        tmp.name = nameVal.getText().toString();
                        if (!dmg.getText().toString().equals("")){
                            tmp.dmg = Integer.parseInt(dmg.getText().toString());
                        }else{
                            tmp.dmg = 0;
                        }
                        if (!crit.getText().toString().equals(""))
                            tmp.crit = Integer.parseInt(crit.getText().toString());
                        else
                            tmp.crit = 0;
                        if (!hp.getText().toString().equals("")){
                            tmp.hp = Integer.parseInt(hp.getText().toString());
                        }else
                            tmp.hp = 0;
                        tmp.itemState = state.getSelectedItemPosition();
                        tmp.range = range.getSelectedItemPosition();
                        tmp.skill = skill.getSelectedItemPosition();
                        tmp.skillBase = skillChar.getSelectedItemPosition();
                        tmp.chars = tmp.chars.clone();
                        tmp.addBrawn = addBrawn.isChecked();
                        tmp.loaded = loaded.isChecked();
                        tmp.slug = slug.isChecked();
                        tmp.firingArc = arc.getText().toString();
                        vh.weapons.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.weapons_layout)).addView(new WeaponLayout()
                                .WeaponLayout(main,((LinearLayout)top.findViewById(R.id.weapons_layout)),vh,vh.weapons.get(vh.weapons.size()-1)));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
            }
        });
        return top;
    }
}
