package com.apps.darkstorm.swrpg.assistant.drive;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
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
    public static void connect(final Activity main, final int number){
        Drive.DriveApi.requestSync(((SWrpg)main.getApplication()).gac).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                switch(String.valueOf(status.isSuccess())){
                    case "false":
                        if(number<5) {
                            Init.connect(main, number + 1);
                            break;
                        }else if(!status.getStatusMessage().equals("Sync request rate limit exceeded.")) {
                            ((SWrpg) main.getApplication()).driveFail = true;
                            break;
                        }
                        System.out.println("GAAH");
                    default:
                        final DriveFolder root = Drive.DriveApi.getRootFolder(((SWrpg) main.getApplication()).gac);
                        root.queryChildren(((SWrpg) main.getApplication()).gac, new Query.Builder()
                            .addFilter(Filters.eq(SearchableField.TITLE, "SWChars")).setSortOrder(new SortOrder.Builder()
                                    .addSortDescending(SortableField.MODIFIED_DATE).build()).build())
                            .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                    @Override
                                    public void onResult(@NonNull final DriveApi.MetadataBufferResult metBufRes) {
                                        if (metBufRes.getStatus().isSuccess()) {
                                            final MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
                                            final DriveFolder[] charsFold = {null};
                                            for (Metadata met : metBuf) {
                                                if (met.isFolder() && !met.isTrashed()) {
                                                    charsFold[0] = met.getDriveId().asDriveFolder();
                                                    break;
                                                }
                                            }
                                            metBuf.release();
                                            if (charsFold[0] == null) {
                                                root.createFolder(((SWrpg) main.getApplication()).gac,
                                                        new MetadataChangeSet.Builder().setTitle("SWChars").build())
                                                        .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                                                            @Override
                                                            public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                                                                if (driveFolderResult.getStatus().isSuccess()) {
                                                                    charsFold[0] = driveFolderResult.getDriveFolder();
                                                                    charsFold[0].queryChildren(((SWrpg) main.getApplication()).gac, new Query.Builder()
                                                                            .addFilter(Filters.eq(SearchableField.TITLE, "SWShips")).build())
                                                                            .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                                                                @Override
                                                                                public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                                                                                    if (metadataBufferResult.getStatus().isSuccess()) {
                                                                                        MetadataBuffer metBuffer = metadataBufferResult.getMetadataBuffer();
                                                                                        final DriveFolder[] shipFold = {null};
                                                                                        for (Metadata met : metBuffer) {
                                                                                            if (met.isFolder() && !met.isTrashed()) {
                                                                                                shipFold[0] = met.getDriveId().asDriveFolder();
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        metBuf.release();
                                                                                        if (shipFold[0] != null) {
                                                                                            shipFold[0].listChildren(((SWrpg)main.getApplication()).gac)
                                                                                                    .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                                                                                @Override
                                                                                                public void onResult(@NonNull final DriveApi.MetadataBufferResult metadataBufferResult) {
                                                                                                    if(metadataBufferResult.getStatus().isSuccess()){
                                                                                                        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                                                                                            @Override
                                                                                                            protected Void doInBackground(Void... params) {
                                                                                                                MetadataBuffer metBuffer = metadataBufferResult.getMetadataBuffer();
                                                                                                                for(Metadata met:metBuffer){
                                                                                                                    if(met.getTitle().endsWith(".vhcl")||met.getTitle().endsWith(".vhcl.bak")){
                                                                                                                        Vehicle tmp = new Vehicle();
                                                                                                                        tmp.reLoadLegacy(((SWrpg)main.getApplication()).gac,met.getDriveId());
                                                                                                                        tmp.save(((SWrpg)main.getApplication()).gac,tmp.getFileId(main,charsFold[0]),false);
                                                                                                                    }
                                                                                                                }
                                                                                                                metBuffer.release();
                                                                                                                shipFold[0].delete(((SWrpg)main.getApplication()).gac);
                                                                                                                ((SWrpg) main.getApplication()).charsFold = charsFold[0];
                                                                                                                return null;
                                                                                                            }
                                                                                                        };
                                                                                                        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                                                                    } else {
                                                                                                        if (number < 5)
                                                                                                            Init.connect(main, number + 1);
                                                                                                        else
                                                                                                            ((SWrpg) main.getApplication()).driveFail = true;
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }else
                                                                                            ((SWrpg)main.getApplication()).charsFold = charsFold[0];
                                                                                    } else {
                                                                                        if (number < 5)
                                                                                            Init.connect(main, number + 1);
                                                                                        else
                                                                                            ((SWrpg) main.getApplication()).driveFail = true;
                                                                                    }
                                                                                    metadataBufferResult.release();
                                                                                }
                                                                            });
                                                                } else {
                                                                    if (number < 5)
                                                                        Init.connect(main, number + 1);
                                                                    else
                                                                        ((SWrpg) main.getApplication()).driveFail = true;
                                                                }
                                                            }
                                                        });
                                            } else {
                                                charsFold[0].queryChildren(((SWrpg) main.getApplication()).gac, new Query.Builder()
                                                        .addFilter(Filters.eq(SearchableField.TITLE, "SWShips")).build())
                                                        .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                                            @Override
                                                            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                                                                if (metadataBufferResult.getStatus().isSuccess()) {
                                                                    MetadataBuffer mets = metadataBufferResult.getMetadataBuffer();
                                                                    final DriveFolder[] shipFold = {null};
                                                                    for (Metadata met : mets) {
                                                                        if (met.isFolder() && !met.isTrashed()) {
                                                                            shipFold[0] = met.getDriveId().asDriveFolder();
                                                                            break;
                                                                        }
                                                                    }
                                                                    mets.release();
                                                                    if (shipFold[0] != null) {
                                                                        shipFold[0].listChildren(((SWrpg)main.getApplication()).gac)
                                                                                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                                                                    @Override
                                                                                    public void onResult(@NonNull final DriveApi.MetadataBufferResult metadataBufferResult) {
                                                                                        if(metadataBufferResult.getStatus().isSuccess()){
                                                                                            AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                                                                                @Override
                                                                                                protected Void doInBackground(Void... params) {
                                                                                                    MetadataBuffer metBuffer = metadataBufferResult.getMetadataBuffer();
                                                                                                    for(Metadata met:metBuffer){
                                                                                                        if(met.getTitle().endsWith(".vhcl")||met.getTitle().endsWith(".vhcl.bak")){
                                                                                                            Vehicle tmp = new Vehicle();
                                                                                                            tmp.reLoadLegacy(((SWrpg)main.getApplication()).gac,met.getDriveId());
                                                                                                            tmp.save(((SWrpg)main.getApplication()).gac,tmp.getFileId(main,charsFold[0]),false);
                                                                                                        }
                                                                                                    }
                                                                                                    metBuffer.release();
                                                                                                    shipFold[0].delete(((SWrpg)main.getApplication()).gac);
                                                                                                    ((SWrpg) main.getApplication()).charsFold = charsFold[0];
                                                                                                    return null;
                                                                                                }
                                                                                            };
                                                                                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                                                        } else {
                                                                                            if (number < 5)
                                                                                                Init.connect(main, number + 1);
                                                                                            else
                                                                                                ((SWrpg) main.getApplication()).driveFail = true;
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }else
                                                                        ((SWrpg)main.getApplication()).charsFold = charsFold[0];
                                                                } else {
                                                                    if (number < 5)
                                                                        Init.connect(main, number + 1);
                                                                    else
                                                                        ((SWrpg) main.getApplication()).driveFail = true;
                                                                }
                                                                metadataBufferResult.release();
                                                            }
                                                        });
                                            }
                                            metBufRes.release();
                                        } else {
                                            if (number < 5)
                                                Init.connect(main, number + 1);
                                            else
                                                ((SWrpg) main.getApplication()).driveFail = true;
                                        }
                                    }
                                });
                }
            }
        });
    }
}
