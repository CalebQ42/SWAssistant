package com.apps.darkstorm.swrpg.assistant.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Duty;
import com.apps.darkstorm.swrpg.assistant.ui.character.DutyLayout;
import com.apps.darkstorm.swrpg.assistant.R;

public class DutyCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_duty,root,false);
        final LinearLayout dutyLay = (LinearLayout)top.findViewById(R.id.duty_layout);
        for (int i = 0;i<chara.duty.size();i++)
            dutyLay.addView(new DutyLayout().DutyLayout(main,dutyLay,chara,chara.duty.get(i)));
        top.findViewById(R.id.duty_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Duty tmp = new Duty();
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_dutobli,null);
                build.setView(dia);
                ((TextView) dia.findViewById(R.id.dutobli_name)).setText(top.getResources().getString(R.string.obligation_text));
                final EditText obligation = (EditText) dia.findViewById(R.id.dutobli_edit);
                obligation.setText(tmp.name);
                final EditText val = (EditText) dia.findViewById(R.id.dutobli_val);
                val.setText(String.valueOf(tmp.val));
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp.name = obligation.getText().toString();
                        if (!val.getText().toString().equals(""))
                            tmp.val = Integer.parseInt(val.getText().toString());
                        else
                            tmp.val = 0;
                        chara.duty.add(tmp);
                        dutyLay.addView(new DutyLayout().DutyLayout(main,dutyLay,chara,chara.duty.get(chara.duty.size()-1)));
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
        });
        return top;
    }
}
