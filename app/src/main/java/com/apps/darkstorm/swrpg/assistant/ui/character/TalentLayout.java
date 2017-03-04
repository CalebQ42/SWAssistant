package com.apps.darkstorm.swrpg.assistant.ui.character;

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

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Talent;
import com.apps.darkstorm.swrpg.assistant.R;

public class TalentLayout {
    public LinearLayout TalentLayout(final Activity main, final LinearLayout talentLay, final Character chara, final Talent t){
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
        LinearLayout.LayoutParams namelp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextSize(16);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        name.setText(t.name);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Talent tmp = t;
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_talent_edit,null);
                build.setView(dia);
                final EditText nameVal = (EditText)dia.findViewById(R.id.talent_name);
                nameVal.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.talent_description);
                desc.setText(tmp.desc);
                final TextView val = (TextView)dia.findViewById(R.id.talent_val);
                val.setText(String.valueOf(tmp.val));
                dia.findViewById(R.id.talent_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.val>0)
                            tmp.val--;
                        val.setText(String.valueOf(tmp.val));

                    }
                });
                dia.findViewById(R.id.talent_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.val++;
                        val.setText(String.valueOf(tmp.val));
                    }
                });
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = nameVal.getText().toString();
                        tmp.desc = desc.getText().toString();
                        chara.talents.set(tmp,t);
                        name.setText(tmp.name);
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = chara.talents.remove(t);
                        talentLay.removeViewAt(tmp);
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
    public LinearLayout TalentLayout(final Activity main, final LinearLayout talentLay, final Minion minion, final Talent t){
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
        LinearLayout.LayoutParams namelp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(namelp);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextSize(16);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        name.setText(t.name);
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Talent tmp = t;
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_talent_edit,null);
                build.setView(dia);
                final EditText nameVal = (EditText)dia.findViewById(R.id.talent_name);
                nameVal.setText(tmp.name);
                final EditText desc = (EditText)dia.findViewById(R.id.talent_description);
                desc.setText(tmp.desc);
                final TextView val = (TextView)dia.findViewById(R.id.talent_val);
                val.setText(String.valueOf(tmp.val));
                dia.findViewById(R.id.talent_minus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.val>0)
                            tmp.val--;
                        val.setText(String.valueOf(tmp.val));

                    }
                });
                dia.findViewById(R.id.talent_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tmp.val++;
                        val.setText(String.valueOf(tmp.val));
                    }
                });
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = nameVal.getText().toString();
                        tmp.desc = desc.getText().toString();
                        minion.talents.set(tmp,t);
                        name.setText(tmp.name);
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = minion.talents.remove(t);
                        talentLay.removeViewAt(tmp);
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
