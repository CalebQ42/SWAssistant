package com.apps.darkstorm.swrpg.load;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.sw.Vehicle;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class DriveLoadVehicles {
    public ArrayList<Vehicle> vehicles;
    public ArrayList<Date> lastMod;

    public DriveLoadVehicles(Activity main) {
        int timeout = 0;
        while (((SWrpg)main.getApplication()).vehicFold == null && timeout < 100) {
            if(((SWrpg)main.getApplication()).driveFail) {
                Snackbar.make(main.findViewById(R.id.content_main),R.string.drive_fail,Snackbar.LENGTH_LONG).show();
                return;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout++;
        }
        if (((SWrpg)main.getApplication()).vehicFold == null)
            return;
        vehicles = new ArrayList<>();
        lastMod = new ArrayList<>();
        DriveApi.MetadataBufferResult metBufRes = ((SWrpg)main.getApplication())
                .vehicFold.queryChildren(((SWrpg)main.getApplication()).gac,
                        new Query.Builder().addFilter(Filters.contains(SearchableField.TITLE,".vhcl")).build()).await();
        MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
        for (Metadata met : metBuf) {
            if (!met.isFolder() && met.getFileExtension()!=null && met.getFileExtension().equals("vhcl") && !met.isTrashed()) {
                Vehicle tmp = new Vehicle();
                tmp.reLoad(((SWrpg)main.getApplication()).gac, met.getDriveId());
                vehicles.add(tmp);
                lastMod.add(met.getModifiedDate());
            }
        }
        metBuf.release();
        metBufRes.release();
    }

    public void saveLocal(Activity main) {
        LoadVehicles lc = new LoadVehicles(main);
        for (int i = 0; i < lc.vehicles.size(); i++) {
            boolean found = false;
            for (int j = 0; j < vehicles.size(); j++) {
                if (lc.vehicles.get(i).ID == vehicles.get(j).ID) {
                    found = true;
                    if (lc.lastMod.get(i).after(lastMod.get(j)))
                        lc.vehicles.get(i).cloudSave(((SWrpg)main.getApplication())
                                .gac, lc.vehicles.get(i).getFileId(main), false);
                    break;
                }
            }
            if (!found) {
                if (((SWrpg)main.getApplication())
                        .prefs.getBoolean(main.getString(R.string.sync_key), true)) {
                    File tmp = new File(lc.vehicles.get(i).getFileLocation(main));
                    tmp.delete();
                } else {
                    lc.vehicles.get(i).cloudSave(((SWrpg)main.getApplication())
                            .gac, lc.vehicles.get(i).getFileId(main), false);
                }
            }
        }
        for (Vehicle chara : vehicles) {
            chara.save(chara.getFileLocation(main));
        }
    }
}
