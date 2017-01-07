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
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.sw.Vehicle;

public class DefenseCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_defense_character,root);
        final TextView defMelee = (TextView)top.findViewById(R.id.melee_defense_text);
        defMelee.setText(String.valueOf(chara.defMelee));
        top.findViewById(R.id.melee_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.melee_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.defMelee));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            chara.defMelee = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.defMelee = 0;
                        }
                        defMelee.setText(String.valueOf(chara.defMelee));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView defranged = (TextView)top.findViewById(R.id.ranged_defense_text);
        defranged.setText(String.valueOf(chara.defRanged));
        top.findViewById(R.id.ranged_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.ranged_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.defRanged));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            chara.defRanged = Integer.parseInt(val.getText().toString());
                        }else{
                            chara.defRanged = 0;
                        }
                        defranged.setText(String.valueOf(chara.defRanged));
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
    public static View getCard(final Activity main, ViewGroup root, final Minion minion){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_defense_character,root);
        final TextView defMelee = (TextView)top.findViewById(R.id.melee_defense_text);
        defMelee.setText(String.valueOf(minion.defMelee));
        top.findViewById(R.id.melee_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.melee_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.defMelee));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            minion.defMelee = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.defMelee = 0;
                        }
                        defMelee.setText(String.valueOf(minion.defMelee));
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                build.show();
                return true;
            }
        });
        final TextView defranged = (TextView)top.findViewById(R.id.ranged_defense_text);
        defranged.setText(String.valueOf(minion.defRanged));
        top.findViewById(R.id.ranged_defense_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                final View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.ranged_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.defRanged));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (!val.getText().toString().equals("")){
                            minion.defRanged = Integer.parseInt(val.getText().toString());
                        }else{
                            minion.defRanged = 0;
                        }
                        defranged.setText(String.valueOf(minion.defRanged));
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
    public static View getCard(final Activity main, ViewGroup root, final Vehicle vh){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_defense_vehicle,root);
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
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.total_defense_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.totalDefense));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (val.getText().toString().equals(""))
                            vh.totalDefense = 0;
                        else
                            vh.totalDefense = Integer.parseInt(val.getText().toString());
                        totalDefense.setText(String.valueOf(vh.totalDefense));
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
        if (vh.defense[1] != -1)
            portDefense.setText(String.valueOf(vh.defense[1]));
        else
            portDefense.setText("-");
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
        if (vh.defense[2] != -1)
            starboardDefense.setText(String.valueOf(vh.defense[2]));
        else
            starboardDefense.setText("-");
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
        return top;
    }
}
