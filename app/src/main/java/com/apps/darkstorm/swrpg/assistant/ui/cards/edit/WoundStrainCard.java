package com.apps.darkstorm.swrpg.assistant.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.apps.darkstorm.swrpg.assistant.R;

public class WoundStrainCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_wound_strain_character,root,false);
        ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(chara.soak));
        top.findViewById(R.id.soak_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.soak_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.soak));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            chara.soak = Integer.parseInt(val.getText().toString());
                            ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(chara.soak));
                        }else{
                            chara.soak = 0;
                            ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(chara.soak));
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
        final TextView woundVal = (TextView)top.findViewById(R.id.wound_text);
        woundVal.setText(String.valueOf(chara.woundCur));
        final TextView strainVal = (TextView)top.findViewById(R.id.strain_text);
        strainVal.setText(String.valueOf(chara.strainCur));
        top.findViewById(R.id.wound_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (chara.woundCur >0 ){
                    chara.woundCur--;
                    woundVal.setText(String.valueOf(chara.woundCur));
                }
            }
        });
        top.findViewById(R.id.wound_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chara.woundCur < chara.woundThresh){
                    chara.woundCur++;
                    woundVal.setText(String.valueOf(chara.woundCur));
                }
            }
        });
        top.findViewById(R.id.wound_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chara.woundCur = chara.woundThresh;
                woundVal.setText(String.valueOf(chara.woundCur));
            }
        });
        top.findViewById(R.id.wound_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.wound_thresh_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.woundThresh));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            chara.woundThresh = Integer.parseInt(val.getText().toString());
                            if (chara.woundThresh < chara.woundCur){
                                chara.woundCur = chara.woundThresh;
                                woundVal.setText(String.valueOf(chara.woundCur));
                            }
                        }else{
                            chara.woundThresh = 0;
                            chara.woundCur = chara.woundThresh;
                            woundVal.setText(String.valueOf(chara.woundCur));
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
        top.findViewById(R.id.strain_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (chara.strainCur >0 ){
                    chara.strainCur--;
                    strainVal.setText(String.valueOf(chara.strainCur));
                }
            }
        });
        top.findViewById(R.id.strain_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chara.strainCur < chara.strainThresh){
                    chara.strainCur++;
                    strainVal.setText(String.valueOf(chara.strainCur));
                }
            }
        });
        top.findViewById(R.id.strain_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chara.strainCur = chara.strainThresh;
                strainVal.setText(String.valueOf(chara.strainCur));
            }
        });
        top.findViewById(R.id.strain_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.strain_thresh_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(chara.strainThresh));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            chara.strainThresh = Integer.parseInt(val.getText().toString());
                            if (chara.strainThresh < chara.strainCur){
                                chara.strainCur = chara.strainThresh;
                                strainVal.setText(String.valueOf(chara.strainCur));
                            }
                        }else{
                            chara.strainThresh = 0;
                            chara.strainCur = chara.strainThresh;
                            strainVal.setText(String.valueOf(chara.strainCur));
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
        return top;
    }
    public static View getCard(final Activity main, ViewGroup root, final Vehicle vh){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_wound_strain_vehicle,root,false);
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
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.hull_trauma_thresh_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.hullTraumaThresh));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (val.getText().toString().equals(""))
                            vh.hullTraumaThresh = 0;
                        else
                            vh.hullTraumaThresh = Integer.parseInt(val.getText().toString());
                        if (vh.hullTraumaThresh < vh.hullTraumaCur){
                            vh.hullTraumaCur = vh.hullTraumaThresh;
                            hullText.setText(String.valueOf(vh.hullTraumaCur));
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
        final TextView sysText = (TextView)top.findViewById(R.id.sys_stress_text);
        sysText.setText(String.valueOf(vh.sysStressCur));
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
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.sys_stress_thresh_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText(String.valueOf(vh.sysStressThresh));
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (val.getText().toString().equals(""))
                            vh.sysStressThresh = 0;
                        else
                            vh.sysStressThresh = Integer.parseInt(val.getText().toString());
                        if (vh.sysStressThresh < vh.sysStressCur){
                            vh.sysStressCur = vh.sysStressThresh;
                            sysText.setText(String.valueOf(vh.sysStressCur));
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
        return top;
    }
}
