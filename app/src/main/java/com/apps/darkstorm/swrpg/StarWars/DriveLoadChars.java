package com.apps.darkstorm.swrpg.StarWars;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.apps.darkstorm.swrpg.InitialConnect;
import com.apps.darkstorm.swrpg.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class DriveLoadChars {
    public ArrayList<Character> chars = new ArrayList<>();
    public ArrayList<Date> lastMod = new ArrayList<>();
    public DriveLoadChars(Context main, GoogleApiClient gac){
        if (main != null) {
            new InitialConnect(main, gac);
            SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            DriveId foldId = DriveId.decodeFromString(pref.getString(main.getString(R.string.swchars_id_key), ""));
            DriveFolder charsFold = foldId.asDriveFolder();
            DriveApi.MetadataBufferResult metbufres = charsFold.listChildren(gac).await();
            for (Metadata met : metbufres.getMetadataBuffer()) {
                if (!met.isFolder() && met.getFileExtension().equals("char") && !met.isTrashed()) {
                    Character tmp = new Character();
                    tmp.reLoad(gac, met.getDriveId());
                    chars.add(tmp);
                    lastMod.add(met.getModifiedDate());
                }
            }
            metbufres.release();
        }
    }
    public void saveToFile(Context main){File location;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            File tmp = Environment.getExternalStorageDirectory();
            location = new File(tmp.getAbsolutePath() + "/SWChars");
            if (!location.exists()){
                if (!location.mkdir()){
                    return;
                }
            }
        }else{
            File tmp = main.getFilesDir();
            location = new File(tmp.getAbsolutePath() + "/SWChars");
            if (!location.exists()){
                if (!location.mkdir()){
                    return;
                }
            }
        }
        SharedPreferences pref = main.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        String def = location.getAbsolutePath();
        String loc = pref.getString(main.getString(R.string.local_location_key),def);
        location = new File(loc);
        if (!location.exists()){
            if (!location.mkdir()){
                return;
            }
        }
        LoadChars lc = new LoadChars(main);
        location.delete();
        location.mkdir();
        for (Character chara:chars){
            chara.save(chara.getFileLocation(main));
        }
    }
}
