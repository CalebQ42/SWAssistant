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
                return name.endsWith(".char")||name.endsWith(".char.bak")||name.endsWith(Character.fileExtension)||
                        name.endsWith(Character.fileExtension+".bak");
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
            if(fil.getAbsolutePath().endsWith(".char")||fil.getAbsolutePath().endsWith(".char.bak")){
                tmp.reLoadLegacy(fil.getAbsolutePath());
                fil.delete();
            }else
                tmp.load(fil.getAbsolutePath());
            characters.add(tmp);
            if(fil.getName().endsWith(".bak")){
                tmp.save(tmp.getFileLocation(ac));
                fil.delete();
            }
        }
        return characters.toArray(new Character[0]);
    }
    public static Minion[] minions(Activity ac){
        File fold = new File(((SWrpg) ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key), ((SWrpg) ac.getApplication()).defaultLoc));
        if (!fold.exists()) {
            if (!fold.mkdir()) {
                return new Minion[0];
            }
        }
        File[] chars = fold.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".minion")||name.endsWith(".minion.bak")||name.endsWith(Minion.fileExtension)||
                        name.endsWith(Minion.fileExtension+".bak");
            }
        });
        ArrayList<Minion> minions = new ArrayList<>();
        outer:
        for (File fil : chars) {
            if(fil.getName().endsWith(".minion")){
                for(File file:chars){
                    if(Objects.equals(file.getName(), fil.getName() + ".bak")) {
                        fil.delete();
                        continue outer;
                    }
                }
            }
            Minion tmp = new Minion();
            if(fil.getAbsolutePath().endsWith(".minion")||fil.getAbsolutePath().endsWith(".minion.bak")){
                tmp.reLoadLegacy(fil.getAbsolutePath());
                fil.delete();
            }else
                tmp.load(fil.getAbsolutePath());
            minions.add(tmp);
            if(fil.getName().endsWith(".bak")){
                tmp.save(tmp.getFileLocation(ac));
                fil.delete();
            }
        }
        return minions.toArray(new Minion[0]);
    }
    public static Vehicle[] vehicles(Activity ac){
        File fold = new File(((SWrpg) ac.getApplication()).prefs.getString(ac.getString(R.string.local_location_key), ((SWrpg) ac.getApplication()).defaultLoc));
        if (!fold.exists()) {
            if (!fold.mkdir()) {
                return new Vehicle[0];
            }
        }
        File vhFold = new File(fold.getAbsolutePath()+"/SWShips");
        if(vhFold.exists() && !vhFold.isFile()){
            for(File fd:vhFold.listFiles())
                fd.renameTo(new File(fold.getAbsoluteFile()+"/"+fd.getName()));
            vhFold.delete();
        }
        File[] chars = fold.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".vhcl")||name.endsWith(".vhcl.bak")||name.endsWith(Vehicle.fileExtension)||
                        name.endsWith(Vehicle.fileExtension+".bak");
            }
        });
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        outer:
        for (File fil : chars) {
            if(fil.getName().endsWith(".vhcl")){
                for(File file:chars){
                    if(Objects.equals(file.getName(), fil.getName() + ".bak")) {
                        fil.delete();
                        continue outer;
                    }
                }
            }
            Vehicle tmp = new Vehicle();
            if(fil.getAbsolutePath().endsWith(".vhcl")||fil.getAbsolutePath().endsWith(".vhcl.bak")) {
                tmp.reLoadLegacy(fil.getAbsolutePath());
                fil.delete();
            }else
                tmp.load(fil.getAbsolutePath());
            vehicles.add(tmp);
            if(fil.getName().endsWith(".bak")){
                tmp.save(tmp.getFileLocation(ac));
                fil.delete();
            }
        }
        return vehicles.toArray(new Vehicle[0]);
    }
}
