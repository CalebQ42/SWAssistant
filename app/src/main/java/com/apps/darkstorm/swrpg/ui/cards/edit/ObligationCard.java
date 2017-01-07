package com.apps.darkstorm.swrpg.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.stuff.Obligation;
import com.apps.darkstorm.swrpg.ui.character.ObligationLayout;

public class ObligationCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_obligation,root);final LinearLayout obligationLay = (LinearLayout)top.findViewById(R.id.obligation_layout);
        for (int i = 0;i<chara.obligation.size();i++)
            obligationLay.addView(new ObligationLayout().ObligationLayout(main,obligationLay,chara,
                    chara.obligation.get(i)));
        top.findViewById(R.id.obligation_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Obligation tmp = new Obligation();
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
                        chara.obligation.add(tmp);
                        obligationLay.addView(new ObligationLayout().ObligationLayout(main,obligationLay,chara,
                                chara.obligation.get(chara.obligation.size()-1)));
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
