package com.apps.darkstorm.swrpg.load;

import android.app.Activity;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.sw.Minion;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class DriveLoadMinions {
    public ArrayList<Minion> minions;
    public ArrayList<Date> lastMod;

    public DriveLoadMinions(Activity main) {
        int timeout = 0;
        while (((SWrpg)main.getApplication()).charsFold == null && timeout < 100) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout++;
        }
        if (((SWrpg)main.getApplication()).charsFold == null)
            return;
        minions = new ArrayList<>();
        lastMod = new ArrayList<>();
        DriveApi.MetadataBufferResult metBufRes = ((SWrpg)main.getApplication())
                .charsFold.queryChildren(((SWrpg)main.getApplication()).gac,
                        new Query.Builder().addFilter(Filters.contains(SearchableField.TITLE,".minion")).build()).await();
        MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
        for (Metadata met : metBuf) {
            if (!met.isFolder() && met.getFileExtension()!= null && met.getFileExtension().equals("minion")
                    && !met.isTrashed()) {
                Minion tmp = new Minion();
                tmp.reLoad(((SWrpg)main.getApplication()).gac, met.getDriveId());
                minions.add(tmp);
                lastMod.add(met.getModifiedDate());
            }
        }
        metBuf.release();
        metBufRes.release();
    }

    public void saveLocal(Activity main) {
        LoadMinions lc = new LoadMinions(main);
        for (int i = 0; i < lc.minions.size(); i++) {
            boolean found = false;
            for (int j = 0; j < minions.size(); j++) {
                if (lc.minions.get(i).ID == minions.get(j).ID) {
                    found = true;
                    if (lc.lastMod.get(i).after(lastMod.get(j)))
                        lc.minions.get(i).cloudSave(((SWrpg)main.getApplication())
                                .gac, lc.minions.get(i).getFileId(main), false);
                    break;
                }
            }
            if (!found) {
                if (((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.sync_key), true)) {
                    File tmp = new File(lc.minions.get(i).getFileLocation(main));
                    tmp.delete();
                } else {
                    lc.minions.get(i).cloudSave(((SWrpg)main.getApplication())
                            .gac, lc.minions.get(i).getFileId(main), false);
                }
            }
        }
        for (Minion chara : minions) {
            chara.save(chara.getFileLocation(main));
        }
    }
}
