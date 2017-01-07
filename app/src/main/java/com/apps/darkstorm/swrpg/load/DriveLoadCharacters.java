package com.apps.darkstorm.swrpg.load;

import android.app.Activity;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.sw.Character;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class DriveLoadCharacters {
    public ArrayList<Character> characters;
    public ArrayList<Date> lastMod;
    public DriveLoadCharacters(Activity main){
        SWrpg app = (SWrpg)main.getApplication();
        int timeout = 0;
        while(app.charsFold == null && timeout<100){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout++;
        }
        if(app.charsFold == null)
            return;
        characters = new ArrayList<>();
        lastMod = new ArrayList<>();
        DriveApi.MetadataBufferResult metBufRes = app.charsFold.queryChildren(app.gac, null).await();
        MetadataBuffer metBuf = metBufRes.getMetadataBuffer();
        for(Metadata met:metBuf){
            if (met.getFileExtension().equals("char") && !met.isTrashed()){
                Character tmp = new Character();
                tmp.reLoad(app.gac,met.getDriveId());
                characters.add(tmp);
                lastMod.add(met.getModifiedDate());
            }
        }
        metBuf.release();
        metBufRes.release();
    }
    public void saveLocal(Activity main){
        SWrpg app = (SWrpg)main.getApplication();
        LoadCharacters lc = new LoadCharacters(main);
        for(int i = 0;i<lc.characters.size();i++){
            boolean found = false;
            for(int j = 0;j<characters.size();j++){
                if(lc.characters.get(i).ID == characters.get(j).ID){
                    found = true;
                    if(lc.lastMod.get(i).after(lastMod.get(j)))
                        lc.characters.get(i).cloudSave(app.gac,lc.characters.get(i).getFileId(main),false);
                    break;
                }
            }
            if(!found){
                if (app.prefs.getBoolean(main.getString(R.string.sync_key),true)){
                    File tmp = new File(lc.characters.get(i).getFileLocation(main));
                    tmp.delete();
                }else{
                    lc.characters.get(i).cloudSave(app.gac,lc.characters.get(i).getFileId(main),false);
                }
            }
        }
        for(Character chara:characters){
            chara.save(chara.getFileLocation(main));
        }
    }
}
