package com.apps.darkstorm.swrpg.StarWars;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.query.Query;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class DriveLoadMinions {
    public ArrayList<Minion> minions = new ArrayList<>();
    private ArrayList<Date> lastMod = new ArrayList<>();
    public DriveLoadMinions(Activity main){
        if (((SWrpg)main.getApplication()).gac.isConnected() && ((SWrpg)main.getApplication()).initConnect) {
            SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            DriveId foldId = DriveId.decodeFromString(pref.getString(main.getString(R.string.swchars_id_key), ""));
            DriveFolder charsFold = foldId.asDriveFolder();
            DriveApi.MetadataBufferResult metbufres = charsFold.queryChildren(((SWrpg)main.getApplication()).gac
                    ,new Query.Builder().build()).await();
            for (Metadata met : metbufres.getMetadataBuffer()) {
                if (met.getFileExtension() != null) {
                    if ((!met.isFolder() && met.getFileExtension().equals("minion")) && !met.isTrashed()) {
                        Minion tmp = new Minion();
                        tmp.reLoad(((SWrpg)main.getApplication()).gac, met.getDriveId());
                        minions.add(tmp);
                        lastMod.add(met.getModifiedDate());
                    }
                }
            }
            metbufres.release();
        }
    }
    public void saveToFile(Activity main){
        if (main != null) {
            LoadMinions lc = new LoadMinions(main);
            SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            ArrayList<Integer> has = new ArrayList<>();
            for (int i = 0; i < minions.size(); i++) {
                boolean resolved = false;
                for (int j = 0; j < lc.minions.size(); j++) {
                    if (minions.get(i).ID == lc.minions.get(j).ID) {
                        has.add(minions.get(i).ID);
                        resolved = true;
                        if (lastMod.get(i).before(lc.lastMod.get(j))) {
                            lc.minions.get(j).cloudSave(((SWrpg)main.getApplication()).gac,
                                    lc.minions.get(j).getFileId(((SWrpg)main.getApplication()).gac,
                                            DriveId.decodeFromString(pref.getString(main.getString(R.string.swchars_id_key), ""))), false);
                        } else if (lastMod.get(i).after(lc.lastMod.get(j))) {
                            minions.get(i).save(minions.get(i).getFileLocation(main));
                        } else if (!minions.get(i).equals(minions.get(j))) {
                            minions.get(i).save(minions.get(i).getFileLocation(main));
                        }
                        break;
                    }
                }
                if (!resolved) {
                    minions.get(i).save(minions.get(i).getFileLocation(main));
                }
            }
            if (has.size() != lc.minions.size()) {
                for (int i = 0; i < lc.minions.size(); i++) {
                    if (!has.contains(lc.minions.get(i).ID)) {
                        if (!pref.getBoolean(main.getString(R.string.sync_key), true)) {
                            lc.minions.get(i).cloudSave(((SWrpg)main.getApplication()).gac,
                                    lc.minions.get(i).getFileId(((SWrpg)main.getApplication()).gac,
                                            DriveId.decodeFromString(pref.getString(main.getString(R.string.swchars_id_key), ""))), false);
                        } else {
                            File tmp = new File(lc.minions.get(i).getFileLocation(main));
                            tmp.delete();
                        }
                    }
                }
            }
        }
    }
}