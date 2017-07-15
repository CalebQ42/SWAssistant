package com.apps.darkstorm.swrpg.assistant.custvars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveLoad implements Serializable{
    public SaveLoad(String filename){
        sav = new File(filename);
    }
    public Object[] load(){
        if(sav.exists()){
            try {
                loadItems.clear();
                inF = new FileInputStream(sav);
                inO = new ObjectInputStream(inF);
                loadItems = (ArrayList<Object>)inO.readObject();
                inO.close();
                inF.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SaveLoad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return loadItems.toArray();
    }
    //<editor-fold desc="Variables">
    private FileInputStream inF;
    private ObjectInputStream inO;
    private File sav;
    private ArrayList<Object> loadItems = new ArrayList<>();
    //</editor-fold>
}
