package com.apps.darkstorm.swrpg.assistant.ui.cards.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.ui.character.SpecializationLayout;
import com.apps.darkstorm.swrpg.assistant.R;

public class SpecializationsCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara){
        final View top = main.getLayoutInflater().inflate(R.layout.edit_specializations,root,false);
        final LinearLayout specLay = (LinearLayout)top.findViewById(R.id.specialization_layout);
        for (int i = 0;i<chara.specializations.size();i++)
            specLay.addView(new SpecializationLayout().SpecializationLayout(
                    main,specLay,chara,chara.specializations.get(i)));
        top.findViewById(R.id.specialization_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                View dia = main.getLayoutInflater().inflate(R.layout.dialog_simple_text,null);
                build.setView(dia);
                build.setTitle(R.string.spec_text);
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setText("");
                build.setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chara.specializations.add(val.getText().toString());
                        ((LinearLayout)top.findViewById(R.id.specialization_layout)).addView(new SpecializationLayout().SpecializationLayout(
                                main,specLay,chara,
                                chara.specializations.get(chara.specializations.size()-1)));
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
