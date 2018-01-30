package com.apps.darkstorm.swrpg.assistant.swElements

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import com.apps.darkstorm.swrpg.assistant.saveLoad.JsonSavable

data class Skill(var name: String = "",var value: Int = 0,var base: Int = 0, var career: Boolean = false): JsonSavable(){
    override fun save(jw: JsonWriter) {
        jw.beginObject()
        jw.name("name").value(name)
        jw.name("value").value(value)
        jw.name("base").value(base)
        jw.name("career").value(career)
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
                "value"-> value = jr.nextInt()
                "base"-> base = jr.nextInt()
                "career"-> career = jr.nextBoolean()
            }
        }
        jr.endObject()
    }
    override fun clone() = copy()
}