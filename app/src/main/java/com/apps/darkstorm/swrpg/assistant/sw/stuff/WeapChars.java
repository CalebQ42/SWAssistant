package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import java.util.ArrayList;
import java.util.Arrays;

public class WeapChars{
    WeapChar[] wc;
    public WeapChars(){
        wc = new WeapChar[0];
    }
    public void add(WeapChar w){
        wc = Arrays.copyOf(wc,wc.length+1);
        wc[wc.length-1] = w;
    }
    public int remove(WeapChar w){
        int i = -1;
        for (int j = 0;j<wc.length;j++){
            if (wc[j].equals(w)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            WeapChar[] newWc = new WeapChar[wc.length - 1];
            for (int j = 0; j < i; j++)
                newWc[j] = wc[j];
            for (int j = i + 1; j < wc.length; j++)
                newWc[j - 1] = wc[j];
            wc = newWc;
        }
        return i;
    }
    public WeapChar get(int i){
        return wc[i];
    }
    public int size(){
        return wc.length;
    }
    public WeapChars clone(){
        WeapChars tmp = new WeapChars();
        tmp.wc = new WeapChar[wc.length];
        for(int i = 0;i<wc.length;i++)
            tmp.wc[i] = wc[i];
        return tmp;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (WeapChar w:wc){
            tmp.add(w.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<WeapChar> out = new ArrayList<>();
        for (Object o:tmp){
            WeapChar w = new WeapChar();
            w.loadFromObject(o);
            out.add(w);
        }
        wc = out.toArray(wc);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof WeapChars))
            return false;
        WeapChars in = (WeapChars)obj;
        if(in.wc.length != wc.length)
            return false;
        for (int i = 0;i<wc.length;i++){
            if (!in.wc[i].equals(wc[i]))
                return false;
        }
        return true;
    }
}
