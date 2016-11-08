package com.apps.darkstorm.swrpg.CustVars;

import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DriveSaveLoad {
    public DriveSaveLoad(DriveId file){
        id = file;
        saveItems = new ArrayList<>();
    }
    public void addSave(Object e){
        saveItems.add(e);
    }
    public void save(final GoogleApiClient gac,boolean async){
        if(async){
            AsyncTask<Void,Void,Void> sav = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        DriveFile file = id.asDriveFile();
                        DriveApi.DriveContentsResult contRes =
                                file.open(gac, DriveFile.MODE_READ_WRITE,
                                        new DriveFile.DownloadProgressListener() {public void onProgress(long l, long l1) {}}).await();
                        DriveContents cont = contRes.getDriveContents();
                        OutputStream contO = cont.getOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(contO);
                        out.writeObject(saveItems);
                        out.close();
                        contO.close();
                        cont.commit(gac,null).await();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            sav.execute();
        }else{
            try {
                DriveFile file = id.asDriveFile();
                DriveApi.DriveContentsResult contRes =
                        file.open(gac, DriveFile.MODE_READ_WRITE,
                                new DriveFile.DownloadProgressListener() {public void onProgress(long l, long l1) {}}).await();
                DriveContents cont = contRes.getDriveContents();
                OutputStream contO = cont.getOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(contO);
                out.writeObject(saveItems);
                out.close();
                contO.close();
                cont.commit(gac,null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Object[] load(final GoogleApiClient gac,boolean async){
        final ArrayList<Object> loadItems = new ArrayList<>();
        if (async){
            AsyncTask<Void,Void,Void> load = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        DriveFile file = id.asDriveFile();
                        DriveApi.DriveContentsResult contRes =
                                file.open(gac, DriveFile.MODE_READ_ONLY,
                                        new DriveFile.DownloadProgressListener() {
                                            public void onProgress(long l, long l1) {
                                            }
                                        }).await();
                        DriveContents cont = contRes.getDriveContents();
                        InputStream contI = cont.getInputStream();
                        ObjectInputStream in = new ObjectInputStream(contI);
                        ArrayList<Object> tmp = (ArrayList<Object>)in.readObject();
                        loadItems.addAll(tmp);
                        in.close();
                        contI.close();
                    }catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            load.execute();
        }else{
            try {
                DriveFile file = id.asDriveFile();
                DriveApi.DriveContentsResult contRes =
                        file.open(gac, DriveFile.MODE_READ_ONLY,
                                new DriveFile.DownloadProgressListener() {
                                    public void onProgress(long l, long l1) {
                                    }
                                }).await();
                DriveContents cont = contRes.getDriveContents();
                InputStream contI = cont.getInputStream();
                ObjectInputStream in = new ObjectInputStream(contI);
                ArrayList<Object> tmp = (ArrayList<Object>)in.readObject();
                loadItems.addAll(tmp);
                in.close();
                contI.close();
            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        return loadItems.toArray();
    }
    //<editor-fold>
    private DriveId id = null;
    private ArrayList<Object> saveItems;
    //</editor-fold>
}
