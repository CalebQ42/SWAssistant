package com.apps.darkstorm.swrpg.assistant.sw

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import com.apps.darkstorm.swrpg.assistant.saveLoad.JsonSavable
import com.apps.darkstorm.swrpg.assistant.swElements.CriticalInjury
import com.apps.darkstorm.swrpg.assistant.swElements.Note
import com.apps.darkstorm.swrpg.assistant.swElements.Weapon

abstract class SWEditable(var name: String = "",var nts: MutableList<Note> = mutableListOf(),
                      var weapons: MutableList<Weapon> = mutableListOf(),var category: String = "",
                      var criticalInjuries: MutableList<CriticalInjury> = mutableListOf(), var desc: String = ""): JsonSavable(){
    abstract fun saving(jw: JsonWriter)
    abstract fun loading(name: String, jr: JsonReader)

    override fun save(jw: JsonWriter) {
        jw.beginObject()
        jw.name("name").value(name)
        jw.name("Notes").beginArray()
        for(n in nts)
            n.save(jw)
        jw.endArray()
        jw.name("Weapons").beginArray()
        for(w in weapons)
            w.save(jw)
        jw.endArray()
        jw.name("category").value(category)
        jw.name("Critical Injuries").beginArray()
        for(c in criticalInjuries)
            c.save(jw)
        jw.endArray()
        jw.name("description").value(desc)
        saving(jw)
        jw.endObject()
    }
    override fun load(jr: JsonReader) {jr.beginObject()
        while(jr.hasNext() && jr.peek() != JsonToken.END_OBJECT){
            if(jr.peek()!= JsonToken.NAME){
                jr.skipValue()
                continue
            }
            val jName = jr.nextName()
            when(jName){
                "name"-> name = jr.nextString()
                "Notes"->{
                    nts = mutableListOf()
                    jr.beginArray()
                    while(jr.hasNext()){
                        val nt = Note()
                        nt.load(jr)
                        nts.add(nt)
                    }
                    jr.endArray()
                }
                "Weapons"->{
                    weapons = mutableListOf()
                    jr.beginArray()
                    while(jr.hasNext()){
                        val wp = Weapon()
                        wp.load(jr)
                        weapons.add(wp)
                    }
                    jr.endArray()
                }
                "category"-> category = jr.nextString()
                "Critical Injuries"->{
                    criticalInjuries = mutableListOf()
                    jr.beginArray()
                    while(jr.hasNext()){
                        val cr = CriticalInjury()
                        cr.load(jr)
                        criticalInjuries.add(cr)
                    }
                    jr.endArray()
                }
                "description"-> desc = jr.nextString()
                else-> loading(jName,jr)
            }
        }
        jr.endObject()
    }
}