package com.apps.darkstorm.swrpg.assistant.swElements

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import com.apps.darkstorm.swrpg.assistant.saveLoad.JsonSavable

data class Note(var title: String = "",var note: String = ""): JsonSavable(){
    override fun save(jw: JsonWriter) {
        jw.beginObject()
        jw.name("title").value(title)
        jw.name("note").value(note)
        jw.endObject()
    }
    override fun load(jr: JsonReader) {
        jr.beginObject()
        while(jr.hasNext() && jr.peek() != JsonToken.END_OBJECT){
            if(jr.peek()!= JsonToken.NAME){
                jr.skipValue()
                continue
            }
            val jName = jr.nextName()
            when(jName){
                "title"-> title = jr.nextString()
                "note"-> note = jr.nextString()
            }
        }
        jr.endObject()
    }
    override fun clone() = copy()

}