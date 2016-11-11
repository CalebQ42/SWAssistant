package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;

public class InitialConnect {
    public InitialConnect(Context main, GoogleApiClient gac){
        SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        DriveFolder root = Drive.DriveApi.getRootFolder(gac);
        DriveApi.MetadataBufferResult metBufRes = root.listChildren(gac).await();
        MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
        DriveId charsFold = null;
        for (Metadata met:metBuf){
            System.out.println(met.getTitle());
            if (met.isFolder() && met.getTitle().equals("SWChars")){
                charsFold = met.getDriveId();
            }
        }
        if (charsFold == null){
            DriveFolder.DriveFolderResult foldRes = root.createFolder(gac,new MetadataChangeSet.Builder().setTitle("SWChars").build()).await();
            charsFold = foldRes.getDriveFolder().getDriveId();
        }
        pref.edit().putString(main.getString(R.string.swchars_id_key),charsFold.encodeToString()).apply();
        metBuf.release();
    }
}
