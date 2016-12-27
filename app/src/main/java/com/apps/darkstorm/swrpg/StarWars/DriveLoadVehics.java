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

public class DriveLoadVehics {
    public ArrayList<Vehicle> vehics = new ArrayList<>();
    private ArrayList<Date> lastMod = new ArrayList<>();
    public DriveLoadVehics(Activity main){
        if (((SWrpg)main.getApplication()).gac.isConnected() && ((SWrpg)main.getApplication()).initConnect) {
            SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            DriveId foldId = DriveId.decodeFromString(pref.getString(main.getString(R.string.ships_id_key), ""));
            DriveFolder charsFold = foldId.asDriveFolder();
            DriveApi.MetadataBufferResult metbufres = charsFold.queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder().build()).await();
            for (Metadata met : metbufres.getMetadataBuffer()) {
                if (met.getFileExtension() != null) {
                    if ((!met.isFolder() && met.getFileExtension().equals("vhcl")) && !met.isTrashed()) {
                        Vehicle tmp = new Vehicle();
                        tmp.reLoad(((SWrpg)main.getApplication()).gac, met.getDriveId());
                        vehics.add(tmp);
                        lastMod.add(met.getModifiedDate());
                        System.out.println(met.getTitle());
                    }
                }
            }
            metbufres.release();
        }
    }
    public void saveToFile(Activity main){
        if (main != null) {
            LoadVehics lc = new LoadVehics(main);
            SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            ArrayList<Integer> has = new ArrayList<>();
            for (int i = 0; i < vehics.size(); i++) {
                boolean resolved = false;
                for (int j = 0; j < lc.vehics.size(); j++) {
                    if (vehics.get(i).ID == lc.vehics.get(j).ID) {
                        has.add(vehics.get(i).ID);
                        resolved = true;
                        if (lastMod.get(i).before(lc.lastMod.get(j))) {
                            lc.vehics.get(j).cloudSave(((SWrpg)main.getApplication()).gac,
                                    lc.vehics.get(j).getFileId(((SWrpg)main.getApplication()).gac,
                                    DriveId.decodeFromString(pref.getString(main.getString(R.string.ships_id_key), ""))), false);
                        } else if (lastMod.get(i).after(lc.lastMod.get(j))) {
                            vehics.get(i).save(vehics.get(i).getFileLocation(main));
                        } else if (!vehics.get(i).equals(vehics.get(j))) {
                            vehics.get(i).save(vehics.get(i).getFileLocation(main));
                        }
                        break;
                    }
                }
                if (!resolved) {
                    vehics.get(i).save(vehics.get(i).getFileLocation(main));
                }
            }
            if (has.size() != lc.vehics.size()) {
                for (int i = 0; i < lc.vehics.size(); i++) {
                    if (!has.contains(lc.vehics.get(i).ID)) {
                        if (!pref.getBoolean(main.getString(R.string.sync_key), true)) {
                            lc.vehics.get(i).cloudSave(((SWrpg)main.getApplication()).gac,
                                    lc.vehics.get(i).getFileId(((SWrpg)main.getApplication()).gac,
                                    DriveId.decodeFromString(pref.getString(main.getString(R.string.ships_id_key), ""))), false);
                        } else {
                            File tmp = new File(lc.vehics.get(i).getFileLocation(main));
                            tmp.delete();
                        }
                    }
                }
            }
        }
    }
}
