package com.apps.darkstorm.swrpg.assistant.drive;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Load {
    public abstract static class onFinish{
        public abstract void finish();
    }
    public static class Characters {
        public ArrayList<Date> lastMod;
        public ArrayList<Character> characters;
        public boolean done;
        private onFinish of;
        public void load(final Activity ac){
            lastMod = new ArrayList<>();
            characters = new ArrayList<>();
            DriveFolder charsFold = ((SWrpg)ac.getApplication()).charsFold;
            charsFold.listChildren(((SWrpg)ac.getApplication()).gac).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(@NonNull final DriveApi.MetadataBufferResult metadataBufferResult) {
                    if (metadataBufferResult.getStatus().isSuccess()){
                            AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    MetadataBuffer metBuf = metadataBufferResult.getMetadataBuffer();
                                    for (Metadata met : metBuf) {
                                        if (met.getTitle().endsWith(".char")) {
                                            Character tmp = new Character();
                                            tmp.reLoad(((SWrpg) ac.getApplication()).gac, met.getDriveId());
                                            characters.add(tmp);
                                            lastMod.add(met.getModifiedDate());
                                        }
                                    }
                                    metBuf.release();
                                    metadataBufferResult.release();
                                    done = true;
                                    of.finish();
                                    return null;
                                }
                            };
                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        done = true;
                        of.finish();
                    }
                }
            });
        }
        public void saveLocal(Activity ac){
            File fold = new File(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),((SWrpg)ac.getApplication()).defaultLoc));
            if (!fold.exists()){
                fold.mkdirs();
                fold.mkdir();
            }
            if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.sync_key),true)){
                for (File f:fold.listFiles()){
                    if (f.getName().endsWith(".char"))
                        f.delete();
                }
                for (Character c: characters)
                    c.save(c.getFileLocation(ac));
            }else {
                Character[] ch = LoadLocal.characters(ac);
                for (int i = 0; i < characters.size(); i++) {
                    Character drch = characters.get(i);
                    boolean found = false;
                    for (Character c : ch) {
                        if (drch.ID == c.ID) {
                            File local = new File(c.getFileLocation(ac));
                            if (new Date(local.lastModified()).before(lastMod.get(i)))
                                drch.save(drch.getFileLocation(ac));
                            found = true;
                        }
                    }
                    if (!found)
                        drch.save(drch.getFileLocation(ac));
                }
            }
        }
        public void setOnFinish(onFinish of){
            this.of = of;
        }
    }
    public static class Vehicles {
        public ArrayList<Date> lastMod;
        public ArrayList<Vehicle> vehicles;
        public boolean done;
        private onFinish of;
        public void load(final Activity ac){
            lastMod = new ArrayList<>();
            vehicles = new ArrayList<>();
            DriveFolder vehicFold = ((SWrpg)ac.getApplication()).vehicFold;
            vehicFold.listChildren(((SWrpg)ac.getApplication()).gac).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(@NonNull final DriveApi.MetadataBufferResult metadataBufferResult) {
                    if (metadataBufferResult.getStatus().isSuccess()){
                            AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    MetadataBuffer metBuf = metadataBufferResult.getMetadataBuffer();
                                    for (Metadata met : metBuf) {
                                        if (met.getTitle().endsWith(".vhcl")) {
                                            Vehicle tmp = new Vehicle();
                                            tmp.reLoad(((SWrpg) ac.getApplication()).gac, met.getDriveId());
                                            vehicles.add(tmp);
                                            lastMod.add(met.getModifiedDate());
                                        }
                                    }
                                    metBuf.release();
                                    metadataBufferResult.release();
                                    done = true;
                                    of.finish();
                                    return null;
                                }
                            };
                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        done = true;
                        of.finish();
                    }
                }
            });
        }
        public void saveLocal(Activity ac){
            File fold = new File(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),((SWrpg)ac.getApplication()).defaultLoc) + "/SWShips");
            if (!fold.exists()){
                fold.mkdirs();
                fold.mkdir();
            }
            if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.sync_key),true)){
                for (File f:fold.listFiles()){
                    if (f.getName().endsWith(".vhcl"))
                        f.delete();
                }
                for (Vehicle c: vehicles)
                    c.save(c.getFileLocation(ac));
            }else {
                Vehicle[] ch = LoadLocal.vehicles(ac);
                for (int i = 0; i < vehicles.size(); i++) {
                    Vehicle drch = vehicles.get(i);
                    boolean found = false;
                    for (Vehicle c : ch) {
                        if (drch.ID == c.ID) {
                            File local = new File(c.getFileLocation(ac));
                            if (new Date(local.lastModified()).before(lastMod.get(i)))
                                drch.save(drch.getFileLocation(ac));
                            found = true;
                        }
                    }
                    if (!found)
                        drch.save(drch.getFileLocation(ac));
                }
            }
        }
        public void setOnFinish(onFinish of){
            this.of = of;
        }
    }
    public static class Minions{
        public ArrayList<Date> lastMod;
        public ArrayList<Minion> minions;
        public boolean done;
        private onFinish of;
        public void load(final Activity ac){
            lastMod = new ArrayList<>();
            minions = new ArrayList<>();
            DriveFolder charsFold = ((SWrpg)ac.getApplication()).charsFold;
            charsFold.listChildren(((SWrpg)ac.getApplication()).gac).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(@NonNull final DriveApi.MetadataBufferResult metadataBufferResult) {
                    if (metadataBufferResult.getStatus().isSuccess()){
                            AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    MetadataBuffer metBuf = metadataBufferResult.getMetadataBuffer();
                                    for (Metadata met : metBuf) {
                                        if (met.getTitle().endsWith(".minion")) {
                                            Minion tmp = new Minion();
                                            tmp.reLoad(((SWrpg) ac.getApplication()).gac, met.getDriveId());
                                            minions.add(tmp);
                                            lastMod.add(met.getModifiedDate());
                                        }
                                    }
                                    metBuf.release();
                                    metadataBufferResult.release();
                                    done = true;
                                    of.finish();
                                    return null;
                                }
                            };
                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        done = true;
                        of.finish();
                    }
                }
            });
        }
        public void saveLocal(Activity ac){
            File fold = new File(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),((SWrpg)ac.getApplication()).defaultLoc));
            if (!fold.exists()){
                fold.mkdirs();
                fold.mkdir();
            }
            if (((SWrpg)ac.getApplication()).prefs.getBoolean(ac.getString(R.string.sync_key),true)){
                for (File f:fold.listFiles()){
                    if (f.getName().endsWith(".minion"))
                        f.delete();
                }
                for (Minion c: minions)
                    c.save(c.getFileLocation(ac));
            }else {
                Minion[] ch = LoadLocal.minions(ac);
                for (int i = 0; i < minions.size(); i++) {
                    Minion drch = minions.get(i);
                    boolean found = false;
                    for (Minion c : ch) {
                        if (drch.ID == c.ID) {
                            File local = new File(c.getFileLocation(ac));
                            if (new Date(local.lastModified()).before(lastMod.get(i)))
                                drch.save(drch.getFileLocation(ac));
                            found = true;
                        }
                    }
                    if (!found)
                        drch.save(drch.getFileLocation(ac));
                }
            }
        }
        public void setOnFinish(onFinish of){
            this.of = of;
        }
    }
}
