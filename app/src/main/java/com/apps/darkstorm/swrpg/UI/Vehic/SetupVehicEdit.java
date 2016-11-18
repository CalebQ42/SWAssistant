package com.apps.darkstorm.swrpg.UI.Vehic;

import android.app.Dialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.Vehicle;

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
        //<editor-fold desc="">
        //</editor-fold>
        //<editor-fold desc="">
        //</editor-fold>
        //<editor-fold desc="">
        //</editor-fold>
    }
}
