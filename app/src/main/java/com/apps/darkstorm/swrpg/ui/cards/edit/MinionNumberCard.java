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
import com.apps.darkstorm.swrpg.sw.Minion;

public class MinionNumberCard {
    public static View getCard(final Activity main, ViewGroup root, final Minion minion){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_minion_number,root,false);
        final TextView woundVal = (TextView)top.findViewById(R.id.wound_text);
        final TextView minNum = (TextView)top.findViewById(R.id.min_num_text);
        minNum.setText(String.valueOf(minion.getMinNum()));
        top.findViewById(R.id.min_num_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.setMinNum(minion.getMinNum()+1);
                minNum.setText(String.valueOf(minion.getMinNum()));
                woundVal.setText(String.valueOf(minion.getWound()));
            }
        });
        top.findViewById(R.id.min_num_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minion.getMinNum() > 0) {
                    minion.setMinNum(minion.getMinNum() - 1);
                    minNum.setText(String.valueOf(minion.getMinNum()));
                    woundVal.setText(String.valueOf(minion.getWound()));
                }
            }
        });
        top.findViewById(R.id.min_num_five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.setMinNum(minion.getMinNum()+5);
                minNum.setText(String.valueOf(minion.getMinNum()));
                woundVal.setText(String.valueOf(minion.getWound()));
            }
        });
        top.findViewById(R.id.min_num_ten).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.setMinNum(minion.getMinNum()+10);
                minNum.setText(String.valueOf(minion.getMinNum()));
                woundVal.setText(String.valueOf(minion.getWound()));
            }
        });
        ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(minion.soak));
        top.findViewById(R.id.soak_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.soak_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.soak));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            minion.soak = Integer.parseInt(val.getText().toString());
                            ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(minion.soak));
                        }else{
                            minion.soak = 0;
                            ((TextView)top.findViewById(R.id.soak_text)).setText(String.valueOf(minion.soak));
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
        woundVal.setText(String.valueOf(minion.getWound()));
        top.findViewById(R.id.wound_minus).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (minion.getWound() >0 ){
                    minion.setWound(minion.getWound()-1);
                    woundVal.setText(String.valueOf(minion.getWound()));
                    minNum.setText(String.valueOf(minion.getMinNum()));
                }
            }
        });
        top.findViewById(R.id.wound_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minion.getWound() < minion.woundThresh){
                    minion.setWound(minion.getWound()+1);
                    woundVal.setText(String.valueOf(minion.getWound()));
                    minNum.setText(String.valueOf(minion.getMinNum()));
                }
            }
        });
        top.findViewById(R.id.wound_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minion.setWound(minion.woundThresh);
                woundVal.setText(String.valueOf(minion.getWound()));
            }
        });
        ((TextView)top.findViewById(R.id.wound_ind_text)).setText(String.valueOf(minion.getWoundInd()));
        top.findViewById(R.id.wound_ind_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.wound_ind_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                val.setText(String.valueOf(minion.getWoundInd()));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!val.getText().toString().equals("")){
                            minion.setWoundInd(Integer.parseInt(val.getText().toString()));
                            ((TextView)top.findViewById(R.id.wound_ind_text)).setText(String.valueOf(minion.getMinNum()));
                            woundVal.setText(String.valueOf(minion.getWound()));
                        }else{
                            minion.setWoundInd(0);
                            ((TextView)top.findViewById(R.id.wound_ind_text)).setText(String.valueOf(minion.getMinNum()));
                            woundVal.setText(String.valueOf(minion.getWound()));
                        }
                        if (minion.getWound()>minion.woundThresh){
                            minion.setWound(minion.woundThresh);
                            woundVal.setText(String.valueOf(minion.getWound()));
                        }
                        ((TextView)top.findViewById(R.id.wound_ind_text)).setText(String.valueOf(minion.getWoundInd()));
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
        return top;
    }
}
