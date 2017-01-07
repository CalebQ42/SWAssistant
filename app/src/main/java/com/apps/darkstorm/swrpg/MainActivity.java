package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsInteractionListener,
            DiceFragment.OnDiceInteractionListener, GuideMain.OnGuideInteractionListener,VehicleList.OnVehicleListInteractionListener,
            VehicleEdit.OnVehicleEditInteractionListener,MinionCharacterFragment.OnMinionCharacterListInteraction,
            MinionList.OnMinionListInteractionListener,MinionEditMain.OnMinionEditInteractionListener,
            CharacterEditMain.OnFragmentInteractionListener,CharacerList.OnCharacterListInteractionListener,
            CharacterEditAttributes.OnCharEditInteractionListener,CharacterEditNotes.OnNoteInteractionListener{
    SWrpg app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (SWrpg)getApplication();
        app.prefs = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        if(app.prefs.getBoolean(getString(R.string.light_side_key),false))
            app.setTheme(R.style.LightSide);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (app.prefs.getBoolean(getString(R.string.dice_key),false)){
            getFragmentManager().beginTransaction().replace(R.id.content_main,DiceFragment.newInstance()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case (R.id.characters):
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,MinionCharacterFragment.newInstance())
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                android.R.anim.fade_in,android.R.anim.fade_out)
                        .addToBackStack("").commit();
                break;
            case (R.id.vehicles):
                getFragmentManager().beginTransaction().replace(R.id.content_main,VehicleList.newInstance())
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                android.R.animator.fade_in,android.R.animator.fade_out)
                        .addToBackStack("").commit();
                break;
            case (R.id.gm_mode):
                break;
            case (R.id.dice):
                getFragmentManager().beginTransaction().replace(R.id.content_main,DiceFragment.newInstance())
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                android.R.animator.fade_in,android.R.animator.fade_out)
                        .addToBackStack("").commit();
                break;
            case (R.id.guide):
                getFragmentManager().beginTransaction().replace(R.id.content_main,GuideMain.newInstance())
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("").commit();
                break;
            case (R.id.settings):
                getFragmentManager().beginTransaction().replace(R.id.content_main,SettingsFragment.newInstance())
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("").commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onStart(){
        super.onStart();
        app.askingPerm = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
        }else{
            File location;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                File tmp = Environment.getExternalStorageDirectory();
                location = new File(tmp.getAbsolutePath() + "/SWChars");
                if (!location.exists()){
                    location.mkdir();
                }
            }else{
                File tmp = this.getFilesDir();
                location = new File(tmp.getAbsolutePath() + "/SWChars");
                if (!location.exists()){
                    location.mkdir();
                }
            }
            app.defaultLoc = location.getAbsolutePath();
            app.askingPerm = false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 40);
        }else{
            app.askingPerm = false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch(requestCode){
            case 50:
                boolean r = false;
                boolean w = false;
                boolean rw;
                for (int i = 0;i<permissions.length;i++){
                    String perm = permissions[i];
                    if (perm.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        w = true;
                    else if (perm.equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        r = true;
                }
                rw = r && w;
                if (rw){
                    File location;
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                        File tmp = Environment.getExternalStorageDirectory();
                        location = new File(tmp.getAbsolutePath() + "/SWChars");
                        if (!location.exists()){
                            location.mkdir();
                        }
                    }else{
                        File tmp = this.getFilesDir();
                        location = new File(tmp.getAbsolutePath() + "/SWChars");
                        if (!location.exists()){
                            location.mkdir();
                        }
                    }
                    app.defaultLoc = location.getAbsolutePath();
                }else{
                    AlertDialog.Builder build = new AlertDialog.Builder(this);
                    build.setMessage(R.string.permission_error).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            app.prefs.edit().putBoolean(getString(R.string.dice_key),true).apply();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.content_main,DiceFragment.newInstance())
                                    .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                    android.R.animator.fade_in,android.R.animator.fade_out).commit();
                        }
                    });
                    build.show();
                }
                break;
        }
        app.askingPerm = false;
    }
}
