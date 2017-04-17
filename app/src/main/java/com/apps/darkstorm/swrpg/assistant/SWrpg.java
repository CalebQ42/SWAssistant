package com.apps.darkstorm.swrpg.assistant;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.apps.darkstorm.swrpg.assistant.sw.Character;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveFolder;

import java.util.Collections;

public class SWrpg extends Application{
    public GoogleApiClient gac = null;
    public DriveFolder charsFold = null;
    public DriveFolder vehicFold = null;
    public String defaultLoc = "";
    public boolean askingPerm = false;
    public boolean driveFail = false;
    public SharedPreferences prefs = null;
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void addShortcut(Character pl, Activity main){
        if(!pl.name.equals("")&&!pl.external) {
            ShortcutManager manag = getSystemService(ShortcutManager.class);
            Intent tmp = new Intent(Intent.ACTION_EDIT);
            tmp.setData(Uri.parse("content://character/" + String.valueOf(pl.ID)));
            tmp.setPackage(this.getPackageName());
            tmp.setClass(main, MainDrawer.class);
            String name = pl.name;
            ShortcutInfo cut = new ShortcutInfo.Builder(main, "character" + "/" + String.valueOf(pl.ID))
                    .setShortLabel(pl.name)
                    .setLongLabel(pl.name)
                    .setIcon(Icon.createWithResource(main, R.drawable.char_launch))
                    .setIntent(tmp)
                    .setActivity(main.getComponentName())
                    .build();
            if (manag.getDynamicShortcuts().size() == 2)
                manag.removeDynamicShortcuts(Collections.singletonList(manag.getDynamicShortcuts().get(0).getId()));
            manag.addDynamicShortcuts(Collections.singletonList(cut));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void addShortcut(Minion pl, Activity main){
        if(!pl.name.equals("")&&!pl.external) {
            ShortcutManager manag = getSystemService(ShortcutManager.class);
            Intent tmp = new Intent(Intent.ACTION_EDIT);
            tmp.setData(Uri.parse("content://minion/" + String.valueOf(pl.ID)));
            tmp.setPackage(this.getPackageName());
            tmp.setClass(main, MainDrawer.class);
            ShortcutInfo cut = new ShortcutInfo.Builder(main, "minion" + "/" + String.valueOf(pl.ID))
                    .setShortLabel(pl.name)
                    .setLongLabel(pl.name)
                    .setIcon(Icon.createWithResource(main, R.drawable.minion_launch))
                    .setIntent(tmp)
                    .setActivity(main.getComponentName())
                    .build();
            if (manag.getDynamicShortcuts().size() == 2)
                manag.removeDynamicShortcuts(Collections.singletonList(manag.getDynamicShortcuts().get(0).getId()));
            manag.addDynamicShortcuts(Collections.singletonList(cut));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void addShortcut(Vehicle pl, Activity main){
        if(!pl.name.equals("")&&!pl.external) {
            ShortcutManager manag = getSystemService(ShortcutManager.class);
            Intent tmp = new Intent(Intent.ACTION_EDIT);
            tmp.setData(Uri.parse("content://vehicle/" + String.valueOf(pl.ID)));
            tmp.setPackage(this.getPackageName());
            tmp.setClass(main, MainDrawer.class);
            ShortcutInfo cut = new ShortcutInfo.Builder(main, "vehicle" + "/" + String.valueOf(pl.ID))
                    .setShortLabel(pl.name)
                    .setLongLabel(pl.name)
                    .setIcon(Icon.createWithResource(main, R.drawable.vehic_launch))
                    .setIntent(tmp)
                    .setActivity(main.getComponentName())
                    .build();
            if (manag.getDynamicShortcuts().size() == 2)
                manag.removeDynamicShortcuts(Collections.singletonList(manag.getDynamicShortcuts().get(0).getId()));
            manag.addDynamicShortcuts(Collections.singletonList(cut));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public boolean hasShortcut(Character pl){
        ShortcutManager manag = getSystemService(ShortcutManager.class);
        for(ShortcutInfo si:manag.getDynamicShortcuts()){
            if(si.getId().equals("character"+"/"+String.valueOf(pl.ID))){
                return true;
            }
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public boolean hasShortcut(Minion pl){
        ShortcutManager manag = getSystemService(ShortcutManager.class);
        for(ShortcutInfo si:manag.getDynamicShortcuts()){
            if(si.getId().equals("minion"+"/"+String.valueOf(pl.ID))){
                return true;
            }
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public boolean hasShortcut(Vehicle pl){
        ShortcutManager manag = getSystemService(ShortcutManager.class);
        for(ShortcutInfo si:manag.getDynamicShortcuts()){
            if(si.getId().equals("vehicle"+"/"+String.valueOf(pl.ID))){
                return true;
            }
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void updateShortcut(Character pl, Activity main){
        if(!pl.name.equals("")) {
            ShortcutManager manag = getSystemService(ShortcutManager.class);
            Intent tmp = new Intent(Intent.ACTION_EDIT);
            tmp.setData(Uri.parse("content://character/" + String.valueOf(pl.ID)));
            tmp.setPackage(this.getPackageName());
            tmp.setClass(main, MainDrawer.class);
            ShortcutInfo cut = new ShortcutInfo.Builder(main, "character" + "/" + String.valueOf(pl.ID))
                    .setShortLabel(pl.name)
                    .setLongLabel(pl.name)
                    .setIcon(Icon.createWithResource(main, R.drawable.person_silhouette))
                    .setIntent(tmp)
                    .setActivity(main.getComponentName())
                    .build();
            manag.updateShortcuts(Collections.singletonList(cut));
        }else{
            deleteShortcut(pl,main);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void updateShortcut(Minion pl, Activity main){
        if(!pl.name.equals("")) {
            ShortcutManager manag = getSystemService(ShortcutManager.class);
            Intent tmp = new Intent(Intent.ACTION_EDIT);
            tmp.setData(Uri.parse("content://minion/" + String.valueOf(pl.ID)));
            tmp.setPackage(this.getPackageName());
            tmp.setClass(main, MainDrawer.class);
            ShortcutInfo cut = new ShortcutInfo.Builder(main, "minion" + "/" + String.valueOf(pl.ID))
                    .setShortLabel(pl.name)
                    .setLongLabel(pl.name)
                    .setIcon(Icon.createWithResource(main, R.drawable.person_silhouette))
                    .setIntent(tmp)
                    .setActivity(main.getComponentName())
                    .build();
            manag.updateShortcuts(Collections.singletonList(cut));
        }else{
            deleteShortcut(pl,main);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void updateShortcut(Vehicle pl, Activity main){
        if(!pl.name.equals("")) {
            ShortcutManager manag = getSystemService(ShortcutManager.class);
            Intent tmp = new Intent(Intent.ACTION_EDIT);
            tmp.setData(Uri.parse("content://vehicle/" + String.valueOf(pl.ID)));
            tmp.setPackage(this.getPackageName());
            tmp.setClass(main, MainDrawer.class);
            ShortcutInfo cut = new ShortcutInfo.Builder(main, "vehicle" + "/" + String.valueOf(pl.ID))
                    .setShortLabel(pl.name)
                    .setLongLabel(pl.name)
                    .setIcon(Icon.createWithResource(main, R.drawable.person_silhouette))
                    .setIntent(tmp)
                    .setActivity(main.getComponentName())
                    .build();
            manag.updateShortcuts(Collections.singletonList(cut));
        }else{
            deleteShortcut(pl,main);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void deleteShortcut(Character pl, Activity main){
        ShortcutManager manag = getSystemService(ShortcutManager.class);
        manag.removeDynamicShortcuts(Collections.singletonList("character"+"/"+String.valueOf(pl.ID)));
        manag.disableShortcuts(Collections.singletonList("character"+"/"+String.valueOf(pl.ID)));
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void deleteShortcut(Minion pl, Activity main){
        ShortcutManager manag = getSystemService(ShortcutManager.class);
        manag.removeDynamicShortcuts(Collections.singletonList("minion"+"/"+String.valueOf(pl.ID)));
        manag.disableShortcuts(Collections.singletonList("minion"+"/"+String.valueOf(pl.ID)));
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void deleteShortcut(Vehicle pl, Activity main){
        ShortcutManager manag = getSystemService(ShortcutManager.class);
        manag.removeDynamicShortcuts(Collections.singletonList("vehicle"+"/"+String.valueOf(pl.ID)));
        manag.disableShortcuts(Collections.singletonList("vehicle"+"/"+String.valueOf(pl.ID)));
    }
}
