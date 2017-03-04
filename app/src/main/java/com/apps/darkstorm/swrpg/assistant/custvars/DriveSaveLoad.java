package com.apps.darkstorm.swrpg.assistant.custvars;

import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;

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
                        DriveContents cont = Drive.DriveApi.newDriveContents(gac).await().getDriveContents();
                        OutputStream contO = cont.getOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(contO);
                        out.writeObject(saveItems);
                        out.close();
                        contO.close();
                        cont.commit(gac,null).await();
                        String title = id.asDriveFile().getMetadata(gac).await().getMetadata().getOriginalFilename();
                        DriveFolder par = id.asDriveFile().listParents(gac).await().getMetadataBuffer().get(0).getDriveId().asDriveFolder();
                        id.asDriveResource().delete(gac);
                        par.createFile(gac, new MetadataChangeSet.Builder().setTitle(title).build(),cont);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            sav.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            try {
                DriveFile file = id.asDriveFile();
                DriveApi.DriveContentsResult contRes =
                        file.open(gac, DriveFile.MODE_WRITE_ONLY,
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
        }
    }
    public Object[] load(final GoogleApiClient gac){
        ArrayList<Object> loadItems = new ArrayList<>();
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
        return loadItems.toArray();
    }
    public void setMime(String mime){
        this.mime = mime;
    }
    //<editor-fold>
    private DriveId id = null;
    private ArrayList<Object> saveItems;
    private String mime;
    //</editor-fold>
}
