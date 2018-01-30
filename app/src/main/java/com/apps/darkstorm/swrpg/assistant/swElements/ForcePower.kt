package com.apps.darkstorm.swrpg.assistant.swElements

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import com.apps.darkstorm.swrpg.assistant.saveLoad.JsonSavable

data class ForcePower(var name: String = "",var desc: String = ""): JsonSavable(){
    override fun save(jw: JsonWriter) {
        jw.beginObject()
        jw.name("name").value(name)
        jw.name("description").value(desc)
        jw.endObject()
    }
    override fun load(jr: JsonReader) {
        jr.beginObject()
        while(jr.hasNext() && jr.peek() != JsonToken.END_OBJECT){
            if(jr.peek()!=JsonToken.NAME){
                jr.skipValue()
                continue
            }
            val jName = jr.nextName()
            when(jName){
                "name"-> name = jr.nextString()
                "description"-> desc = jr.nextString()
            }
        }
        jr.endObject()
    }
    override fun clone() = copy()
}