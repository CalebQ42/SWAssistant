package com.apps.darkstorm.swrpg.UI.Vehic;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.Stuff.CriticalInjury;
import com.apps.darkstorm.swrpg.StarWars.Stuff.WeapChar;
import com.apps.darkstorm.swrpg.StarWars.Stuff.Weapon;
import com.apps.darkstorm.swrpg.StarWars.Vehicle;
import com.apps.darkstorm.swrpg.UI.CritInjLayout;
import com.apps.darkstorm.swrpg.UI.WeapCharLayout;
import com.apps.darkstorm.swrpg.UI.WeaponLayout;

public class SetupVehicEdit {
    public static void setup(final View top, final Vehicle vh){
        //<editor-fold desc="name_card">
        final TextView name = (TextView)top.findViewById(R.id.name);
        name.setText(vh.name);
        top.findViewById(R.id.name_card).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.name_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                val.setText(vh.name);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        vh.name = val.getText().toString();
                        name.setText(vh.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        dia.cancel();
                    }
                });
                dia.show();
                return false;
            }
        });
        //</editor-fold>
        //<editor-fold desc="basic_info">
        final TextView model = (TextView)top.findViewById(R.id.model_text);
        model.setText(vh.model);
        top.findViewById(R.id.model_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.model_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.model);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vh.model = val.getText().toString();
                        model.setText(vh.model);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView speed = (TextView)top.findViewById(R.id.speed_text);
        speed.setText(String.valueOf(vh.speed));
        top.findViewById(R.id.speed_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.speed_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.speed);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.speed = 0;
                        else
                            vh.speed = Integer.parseInt(val.getText().toString());
                        speed.setText(vh.speed);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView silhouette = (TextView)top.findViewById(R.id.silhouette_text);
        silhouette.setText(String.valueOf(vh.silhouette));
        top.findViewById(R.id.silhouette_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.silhouette_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.silhouette);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int oldSilhouette = vh.silhouette;
                        if (val.getText().toString().equals(""))
                            vh.setSilhouette(0);
                        else
                            vh.setSilhouette(Integer.parseInt(val.getText().toString()));
                        silhouette.setText(vh.silhouette);
                        if ((oldSilhouette >4) != (vh.silhouette>4)){
                            if (vh.silhouette>4) {
                                ((TextView) top.findViewById(R.id.port_defense_text)).setText(String.valueOf(vh.defense[1]));
                                ((TextView) top.findViewById(R.id.starboard_defense_text)).setText(String.valueOf(vh.defense[2]));
                            }else {
                                ((TextView) top.findViewById(R.id.port_defense_text)).setText("-");
                                ((TextView) top.findViewById(R.id.starboard_defense_text)).setText("-");
                            }
                        }
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView handling = (TextView)top.findViewById(R.id.handling_text);
        handling.setText(String.valueOf(vh.handling));
        top.findViewById(R.id.handling_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.handling_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.handling);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.handling = 0;
                        else
                            vh.handling = Integer.parseInt(val.getText().toString());
                        handling.setText(vh.handling);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView armor = (TextView)top.findViewById(R.id.armor_text);
        armor.setText(String.valueOf(vh.armor));
        top.findViewById(R.id.armor_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.armor_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.armor);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.armor = 0;
                        else
                            vh.armor = Integer.parseInt(val.getText().toString());
                        armor.setText(vh.armor);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView hp = (TextView)top.findViewById(R.id.hp_text);
        hp.setText(String.valueOf(vh.hp));
        top.findViewById(R.id.hp_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.hard_points_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.hp);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.hp = 0;
                        else
                            vh.hp = Integer.parseInt(val.getText().toString());
                        hp.setText(vh.hp);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView passengerCapacity = (TextView)top.findViewById(R.id.passenger_capacity_text);
        passengerCapacity.setText(String.valueOf(vh.passengerCapacity));
        top.findViewById(R.id.passenger_capacity_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.passenger_capacity_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.passengerCapacity);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.passengerCapacity = 0;
                        else
                            vh.passengerCapacity = Integer.parseInt(val.getText().toString());
                        passengerCapacity.setText(vh.passengerCapacity);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView encumbranceCapacity = (TextView)top.findViewById(R.id.encumbrance_capacity_text);
        encumbranceCapacity.setText(String.valueOf(vh.encumCapacity));
        top.findViewById(R.id.encumbrance_capacity_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.encumbrance_capacity_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.encumCapacity);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.encumCapacity = 0;
                        else
                            vh.encumCapacity = Integer.parseInt(val.getText().toString());
                        encumbranceCapacity.setText(vh.encumCapacity);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
        //<editor-fold desc="defense_card">
        if (vh.silhouette > 4){
            if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
            else
                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
        }else{
            if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
            else
                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
        }
        final TextView totalDefense = (TextView)top.findViewById(R.id.total_defense_text);
        totalDefense.setText(String.valueOf(vh.totalDefense));
        top.findViewById(R.id.total_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.total_defense_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.totalDefense);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.totalDefense = 0;
                        else
                            vh.totalDefense = Integer.parseInt(val.getText().toString());
                        totalDefense.setText(vh.totalDefense);
                        if (vh.silhouette > 4){
                            if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }else{
                            if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView foreDefense = (TextView)top.findViewById(R.id.fore_defense_text);
        foreDefense.setText(String.valueOf(vh.defense[0]));
        top.findViewById(R.id.fore_defense_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.defense[0]!=-1){
                    if (vh.defense[0]!=0){
                        vh.defense[0]--;
                        foreDefense.setText(String.valueOf(vh.defense[0]));
                        if (vh.silhouette > 4){
                            if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }else{
                            if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        top.findViewById(R.id.fore_defense_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.defense[0]!=-1){
                    vh.defense[0]++;
                    foreDefense.setText(String.valueOf(vh.defense[0]));
                    if (vh.silhouette > 4){
                        if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                            top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                    }else{
                        if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                            top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                    }
                }
            }
        });
        final TextView portDefense = (TextView)top.findViewById(R.id.port_defense_text);
        portDefense.setText(String.valueOf(vh.defense[1]));
        top.findViewById(R.id.port_defense_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.defense[1]!=-1){
                    if (vh.defense[1]!=0){
                        vh.defense[1]--;
                        portDefense.setText(String.valueOf(vh.defense[1]));
                        if (vh.silhouette > 4){
                            if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }else{
                            if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        top.findViewById(R.id.port_defense_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.defense[1]!=-1){
                    vh.defense[1]++;
                    portDefense.setText(String.valueOf(vh.defense[1]));
                    if (vh.silhouette > 4){
                        if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                            top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                    }else{
                        if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                            top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                    }
                }
            }
        });
        final TextView starboardDefense = (TextView)top.findViewById(R.id.starboard_defense_text);
        starboardDefense.setText(String.valueOf(vh.defense[2]));
        top.findViewById(R.id.starboard_defense_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.defense[2]!=-1){
                    if (vh.defense[2]!=0){
                        vh.defense[2]--;
                        starboardDefense.setText(String.valueOf(vh.defense[2]));
                        if (vh.silhouette > 4){
                            if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }else{
                            if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        top.findViewById(R.id.starboard_defense_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.defense[2]!=-1){
                    vh.defense[2]++;
                    starboardDefense.setText(String.valueOf(vh.defense[2]));
                    if (vh.silhouette > 4){
                        if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                            top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                    }else{
                        if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                            top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                    }
                }
            }
        });
        final TextView aftDefense = (TextView)top.findViewById(R.id.aft_defense_text);
        aftDefense.setText(String.valueOf(vh.defense[3]));
        top.findViewById(R.id.aft_defense_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.defense[3]!=-1){
                    if (vh.defense[3]!=0){
                        vh.defense[3]--;
                        aftDefense.setText(String.valueOf(vh.defense[3]));
                        if (vh.silhouette > 4){
                            if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }else{
                            if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                                top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                            else
                                top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        top.findViewById(R.id.aft_defense_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.defense[3]!=-1){
                    vh.defense[3]++;
                    aftDefense.setText(String.valueOf(vh.defense[3]));
                    if (vh.silhouette > 4){
                        if (vh.defense[0]+vh.defense[1]+vh.defense[2]+vh.defense[3] != vh.totalDefense)
                            top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                    }else{
                        if (vh.defense[0]+vh.defense[3] != vh.totalDefense)
                            top.findViewById(R.id.defense_warning).setVisibility(View.VISIBLE);
                        else
                            top.findViewById(R.id.defense_warning).setVisibility(View.GONE);
                    }
                }
            }
        });
        //</editor-fold>
        //<editor-fold desc="damage_card">
        final TextView hullText = (TextView)top.findViewById(R.id.hull_trauma_text);
        hullText.setText(String.valueOf(vh.hullTraumaCur));
        top.findViewById(R.id.hull_trauma_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.hullTraumaCur > 0){
                    vh.hullTraumaCur--;
                    hullText.setText(String.valueOf(vh.hullTraumaCur));
                }
            }
        });
        top.findViewById(R.id.hull_trauma_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.hullTraumaCur <vh.hullTraumaThresh){
                    vh.hullTraumaCur++;
                    hullText.setText(String.valueOf(vh.hullTraumaCur));
                }
            }
        });
        top.findViewById(R.id.hull_trauma_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.hullTraumaCur = vh.hullTraumaThresh;
                hullText.setText(String.valueOf(vh.hullTraumaCur));
            }
        });
        top.findViewById(R.id.hull_trauma_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.hull_trauma_thresh_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.hullTraumaThresh);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.hullTraumaThresh = 0;
                        else
                            vh.hullTraumaThresh = Integer.parseInt(val.getText().toString());
                        if (vh.hullTraumaThresh < vh.hullTraumaCur){
                            vh.hullTraumaCur = vh.hullTraumaThresh;
                            hullText.setText(String.valueOf(vh.hullTraumaCur));
                        }
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView sysText = (TextView)top.findViewById(R.id.sys_stress_text);
        hullText.setText(String.valueOf(vh.sysStressCur));
        top.findViewById(R.id.sys_stress_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.sysStressCur > 0){
                    vh.sysStressCur--;
                    sysText.setText(String.valueOf(vh.sysStressCur));
                }
            }
        });
        top.findViewById(R.id.sys_stress_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.sysStressCur<vh.sysStressThresh){
                    vh.sysStressCur++;
                    sysText.setText(String.valueOf(vh.sysStressCur));
                }
            }
        });
        top.findViewById(R.id.sys_stress_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.sysStressCur = vh.sysStressThresh;
                sysText.setText(String.valueOf(vh.sysStressCur));
            }
        });
        top.findViewById(R.id.sys_stress_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.sys_stress_thresh_text);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.sysStressThresh);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.sysStressThresh = 0;
                        else
                            vh.sysStressThresh = Integer.parseInt(val.getText().toString());
                        if (vh.sysStressThresh < vh.sysStressCur){
                            vh.sysStressCur = vh.sysStressThresh;
                            sysText.setText(String.valueOf(vh.sysStressCur));
                        }
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
        //<editor-fold desc="Weapon_card">
        for (int i = 0;i<vh.weapons.size();i++){
            ((LinearLayout)top.findViewById(R.id.weapon_list_layout)).addView(new WeaponLayout().WeaponLayout(top.getContext(),
                    ((LinearLayout)top.findViewById(R.id.weapon_list_layout)),vh,vh.weapons.get(i)));
        }
        top.findViewById(R.id.weapon_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context main = top.getContext();
                final Weapon tmp = new Weapon();
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
                        vh.weapons.add(tmp);
                        ((LinearLayout)top.findViewById(R.id.weapon_list_layout)).addView(new WeaponLayout()
                                .WeaponLayout(main,((LinearLayout)top.findViewById(R.id.weapon_list_layout)),vh,vh.weapons.get(vh.weapons.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.weapon_edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="crit_inj_card">
        final LinearLayout critLay = (LinearLayout) top.findViewById(R.id.crit_inj_list_layout);
        for (int i = 0;i<vh.crits.size();i++)
            critLay.addView(new CritInjLayout().CritInjLayout(top.getContext(),critLay,vh,vh.crits.get(i)));
        top.findViewById(R.id.crit_inj_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CriticalInjury tmp = new CriticalInjury();
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_critical_injury_edit);
                final EditText name = (EditText)dia.findViewById(R.id.crit_name);
                name.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.crit_desc);
                desc.setText(tmp.desc);
                final Spinner sev = (Spinner)dia.findViewById(R.id.crit_severity);
                ArrayAdapter<CharSequence> sevAdap = ArrayAdapter.createFromResource(top.getContext(),R.array.crit_injury_severities,R.layout.spinner_base);
                sev.setAdapter(sevAdap);
                sev.setSelection(tmp.severity);
                dia.findViewById(R.id.crit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.name = name.getText().toString();
                        tmp.desc = desc.getText().toString();
                        tmp.severity = sev.getSelectedItemPosition();
                        vh.crits.add(tmp);
                        critLay.addView(new CritInjLayout().CritInjLayout(top.getContext(),critLay,vh,
                                vh.crits.get(vh.crits.size()-1)));
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.crit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
            }
        });
        //</editor-fold>
        //<editor-fold desc="desc_card">
        ((TextView)top.findViewById(R.id.desc_main)).setText(vh.desc);
        top.findViewById(R.id.desc_card).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.description_text);
                final EditText desc = (EditText)dia.findViewById(R.id.edit_val);
                desc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                desc.setText(vh.desc);
                desc.setSingleLine(false);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vh.desc = desc.getText().toString();
                        ((TextView)top.findViewById(R.id.desc_main)).setText(vh.desc);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        //</editor-fold>
    }
}
