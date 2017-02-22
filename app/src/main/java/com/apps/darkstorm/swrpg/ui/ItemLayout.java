package com.apps.darkstorm.swrpg.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.sw.stuff.Item;

public class ItemLayout {
    public LinearLayout ItemLayout(final View toppest, final Activity main, final LinearLayout invLay, final Character chara, final Item it){
        LinearLayout top = new LinearLayout(main);
        LinearLayout.LayoutParams toplp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        toplp.setMargins(4,4,4,4);
        toplp.setMarginEnd(4);
        toplp.setMarginStart(4);
        top.setLayoutParams(toplp);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless,outVal,true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            top.setForeground(main.getDrawable(outVal.resourceId));
        else
            top.setBackgroundResource(outVal.resourceId);
        final TextView name = new TextView(main);
        LinearLayout.LayoutParams namelp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        name.setText(it.name);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_item_edit,null);
                build.setView(dia);
                final EditText item = (EditText)dia.findViewById(R.id.item_name);
                item.setText(it.name);
                final EditText desc = (EditText)dia.findViewById(R.id.item_desc);
                desc.setText(it.desc);
                final EditText num = (EditText)dia.findViewById(R.id.item_num);
                num.setText(String.valueOf(it.count));
                final EditText encum = (EditText)dia.findViewById(R.id.encum_num);
                encum.setText(String.valueOf(it.encum));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        it.name = item.getText().toString();
                        it.desc = desc.getText().toString();
                        if (!num.getText().toString().equals(""))
                            it.count = Integer.parseInt(num.getText().toString());
                        else
                            it.count = 0;
                        if (!encum.getText().toString().equals(""))
                            it.encum = Integer.parseInt(encum.getText().toString());
                        else
                            it.encum = 0;
                        name.setText(it.name);
                        if (chara.isOverEncum())
                            toppest.findViewById(R.id.encum_warning).setVisibility(View.VISIBLE);
                        else
                            toppest.findViewById(R.id.encum_warning).setVisibility(View.GONE);
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = chara.inv.remove(it);
                        invLay.removeViewAt(tmp);
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
        top.addView(name);
        return top;
    }
    public LinearLayout ItemLayout(final Activity main, final LinearLayout invLay, final Minion minion, final Item it){
        LinearLayout top = new LinearLayout(main);
        LinearLayout.LayoutParams toplp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        toplp.setMargins(4,4,4,4);
        toplp.setMarginEnd(4);
        toplp.setMarginStart(4);
        top.setLayoutParams(toplp);
        TypedValue outVal = new TypedValue();
        main.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless,outVal,true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            top.setForeground(main.getDrawable(outVal.resourceId));
        else
            top.setBackgroundResource(outVal.resourceId);
        final TextView name = new TextView(main);
        LinearLayout.LayoutParams namelp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        name.setText(it.name);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_item_edit,null);
                build.setView(dia);
                final EditText item = (EditText)dia.findViewById(R.id.item_name);
                item.setText(it.name);
                final EditText desc = (EditText)dia.findViewById(R.id.item_desc);
                desc.setText(it.desc);
                final EditText num = (EditText)dia.findViewById(R.id.item_num);
                num.setText(String.valueOf(it.count));
                dia.findViewById(R.id.minion_hide).setVisibility(View.GONE);
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        it.name = item.getText().toString();
                        it.desc = desc.getText().toString();
                        if (!num.getText().toString().equals(""))
                            it.count = Integer.parseInt(num.getText().toString());
                        else
                            it.count = 0;
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = minion.inv.remove(it);
                        invLay.removeViewAt(tmp);
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
        top.addView(name);
        return top;
    }
}
