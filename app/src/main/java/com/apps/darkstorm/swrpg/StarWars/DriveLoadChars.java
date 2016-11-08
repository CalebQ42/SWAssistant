package com.apps.darkstorm.swrpg.StarWars;

import android.content.Context;
import android.content.SharedPreferences;

import com.apps.darkstorm.swrpg.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;

import java.util.ArrayList;

public class DriveLoadChars {
    ArrayList<Character> chars;
    ArrayList<Long> lastMod;
    public DriveLoadChars(Context main, GoogleApiClient gac){
        SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        DriveFolder root = Drive.DriveApi.getRootFolder(gac);
        DriveApi.MetadataBufferResult metBufRes = root.listChildren(gac).await();
        MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
        DriveId charsFold = null;
        for (Metadata met:metBuf){
            if (met.isFolder() && met.getTitle().equals("SWChars")){
                charsFold = met.getDriveId();
                break;
            }
        }
        if (charsFold == null){
            DriveFolder.DriveFolderResult foldRes = root.createFolder(gac,new MetadataChangeSet.Builder().setTitle("SWChars").build()).await();

        }
    }
}
