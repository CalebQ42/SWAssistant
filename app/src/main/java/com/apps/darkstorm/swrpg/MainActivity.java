package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.darkstorm.swrpg.load.DriveLoadCharacters;
import com.apps.darkstorm.swrpg.load.DriveLoadMinions;
import com.apps.darkstorm.swrpg.load.DriveLoadVehicles;
import com.apps.darkstorm.swrpg.load.InitialConnect;
import com.apps.darkstorm.swrpg.load.LoadCharacters;
import com.apps.darkstorm.swrpg.load.LoadMinions;
import com.apps.darkstorm.swrpg.load.LoadVehicles;
import com.apps.darkstorm.swrpg.sw.Character;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.sw.Vehicle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsInteractionListener,
            DiceFragment.OnDiceInteractionListener, GuideMain.OnGuideInteractionListener,VehicleList.OnVehicleListInteractionListener,
            VehicleEdit.OnVehicleEditInteractionListener,MinionCharacterFragment.OnMinionCharacterListInteraction,
            MinionList.OnMinionListInteractionListener,MinionEditMain.OnMinionEditInteractionListener,
            CharacterEditMain.OnFragmentInteractionListener,CharacterList.OnCharacterListInteractionListener,
            CharacterEditAttributes.OnCharEditInteractionListener,CharacterEditNotes.OnNoteInteractionListener,
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
            GMFragment.OnGMInteractionListener, DownloadFragment.OnDownloadInteractionListener{

    InterstitialAd inti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((SWrpg) getApplication()).prefs = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.light_side_key), false))
            setTheme(R.style.LightSide);
        super.onCreate(savedInstanceState);
        inti = new InterstitialAd(this);
        if(BuildConfig.DEBUG){
            inti.setAdUnitId(getString(R.string.interstitial_test));
        }else {
            if (BuildConfig.APPLICATION_ID.equals("com.apps.darkstorm.swrpg"))
                inti.setAdUnitId(getString(R.string.free_interstitial));
            else
                inti.setAdUnitId(getString(R.string.paid_interstitial));
        }
        inti.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                AdRequest req = new AdRequest.Builder().addKeyword("Star Wars").addKeyword("RPG").build();
                inti.loadAd(req);
            }
        });
        AdRequest req = new AdRequest.Builder().addKeyword("Star Wars").addKeyword("RPG").build();
        inti.loadAd(req);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final Intent in = getIntent();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final Handler handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(!MainActivity.this.isDestroyed()) {
                    String data = in.getDataString();
                    data = data.replace("content://", "");
                    ArrayList<String> seg = new ArrayList<>();
                    seg.addAll(Arrays.asList(data.split("/")));
                    switch (seg.get(0)) {
                        case "character":
                            LoadCharacters lc = new LoadCharacters(MainActivity.this);
                            int id = Integer.parseInt(seg.get(1));
                            boolean changed = false;
                            for (Character ch : lc.characters) {
                                if (ch.ID == id) {
                                    changed = true;
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                                            CharacterEditMain.newInstance(ch)).commit();
                                }
                            }
                            if (!changed) {
                                if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.dice_key), false)) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, DiceFragment.newInstance()).commit();
                                } else {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, MinionCharacterFragment.newInstance()).commit();
                                }
                            }
                            if(msg.obj instanceof AlertDialog){
                                AlertDialog build = (AlertDialog)msg.obj;
                                build.cancel();
                            }
                            break;
                        case "minion":
                            LoadMinions lm = new LoadMinions(MainActivity.this);
                            id = Integer.parseInt(seg.get(1));
                            changed = false;
                            for (Minion ch : lm.minions) {
                                if (ch.ID == id) {
                                    changed = true;
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                                            MinionEditMain.newInstance(ch)).commit();
                                }
                            }
                            if (!changed) {
                                if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.dice_key), false)) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, DiceFragment.newInstance()).commit();
                                } else {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, MinionCharacterFragment.newInstance()).commit();
                                }
                            }
                            if(msg.obj instanceof AlertDialog){
                                AlertDialog build = (AlertDialog)msg.obj;
                                build.cancel();
                            }
                            break;
                        case "vehicle":
                            LoadVehicles lv = new LoadVehicles(MainActivity.this);
                            id = Integer.parseInt(seg.get(1));
                            changed = false;
                            for (Vehicle ch : lv.vehicles) {
                                if (ch.ID == id) {
                                    changed = true;
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main,
                                            VehicleEdit.newInstance(ch)).commit();
                                }
                            }
                            if (!changed) {
                                if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.dice_key), false)) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, DiceFragment.newInstance()).commit();
                                } else {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, MinionCharacterFragment.newInstance()).commit();
                                }
                            }
                            if(msg.obj instanceof AlertDialog){
                                AlertDialog build = (AlertDialog)msg.obj;
                                build.cancel();
                            }
                            break;
                    }
                }
            }
        };
        if(in.getAction()!=null) {
            if (in.getAction().equals(Intent.ACTION_EDIT)) {
                if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.google_drive_key), false)) {
                    final AlertDialog.Builder build = new AlertDialog.Builder(this);
                    View dia = getLayoutInflater().inflate(R.layout.dialog_loading, null);
                    ((TextView) dia.findViewById(R.id.loading_text)).setText(R.string.drive_loading);
                    build.setView(dia);
                    final AlertDialog builded = build.create();
                    builded.show();
                    AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            gacMaker();
                            while (((SWrpg) getApplication()).vehicFold == null) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            String data = in.getDataString();
                            data = data.replace("content://", "");
                            ArrayList<String> seg = new ArrayList<>();
                            seg.addAll(Arrays.asList(data.split("/")));
                            if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.google_drive_key), false)) {
                                if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.google_drive_key), false)) {
                                    switch (seg.get(0)) {
                                        case "character":
                                            DriveLoadCharacters dlc = new DriveLoadCharacters(MainActivity.this);
                                            dlc.saveLocal(MainActivity.this);
                                            break;
                                        case "minion":
                                            DriveLoadMinions dlm = new DriveLoadMinions(MainActivity.this);
                                            dlm.saveLocal(MainActivity.this);
                                            break;
                                        case "vehicle":
                                            DriveLoadVehicles dlv = new DriveLoadVehicles(MainActivity.this);
                                            dlv.saveLocal(MainActivity.this);
                                            break;
                                    }
                                }
                            }
                            Message out = handle.obtainMessage();
                            out.obj = builded;
                            handle.sendMessage(out);
                            return null;
                        }
                    };
                    async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    handle.sendEmptyMessage(0);
                }
            } else if (in.getAction().equals(Intent.ACTION_VIEW)) {
                try {
                    switch (in.getDataString()) {
                        case "dice":
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, DiceFragment.newInstance()).commit();
                            break;
                        case "guide":
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, GuideMain.newInstance()).commit();
                            break;
                        default:
                            String path = in.getDataString();
                            if (path.startsWith("file://"))
                                path = path.replaceFirst("file://", "");
                            if (path.endsWith(".char")) {
                                Character tmp = new Character();
                                tmp.reLoad(path);
                                tmp.external = true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, CharacterEditMain.newInstance(tmp)).commit();
                            } else if (path.endsWith(".vhcl")) {
                                Vehicle tmp = new Vehicle();
                                tmp.reLoad(path);
                                tmp.external = true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, VehicleEdit.newInstance(tmp)).commit();
                            } else if (path.endsWith(".minion")) {
                                Minion tmp = new Minion();
                                tmp.reLoad(path);
                                tmp.external = true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, MinionEditMain.newInstance(tmp)).commit();
                            } else {
                                if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.dice_key), false)) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, DiceFragment.newInstance()).commit();
                                } else {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, MinionCharacterFragment.newInstance()).commit();
                                }
                            }
                    }
                } catch (java.lang.NullPointerException ignored) {
                    if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.dice_key), false)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, DiceFragment.newInstance()).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, MinionCharacterFragment.newInstance()).commit();
                    }
                }
            } else {
                if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.dice_key), false)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, DiceFragment.newInstance()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, MinionCharacterFragment.newInstance()).commit();
                }
            }
        }else{
            if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.dice_key), false)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, DiceFragment.newInstance()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, MinionCharacterFragment.newInstance()).commit();
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.overflow,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.g_plus:
                String url = "https://plus.google.com/communities/117741233533206107778";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.translate:
                String urlial = "https://crowdin.com/project/swrpg/invite";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlial));
                startActivity(intent);
                return true;
            default:
                return false;
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
            case (R.id.download):
//                Toast.makeText(this,"Coming!",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,DownloadFragment.newInstance())
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
            case (R.id.interstitial):
                if(inti.isLoaded()){
                    inti.show();
                }else{
                    Toast.makeText(this,R.string.ad_not_loaded,Toast.LENGTH_SHORT).show();
                }
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

    public void onResume(){
        super.onResume();
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
            } catch (IntentSender.SendIntentException ignored) {
                ((SWrpg)getApplication()).driveFail = true;
            }
        } else {
            GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
            gaa.getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            ((SWrpg)getApplication()).driveFail = true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
                }else{
                    ((SWrpg)getApplication()).driveFail = true;
                }
                break;
        }
    }
}
