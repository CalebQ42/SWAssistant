package com.apps.darkstorm.swrpg.assistant.sw;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;

import com.apps.darkstorm.swrpg.assistant.EditGeneral;
import com.apps.darkstorm.swrpg.assistant.MainDrawer;
import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Notes;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapons;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

import java.io.File;
import java.util.Collections;

public abstract class Editable {
    public int ID;
    public String name = "";
    public Notes nts = new Notes();
    public Weapons weapons = new Weapons();
    public String category = "";
    public CriticalInjuries critInjuries = new CriticalInjuries();
    String desc = "";

    boolean editing = false;
    private boolean saving = false;
    String loc="";
    public boolean external = false;

    public abstract int cardNumber();
    public abstract void setupCards(Activity ac, EditGeneral.EditableAdap ea, CardView c, int pos,Handler parentHandle);
    public abstract void save(String filename);
    public abstract void cloudSave(GoogleApiClient gac, DriveId fil, boolean async);
    public abstract void reLoad(GoogleApiClient gac,DriveId fil);
    public abstract void reLoad(String filename);
    public abstract DriveId getFileId(Activity ac);
    public abstract String getFileLocation(Activity ac);
    public abstract Editable clone();
    public void startEditing(final Activity ac){
        if (!editing) {
            editing = true;
            if (((SWrpg) ac.getApplication()).prefs.getBoolean(ac.getString(R.string.google_drive_key), false) && !external) {
                AsyncTask<Void, Void, Void> blablah = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Editable tmpChar = Editable.this.clone();
                        save(getFileLocation(ac));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                            if (hasShortcut(ac))
                                updateShortcut(ac);
                            else
                                addShortcut(ac);
                        }
                        if(((SWrpg)ac.getApplication()).vehicFold!=null)
                            cloudSave(((SWrpg) ac.getApplication()).gac, getFileId(ac), false);
                        do {
                            if (!saving) {
                                saving = true;
                                if (!Editable.this.equals(tmpChar)) {
                                    if (!tmpChar.name.equals(name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                        if (hasShortcut(ac))
                                            updateShortcut(ac);
                                        else
                                            addShortcut(ac);
                                    }
                                    save(getFileLocation(ac));
                                    if(((SWrpg)ac.getApplication()).vehicFold!=null)
                                        cloudSave(((SWrpg) ac.getApplication()).gac,
                                                getFileId(ac), false);
                                    tmpChar = Editable.this.clone();
                                }
                                saving = false;
                            }
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (editing);
                        if (!saving) {
                            saving = true;
                            if (!Editable.this.equals(tmpChar)) {
                                if (!tmpChar.name.equals(name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                    if (hasShortcut(ac))
                                        updateShortcut(ac);
                                    else
                                        addShortcut(ac);
                                }
                                save(getFileLocation(ac));
                                if(((SWrpg)ac.getApplication()).vehicFold!=null)
                                    cloudSave(((SWrpg) ac.getApplication()).gac,
                                            getFileId(ac), false);
                            }
                            saving = false;
                        }
                        return null;
                    }
                };
                blablah.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Thread tmp = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Editable tmpChar = Editable.this.clone();
                        save(getFileLocation(ac));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                            if (hasShortcut(ac))
                                updateShortcut(ac);
                            else
                                addShortcut(ac);
                        }
                        do {
                            if (!saving) {
                                saving = true;
                                if (!Editable.this.equals(tmpChar)) {
                                    if (!tmpChar.name.equals(Editable.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                        if (hasShortcut(ac))
                                            updateShortcut(ac);
                                        else {
                                            addShortcut(ac);
                                        }
                                    }
                                    save(getFileLocation(ac));
                                    tmpChar = Editable.this.clone();
                                }
                                saving = false;
                            }
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (editing);
                        if (!saving) {
                            saving = true;
                            if (!Editable.this.equals(tmpChar)) {
                                if (!tmpChar.name.equals(Editable.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                    if (hasShortcut(ac))
                                        updateShortcut(ac);
                                    else {
                                        addShortcut(ac);
                                    }
                                }
                                save(getFileLocation(ac));
                            }
                            saving = false;
                        }
                    }
                });
                tmp.start();
            }
        }
    }
    public void delete(final Activity main){
        File tmp = new File(getFileLocation(main));
        //noinspection ResultOfMethodCallIgnored
        tmp.delete();
        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false)){
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    getFileId(main).asDriveResource().delete(((SWrpg)main.getApplication()).gac).await();
                    return null;
                }
            };
            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            deleteShortcut(main);
    }

    public void stopEditing(){
        editing = false;
    }


    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void addShortcut(Activity ac){
        if(!name.equals("")&&!external){
            ShortcutManager mang = ac.getSystemService(ShortcutManager.class);
            String type = "";
            String data = "content://";
            Icon icon = null;
            if(this instanceof Vehicle) {
                data += "vehicle/";
                type = "vehicle/";
                icon = Icon.createWithResource(ac, R.drawable.vehic_launch);
            }else if(this instanceof Minion) {
                data += "minion/";
                type = "minion/";
                icon = Icon.createWithResource(ac, R.drawable.minion_launch);
            }else if(this instanceof Character) {
                data += "character/";
                type = "character/";
                icon = Icon.createWithResource(ac, R.drawable.char_launch);
            }
            data += String.valueOf(ID);
            Intent b = new Intent(Intent.ACTION_EDIT);
            b.setData(Uri.parse(data));
            b.setPackage(ac.getPackageName());
            b.setClass(ac,MainDrawer.class);
            ShortcutInfo sh = new ShortcutInfo.Builder(ac,type + String.valueOf(ID))
                    .setShortLabel(name)
                    .setLongLabel(name)
                    .setIcon(icon)
                    .setIntent(b)
                    .setActivity(ac.getComponentName())
                    .build();
            if (mang.getDynamicShortcuts().size() == 2)
                mang.removeDynamicShortcuts(Collections.singletonList(mang.getDynamicShortcuts().get(0).getId()));
            mang.addDynamicShortcuts(Collections.singletonList(sh));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private boolean hasShortcut(Activity ac){
        ShortcutManager manag = ac.getSystemService(ShortcutManager.class);
        String type = "";
        if(this instanceof Character)
            type = "character/";
        else if(this instanceof Minion)
            type = "minion/";
        else if(this instanceof Vehicle)
            type = "vehicle/";
        for(ShortcutInfo si:manag.getDynamicShortcuts()){
            if(si.getId().equals(type+String.valueOf(ID)))
                return true;
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void updateShortcut(Activity ac){
        if(!name.equals("")&&!external){
            ShortcutManager mang = ac.getSystemService(ShortcutManager.class);
            String type = "";
            String data = "content://";
            Icon icon = null;
            if(this instanceof Vehicle) {
                data += "vehicle/";
                type = "vehicle/";
                icon = Icon.createWithResource(ac, R.drawable.vehic_launch);
            }else if(this instanceof Minion) {
                data += "minion/";
                type = "minion/";
                icon = Icon.createWithResource(ac, R.drawable.minion_launch);
            }else if(this instanceof Character) {
                data += "character/";
                type = "character/";
                icon = Icon.createWithResource(ac, R.drawable.char_launch);
            }
            data += String.valueOf(ID);
            Intent b = new Intent(Intent.ACTION_EDIT);
            b.setData(Uri.parse(data));
            b.setPackage(ac.getPackageName());
            b.setClass(ac,MainDrawer.class);
            ShortcutInfo sh = new ShortcutInfo.Builder(ac,type + String.valueOf(ID))
                    .setShortLabel(name)
                    .setLongLabel(name)
                    .setIcon(icon)
                    .setIntent(b)
                    .setActivity(ac.getComponentName())
                    .build();
            mang.updateShortcuts(Collections.singletonList(sh));
        }else{
            deleteShortcut(ac);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void deleteShortcut(Activity ac){
        ShortcutManager manag = ac.getSystemService(ShortcutManager.class);
        String type = "";
        if(this instanceof Character)
            type = "character/";
        else if(this instanceof Minion)
            type = "minion/";
        else if(this instanceof Vehicle)
            type = "vehicle/";
        manag.removeDynamicShortcuts(Collections.singletonList(type+String.valueOf(ID)));
        manag.disableShortcuts(Collections.singletonList(type+String.valueOf(ID)));
    }
}
