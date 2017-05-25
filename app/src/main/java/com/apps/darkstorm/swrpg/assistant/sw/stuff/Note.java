package com.apps.darkstorm.swrpg.assistant.sw.stuff;

import android.util.JsonReader;
import android.util.JsonWriter;

import com.apps.darkstorm.swrpg.assistant.sw.JsonSavable;

import java.io.IOException;
import java.util.ArrayList;

public class Note implements JsonSavable {
    //Version 1 0-1
    public String title = "",note = "";
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(title);
        tmp.add(note);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 2:
                title = (String)tmp[0];
                note = (String)tmp[1];
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Note))
            return false;
        Note in = (Note)obj;
        return in.title.equals(title) && in.note.equals(note);
    }
    public Note clone(){
        Note n = new Note();
        n.title = title;
        n.note = note;
        return n;
    }

    public void saveJson(JsonWriter jw) throws IOException{
        jw.beginObject();
        jw.name("title").value(title);
        jw.name("note").value(note);
        jw.endObject();
    }

    public void loadJson(JsonReader jr) throws IOException{
        jr.beginObject();
        jr.skipValue();
        title = jr.nextString();
        jr.skipValue();
        note = jr.nextString();
        jr.endObject();
    }
}
