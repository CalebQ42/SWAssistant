package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.apps.darkstorm.swrpg.load.InitialConnect;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsInteractionListener,
            DiceFragment.OnDiceInteractionListener, GuideMain.OnGuideInteractionListener,VehicleList.OnVehicleListInteractionListener,
            VehicleEdit.OnVehicleEditInteractionListener,MinionCharacterFragment.OnMinionCharacterListInteraction,
            MinionList.OnMinionListInteractionListener,MinionEditMain.OnMinionEditInteractionListener,
            CharacterEditMain.OnFragmentInteractionListener,CharacerList.OnCharacterListInteractionListener,
            CharacterEditAttributes.OnCharEditInteractionListener,CharacterEditNotes.OnNoteInteractionListener,
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
            GMFragment.OnGMInteractionListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((SWrpg)getApplication()).prefs = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        if(((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.light_side_key),false))
            setTheme(R.style.LightSide);
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

        if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.dice_key),false)){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,DiceFragment.newInstance()).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,MinionCharacterFragment.newInstance()).commit();
        }
    }

    public void gacMaker(){
        if(((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            if(((SWrpg)getApplication()).gac == null) {
                ((SWrpg)getApplication()).gac = new GoogleApiClient.Builder(this)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
            }
            if(!((SWrpg)getApplication()).gac.isConnected())
                ((SWrpg)getApplication()).gac.connect();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,VehicleList.newInstance())
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                android.R.anim.fade_in,android.R.anim.fade_out)
                        .addToBackStack("").commit();
                break;
            case (R.id.gm_mode):
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,GMFragment.newInstance())
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                android.R.anim.fade_in,android.R.anim.fade_out)
                        .addToBackStack("").commit();
                break;
            case (R.id.dice):
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,DiceFragment.newInstance())
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                android.R.anim.fade_in,android.R.anim.fade_out)
                        .addToBackStack("").commit();
                break;
            case (R.id.guide):
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,GuideMain.newInstance())
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack("").commit();
                break;
            case (R.id.settings):
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,SettingsFragment.newInstance())
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack("").commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onStart(){
        super.onStart();
        ((SWrpg)getApplication()).askingPerm = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
            }
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
            ((SWrpg)getApplication()).defaultLoc = location.getAbsolutePath();
            ((SWrpg)getApplication()).askingPerm = false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 40);
            }
        }else{
            ((SWrpg)getApplication()).askingPerm = false;
        }
        gacMaker();
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
                    ((SWrpg)getApplication()).defaultLoc = location.getAbsolutePath();
                }else{
                    AlertDialog.Builder build = new AlertDialog.Builder(this);
                    build.setMessage(R.string.permission_error).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
                            }
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ((SWrpg)getApplication()).prefs.edit().putBoolean(getString(R.string.dice_key),true).apply();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_main,DiceFragment.newInstance())
                                    .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                                    android.R.anim.fade_in,android.R.anim.fade_out).commit();
                        }
                    });
                    build.show();
                }
                break;
        }
        ((SWrpg)getApplication()).askingPerm = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 1);
            } catch (IntentSender.SendIntentException ignored) {}
        } else {
            GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
            gaa.getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("Connected");
        InitialConnect.connect(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case 5:
                if (resultCode == RESULT_OK) {
                    ((SWrpg)getApplication()).gac.connect();
                }
                break;
        }
    }
}
