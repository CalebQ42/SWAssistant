package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Vehicle;

public class VehicleInfoCard {
    public static View getCard(final Activity main, final ViewGroup root, final Vehicle vh){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_vehicle_info,root,false);
        final TextView model = (TextView)top.findViewById(R.id.model_text);
        model.setText(vh.model);
        top.findViewById(R.id.model_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.model_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(vh.model);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vh.model = val.getText().toString();
                        model.setText(vh.model);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView speed = (TextView)top.findViewById(R.id.speed_text);
        speed.setText(String.valueOf(vh.speed));
        top.findViewById(R.id.speed_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.speed_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.speed));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (val.getText().toString().equals(""))
                            vh.speed = 0;
                        else
                            vh.speed = Integer.parseInt(val.getText().toString());
                        speed.setText(String.valueOf(vh.speed));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView silhouette = (TextView)top.findViewById(R.id.silhouette_text);
        silhouette.setText(String.valueOf(vh.silhouette));
        top.findViewById(R.id.silhouette_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.silhouette_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.silhouette));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int oldSilhouette = vh.silhouette;
                        if (val.getText().toString().equals(""))
                            vh.setSilhouette(0);
                        else
                            vh.setSilhouette(Integer.parseInt(val.getText().toString()));
                        silhouette.setText(String.valueOf(vh.silhouette));
                        if ((oldSilhouette >4) != (vh.silhouette>4)){
                            if (vh.silhouette>4) {
                                ((TextView) root.findViewById(R.id.port_defense_text)).setText(String.valueOf(vh.defense[1]));
                                ((TextView) root.findViewById(R.id.starboard_defense_text)).setText(String.valueOf(vh.defense[2]));
                            }else {
                                ((TextView) root.findViewById(R.id.port_defense_text)).setText("-");
                                ((TextView) root.findViewById(R.id.starboard_defense_text)).setText("-");
                            }
                        }
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView handling = (TextView)top.findViewById(R.id.handling_text);
        handling.setText(String.valueOf(vh.handling));
        top.findViewById(R.id.handling_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.handling_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.handling));
                val.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (val.getText().toString().equals(""))
                            vh.handling = 0;
                        else
                            vh.handling = Integer.parseInt(val.getText().toString());
                        handling.setText(String.valueOf(vh.handling));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView armor = (TextView)top.findViewById(R.id.armor_text);
        armor.setText(String.valueOf(vh.armor));
        top.findViewById(R.id.armor_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.armor_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.armor));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (val.getText().toString().equals(""))
                            vh.armor = 0;
                        else
                            vh.armor = Integer.parseInt(val.getText().toString());
                        armor.setText(String.valueOf(vh.armor));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView hp = (TextView)top.findViewById(R.id.hp_text);
        hp.setText(String.valueOf(vh.hp));
        top.findViewById(R.id.hp_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.hard_points_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.hp));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (val.getText().toString().equals(""))
                            vh.hp = 0;
                        else
                            vh.hp = Integer.parseInt(val.getText().toString());
                        hp.setText(String.valueOf(vh.hp));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView passengerCapacity = (TextView)top.findViewById(R.id.passenger_capacity_text);
        passengerCapacity.setText(String.valueOf(vh.passengerCapacity));
        top.findViewById(R.id.passenger_capacity_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.passenger_capacity_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.passengerCapacity));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (val.getText().toString().equals(""))
                            vh.passengerCapacity = 0;
                        else
                            vh.passengerCapacity = Integer.parseInt(val.getText().toString());
                        passengerCapacity.setText(String.valueOf(vh.passengerCapacity));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView encumbranceCapacity = (TextView)top.findViewById(R.id.encumbrance_capacity_text);
        encumbranceCapacity.setText(String.valueOf(vh.encumCapacity));
        top.findViewById(R.id.encumbrance_capacity_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.encumbrance_capacity_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.encumCapacity));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (val.getText().toString().equals(""))
                            vh.encumCapacity = 0;
                        else
                            vh.encumCapacity = Integer.parseInt(val.getText().toString());
                        encumbranceCapacity.setText(String.valueOf(vh.encumCapacity));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        return top;
    }
}
