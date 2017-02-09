package com.apps.darkstorm.swrpg.load;

import android.app.Activity;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.sw.Character;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class LoadCharacters {
    public ArrayList<Character> characters;
    public ArrayList<Date> lastMod;
    public LoadCharacters(Activity main){
        characters = new ArrayList<>();
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
                    return name.endsWith(".char")||name.endsWith(".char.bak");
                }
            });
            outer:
            for (File fil : chars) {
                if(fil.getName().endsWith(".char")){
                    for(File file:chars){
                        if(Objects.equals(file.getName(), fil.getName() + ".bak")) {
                            fil.delete();
                            continue outer;
                        }
                    }
                }
                Character tmp = new Character();
                tmp.reLoad(fil.getAbsolutePath());
                characters.add(tmp);
                lastMod.add(new Date(fil.lastModified()));
                if(fil.getName().endsWith(".bak")){
                    tmp.save(tmp.getFileLocation(main));
                    fil.delete();
                }
            }
        }
    }
}
