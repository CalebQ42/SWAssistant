package com.apps.darkstorm.swrpg.load;

import android.app.Activity;
import android.os.AsyncTask;

import com.apps.darkstorm.swrpg.SWrpg;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
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
                DriveApi.MetadataBufferResult metBufRes = root.queryChildren(((SWrpg)main.getApplication())
                        .gac,new Query.Builder()
                        .addFilter(Filters.eq(SearchableField.TITLE,"SWChars")).build()).await();
                MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
                int tries = 0;
                while(metBuf.getCount()==0&&tries<5){
                    metBuf.release();
                    metBufRes.release();
                    metBufRes = root.queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder()
                            .addFilter(Filters.eq(SearchableField.TITLE,"SWChars")).build()).await();
                    metBuf = metBufRes.getMetadataBuffer();
                    tries++;
                }
                if(metBuf.getCount()==0){
                    System.out.println("Creating SWChars");
                    ((SWrpg)main.getApplication()).charsFold = root.createFolder
                            (((SWrpg)main.getApplication()).gac,new MetadataChangeSet.Builder()
                                    .setTitle("SWChars").build()).await().getDriveFolder();
                }else{
                    ((SWrpg)main.getApplication()).charsFold = metBuf.get(0).getDriveId().asDriveFolder();
                }
                metBuf.release();
                metBufRes.release();
                metBufRes = ((SWrpg)main.getApplication()).charsFold.queryChildren
                        (((SWrpg)main.getApplication()).gac,new Query.Builder()
                        .addFilter(Filters.eq(SearchableField.TITLE,"SWShips")).build()).await();
                metBuf = metBufRes.getMetadataBuffer();
                if(metBuf.getCount()==0){
                    System.out.println("Creating SWShips");
                    ((SWrpg)main.getApplication()).vehicFold = ((SWrpg)main.getApplication())
                            .charsFold.createFolder(((SWrpg)main.getApplication())
                                    .gac,new MetadataChangeSet.Builder()
                            .setTitle("SWShips").build()).await().getDriveFolder();
                }else{
                    ((SWrpg)main.getApplication()).vehicFold = metBuf.get(0).getDriveId().asDriveFolder();
                }
                metBuf.release();
                metBufRes.release();
                return null;
            }
        };
        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
