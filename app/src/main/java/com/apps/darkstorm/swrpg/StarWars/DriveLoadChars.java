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

public class DriveLoadChars {
    public ArrayList<Character> chars = new ArrayList<>();
    private ArrayList<Date> lastMod = new ArrayList<>();
    public DriveLoadChars(Activity main){
        if (((SWrpg)main.getApplication()).gac.isConnected() && ((SWrpg)main.getApplication()).initConnect) {
            SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            DriveId foldId = DriveId.decodeFromString(pref.getString(main.getString(R.string.swchars_id_key), ""));
            DriveFolder charsFold = foldId.asDriveFolder();
            DriveApi.MetadataBufferResult metbufres = charsFold.queryChildren(((SWrpg)main.getApplication()).gac
                    ,new Query.Builder().build()).await();
            for (Metadata met : metbufres.getMetadataBuffer()) {
                if (met.getFileExtension() != null) {
                    if ((!met.isFolder() && met.getFileExtension().equals("char")) && !met.isTrashed()) {
                        Character tmp = new Character();
                        tmp.reLoad(((SWrpg)main.getApplication()).gac, met.getDriveId());
                        chars.add(tmp);
                        lastMod.add(met.getModifiedDate());
                    }
                }
            }
            metbufres.release();
        }
    }
    public void saveToFile(Activity main){
        if (main != null) {
            LoadChars lc = new LoadChars(main);
            SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            ArrayList<Integer> has = new ArrayList<>();
            for (int i = 0; i < chars.size(); i++) {
                boolean resolved = false;
                for (int j = 0; j < lc.chars.size(); j++) {
                    if (chars.get(i).ID == lc.chars.get(j).ID) {
                        has.add(chars.get(i).ID);
                        resolved = true;
                        if (lastMod.get(i).before(lc.lastMod.get(j))) {
                            lc.chars.get(j).cloudSave(((SWrpg)main.getApplication()).gac,
                                    lc.chars.get(j).getFileId(((SWrpg)main.getApplication()).gac,
                                    DriveId.decodeFromString(pref.getString(main.getString(R.string.swchars_id_key), ""))), false);
                        } else if (lastMod.get(i).after(lc.lastMod.get(j))) {
                            chars.get(i).save(chars.get(i).getFileLocation(main));
                        } else if (!chars.get(i).equals(chars.get(j))) {
                            chars.get(i).save(chars.get(i).getFileLocation(main));
                        }
                        break;
                    }
                }
                if (!resolved) {
                    chars.get(i).save(chars.get(i).getFileLocation(main));
                }
            }
            if (has.size() != lc.chars.size()) {
                for (int i = 0; i < lc.chars.size(); i++) {
                    if (!has.contains(lc.chars.get(i).ID)) {
                        if (!pref.getBoolean(main.getString(R.string.sync_key), true)) {
                            lc.chars.get(i).cloudSave(((SWrpg)main.getApplication()).gac,
                                    lc.chars.get(i).getFileId(((SWrpg)main.getApplication()).gac,
                                    DriveId.decodeFromString(pref.getString(main.getString(R.string.swchars_id_key), ""))), false);
                        } else {
                            File tmp = new File(lc.chars.get(i).getFileLocation(main));
                            tmp.delete();
                        }
                    }
                }
            }
        }
    }
}
