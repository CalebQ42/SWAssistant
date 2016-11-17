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
        final TextView model = (TextView)top.findViewById(R.id.model_text);
        model.setText(vh.model);
        top.findViewById(R.id.model_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.model);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vh.model = val.getText().toString();
                        model.setText(vh.model);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView speed = (TextView)top.findViewById(R.id.speed_text);
        speed.setText(String.valueOf(vh.speed));
        top.findViewById(R.id.speed_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.speed);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.speed = 0;
                        else
                            vh.speed = Integer.parseInt(val.getText().toString());
                        speed.setText(vh.speed);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView silhouette = (TextView)top.findViewById(R.id.silhouette_text);
        silhouette.setText(String.valueOf(vh.silhouette));
        top.findViewById(R.id.silhouette_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.silhouette);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.silhouette = 0;
                        else
                            vh.silhouette = Integer.parseInt(val.getText().toString());
                        silhouette.setText(vh.silhouette);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView handling = (TextView)top.findViewById(R.id.handling_text);
        handling.setText(String.valueOf(vh.handling));
        top.findViewById(R.id.handling_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.handling);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.handling = 0;
                        else
                            vh.handling = Integer.parseInt(val.getText().toString());
                        handling.setText(vh.handling);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
        final TextView armor = (TextView)top.findViewById(R.id.armor_text);
        armor.setText(String.valueOf(vh.armor));
        top.findViewById(R.id.armor_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dia = new Dialog(top.getContext());
                dia.setContentView(R.layout.dialog_simple_edit);
                final EditText val = (EditText)top.findViewById(R.id.edit_val);
                val.setText(vh.armor);
                val.setInputType(InputType.TYPE_CLASS_NUMBER);
                dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (val.getText().toString().equals(""))
                            vh.armor = 0;
                        else
                            vh.armor = Integer.parseInt(val.getText().toString());
                        armor.setText(vh.armor);
                        dia.cancel();
                    }
                });
                dia.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.cancel();
                    }
                });
                dia.show();
                return true;
            }
        });
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
