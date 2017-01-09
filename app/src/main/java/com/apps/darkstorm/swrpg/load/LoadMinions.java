package com.apps.darkstorm.swrpg.load;

import android.app.Activity;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.sw.Minion;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;

public class LoadMinions {
    public ArrayList<Minion> minions;
    public ArrayList<Date> lastMod;
    public LoadMinions(Activity main){
        minions = new ArrayList<>();
        lastMod = new ArrayList<>();
        if(main!= null) {
            File fold = new File(((SWrpg) main.getApplication()).prefs.getString(main.getString(R.string.local_location_key), ((SWrpg) main.getApplication()).defaultLoc));
            if (!fold.exists()) {
                if (!fold.mkdir()) {
                    return;
                }
            }
            File[] chars = fold.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".minion")) {
                        return true;
                    }
                    return false;
                }
            });
            for (File fil : chars) {
                Minion tmp = new Minion();
                tmp.reLoad(fil.getAbsolutePath());
                minions.add(tmp);
                lastMod.add(new Date(fil.lastModified()));
            }
        }
    }
}
