package com.apps.darkstorm.swrpg.custvars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
public class SaveLoad implements Serializable{
    public SaveLoad(String filename){
        if(filename.contains("/")) {
            savFolder = new File(filename.substring(0, filename.lastIndexOf("/")));
        }
        sav = new File(filename);
    }
    public void addSave(Object e){
        saveItems.add(e);
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
    public void save(){
        try {
            if(savFolder != null && !savFolder.exists())
                savFolder.mkdirs();
            if(sav.exists()){
                sav.delete();
            }
            tada=new FileOutputStream(sav);
            toodoo = new ObjectOutputStream(tada);
            toodoo.writeObject(saveItems);
            toodoo.close();
            tada.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //<editor-fold desc="Variables">
    private FileInputStream inF;
    private ObjectInputStream inO;
    private FileOutputStream tada;
    private ObjectOutputStream toodoo;
    private File savFolder=null,sav;
    private ArrayList<Object> saveItems = new ArrayList<>();
    private ArrayList<Object> loadItems = new ArrayList<>();
    //</editor-fold>
}
