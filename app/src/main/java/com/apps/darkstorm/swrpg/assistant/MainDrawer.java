package com.apps.darkstorm.swrpg.assistant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.apps.darkstorm.swrpg.assistant.dice.DiceHolder;
import com.apps.darkstorm.swrpg.assistant.drive.Init;
import com.apps.darkstorm.swrpg.assistant.drive.Load;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener,
        DiceRollFragment.OnDiceRollFragmentInteraction, SettingsFragment.OnSettingInterfactionInterface,
        CharacterList.OnCharacterListInteractionListener,VehicleList.OnVehicleListInteractionListener,MinionList.OnMinionListInteractionListener,
        EditFragment.OnCharacterEditInteractionListener,EditGeneral.OnEditInteractionListener,NoteEdit.OnNoteEditInteractionListener,
        NotesFragment.OnFragmentInteractionListener,NotesListFragment.OnFragmentInteractionListener, GMFragment.OnFragmentInteractionListener,
        GuideMain.OnGuideInteractionListener,DownloadFragment.OnFragmentInteractionListener{


    ServiceConnection iapsService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((SWrpg)getApplication()).prefs = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.light_side_key), false))
            setTheme(R.style.LightSide);
        super.onCreate(savedInstanceState);
        iapsMaker();
        if(((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.thank_you_key),true)) {
            AsyncTask<Void, Void, Void> asyncIAP = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Bundle owned = ((SWrpg)getApplication()).iaps.getPurchaseHistory(3,getPackageName(),"inapp","",null);
                        if(owned.getInt("RESPONSE_CODE")==0){
                            //noinspection ConstantConditions
                            if(owned.getStringArrayList("INAPP_PURCHASE_ITEM_LIST").size()>0)
                                ((SWrpg)getApplication()).bought = true;
                        }
                    } catch (RemoteException|NullPointerException e) {
                        return null;
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    if(((SWrpg)getApplication()).bought)
                        Toast.makeText(MainDrawer.this,R.string.thanks_toast,Toast.LENGTH_LONG).show();
                }
            };
            asyncIAP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        AsyncTask<Void, Void, Void> asyncIAP = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Bundle owned = ((SWrpg)getApplication()).iaps.getPurchases(3,getPackageName(),"inapp",null);
                    if(owned.getInt("RESPONSE_CODE")==0){
                        //noinspection ConstantConditions
                        if(owned.getStringArrayList("INAPP_PURCHASE_ITEM_LIST").size()>0){
                            //noinspection ConstantConditions
                            for(String s:owned.getStringArrayList("INAPP_PURCHASE_DATA_LIST")){
                                JSONObject boj = new JSONObject(s);
                                String tok = boj.getString("purchaseToken");
                                ((SWrpg)getApplication()).iaps.consumePurchase(3,getPackageName(),tok);
                            }
                        }
                    }
                } catch (RemoteException|NullPointerException|JSONException ignored) {}
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                if(((SWrpg)getApplication()).bought)
                    Toast.makeText(MainDrawer.this,R.string.thanks_toast,Toast.LENGTH_LONG).show();
            }
        };
        asyncIAP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        if(((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false))
            gacMaker();
        if (Intent.ACTION_VIEW.equals(intent.getAction())&& intent.getData()!=null){
            switch(intent.getDataString()) {
                case "dice":
                    getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance()).commit();
                    break;
                case "guide":
                    getFragmentManager().beginTransaction().replace(R.id.content_main, GuideMain.newInstance()).commit();
                    break;
                default:
                    String path = intent.getData().getPath();
                    if (path.endsWith(".char")) {
                        Character tmp = new Character();
                        tmp.reLoad(path);
                        tmp.external = true;
                        getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(tmp)).commit();
                    } else if (path.endsWith(".vhcl")) {
                        Vehicle tmp = new Vehicle();
                        tmp.reLoad(path);
                        tmp.external = true;
                        getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(tmp)).commit();
                    } else if (path.endsWith(".minion")) {
                        Minion tmp = new Minion();
                        tmp.reLoad(path);
                        tmp.external = true;
                        getFragmentManager().beginTransaction().replace(R.id.content_main, EditFragment.newInstance(tmp)).commit();
                    } else {
                        if (((SWrpg) getApplication()).prefs.getBoolean(getString(R.string.dice_key), false))
                            getFragmentManager().beginTransaction().replace(R.id.content_main, DiceRollFragment.newInstance()).commit();
                        else
                            getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                    }
            }
        }else if(Intent.ACTION_EDIT.equals(intent.getAction())&&intent.getData()!=null){
            if (intent.getDataString().startsWith("content://")){
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.dialog_loading,null);
                ((TextView)v.findViewById(R.id.loading_message)).setText(R.string.still_loading);
                b.setView(v);
                final AlertDialog ad = b.show();
                String data = intent.getDataString().substring("content://".length());
                if (data.startsWith("character")){
                    data = data.replace("character/","");
                    final int ID = Integer.parseInt(data);
                    if (((SWrpg)getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                while (!((SWrpg) getApplication()).driveFail && ((SWrpg)getApplication()).charsFold==null){
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
                                    final boolean[] found = {false};
                                    final Load.Characters ld = new Load.Characters();
                                    ld.setOnFinish(new Load.OnLoad() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public boolean onLoad(final Editable ed) {
                                            if (ed.ID == ID){
                                                found[0] = true;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ad.cancel();
                                                        getFragmentManager().beginTransaction().replace(R.id.content_main,
                                                                EditFragment.newInstance(ed)).commit();
                                                    }
                                                });
                                                return true;
                                            }
                                            return false;
                                        }

                                        @Override
                                        public void onFinish(ArrayList<Editable> characters) {
                                            if (!found[0]) {
                                                ad.cancel();
                                                getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                                            }
                                        }
                                    });
                                    ld.load(MainDrawer.this);
                                }
                                return null;
                            }
                        };
                        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                while (!((SWrpg) getApplication()).driveFail && ((SWrpg)getApplication()).charsFold==null){
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
                                    final boolean[] found = {false};
                                    ld.setOnFinish(new Load.OnLoad() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public boolean onLoad(final Editable ed) {
                                            if (ed.ID == ID){
                                                found[0] = true;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ad.cancel();
                                                        getFragmentManager().beginTransaction().replace(R.id.content_main,
                                                                EditFragment.newInstance(ed)).commit();
                                                    }
                                                });
                                                return true;
                                            }
                                            return false;
                                        }

                                        @Override
                                        public void onFinish(ArrayList<Editable> characters) {
                                            if (!found[0]) {
                                                ad.cancel();
                                                getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                                            }
                                        }
                                    });
                                    ld.load(MainDrawer.this);
                                }
                                return null;
                            }
                        };
                        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                while (!((SWrpg) getApplication()).driveFail && ((SWrpg)getApplication()).charsFold==null){
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
                                    final boolean[] found = {false};
                                    ld.setOnFinish(new Load.OnLoad() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public boolean onLoad(final Editable ed) {
                                            if (ed.ID == ID){
                                                found[0] = true;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ad.cancel();
                                                        getFragmentManager().beginTransaction().replace(R.id.content_main,
                                                                EditFragment.newInstance(ed)).commit();
                                                    }
                                                });
                                                return true;
                                            }
                                            return false;
                                        }

                                        @Override
                                        public void onFinish(ArrayList<Editable> characters) {
                                            if (!found[0]) {
                                                ad.cancel();
                                                getFragmentManager().beginTransaction().replace(R.id.content_main, CharacterList.newInstance()).commit();
                                            }
                                        }
                                    });
                                    ld.load(MainDrawer.this);
                                }
                                return null;
                            }
                        };
                        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                    ad.cancel();
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
            case R.id.dice_roll:
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.fragment_dice_roll,null);
                b.setView(view);
                view.findViewById(R.id.instant_recycler).setVisibility(View.GONE);
                view.findViewById(R.id.instant_dice_text).setVisibility(View.GONE);
                view.findViewById(R.id.fab_space).setVisibility(View.GONE);
                view.findViewById(R.id.dice_reset).setVisibility(View.GONE);
                view.findViewById(R.id.dice_label).setVisibility(View.GONE);
                final DiceHolder dh = new DiceHolder();
                final DiceRollFragment.DiceList dl = new DiceRollFragment.DiceList(this,dh);
                RecyclerView r = (RecyclerView)view.findViewById(R.id.dice_recycler);
                r.setAdapter(dl);
                r.setLayoutManager(new LinearLayoutManager(this));
                b.setPositiveButton(R.string.roll_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dh.roll().showDialog(MainDrawer.this);
                        dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                b.show();
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
                if(cur instanceof GMFragment)
                    getFragmentManager().beginTransaction().replace(R.id.content_main, GMFragment.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, GMFragment.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("GM").commit();
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
                if (cur instanceof DownloadFragment)
                    getFragmentManager().beginTransaction().replace(R.id.content_main, DownloadFragment.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, DownloadFragment.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Download").commit();
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
                if (cur instanceof GuideMain)
                    getFragmentManager().beginTransaction().replace(R.id.content_main, GuideMain.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.content_main, GuideMain.newInstance())
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Guide").commit();
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
        if(((SWrpg)getApplication()).gac == null) {
            ((SWrpg)getApplication()).gac = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            Init.connect(MainDrawer.this,0);
                        }
                        @Override
                        public void onConnectionSuspended(int i) {
                            System.out.println("HelloSuspend");
                        }
                    })
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        if (((SWrpg)getApplication()).gac.isConnected())
            Init.connect(this,0);
        else
            ((SWrpg)getApplication()).gac.connect();
    }

    public void iapsMaker(){
        iapsService =  new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                ((SWrpg)getApplication()).iaps = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                ((SWrpg)getApplication()).iaps = IInAppBillingService.Stub.asInterface(service);
            }
        };
        Intent iapInt = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        iapInt.setPackage("com.android.vending");
        bindService(iapInt, iapsService, Context.BIND_AUTO_CREATE);
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 5);
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
                if (resultCode == RESULT_OK)
                    ((SWrpg)getApplication()).gac.connect();
                else
                    ((SWrpg)getApplication()).driveFail = true;
                break;
            case 100:
                if(data.getIntExtra("RESPONSE_CODE",0) == RESULT_OK){
                    String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
                    try {
                        JSONObject obj = new JSONObject(purchaseData);
                        String token = obj.getString("purchaseToken");
                        ((SWrpg)getApplication()).iaps.consumePurchase(3,getPackageName(),token);
                    } catch (JSONException|RemoteException ignored) {}
                    Toast.makeText(this,R.string.thanks_toast,Toast.LENGTH_LONG).show();
                    Fragment cur = getFragmentManager().findFragmentById(R.id.content_main);
                    if(cur instanceof SettingsFragment) ((SettingsFragment)cur).showThanks();
                }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(((SWrpg)getApplication()).iaps!=null)
            unbindService(iapsService);
    }
}
