package com.apps.darkstorm.swrpg.assistant.ui.cards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.R;

public class CharacterCard {
    public static View getCard(final Activity main, ViewGroup root, final Character chara, final Handler handle){
        View top = main.getLayoutInflater().inflate(R.layout.card_character,root,false);
        ((TextView)top.findViewById(R.id.name_text)).setText(chara.name);
        if (chara.career.equals(""))
            top.findViewById(R.id.career_text).setVisibility(View.GONE);
        else
            ((TextView)top.findViewById(R.id.career_text)).setText(chara.career);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message out = handle.obtainMessage();
                out.obj = chara;
                out.arg1 = 1;
                handle.sendMessage(out);
            }
        });
        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(main);
                build.setMessage(R.string.character_delete);
                build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Message out = handle.obtainMessage();
                        out.obj = chara;
                        out.arg1 = -1;
                        handle.sendMessage(out);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                build.show();
                return false;
            }
        });
        return top;
    }
}
