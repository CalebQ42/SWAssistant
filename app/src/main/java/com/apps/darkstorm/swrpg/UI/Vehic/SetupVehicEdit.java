package com.apps.darkstorm.swrpg.UI.Vehic;

import android.app.Dialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.StarWars.Vehicle;

public class SetupVehicEdit {
    public static void setup(final View top, final Vehicle vh){
        //<editor-fold desc="name_card">
        final TextView name = (TextView)top.findViewById(R.id.name);
        name.setText(vh.name);
        top.findViewById(R.id.name_card).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                ((TextView)dia.findViewById(R.id.edit_name)).setText(top.getResources().getString(R.string.name_text));
                final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                val.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                val.setText(vh.name);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        vh.name = val.getText().toString();
                        name.setText(vh.name);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        dia.cancel();
                    }
                });
                dia.show();
                return false;
            }
        });
        //</editor-fold>
        //<editor-fold desc="basic_info">

        //</editor-fold>
        //<editor-fold desc="">
        //</editor-fold>
        //<editor-fold desc="">
        //</editor-fold>
        //<editor-fold desc="">
        //</editor-fold>
        //<editor-fold desc="">
        //</editor-fold>
        //<editor-fold desc="">
        //</editor-fold>
        //<editor-fold desc="">
        //</editor-fold>
    }
}
