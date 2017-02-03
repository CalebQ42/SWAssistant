package com.apps.darkstorm.swrpg.load;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.apps.darkstorm.swrpg.SWrpg;
import com.google.android.gms.common.api.ResultCallback;
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
        final DriveFolder root = Drive.DriveApi.getRootFolder(((SWrpg)main.getApplication()).gac);
        root.queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE,"SWChars")).build())
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(@NonNull final DriveApi.MetadataBufferResult metBufRes) {
                        if(metBufRes.getStatus().isSuccess()){
                            final MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
                            final DriveFolder[] charsFold = {null};
                            System.out.println("Root List Size: "+String.valueOf(metBuf.getCount()));
                            for(Metadata met:metBuf){
                                if(met.isFolder() && !met.isTrashed()){
                                    charsFold[0] = met.getDriveId().asDriveFolder();
                                    break;
                                }
                            }
                            metBuf.release();
                            if(charsFold[0] ==null){
                                System.out.println("Creating Chars Folder");
                                root.createFolder(((SWrpg)main.getApplication()).gac,
                                        new MetadataChangeSet.Builder().setTitle("SWChars").build())
                                        .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                                            @Override
                                            public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                                                if(driveFolderResult.getStatus().isSuccess()){
                                                    charsFold[0] = driveFolderResult.getDriveFolder();
                                                    charsFold[0].queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder()
                                                            .addFilter(Filters.eq(SearchableField.TITLE,"SWShips")).build())
                                                            .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                                                @Override
                                                                public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                                                                    if(metadataBufferResult.getStatus().isSuccess()) {
                                                                        MetadataBuffer metBuffer = metadataBufferResult.getMetadataBuffer();
                                                                        final DriveFolder[] shipFold = {null};
                                                                        for (Metadata met:metBuffer){
                                                                            if(met.isFolder()&&!met.isTrashed()){
                                                                                shipFold[0] = met.getDriveId().asDriveFolder();
                                                                                break;
                                                                            }
                                                                        }
                                                                        metBuf.release();
                                                                        if(shipFold[0] ==null){
                                                                            charsFold[0].createFolder(((SWrpg)main.getApplication()).gac,
                                                                                    new MetadataChangeSet.Builder().setTitle("SWShips").build())
                                                                                    .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                                                                                        @Override
                                                                                        public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                                                                                            if(driveFolderResult.getStatus().isSuccess()){
                                                                                                shipFold[0] = driveFolderResult.getDriveFolder();
                                                                                                ((SWrpg)main.getApplication()).charsFold = charsFold[0];
                                                                                                ((SWrpg)main.getApplication()).vehicFold = shipFold[0];
                                                                                                System.out.println("Done");
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                    metadataBufferResult.release();
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }else{
                                charsFold[0].queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder()
                                        .addFilter(Filters.eq(SearchableField.TITLE,"SWShips")).build())
                                        .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                            @Override
                                            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                                                if(metadataBufferResult.getStatus().isSuccess()){
                                                    MetadataBuffer mets = metadataBufferResult.getMetadataBuffer();
                                                    DriveFolder vehic = null;
                                                    for (Metadata met:mets){
                                                        if(met.isFolder()&&!met.isTrashed()){
                                                            vehic = met.getDriveId().asDriveFolder();
                                                            break;
                                                        }
                                                    }
                                                    mets.release();
                                                    if(vehic == null){
                                                        charsFold[0].createFolder(((SWrpg)main.getApplication()).gac,new MetadataChangeSet.Builder()
                                                                .setTitle("SWShips").build())
                                                                .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                                                                    @Override
                                                                    public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                                                                        if(driveFolderResult.getStatus().isSuccess()){
                                                                            ((SWrpg)main.getApplication()).vehicFold = driveFolderResult.getDriveFolder();
                                                                            ((SWrpg)main.getApplication()).charsFold = charsFold[0];
                                                                        }
                                                                    }
                                                                });
                                                    }else{
                                                        ((SWrpg)main.getApplication()).vehicFold = vehic;
                                                        ((SWrpg)main.getApplication()).charsFold = charsFold[0];
                                                    }
                                                }
                                                metadataBufferResult.release();
                                            }
                                        });
                            }
                            metBufRes.release();
                        }
                    }
                });
    }
}
