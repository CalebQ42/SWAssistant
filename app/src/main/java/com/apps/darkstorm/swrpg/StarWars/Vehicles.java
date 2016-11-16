package com.apps.darkstorm.swrpg.StarWars;

import android.content.Context;
import android.view.View;

import com.apps.darkstorm.swrpg.StarWars.CharStuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.StarWars.CharStuff.Weapons;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

public class Vehicles {
    //
    //  |  Version 1  |
    //  |             |
    //  V     0-
    //
    public int ID;
    public String name = "";
    public int silhouette;
    public int speed;
    public int handling;
    public int armor;
    //0-Fore,1-Port,2-Starboard,3-Aft;
    public int[] defense = new int[4];
    public int totalDefense;
    public int hullTramaThresh;
    public int hullTramaCur;
    public int sysStressThresh;
    public int sysStressCur;
    public int encumCapacity;
    public int passengerCapacity;
    public int hp;
    public Weapons weapons = new Weapons();
    public CriticalInjuries crits = new CriticalInjuries();
    private boolean[] showCards = new boolean[0];
    public String desc = "";
    //
    //  ^                 ^
    //  |  Version 1 End  |
    //  |                 |
    //

    private boolean editing = false;
    private boolean saving = false;


    public Vehicles(){
        defense[1] = -1;
        defense[2] = -1;
    }
    public Vehicles(int ID){
        this.ID = ID;
        defense[1] = -1;
        defense[2] = -1;
    }
    public void setSilhouette(int sil){
        silhouette = sil;
        if (sil>4){
            if(defense[1]==-1)
                defense[1]=0;
            if(defense[2]==-1)
                defense[2] = 0;
        }else{
            if(defense[1]!=-1)
                defense[1] = -1;
            if(defense[2]!=-1)
                defense[2]=-1;
        }
    }
    public void stopEditing(){
        editing=false;
    }
    //TBI
    public void showHideCards(final View top){
        //TBI
    }
    //TBI
    public Vehicles clone(){
        Vehicles tmp = new Vehicles();
        //TBI
        return tmp;
    }
    //TBI
    public void startEditing(final Context main, final GoogleApiClient gac, final DriveId fold){
        //TBI
    }
    //TBI
    public void startEditing(final Context main){
        //TBI
    }
    //TBI
    void save(String filename){
        //TBI
    }
    //TBI
    void cloudSave(GoogleApiClient gac,DriveId fil, boolean async){
        //TBI
    }
    //TBI
    public void reLoad(String filename){
        //TBI
    }
    //TBI
    void reLoad(GoogleApiClient gac,DriveId fil){
        //TBI
    }
    //TBI
    public String getFileLocation(Context main){
        //TBI
        return "";
    }
    //TBI
    public DriveId getFileId(GoogleApiClient gac,DriveId fold){
        //TBI
        return null;
    }
    //TBI
    public boolean equals(Object obj){
        //TBI
        return false;
    }
}
