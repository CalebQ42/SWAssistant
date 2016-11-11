package com.apps.darkstorm.swrpg.StarWars;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.apps.darkstorm.swrpg.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class LoadChars {
    public ArrayList<Character> chars;
    public ArrayList<Date> lastMod;
    public LoadChars(Context main){
        chars = new ArrayList<>();
        lastMod = new ArrayList<>();
        File location;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            File tmp = Environment.getExternalStorageDirectory();
            location = new File(tmp.getAbsolutePath() + "/SWChars");
            if (!location.exists()){
                if (!location.mkdir()){
                    return;
                }
            }
        }else{
            File tmp = main.getFilesDir();
            location = new File(tmp.getAbsolutePath() + "/SWChars");
            if (!location.exists()){
                if (!location.mkdir()){
                    return;
                }
            }
        }
        SharedPreferences pref = main.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String def = location.getAbsolutePath();
        String loc = pref.getString(main.getString(R.string.local_location_key),def);
        location = new File(loc);
        if (!location.exists()){
            if (!location.mkdir()){
                return;
            }
        }
        File[] files = location.listFiles();
        if (files != null) {
            for (File fi : files) {
                if (fi.isFile() && fi.getName().endsWith(".char")) {
                    Character tmp = new Character();
                    tmp.reLoad(fi.getAbsolutePath());
                    chars.add(tmp);
                    lastMod.add(new Date(fi.lastModified()));
                }
            }
        }
    }
}
