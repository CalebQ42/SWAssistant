package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.io.File;

public class Settings extends AppCompatActivity{

    boolean completeFail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.light_side_key),false)){
            setTheme(R.style.LightSide);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
            ((TextView)findViewById(R.id.save_text)).setText(R.string.not_allowed_text);
        }else {
            File location;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File tmp = Environment.getExternalStorageDirectory();
                location = new File(tmp.getAbsolutePath() + "/SWChars");
                if (!location.exists()) {
                    if (!location.mkdir()) {
                    }
                }
            } else {
                File tmp = getFilesDir();
                location = new File(tmp.getAbsolutePath() + "/SWChars");
                if (!location.exists()) {
                    if (!location.mkdir()) {
                    }
                }
            }
            final String loc = pref.getString(getString(R.string.local_location_key),location.getAbsolutePath());
            ((TextView)findViewById(R.id.save_text)).setText(loc);
            findViewById(R.id.save_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder build = new AlertDialog.Builder(Settings.this);
                    build.setMessage(R.string.save_warning);
                    build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            final Dialog dia = new Dialog(Settings.this);
                            dia.setContentView(R.layout.dialog_simple_edit);
                            final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                            val.setText(loc);
                            ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.save_location_text);
                            dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pref.edit().putString(getString(R.string.local_location_key),val.getText().toString()).apply();
                                    ((TextView)findViewById(R.id.save_text)).setText(val.getText());
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
                        }
                    });
                    build.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    build.show();
                }
            });
        }
        Switch dice = (Switch)findViewById(R.id.dice_switch);
        dice.setChecked(pref.getBoolean(getString(R.string.dice_key),false));
        dice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pref.edit().putBoolean(getString(R.string.dice_key),b).apply();
            }
        });
        Switch light = (Switch)findViewById(R.id.light_switch);
        light.setChecked(pref.getBoolean(getString(R.string.light_side_key),false));
        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pref.edit().putBoolean(getString(R.string.light_side_key),b).apply();
                if (b){
                    Toast.makeText(Settings.this,R.string.light_side_toast,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Settings.this,R.string.dark_side_toast,Toast.LENGTH_LONG).show();
                }
            }
        });
        Switch cloud = (Switch)findViewById(R.id.cloud_switch);
        cloud.setChecked(pref.getBoolean(getString(R.string.cloud_key),false));
        cloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pref.edit().putBoolean(getString(R.string.cloud_key),b).apply();
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        if (requestCode == 50 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            final SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
            File location;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File tmp = Environment.getExternalStorageDirectory();
                location = new File(tmp.getAbsolutePath() + "/SWChars");
                if (!location.exists()) {
                    if (!location.mkdir()) {
                    }
                }
            } else {
                File tmp = getFilesDir();
                location = new File(tmp.getAbsolutePath() + "/SWChars");
                if (!location.exists()) {
                    if (!location.mkdir()) {
                    }
                }
            }
            final String loc = pref.getString(getString(R.string.local_location_key),location.getAbsolutePath());
            ((TextView)findViewById(R.id.save_text)).setText(loc);
            findViewById(R.id.save_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder build = new AlertDialog.Builder(Settings.this);
                    build.setMessage(R.string.save_warning);
                    build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            final Dialog dia = new Dialog(Settings.this);
                            dia.setContentView(R.layout.dialog_simple_edit);
                            final EditText val = (EditText)dia.findViewById(R.id.edit_val);
                            val.setText(loc);
                            ((TextView)dia.findViewById(R.id.edit_name)).setText(R.string.save_location_text);
                            dia.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pref.edit().putString(getString(R.string.local_location_key),val.getText().toString()).apply();
                                    ((TextView)findViewById(R.id.save_text)).setText(val.getText());
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
                        }
                    });
                    build.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    build.show();
                }
            });
        }
    }
}
