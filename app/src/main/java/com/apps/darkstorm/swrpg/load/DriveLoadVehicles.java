package com.apps.darkstorm.swrpg.load;

import android.app.Activity;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.sw.Vehicle;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class DriveLoadVehicles {
    public ArrayList<Vehicle> vehicles;
    public ArrayList<Date> lastMod;

    public DriveLoadVehicles(Activity main) {
        SWrpg app = (SWrpg) main.getApplication();
        int timeout = 0;
        while (app.vehicFold == null && timeout < 100) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout++;
        }
        if (app.vehicFold == null)
            return;
        vehicles = new ArrayList<>();
        lastMod = new ArrayList<>();
        DriveApi.MetadataBufferResult metBufRes = app.vehicFold.queryChildren(app.gac, null).await();
        MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
        for (Metadata met : metBuf) {
            if (met.getFileExtension().equals("vhcl") && !met.isTrashed()) {
                Vehicle tmp = new Vehicle();
                tmp.reLoad(app.gac, met.getDriveId());
                vehicles.add(tmp);
                lastMod.add(met.getModifiedDate());
            }
        }
        metBuf.release();
        metBufRes.release();
    }

    public void saveLocal(Activity main) {
        SWrpg app = (SWrpg) main.getApplication();
        LoadVehicles lc = new LoadVehicles(main);
        for (int i = 0; i < lc.vehicles.size(); i++) {
            boolean found = false;
            for (int j = 0; j < vehicles.size(); j++) {
                if (lc.vehicles.get(i).ID == vehicles.get(j).ID) {
                    found = true;
                    if (lc.lastMod.get(i).after(lastMod.get(j)))
                        lc.vehicles.get(i).cloudSave(app.gac, lc.vehicles.get(i).getFileId(main), false);
                    break;
                }
            }
            if (!found) {
                if (app.prefs.getBoolean(main.getString(R.string.sync_key), true)) {
                    File tmp = new File(lc.vehicles.get(i).getFileLocation(main));
                    tmp.delete();
                } else {
                    lc.vehicles.get(i).cloudSave(app.gac, lc.vehicles.get(i).getFileId(main), false);
                }
            }
        }
        for (Vehicle chara : vehicles) {
            chara.save(chara.getFileLocation(main));
        }
    }
}
