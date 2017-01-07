package com.apps.darkstorm.swrpg.dice;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;

public class DiceResults {
    public int suc;
    public int fail;
    public int adv;
    public int thr;
    public int tri;
    public int desp;
    public int lt;
    public int dk;
    public void showDialog(Activity main){
        AlertDialog.Builder build = new AlertDialog.Builder(main);
        LayoutInflater inflate = main.getLayoutInflater();
        View dia = inflate.inflate(R.layout.dialog_dice_results,null);
        build.setView(dia);
        boolean shown = false;
        if (suc-fail != 0){
            shown = true;
            dia.findViewById(R.id.success_fail_layout).setVisibility(View.VISIBLE);
            ((TextView)dia.findViewById(R.id.success_fail_val)).setText(String.valueOf(Math.abs(suc-fail)));
            if (suc-fail >0){
                ((TextView)dia.findViewById(R.id.success_fail_desc)).setText(R.string.success_text);
            }else{
                ((TextView)dia.findViewById(R.id.success_fail_desc)).setText(R.string.fail_text);
            }
        }
        if (adv-thr != 0){
            shown = true;
            dia.findViewById(R.id.adv_thr_layout).setVisibility(View.VISIBLE);
            ((TextView)dia.findViewById(R.id.adv_thr_val)).setText(String.valueOf(Math.abs(adv-thr)));
            if (adv-thr>0){
                ((TextView)dia.findViewById(R.id.adv_thr_desc)).setText(R.string.advantage_text);
            }else{
                ((TextView)dia.findViewById(R.id.adv_thr_desc)).setText(R.string.threat_text);
            }
        }
        if (tri > 0){
            shown = true;
            dia.findViewById(R.id.triumph_layout).setVisibility(View.VISIBLE);
            ((TextView)dia.findViewById(R.id.triumph_val)).setText(String.valueOf(tri));
        }
        if (desp >0){
            shown = true;
            dia.findViewById(R.id.despair_layout).setVisibility(View.VISIBLE);
            ((TextView)dia.findViewById(R.id.despair_val)).setText(String.valueOf(desp));
        }
        if (lt >0){
            shown = true;
            dia.findViewById(R.id.light_points_layout).setVisibility(View.VISIBLE);
            ((TextView)dia.findViewById(R.id.light_points_val)).setText(String.valueOf(lt));
        }
        if (dk>0){
            shown = true;
            dia.findViewById(R.id.dark_points_layout).setVisibility(View.VISIBLE);
            ((TextView)dia.findViewById(R.id.dark_points_val)).setText(String.valueOf(dk));
        }
        if (!shown){
            dia.findViewById(R.id.no_res_text).setVisibility(View.VISIBLE);
        }
        build.show();
    }
}
