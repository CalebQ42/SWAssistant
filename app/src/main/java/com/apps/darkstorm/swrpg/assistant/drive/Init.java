package com.apps.darkstorm.swrpg.assistant.drive;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

public class Init {
    public static void connect(final Activity main){
        Drive.DriveApi.requestSync(((SWrpg)main.getApplication()).gac).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    final DriveFolder root = Drive.DriveApi.getRootFolder(((SWrpg)main.getApplication()).gac);
                    root.queryChildren(((SWrpg)main.getApplication()).gac,new Query.Builder()
                            .addFilter(Filters.eq(SearchableField.TITLE,"SWChars")).setSortOrder(new SortOrder.Builder()
                                    .addSortDescending(SortableField.MODIFIED_DATE).build()).build())
                            .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                @Override
                                public void onResult(@NonNull final DriveApi.MetadataBufferResult metBufRes) {
                                    if(metBufRes.getStatus().isSuccess()){
                                        final MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
                                        final DriveFolder[] charsFold = {null};
                                        for(Metadata met:metBuf){
                                            if(met.isFolder() && !met.isTrashed()){
                                                charsFold[0] = met.getDriveId().asDriveFolder();
                                                break;
                                            }
                                        }
                                        metBuf.release();
                                        if(charsFold[0] ==null){
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
                                                                                                        }else{
                                                                                                            ((SWrpg)main.getApplication()).driveFail = true;
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }else{
                                                                                    ((SWrpg)main.getApplication()).driveFail = true;
                                                                                }
                                                                                metadataBufferResult.release();
                                                                            }
                                                                        });
                                                            }else{
                                                                ((SWrpg)main.getApplication()).driveFail = true;
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
                                                                                    }else{
                                                                                        ((SWrpg)main.getApplication()).driveFail = true;
                                                                                    }
                                                                                }
                                                                            });
                                                                }else{
                                                                    ((SWrpg)main.getApplication()).vehicFold = vehic;
                                                                    ((SWrpg)main.getApplication()).charsFold = charsFold[0];
                                                                }
                                                            }else{
                                                                ((SWrpg)main.getApplication()).driveFail = true;
                                                            }
                                                            metadataBufferResult.release();
                                                        }
                                                    });
                                        }
                                        metBufRes.release();
                                    }else{
                                        ((SWrpg)main.getApplication()).driveFail = true;
                                    }
                                }
                            });
                }else{
                    ((SWrpg)main.getApplication()).driveFail = true;
                }
            }
        });
    }
}
