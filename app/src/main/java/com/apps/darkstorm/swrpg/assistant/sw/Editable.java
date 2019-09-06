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
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.apps.darkstorm.swrpg.assistant.EditGeneral;
import com.apps.darkstorm.swrpg.assistant.MainDrawer;
import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Notes;
import com.apps.darkstorm.swrpg.assistant.sw.stuff.Weapons;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;

public abstract class Editable implements JsonSavable{
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
    public abstract void setupCards(Activity ac, EditGeneral.EditableAdap ea, CardView c, int pos, Handler parentHandle);
    public abstract void reLoadLegacy(GoogleApiClient gac, DriveId fil);
    public abstract void reLoadLegacy(String filename);
    public abstract Editable clone();
    public abstract String getFileExtension();
    public void exportTo(String folder){
        File fold = new File(folder);
        if(!fold.exists()) {
            if (!fold.mkdir())
                return;
        }
        File f = new File(folder+"/"+name+getFileExtension());
        if(f.exists()){
            if(!f.delete())
                return;
        }
        save(folder+"/"+name+getFileExtension());
    }
    public String getFileLocation(Activity main){
        if(main!= null) {
            if(external)
                return this.loc;
            String loc = ((SWrpg) main.getApplication()).prefs.getString(main.getString(R.string.local_location_key),
                    ((SWrpg) main.getApplication()).defaultLoc);
            File location = new File(loc);
            if (!location.exists()) {
                if (!location.mkdir()) {
                    return "";
                }
            }
            return location.getAbsolutePath() + "/" + Integer.toString(ID) + getFileExtension();
        }else{
            return "";
        }
    }
    public DriveId getFileId(final Activity main){
        if(external)
            return null;
        final boolean[] finished = {false};
        final String name = Integer.toString(ID) + getFileExtension();
        final DriveId[] fi = {null};
        ((SWrpg)main.getApplication())
                .charsFold.queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder().addFilter(
                        Filters.eq(SearchableField.TITLE,name)).build()).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.MetadataBufferResult res) {
                        if (res.getStatus().isSuccess()){
                            for (Metadata met:res.getMetadataBuffer()){
                                if (!met.isTrashed()){
                                    fi[0] = met.getDriveId();
                                    break;
                                }
                            }
                            res.release();
                            if (fi[0] == null){
                                final boolean[] blocking = {true};
                                ((SWrpg)main.getApplication()).charsFold.createFile
                                        (((SWrpg)main.getApplication())
                                                .gac,new MetadataChangeSet.Builder().setTitle(name).build(),null)
                                        .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                            @Override
                                            public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                                fi[0] = driveFileResult.getDriveFile().getDriveId();
                                                blocking[0]=false;
                                            }
                                        });
                                while(blocking[0]){
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            finished[0] = true;
                        }else
                            finished[0] = true;
                    }
                });
        while(!finished[0]){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return fi[0];
    }
    public DriveId getFileId(Activity main,DriveFolder fold){
        if(external)
            return null;
        String name = Integer.toString(ID) + getFileExtension();
        DriveId fi = null;
        DriveApi.MetadataBufferResult res = fold.queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder().addFilter(
                        Filters.eq(SearchableField.TITLE,name)).build()).await();
        for (Metadata met:res.getMetadataBuffer()){
            if (!met.isTrashed()){
                fi = met.getDriveId();
                break;
            }
        }
        res.release();
        if (fi == null){
            fi = fold.createFile
                    (((SWrpg)main.getApplication())
                            .gac,new MetadataChangeSet.Builder().setTitle(name).build(),null).await()
                    .getDriveFile().getDriveId();
        }
        return fi;
    }
    public void save(String filename){
        try {
            File savFolder = null;
            if(filename.contains("/")) {
                savFolder = new File(filename.substring(0, filename.lastIndexOf("/")));
            }
            if(savFolder != null && !savFolder.exists())
                savFolder.mkdirs();
            File tmp = new File(filename);
            tmp.renameTo(new File(filename+".bak"));
            tmp = new File(filename + ".bak");
            new File(filename).delete();
            JsonWriter jw = new JsonWriter(new FileWriter(filename));
            jw.setLenient(true);
            jw.setIndent("  ");
            jw.beginObject();
            saveJson(jw);
            jw.endObject();
            jw.close();
            tmp.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void save(final GoogleApiClient gac, final DriveId fil,boolean blocking){
        if (fil != null) {
            if(!blocking){
                fil.asDriveFile().open(gac, DriveFile.MODE_WRITE_ONLY,null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                        if (driveContentsResult.getStatus().isSuccess()) {
                            try {
                                DriveContents cont = driveContentsResult.getDriveContents();
                                OutputStream os = cont.getOutputStream();
                                OutputStreamWriter osw = new OutputStreamWriter(os);
                                JsonWriter jw = new JsonWriter(osw);
                                jw.setLenient(true);
                                jw.beginObject();
                                saveJson(jw);
                                jw.endObject();
                                jw.close();
                                osw.close();
                                os.close();
                                cont.commit(gac, null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }else{
                DriveApi.DriveContentsResult dcr = fil.asDriveFile().open(gac, DriveFile.MODE_WRITE_ONLY,null).await();
                if(dcr.getStatus().isSuccess()){
                    try {
                        DriveContents cont = dcr.getDriveContents();
                        OutputStream os = cont.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        JsonWriter jw = new JsonWriter(osw);
                        jw.setLenient(true);
                        jw.beginObject();
                        saveJson(jw);
                        jw.endObject();
                        jw.close();
                        osw.close();
                        os.close();
                        cont.commit(gac, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void load(String filename){
        try {
            JsonReader jr = new JsonReader(new FileReader(filename));
            jr.setLenient(true);
            if (jr.peek().equals(JsonToken.STRING)) {
                jr.close();
                reLoadLegacy(filename);
            }else if (jr.peek().equals(JsonToken.BEGIN_OBJECT)) {
                jr.beginObject();
                loadJson(jr);
                jr.endObject();
                jr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void load(GoogleApiClient gac,DriveId fil,boolean blocking){
        if (fil != null) {
            if(!blocking) {
                fil.asDriveFile().open(gac, DriveFile.MODE_READ_ONLY,null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                        if (driveContentsResult.getStatus().isSuccess()) {
                            try {
                                DriveContents dc = driveContentsResult.getDriveContents();
                                InputStream is = dc.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                JsonReader jw = new JsonReader(isr);

                                jw.beginObject();
                                loadJson(jw);
                                jw.endObject();
                                jw.close();
                                isr.close();
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }else{
                DriveApi.DriveContentsResult dcr = fil.asDriveFile().open(gac,DriveFile.MODE_READ_ONLY,null).await();
                if(dcr.getStatus().isSuccess()){
                    try {
                        DriveContents dc = dcr.getDriveContents();
                        InputStream is = dc.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        JsonReader jw = new JsonReader(isr);
                        jw.beginObject();
                        loadJson(jw);
                        jw.endObject();
                        jw.close();
                        isr.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
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
                        if(((SWrpg)ac.getApplication()).charsFold!=null)
                            save(((SWrpg) ac.getApplication()).gac, getFileId(ac),true);
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
                                    if(((SWrpg)ac.getApplication()).charsFold!=null)
                                        save(((SWrpg) ac.getApplication()).gac, getFileId(ac),true);
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
                                if(((SWrpg)ac.getApplication()).charsFold!=null)
                                    save(((SWrpg) ac.getApplication()).gac, getFileId(ac),true);
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
