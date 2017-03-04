package com.apps.darkstorm.swrpg.assistant.dice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.R;

public class DiceResults {
    public int success;
    public int fail;
    public int advantage;
    public int threat;
    public int triumph;
    public int despair;
    public int lt;
    public int dk;
    public void showDialog(final Activity main){
        if(main!=null) {
            AlertDialog.Builder build = new AlertDialog.Builder(main);
            LayoutInflater inflate = main.getLayoutInflater();
            View dia = inflate.inflate(R.layout.dialog_dice_results, null);
            build.setView(dia);
            boolean shown = false;
            if (success - fail != 0) {
                shown = true;
                dia.findViewById(R.id.success_fail_layout).setVisibility(View.VISIBLE);
                ((TextView) dia.findViewById(R.id.success_fail_val)).setText(String.valueOf(Math.abs(success - fail)));
                if (success - fail > 0) {
                    ((TextView) dia.findViewById(R.id.success_fail_desc)).setText(R.string.success_text);
                } else {
                    ((TextView) dia.findViewById(R.id.success_fail_desc)).setText(R.string.fail_text);
                }
            }
            if (advantage - threat != 0) {
                shown = true;
                dia.findViewById(R.id.adv_thr_layout).setVisibility(View.VISIBLE);
                ((TextView) dia.findViewById(R.id.adv_thr_val)).setText(String.valueOf(Math.abs(advantage - threat)));
                if (advantage - threat > 0) {
                    ((TextView) dia.findViewById(R.id.adv_thr_desc)).setText(R.string.advantage_text);
                } else {
                    ((TextView) dia.findViewById(R.id.adv_thr_desc)).setText(R.string.threat_text);
                }
            }
            if (triumph > 0) {
                shown = true;
                dia.findViewById(R.id.triumph_layout).setVisibility(View.VISIBLE);
                ((TextView) dia.findViewById(R.id.triumph_val)).setText(String.valueOf(triumph));
            }
            if (despair > 0) {
                shown = true;
                dia.findViewById(R.id.despair_layout).setVisibility(View.VISIBLE);
                ((TextView) dia.findViewById(R.id.despair_val)).setText(String.valueOf(despair));
            }
            if (lt > 0) {
                shown = true;
                dia.findViewById(R.id.light_points_layout).setVisibility(View.VISIBLE);
                ((TextView) dia.findViewById(R.id.light_points_val)).setText(String.valueOf(lt));
            }
            if (dk > 0) {
                shown = true;
                dia.findViewById(R.id.dark_points_layout).setVisibility(View.VISIBLE);
                ((TextView) dia.findViewById(R.id.dark_points_val)).setText(String.valueOf(dk));
            }
            if (!shown) {
                dia.findViewById(R.id.no_res_text).setVisibility(View.VISIBLE);
            }
            build.setNeutralButton(R.string.modify_results, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    modifyResults(main);
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
    }
    public void modifyResults(final Activity main){
        if(main!=null) {
            AlertDialog.Builder build = new AlertDialog.Builder(main);
            View dia = main.getLayoutInflater().inflate(R.layout.dialog_modify_results,null);
            build.setView(dia);
            final TextView successVal = (TextView)dia.findViewById(R.id.success_value);
            successVal.setText(String.valueOf(success));
            dia.findViewById(R.id.success_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(success!=0){
                        success--;
                        successVal.setText(String.valueOf(success));
                    }
                }
            });
            dia.findViewById(R.id.success_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    success++;
                    successVal.setText(String.valueOf(success));
                }
            });
            final TextView failVal = (TextView)dia.findViewById(R.id.fail_value);
            failVal.setText(String.valueOf(fail));
            dia.findViewById(R.id.fail_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fail!=0){
                        fail--;
                        failVal.setText(String.valueOf(fail));
                    }
                }
            });
            dia.findViewById(R.id.fail_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fail++;
                    failVal.setText(String.valueOf(fail));
                }
            });
            final TextView advantageVal = (TextView)dia.findViewById(R.id.advantage_value);
            advantageVal.setText(String.valueOf(advantage));
            dia.findViewById(R.id.advantage_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(advantage!=0){
                        advantage--;
                        advantageVal.setText(String.valueOf(advantage));
                    }
                }
            });
            dia.findViewById(R.id.advantage_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    advantage++;
                    advantageVal.setText(String.valueOf(advantage));
                }
            });
            final TextView threatVal = (TextView)dia.findViewById(R.id.threat_value);
            threatVal.setText(String.valueOf(threat));
            dia.findViewById(R.id.threat_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(threat!=0){
                        threat--;
                        threatVal.setText(String.valueOf(threat));
                    }
                }
            });
            dia.findViewById(R.id.threat_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    threat++;
                    threatVal.setText(String.valueOf(threat));
                }
            });
            final TextView triumphVal = (TextView)dia.findViewById(R.id.triumph_value);
            triumphVal.setText(String.valueOf(triumph));
            dia.findViewById(R.id.triumph_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(triumph!=0){
                        triumph--;
                        triumphVal.setText(String.valueOf(triumph));
                    }
                }
            });
            dia.findViewById(R.id.triumph_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    triumph++;
                    triumphVal.setText(String.valueOf(triumph));
                }
            });
            final TextView despairVal = (TextView)dia.findViewById(R.id.despair_value);
            despairVal.setText(String.valueOf(despair));
            dia.findViewById(R.id.despair_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(despair!=0){
                        despair--;
                        despairVal.setText(String.valueOf(despair));
                    }
                }
            });
            dia.findViewById(R.id.despair_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    despair++;
                    despairVal.setText(String.valueOf(despair));
                }
            });
            final TextView ltVal = (TextView)dia.findViewById(R.id.lt_value);
            ltVal.setText(String.valueOf(lt));
            dia.findViewById(R.id.lt_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lt!=0){
                        lt--;
                        ltVal.setText(String.valueOf(lt));
                    }
                }
            });
            dia.findViewById(R.id.lt_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lt++;
                    ltVal.setText(String.valueOf(lt));
                }
            });
            final TextView dkVal = (TextView)dia.findViewById(R.id.dk_value);
            dkVal.setText(String.valueOf(dk));
            dia.findViewById(R.id.dk_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dk!=0){
                        dk--;
                        dkVal.setText(String.valueOf(dk));
                    }
                }
            });
            dia.findViewById(R.id.dk_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dk++;
                    dkVal.setText(String.valueOf(dk));
                }
            });
            build.setPositiveButton(R.string.re_show, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDialog(main);
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
    }
    public static void modifyResults(final DiceResults dr, final Activity main, DialogInterface.OnClickListener show){
        if(main!=null) {
            AlertDialog.Builder build = new AlertDialog.Builder(main);
            View dia = main.getLayoutInflater().inflate(R.layout.dialog_modify_results,null);
            build.setView(dia);
            final TextView successVal = (TextView)dia.findViewById(R.id.success_value);
            successVal.setText(String.valueOf(dr.success));
            dia.findViewById(R.id.success_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dr.success!=0){
                        dr.success--;
                        successVal.setText(String.valueOf(dr.success));
                    }
                }
            });
            dia.findViewById(R.id.success_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr.success++;
                    successVal.setText(String.valueOf(dr.success));
                }
            });
            final TextView failVal = (TextView)dia.findViewById(R.id.fail_value);
            failVal.setText(String.valueOf(dr.fail));
            dia.findViewById(R.id.fail_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dr.fail!=0){
                        dr.fail--;
                        failVal.setText(String.valueOf(dr.fail));
                    }
                }
            });
            dia.findViewById(R.id.fail_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr.fail++;
                    failVal.setText(String.valueOf(dr.fail));
                }
            });
            final TextView advantageVal = (TextView)dia.findViewById(R.id.advantage_value);
            advantageVal.setText(String.valueOf(dr.advantage));
            dia.findViewById(R.id.advantage_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dr.advantage!=0){
                        dr.advantage--;
                        advantageVal.setText(String.valueOf(dr.advantage));
                    }
                }
            });
            dia.findViewById(R.id.advantage_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr.advantage++;
                    advantageVal.setText(String.valueOf(dr.advantage));
                }
            });
            final TextView threatVal = (TextView)dia.findViewById(R.id.threat_value);
            threatVal.setText(String.valueOf(dr.threat));
            dia.findViewById(R.id.threat_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dr.threat!=0){
                        dr.threat--;
                        threatVal.setText(String.valueOf(dr.threat));
                    }
                }
            });
            dia.findViewById(R.id.threat_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr.threat++;
                    threatVal.setText(String.valueOf(dr.threat));
                }
            });
            final TextView triumphVal = (TextView)dia.findViewById(R.id.triumph_value);
            triumphVal.setText(String.valueOf(dr.triumph));
            dia.findViewById(R.id.triumph_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dr.triumph!=0){
                        dr.triumph--;
                        triumphVal.setText(String.valueOf(dr.triumph));
                    }
                }
            });
            dia.findViewById(R.id.triumph_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr.triumph++;
                    triumphVal.setText(String.valueOf(dr.triumph));
                }
            });
            final TextView despairVal = (TextView)dia.findViewById(R.id.despair_value);
            despairVal.setText(String.valueOf(dr.despair));
            dia.findViewById(R.id.despair_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dr.despair!=0){
                        dr.despair--;
                        despairVal.setText(String.valueOf(dr.despair));
                    }
                }
            });
            dia.findViewById(R.id.despair_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr.despair++;
                    despairVal.setText(String.valueOf(dr.despair));
                }
            });
            final TextView ltVal = (TextView)dia.findViewById(R.id.lt_value);
            ltVal.setText(String.valueOf(dr.lt));
            dia.findViewById(R.id.lt_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dr.lt!=0){
                        dr.lt--;
                        ltVal.setText(String.valueOf(dr.lt));
                    }
                }
            });
            dia.findViewById(R.id.lt_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr.lt++;
                    ltVal.setText(String.valueOf(dr.lt));
                }
            });
            final TextView dkVal = (TextView)dia.findViewById(R.id.dk_value);
            dkVal.setText(String.valueOf(dr.dk));
            dia.findViewById(R.id.dk_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dr.dk!=0){
                        dr.dk--;
                        dkVal.setText(String.valueOf(dr.dk));
                    }
                }
            });
            dia.findViewById(R.id.dk_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dr.dk++;
                    dkVal.setText(String.valueOf(dr.dk));
                }
            });
            build.setPositiveButton(R.string.re_show, show)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
    }
}
