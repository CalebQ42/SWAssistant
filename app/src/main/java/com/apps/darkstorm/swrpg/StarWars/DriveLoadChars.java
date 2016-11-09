package com.apps.darkstorm.swrpg.StarWars;

import android.content.Context;
import android.content.SharedPreferences;

import com.apps.darkstorm.swrpg.InitialConnect;
import com.apps.darkstorm.swrpg.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;

import java.util.ArrayList;

public class DriveLoadChars {
    public ArrayList<Character> chars = new ArrayList<>();
    public ArrayList<Long> lastMod = new ArrayList<>();
    public DriveLoadChars(Context main, GoogleApiClient gac){
        SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String tmpId = pref.getString(main.getString(R.string.swchars_id_key),"");
        if (tmpId.equals("")){
            new InitialConnect(main,gac);
        }
        DriveId foldId = DriveId.decodeFromString(pref.getString(main.getString(R.string.swchars_id_key),""));
        DriveFolder charsFold = foldId.asDriveFolder();
        DriveApi.MetadataBufferResult metbufres = charsFold.listChildren(gac).await();
        for (Metadata met:metbufres.getMetadataBuffer()){
            if (!met.isFolder() && met.getFileExtension().equals("char")){
                Character tmp = new Character();
                tmp.reLoad(gac,met.getDriveId());
                chars.add(tmp);
                lastMod.add(met.getModifiedDate().getTime());
            }
        }
        metbufres.release();
    }
    public void saveToFile(Context main, boolean simple){
        if (!simple){
            LoadChars lc = new LoadChars(main);
            for (int i = 0;i<chars.size();i++){
                if (lc.chars.size()>i){
                    if (lc.lastMod.get(i) <= lastMod.get(i)){
                        chars.get(i).save(chars.get(i).getFileLocation(main));
                    }
                }else{
                    chars.get(i).save(chars.get(i).getFileLocation(main));
                }
            }
        }else{
            for (Character chara:chars){
                chara.save(chara.getFileLocation(main));
            }
        }
    }
}
