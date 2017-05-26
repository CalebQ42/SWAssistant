package com.apps.darkstorm.swrpg.assistant.drive;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class DriveSaveLoad {
    public DriveSaveLoad(DriveId file){
        id = file;
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
    //<editor-fold>
    private DriveId id = null;
    //</editor-fold>
}
