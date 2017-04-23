package com.apps.darkstorm.swrpg.assistant.dice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.R;

public class DiceResults {

    public int success,failure,advantage,threat,triumph,despair,lightSide,darkSide;

    public void showDialog(final Activity ac){
        DiceResults simp = simplify();
        AlertDialog.Builder build = new AlertDialog.Builder(ac);
        @SuppressLint("InflateParams") View v = ac.getLayoutInflater().inflate(R.layout.dialog_dice_results,null);
        build.setView(v);
        boolean res = false;
        if (simp.success>0){
            res = true;
            v.findViewById(R.id.success_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.success_num)).setText(String.valueOf(simp.success));
        }else if (simp.failure>0){
            res = true;
            v.findViewById(R.id.success_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.success_label)).setText(ac.getString(R.string.failure));
            ((TextView)v.findViewById(R.id.success_num)).setText(String.valueOf(simp.failure));
        }
        if (simp.advantage>0){
            res = true;
            v.findViewById(R.id.advantage_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.advantage_num)).setText(String.valueOf(simp.advantage));
        }else if (simp.threat>0){
            res = true;
            v.findViewById(R.id.advantage_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.advantage_label)).setText(ac.getString(R.string.threat_text));
            ((TextView)v.findViewById(R.id.advantage_num)).setText(String.valueOf(simp.threat));
        }
        if (simp.triumph>0){
            res = true;
            v.findViewById(R.id.triumph_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.triumph_num)).setText(String.valueOf(simp.triumph));
        }
        if (simp.despair>0){
            res = true;
            v.findViewById(R.id.despair_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.despair_num)).setText(String.valueOf(simp.despair));
        }
        if (simp.lightSide>0){
            res = true;
            v.findViewById(R.id.light_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.light_num)).setText(String.valueOf(simp.lightSide));
        }
        if (simp.darkSide>0){
            res = true;
            v.findViewById(R.id.dark_lay).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.dark_num)).setText(String.valueOf(simp.darkSide));
        }
        if (!res){
            v.findViewById(R.id.no_res_text).setVisibility(View.VISIBLE);
        }
        build.setNegativeButton(R.string.modify_results, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showEditDialog(ac);
                dialog.cancel();
            }
        });
        build.show();
    }

    public void showEditDialog(final Activity ac){
        AlertDialog.Builder build = new AlertDialog.Builder(ac);
        @SuppressLint("InflateParams") View v = ac.getLayoutInflater().inflate(R.layout.dialog_results_modify,null);
        build.setView(v);
        final TextView successNum = (TextView)v.findViewById(R.id.success_num);
        final TextView failureNum = (TextView)v.findViewById(R.id.failure_num);
        successNum.setText(String.valueOf(success));
        v.findViewById(R.id.success_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                success++;
                successNum.setText(String.valueOf(success));
            }
        });
        v.findViewById(R.id.success_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (success >0){
                    success--;
                    successNum.setText(String.valueOf(success));
                }else if (success == 0){
                    failure++;
                    failureNum.setText(String.valueOf(failure));
                }
            }
        });
        failureNum.setText(String.valueOf(failure));
        v.findViewById(R.id.failure_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failure++;
                failureNum.setText(String.valueOf(failure));
            }
        });
        v.findViewById(R.id.failure_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (failure >0){
                    failure--;
                    failureNum.setText(String.valueOf(failure));
                }else if (failure == 0){
                    success++;
                    successNum.setText(String.valueOf(success));
                }
            }
        });
        final TextView advantageNum = (TextView)v.findViewById(R.id.advantage_num);
        final TextView threatNum = (TextView)v.findViewById(R.id.threat_num);
        advantageNum.setText(String.valueOf(advantage));
        v.findViewById(R.id.advantage_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advantage++;
                advantageNum.setText(String.valueOf(advantage));
            }
        });
        v.findViewById(R.id.advantage_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advantage >0){
                    advantage--;
                    advantageNum.setText(String.valueOf(advantage));
                }else if (advantage == 0){
                    threat++;
                    threatNum.setText(String.valueOf(threat));
                }
            }
        });
        threatNum.setText(String.valueOf(threat));
        v.findViewById(R.id.threat_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threat++;
                threatNum.setText(String.valueOf(threat));
            }
        });
        v.findViewById(R.id.threat_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (threat >0){
                    threat--;
                    threatNum.setText(String.valueOf(threat));
                }else if (threat == 0){
                    advantage++;
                    advantageNum.setText(String.valueOf(advantage));
                }
            }
        });
        final TextView triumphNum = (TextView)v.findViewById(R.id.triumph_num);
        triumphNum.setText(String.valueOf(triumph));
        v.findViewById(R.id.triumph_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triumph++;
                triumphNum.setText(String.valueOf(triumph));
            }
        });
        v.findViewById(R.id.triumph_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (triumph >0){
                    triumph--;
                    triumphNum.setText(String.valueOf(triumph));
                }
            }
        });
        final TextView despairNum = (TextView)v.findViewById(R.id.despair_num);
        despairNum.setText(String.valueOf(despair));
        v.findViewById(R.id.despair_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                despair++;
                despairNum.setText(String.valueOf(despair));
            }
        });
        v.findViewById(R.id.despair_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (despair >0){
                    despair--;
                    despairNum.setText(String.valueOf(despair));
                }
            }
        });
        final TextView lightSideNum = (TextView)v.findViewById(R.id.light_side_num);
        lightSideNum.setText(String.valueOf(lightSide));
        v.findViewById(R.id.light_side_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightSide++;
                lightSideNum.setText(String.valueOf(lightSide));
            }
        });
        v.findViewById(R.id.light_side_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightSide >0){
                    lightSide--;
                    lightSideNum.setText(String.valueOf(lightSide));
                }
            }
        });
        final TextView darkSideNum = (TextView)v.findViewById(R.id.dark_side_num);
        darkSideNum.setText(String.valueOf(darkSide));
        v.findViewById(R.id.dark_side_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkSide++;
                darkSideNum.setText(String.valueOf(darkSide));
            }
        });
        v.findViewById(R.id.dark_side_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (darkSide >0){
                    darkSide--;
                    darkSideNum.setText(String.valueOf(darkSide));
                }
            }
        });
        build.setPositiveButton(R.string.re_show, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog(ac);
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

    public DiceResults simplify(){
        DiceResults hold = this.clone();
        if (success>failure){
            hold.success = success - failure;
            hold.failure = 0;
        }else{
            hold.failure = failure-success;
            hold.success = 0;
        }
        if (advantage>threat){
            hold.advantage = advantage-threat;
            hold.threat = 0;
        }else{
            hold.threat = threat-advantage;
            hold.advantage = 0;
        }
        return hold;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    public DiceResults clone(){
        DiceResults out = new DiceResults();
        out.success = success;
        out.failure = failure;
        out.advantage = advantage;
        out.threat = threat;
        out.triumph = triumph;
        out.despair = despair;
        out.lightSide = lightSide;
        out.darkSide = darkSide;
        return out;
    }
}
