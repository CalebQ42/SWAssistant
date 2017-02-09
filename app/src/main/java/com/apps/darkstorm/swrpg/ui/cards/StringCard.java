package com.apps.darkstorm.swrpg.ui.cards;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;

public class StringCard {
    public static View getCard(final Activity main, ViewGroup vg,final String str, final Handler handle){
        View top = main.getLayoutInflater().inflate(R.layout.card_minion_vehicle_note,vg,false);
        ((TextView)top.findViewById(R.id.name_text)).setText(str);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message out = handle.obtainMessage();
                out.obj = str;
                System.out.println("From Card: "+ out.obj);
                handle.sendMessage(out);
            }
        });
        return top;
    }
}
