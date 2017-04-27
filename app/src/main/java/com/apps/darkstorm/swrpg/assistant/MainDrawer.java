package com.apps.darkstorm.swrpg.assistant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.apps.darkstorm.swrpg.assistant.drive.Init;
import com.apps.darkstorm.swrpg.assistant.drive.Load;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.io.File;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener,
        DiceRollFragment.OnDiceRollFragmentInteraction, SettingsFragment.OnSettingInterfactionInterface,
        CharacterList.OnCharacterListInteractionListener,VehicleList.OnVehicleListInteractionListener,MinionList.OnMinionListInteractionListener,
        EditFragment.OnCharacterEditInteractionListener,EditGeneral.OnEditInteractionListener,NoteEdit.OnNoteEditInteractionListener,
        NotesFragment.OnFragmentInteractionListener,NotesListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((SWrpg)getApplication()).prefs = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.light_side_key), false))
            setTheme(R.style.LightSide);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent intent = getIntent();
        ((SWrpg)getApplication()).askingPerm = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
            }
        }else{
            File location;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File tmp = Environment.getExternalStorageDirectory();
                location = new File(tmp.getAbsolutePath() + "/SWChars");
            } else {
                File tmp = this.getFilesDir();
                location = new File(tmp.getAbsolutePath() + "/SWChars");
            }
            ((SWrpg) getApplication()).defaultLoc = location.getAbsolutePath();
            ((SWrpg) getApplication()).askingPerm = false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 40);
            }
        }else{
            ((SWrpg)getApplication()).askingPerm = false;
        }
        gacMaker();
        if (Intent.ACTION_VIEW.equals(intent.getAction())&& intent.getData()!=null){
            String path = intent.getData().getPath();
            if (path.endsWith(".char")){
                Character tmp = new Character();
                tmp.reLoad(path);
                tmp.external = true;
                getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(tmp)).commit();
            }else if(path.endsWith(".vhcl")){
                Vehicle tmp = new Vehicle();
                tmp.reLoad(path);
                tmp.external = true;
                getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(tmp)).commit();
            }else if(path.endsWith(".minion")){
                Minion tmp = new Minion();
                tmp.reLoad(path);
                tmp.external = true;
                getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(tmp)).commit();
            }else{
                if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.dice_key),false))
                    getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance()).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
            }
        }else if(Intent.ACTION_EDIT.equals(intent.getAction())&&intent.getData()!=null){
            switch(intent.getDataString()){
                case "die":
                    getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance()).commit();
                    break;
                case "guide":
                    break;
                default:
                    if (intent.getDataString().startsWith("content://")){
                        AlertDialog.Builder b = new AlertDialog.Builder(this);
                        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.dialog_loading,null);
                        ((TextView)v.findViewById(R.id.loading_message)).setText(R.string.still_loading);
                        b.setView(v);
                        final AlertDialog ad = b.show();
                        String data = intent.getDataString().substring("content://".length()-1);
                        if (data.startsWith("character")){
                            data = data.replace("character/","");
                            final int ID = Integer.parseInt(data);
                            if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                                while (!((SWrpg) getApplication()).driveFail){
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(((SWrpg) getApplication()).driveFail){
                                    ad.cancel();
                                    getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                                }else{
                                    final Load.Characters ld = new Load.Characters();
                                    ld.setOnFinish(new Load.onFinish() {
                                        @Override
                                        public void finish() {
                                            ld.saveLocal(MainDrawer.this);
                                            boolean found = false;
                                            for (int i = 0; i<ld.characters.size(); i++){
                                                Character c = ld.characters.get(i);
                                                if (c.ID == ID){
                                                    found = true;
                                                    ad.cancel();
                                                    getFragmentManager().beginTransaction().replace(R.id.content_main,
                                                            EditFragment.newInstance(c)).commit();
                                                    break;
                                                }
                                            }
                                            if (!found){
                                                ad.cancel();
                                                getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                                            }
                                        }
                                    });
                                    ld.load(MainDrawer.this);
                                }
                            }else{
                                Character[] ch = LoadLocal.characters(MainDrawer.this);
                                boolean found = false;
                                for (Character aCh : ch) {
                                    if (aCh.ID == ID) {
                                        found = true;
                                        ad.cancel();
                                        getFragmentManager().beginTransaction().replace(R.id.content_main,
                                                EditFragment.newInstance(aCh)).commit();
                                        break;
                                    }
                                }
                                if (!found){
                                    ad.cancel();
                                    getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                                }
                            }
                        }else if(data.startsWith("minion")){
                            data = data.replace("minion/","");
                            final int ID = Integer.parseInt(data);
                            if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                                while (!((SWrpg) getApplication()).driveFail){
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(((SWrpg) getApplication()).driveFail){
                                    ad.cancel();
                                    getFragmentManager().beginTransaction().replace(R.id.content_main, MinionList.newInstance()).commit();
                                }else{
                                    final Load.Minions ld = new Load.Minions();
                                    ld.setOnFinish(new Load.onFinish() {
                                        @Override
                                        public void finish() {
                                            ld.saveLocal(MainDrawer.this);
                                            boolean found = false;
                                            for (int i = 0;i<ld.minions.size();i++){
                                                if (ld.minions.get(i).ID == ID){
                                                    found = true;
                                                    ad.cancel();
                                                    getFragmentManager().beginTransaction().replace(R.id.content_main,
                                                            EditFragment.newInstance(ld.minions.get(i))).commit();
                                                    break;
                                                }
                                            }
                                            if (!found){
                                                ad.cancel();
                                                getFragmentManager().beginTransaction().replace(R.id.content_main, MinionList.newInstance()).commit();
                                            }
                                        }
                                    });
                                    ld.load(MainDrawer.this);
                                }
                            }else{
                                Minion[] ch = LoadLocal.minions(MainDrawer.this);
                                boolean found = false;
                                for (Minion aCh : ch) {
                                    if (aCh.ID == ID) {
                                        found = true;
                                        ad.cancel();
                                        getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(aCh)).commit();
                                        break;
                                    }
                                }
                                if (!found){
                                    ad.cancel();
                                    getFragmentManager().beginTransaction().replace(R.id.content_main, MinionList.newInstance()).commit();
                                }
                            }
                        }else if(data.startsWith("vehicle")){
                            data = data.replace("vehicle/","");
                            final int ID = Integer.parseInt(data);
                            if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                                while (!((SWrpg) getApplication()).driveFail){
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(((SWrpg) getApplication()).driveFail){
                                    ad.cancel();
                                    getFragmentManager().beginTransaction().replace(R.id.content_main, VehicleList.newInstance()).commit();
                                }else{
                                    final Load.Vehicles ld = new Load.Vehicles();
                                    ld.setOnFinish(new Load.onFinish() {
                                        @Override
                                        public void finish() {
                                            ld.saveLocal(MainDrawer.this);
                                            boolean found = false;
                                            for (int i = 0; i<ld.vehicles.size(); i++){
                                                if (ld.vehicles.get(i).ID == ID){
                                                    found = true;
                                                    ad.cancel();
                                                    getFragmentManager().beginTransaction().replace(R.id.content_main,
                                                            EditFragment.newInstance(ld.vehicles.get(i))).commit();
                                                    break;
                                                }
                                            }
                                            if (!found){
                                                ad.cancel();
                                                getFragmentManager().beginTransaction().replace(R.id.content_main, VehicleList.newInstance()).commit();
                                            }
                                        }
                                    });
                                    ld.load(MainDrawer.this);
                                }
                            }else{
                                Vehicle[] ch = LoadLocal.vehicles(MainDrawer.this);
                                boolean found = false;
                                for (Vehicle aCh : ch) {
                                    if (aCh.ID == ID) {
                                        found = true;
                                        ad.cancel();
                                        getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(aCh)).commit();
                                        break;
                                    }
                                }
                                if (!found){
                                    ad.cancel();
                                    getFragmentManager().beginTransaction().replace(R.id.content_main, VehicleList.newInstance()).commit();
                                }
                            }
                        }else{
                            if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.dice_key),false))
                                getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance()).commit();
                            else
                                getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                        }
                    }else{
                        if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.dice_key),false))
                            getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance()).commit();
                        else
                            getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                    }
                    break;
            }
        }else{
            if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.dice_key),false))
                getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance()).commit();
            else
                getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment cur = getFragmentManager().findFragmentById(R.id.content_main);
            if (cur != null){
                Fragment childMost = cur.getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 1);
                if (childMost != null && childMost.getChildFragmentManager().getBackStackEntryCount()>0){
                    childMost.getChildFragmentManager().popBackStack();
                    ((FloatingActionButton)findViewById(R.id.fab)).show();
                }else if(cur.getChildFragmentManager().getBackStackEntryCount()>0){
                    cur.getChildFragmentManager().popBackStack();
                }else{
                    super.onBackPressed();
                }
            } else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String url;
        Intent i;
        switch (id){
            case R.id.g_plus_item:
                url = "https://plus.google.com/communities/117741233533206107778";
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.translate_item:
                url = "https://crowdin.com/project/swrpg";
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment cur = getFragmentManager().findFragmentById(R.id.content_main);
        switch (id){
            case R.id.gm_nav:
                break;
            case R.id.char_nav:
                if (cur instanceof CharacterList)
                    getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Character List").commit();
                break;
            case R.id.min_nav:
                if (cur instanceof MinionList)
                    getFragmentManager().beginTransaction().replace(R.id.content_main, MinionList.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, MinionList.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Minion List").commit();
                break;
            case R.id.vehic_nav:
                if (cur instanceof VehicleList)
                    getFragmentManager().beginTransaction().replace(R.id.content_main, VehicleList.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, VehicleList.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Vehicle List").commit();
                break;
            case R.id.dnld_nav:
                break;
            case R.id.dice_nav:
                if (cur instanceof DiceRollFragment)
                    getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Dice").commit();
                break;
            case R.id.guid_nav:
                break;
            case R.id.stng_nav:
                if (cur instanceof SettingsFragment)
                    getFragmentManager().beginTransaction().replace(R.id.content_main, SettingsFragment.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, SettingsFragment.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Settings").commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gacMaker(){
        if(((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
            if(((SWrpg)getApplication()).gac == null) {
                ((SWrpg)getApplication()).gac = new GoogleApiClient.Builder(this)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                Init.connect(MainDrawer.this);
                            }
                            @Override
                            public void onConnectionSuspended(int i) {}
                        })
                        .addOnConnectionFailedListener(this)
                        .build();
            }
            ((SWrpg)getApplication()).gac.connect();
        }
    }
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 1);
            } catch (IntentSender.SendIntentException ignored) {
                ((SWrpg)getApplication()).driveFail = true;
            }
        } else {
            GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
            gaa.getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            ((SWrpg)getApplication()).driveFail = true;
        }
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case 5:
                if (resultCode == RESULT_OK) {
                    ((SWrpg)getApplication()).gac.connect();
                }else{
                    ((SWrpg)getApplication()).driveFail = true;
                }
                break;
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
                    }else{
                        File tmp = this.getFilesDir();
                        location = new File(tmp.getAbsolutePath() + "/SWChars");
                    }
                    ((SWrpg)getApplication()).defaultLoc = location.getAbsolutePath();
                    Fragment fr = getFragmentManager().findFragmentById(R.id.content_main);
                    if(fr instanceof CharacterList){
                        ((CharacterList)fr).loadCharacters();
                    }
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
                            if (getFragmentManager().findFragmentById(R.id.content_main) instanceof DiceRollFragment)
                                getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance())
                                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                            else
                                getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance())
                                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Dice").commit();
                        }
                    });
                    build.show();
                }
                break;
        }
        ((SWrpg)getApplication()).askingPerm = false;
    }
}
