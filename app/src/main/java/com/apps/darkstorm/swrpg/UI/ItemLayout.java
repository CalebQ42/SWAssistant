package com.apps.darkstorm.swrpg.UI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Item;
import com.apps.darkstorm.swrpg.StarWars.Character;

public class ItemLayout {
    public LinearLayout ItemLayout(final Context main, final LinearLayout invLay, final Character chara, final Item it){
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
                final Dialog dia = new Dialog(main);
                dia.setContentView(R.layout.dialog_item_edit);
                final EditText item = (EditText)dia.findViewById(R.id.item_name);
                item.setText(it.name);
                final EditText desc = (EditText)dia.findViewById(R.id.item_desc);
                desc.setText(it.desc);
                final EditText num = (EditText)dia.findViewById(R.id.item_num);
                num.setText(String.valueOf(it.count));
                dia.findViewById(R.id.item_delete).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.item_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        it.name = item.getText().toString();
                        it.desc = desc.getText().toString();
                        if (!num.getText().toString().equals(""))
                            it.count = Integer.parseInt(num.getText().toString());
                        else
                            it.count = 0;
                        name.setText(it.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tmp = chara.inv.remove(it);
                        invLay.removeViewAt(tmp);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.item_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        top.addView(name);
        return top;
    }
}
