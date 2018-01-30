package com.apps.darkstorm.swrpg.assistant.saveLoad

import android.util.JsonReader
import android.util.JsonWriter
import com.apps.darkstorm.cdr.saveLoad.Save
import com.apps.darkstorm.swrpg.assistant.R
import com.apps.darkstorm.swrpg.assistant.SW
import com.google.android.gms.drive.DriveFile
import org.jetbrains.anko.doAsync
import java.io.Reader
import java.io.Writer

abstract class JsonSavable: Cloneable{
    var editing = false
    var saving = false

    abstract fun save(jw: JsonWriter)
    abstract fun load(jr: JsonReader)
    abstract override fun equals(other: Any?): Boolean
    abstract override fun hashCode(): Int
    abstract override fun clone(): JsonSavable
    fun saveJson(wr: Writer){
        val jw = JsonWriter(wr)
        jw.isLenient = true
        jw.setIndent("  ")
        save(jw)
        jw.close()
        wr.close()
    }
    fun loadJson(rdr: Reader){
        val jr = JsonReader(rdr)
        jr.isLenient = true
        load(jr)
        jr.close()
        rdr.close()
    }
    fun stopEditing(){
        editing = false
    }
    fun startEditing(nameGetter: (SW)->String,sw: SW, df: ((SW)->DriveFile?)? = null){
        if(!editing) {
            doAsync {
                editing = true
                Save.local(this@JsonSavable, nameGetter(sw))
                if(sw.prefs.getBoolean(sw.getString(R.string.google_drive_key),false))
                    Save.drive(this@JsonSavable, sw.drc, df!!(sw)!!, true)
                var tmp = clone()
                while (editing) {
                    if (tmp != this && !saving) {
                        saving = true
                        Save.local(this@JsonSavable, nameGetter(sw))
                        if(sw.prefs.getBoolean(sw.getString(R.string.google_drive_key),false))
                            Save.drive(this@JsonSavable, sw.drc, df!!(sw)!!, true)
                        tmp = clone()
                        saving = false
                    }
                    Thread.sleep(300)
                }
                if (tmp != this && !saving) {
                    saving = true
                    Save.local(this@JsonSavable, nameGetter(sw))
                    if(sw.prefs.getBoolean(sw.getString(R.string.google_drive_key),false))
                        Save.drive(this@JsonSavable, sw.drc, df!!(sw)!!, true)
                    saving = false
                }
            }
        }
    }
//    basicArrayLoad
//        arr = mutableListOf()
//        jr.beginArray()
//        while(jr.hasNext()){
//            val chr = Type()
//            chr.load(jr)
//            arr.add(chr)
//        }
//        jr.endArray()

//    basicLoad
//        jr.beginObject()
//        while(jr.hasNext() && jr.peek() != JsonToken.END_OBJECT){
//            if(jr.peek()!=JsonToken.NAME){
//                jr.skipValue()
//                continue
//            }
//            val jName = jr.nextName()
//            when(jName){
//            }
//        }
//        jr.endObject()
}