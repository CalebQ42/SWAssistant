package com.apps.darkstorm.swrpg.assistant.drive;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.local.LoadLocal;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Editable;
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
    public abstract static class OnLoad{
        public abstract void onStart();
        public abstract boolean onLoad(Editable ed);
        public abstract void onFinish(ArrayList<Editable> characters);
    }
    public static class Characters {
        ArrayList<Date> lastMod;
        private ArrayList<Editable> characters;
        boolean done;
        private OnLoad ol;
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
                                protected void onPreExecute() {
                                    ol.onStart();
                                }

                                @Override
                                protected Void doInBackground(Void... params) {
                                    MetadataBuffer metBuf = metadataBufferResult.getMetadataBuffer();
                                    for (Metadata met : metBuf) {
                                        if (met.getTitle().endsWith(".char")) {
                                            Character tmp = new Character();
                                            tmp.reLoadLegacy(((SWrpg) ac.getApplication()).gac, met.getDriveId());
                                            System.out.println("Loading: "+tmp.name +" legacy");
                                            characters.add(tmp);
                                            lastMod.add(met.getModifiedDate());
                                            boolean br = ol.onLoad(tmp);
                                            if (br)
                                                break;
                                            met.getDriveId().asDriveResource().delete(((SWrpg) ac.getApplication()).gac);
                                            tmp.save(((SWrpg) ac.getApplication()).gac,tmp.getFileId(ac),false);
                                        }else if(met.getTitle().endsWith(Character.fileExtension)){
                                            Character tmp = new Character();
                                            tmp.load(((SWrpg)ac.getApplication()).gac,met.getDriveId(),true);
                                            System.out.println("Loading: "+tmp.name);
                                            characters.add(tmp);
                                            lastMod.add(met.getModifiedDate());
                                            boolean br = ol.onLoad(tmp);
                                            if(br)
                                                break;
                                        }
                                    }
                                    metBuf.release();
                                    metadataBufferResult.release();
                                    done = true;
                                    ol.onFinish(characters);
                                    return null;
                                }
                            };
                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        done = true;
                        ol.onFinish(characters);
                    }
                }
            });
        }
        @SuppressWarnings("ResultOfMethodCallIgnored")
        public void saveLocal(Activity ac){
            File fold = new File(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),((SWrpg)ac.getApplication()).defaultLoc));
            if (!fold.exists()){
                fold.mkdirs();
                fold.mkdir();
            }
            Character[] ch = LoadLocal.characters(ac);
            for(Character c:ch) {
                boolean found = false;
                for(int i = 0;i<characters.size();i++) {
                    Editable ed = characters.get(i);
                    if(ed.ID==c.ID) {
                        found = true;
                        Date local = new Date(new File(c.getFileLocation(ac)).lastModified());
                        if(local.after(lastMod.get(i)))
                            c.save(((SWrpg)ac.getApplication()).gac,c.getFileId(ac),false);
                        else
                            ed.save(ed.getFileLocation(ac));
                        break;
                    }
                }
                if(!found)
                    c.delete(ac);
            }
        }
        public void setOnFinish(OnLoad ol){
            this.ol = ol;
        }
    }
    public static class Minions {
        ArrayList<Date> lastMod;
        private ArrayList<Editable> minions;
        boolean done;
        private OnLoad ol;
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
                            protected void onPreExecute() {
                                ol.onStart();
                            }
                            @Override
                            protected Void doInBackground(Void... params) {
                                MetadataBuffer metBuf = metadataBufferResult.getMetadataBuffer();
                                for (Metadata met : metBuf) {
                                    if (met.getTitle().endsWith(".minion")) {
                                        Minion tmp = new Minion();
                                        tmp.reLoadLegacy(((SWrpg) ac.getApplication()).gac, met.getDriveId());
                                        minions.add(tmp);
                                        lastMod.add(met.getModifiedDate());
                                        boolean br = ol.onLoad(tmp);
                                        if (br)
                                            break;
                                        met.getDriveId().asDriveResource().delete(((SWrpg) ac.getApplication()).gac);
                                        tmp.save(((SWrpg) ac.getApplication()).gac,tmp.getFileId(ac),false);
                                    }else if(met.getTitle().endsWith(Minion.fileExtension)){
                                        Minion tmp = new Minion();
                                        tmp.load(((SWrpg)ac.getApplication()).gac,met.getDriveId(),true);
                                        minions.add(tmp);
                                        lastMod.add(met.getModifiedDate());
                                        boolean br = ol.onLoad(tmp);
                                        if(br)
                                            break;
                                    }
                                }
                                metBuf.release();
                                metadataBufferResult.release();
                                done = true;
                                ol.onFinish(minions);
                                return null;
                            }
                        };
                        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        done = true;
                        ol.onFinish(minions);
                    }
                }
            });
        }
        @SuppressWarnings("ResultOfMethodCallIgnored")
        public void saveLocal(Activity ac){
            File fold = new File(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),((SWrpg)ac.getApplication()).defaultLoc));
            if (!fold.exists()){
                fold.mkdirs();
                fold.mkdir();
            }
            Minion[] ch = LoadLocal.minions(ac);
            for(Minion c:ch) {
                boolean found = false;
                for(int i = 0;i<minions.size();i++) {
                    Editable ed = minions.get(i);
                    if(ed.ID==c.ID) {
                        found = true;
                        Date local = new Date(new File(c.getFileLocation(ac)).lastModified());
                        if(local.after(lastMod.get(i)))
                            c.save(((SWrpg)ac.getApplication()).gac,c.getFileId(ac),false);
                        else
                            ed.save(ed.getFileLocation(ac));
                        break;
                    }
                }
                if(!found)
                    c.delete(ac);
            }
        }
        public void setOnFinish(OnLoad ol){
            this.ol = ol;
        }
    }
    public static class Vehicles {
        ArrayList<Date> lastMod;
        private ArrayList<Editable> vehicles;
        boolean done;
        private OnLoad ol;
        public void load(final Activity ac){
            lastMod = new ArrayList<>();
            vehicles = new ArrayList<>();
            DriveFolder vhFold = ((SWrpg)ac.getApplication()).charsFold;
            vhFold.listChildren(((SWrpg)ac.getApplication()).gac).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(@NonNull final DriveApi.MetadataBufferResult metadataBufferResult) {
                    if (metadataBufferResult.getStatus().isSuccess()){
                        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected void onPreExecute() {
                                ol.onStart();
                            }
                            @Override
                            protected Void doInBackground(Void... params) {
                                MetadataBuffer metBuf = metadataBufferResult.getMetadataBuffer();
                                for (Metadata met : metBuf) {
                                    if (met.getTitle().endsWith(".vhcl")) {
                                        Vehicle tmp = new Vehicle();
                                        tmp.reLoadLegacy(((SWrpg) ac.getApplication()).gac, met.getDriveId());
                                        vehicles.add(tmp);
                                        lastMod.add(met.getModifiedDate());
                                        boolean br = ol.onLoad(tmp);
                                        if (br)
                                            break;
                                        met.getDriveId().asDriveResource().delete(((SWrpg) ac.getApplication()).gac);
                                        tmp.save(((SWrpg) ac.getApplication()).gac,tmp.getFileId(ac),false);
                                    }else if(met.getTitle().endsWith(Vehicle.fileExtension)){
                                        Vehicle tmp = new Vehicle();
                                        tmp.load(((SWrpg)ac.getApplication()).gac,met.getDriveId(),true);
                                        vehicles.add(tmp);
                                        lastMod.add(met.getModifiedDate());
                                        boolean br = ol.onLoad(tmp);
                                        if(br)
                                            break;
                                    }
                                }
                                metBuf.release();
                                metadataBufferResult.release();
                                done = true;
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                ol.onFinish(vehicles);
                            }
                        };
                        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        done = true;
                        ol.onFinish(vehicles);
                    }
                }
            });
        }
        @SuppressWarnings("ResultOfMethodCallIgnored")
        public void saveLocal(Activity ac){
            File fold = new File(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),((SWrpg)ac.getApplication()).defaultLoc));
            if (!fold.exists()){
                fold.mkdirs();
                fold.mkdir();
            }
            Vehicle[] ch = LoadLocal.vehicles(ac);
            for(Vehicle c:ch) {
                boolean found = false;
                for(int i = 0;i<vehicles.size();i++) {
                    Editable ed = vehicles.get(i);
                    if(ed.ID==c.ID) {
                        found = true;
                        Date local = new Date(new File(c.getFileLocation(ac)).lastModified());
                        if(local.after(lastMod.get(i)))
                            c.save(((SWrpg)ac.getApplication()).gac,c.getFileId(ac),false);
                        else
                            ed.save(ed.getFileLocation(ac));
                        break;
                    }
                }
                if(!found)
                    c.delete(ac);
            }
        }
        public void setOnFinish(OnLoad ol){
            this.ol = ol;
        }
    }
}
