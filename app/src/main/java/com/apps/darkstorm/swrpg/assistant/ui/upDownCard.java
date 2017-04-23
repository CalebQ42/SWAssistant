package com.apps.darkstorm.swrpg.assistant.ui;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.R;

public class upDownCard {
    public abstract static class upDown{
        public abstract void up();
        public abstract void down();
        public abstract String labelValue();
    }

    public static void setUp(CardView c,String title,final upDown ud){
        ((TextView)c.findViewById(R.id.up_down_label)).setText(title);
        final TextView num = (TextView)c.findViewById(R.id.up_down_num);
        num.setText(ud.labelValue());
        c.findViewById(R.id.up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ud.up();
                num.setText(ud.labelValue());
            }
        });
        c.findViewById(R.id.down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ud.down();
                num.setText(ud.labelValue());
            }
        });
    }
    public static void setColors(CardView c, Resources r, int cardColId, int textColId){
        c.setCardBackgroundColor(r.getColor(cardColId));
        ViewGroup vg = (ViewGroup)c.findViewById(R.id.up_down_lay);
        for (int i = 0;i<vg.getChildCount();i++){
            if (vg.getChildAt(i) instanceof TextView){
                ((TextView)vg.getChildAt(i)).setTextColor(r.getColor(textColId));
            }else if (vg.getChildAt(i) instanceof Button){
                ((Button)vg.getChildAt(i)).setTextColor(r.getColor(textColId));
            }
        }
    }
}
