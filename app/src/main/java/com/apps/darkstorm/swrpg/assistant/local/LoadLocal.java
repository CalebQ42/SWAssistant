package com.apps.darkstorm.swrpg.assistant.local;

import android.app.Activity;

import com.apps.darkstorm.swrpg.assistant.R;
import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Objects;

public class LoadLocal {
    public static Character[] characters(Activity ac){
        File fold = new File(((SWrpg) ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key), ((SWrpg) ac.getApplication()).defaultLoc));
        if (!fold.exists()) {
            if (!fold.mkdir()) {
                return new Character[0];
            }
        }
        File[] chars = fold.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".char")||name.endsWith(".char.bak");
            }
        });
        ArrayList<Character> characters = new ArrayList<>();
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
            if(fil.getName().endsWith(".bak")){
                tmp.save(tmp.getFileLocation(ac));
                fil.delete();
            }
        }
        return characters.toArray(new Character[0]);
    }
    public static Minion[] minions(Activity ac){
        File fold = new File(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),((SWrpg)ac.getApplication()).defaultLoc));
        if (!fold.exists()){
            fold.mkdirs();
            fold.mkdir();
            return new Minion[0];
        }
        ArrayList<Minion> minions = new ArrayList<>();
        for (File s:fold.listFiles()){
            if (s.getAbsolutePath().endsWith(".minion")){
                Minion tmp = new Minion();
                tmp.reLoad(s.getAbsolutePath());
                minions.add(tmp);
            }
        }
        return minions.toArray(new Minion[0]);
    }
    public static Vehicle[] vehicles(Activity ac){
        File fold = new File(((SWrpg)ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key),((SWrpg)ac.getApplication()).defaultLoc));
        if (!fold.exists()){
            fold.mkdirs();
            fold.mkdir();
        }
        File vhFold = new File(fold.getAbsolutePath()+"/SWShips");
        if (!vhFold.exists()){
            vhFold.mkdir();
            return new Vehicle[0];
        }
        ArrayList<Vehicle> v = new ArrayList<>();
        for (File s:vhFold.listFiles()){
            if (s.getAbsolutePath().endsWith(".vhcl")){
                Vehicle tmp = new Vehicle();
                tmp.reLoad(s.getAbsolutePath());
                v.add(tmp);
            }
        }
        return v.toArray(new Vehicle[0]);
    }
}
