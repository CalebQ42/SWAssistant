package com.apps.darkstorm.swrpg.load;

import android.app.Activity;
import android.os.AsyncTask;

import com.apps.darkstorm.swrpg.SWrpg;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

public class InitialConnect {
    public static void connect(final Activity main){
        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DriveFolder root = Drive.DriveApi.getRootFolder(((SWrpg)main.getApplication()).gac);
                MetadataBuffer metBuf;
                DriveApi.MetadataBufferResult metBufRes;
                int countdown = 0;
                do {
                    metBufRes = root.queryChildren(((SWrpg) main.getApplication()).gac,
                            new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE, "SWChars"))
                                    .addFilter(Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE)).build()).await();
                    metBuf = metBufRes.getMetadataBuffer();
                    countdown++;
                    if (metBuf.getCount() == 0 && countdown < 5){
                        metBufRes.release();
                        metBuf.release();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }while(metBuf.getCount() == 0 && countdown <5);
                DriveId charsFold = null;
                for (Metadata met:metBuf){
                    if (met.getTitle().equals("SWChars") && !met.isTrashed()){
                        charsFold = met.getDriveId();
                        break;
                    }
                }
                metBuf.release();
                metBufRes.release();
                if (charsFold == null){
                    DriveFolder.DriveFolderResult foldRes = root.createFolder(((SWrpg)main.getApplication()).gac,
                            new MetadataChangeSet.Builder().setTitle("SWChars").build()).await();
                    charsFold = foldRes.getDriveFolder().getDriveId();
                }
                ((SWrpg)main.getApplication()).charsFold = charsFold.asDriveFolder();
                DriveFolder fold = charsFold.asDriveFolder();
                metBufRes = fold.queryChildren(((SWrpg)main.getApplication()).gac,
                        new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE,"SWShips"))
                                .addFilter(Filters.eq(SearchableField.MIME_TYPE,DriveFolder.MIME_TYPE)).build()).await();
                metBuf = metBufRes.getMetadataBuffer();
                DriveId shipsFold = null;
                for (Metadata met:metBuf){
                    if (met.isFolder() && met.getTitle().equals("SWShips") && !met.isTrashed()){
                        shipsFold = met.getDriveId();
                        break;
                    }
                }
                metBuf.release();
                metBufRes.release();
                if (shipsFold == null){
                    DriveFolder.DriveFolderResult foldRes = fold.createFolder(((SWrpg)main.getApplication()).gac,
                            new MetadataChangeSet.Builder().setTitle("SWShips").build()).await();
                    shipsFold = foldRes.getDriveFolder().getDriveId();
                }
                ((SWrpg)main.getApplication()).vehicFold = shipsFold.asDriveFolder();
                return null;
            }
        };
        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
