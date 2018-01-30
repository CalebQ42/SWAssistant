package com.apps.darkstorm.swrpg.assistant.swElements

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import com.apps.darkstorm.swrpg.assistant.saveLoad.JsonSavable

data class Weapon(var name: String = "",var damage: Int = 0,var critical: Int = 0,var hp: Int = 0,var range: Int = 0,
                  var skill: Int = 0, var skillBase: Int = 0,var characteristics: MutableList<WeaponCharacteristic> = mutableListOf(),
                  var addBrawn: Boolean = false, var loaded: Boolean = true,var limitedAmmo: Boolean = false,
                  var itemState: Int = 0, var ammo: Int = 0, var firingArc: String = "", var encumbrance: Int = 0):JsonSavable(){
    override fun save(jw: JsonWriter) {
        jw.beginObject()
        jw.name("name").value(name)
        jw.name("damage").value(damage)
        jw.name("critical rating").value(critical)
        jw.name("hard points").value(hp)
        jw.name("range").value(range)
        jw.name("skill").value(skill)
        jw.name("base").value(skillBase)
        jw.name("Weapon Characteristics").beginArray()
        for(wc in characteristics)
            wc.save(jw)
        jw.endArray()
        jw.name("add brawn").value(addBrawn)
        jw.name("loaded").value(loaded)
        jw.name("limited ammo").value(limitedAmmo)
        jw.name("item state").value(itemState)
        jw.name("ammo").value(ammo)
        jw.name("firing arc").value(firingArc)
        jw.name("encumbrance").value(encumbrance)
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
                "name"-> name = jr.nextString()
                "damage"-> damage = jr.nextInt()
                "critical rating"-> critical = jr.nextInt()
                "hard points"-> hp = jr.nextInt()
                "range"-> range = jr.nextInt()
                "skill"-> skill = jr.nextInt()
                "base"-> skillBase = jr.nextInt()
                "Weapon Characteristics"->{
                    characteristics = mutableListOf()
                    jr.beginArray()
                    while(jr.hasNext()){
                        val chr = WeaponCharacteristic()
                        chr.load(jr)
                        characteristics.add(chr)
                    }
                    jr.endArray()
                }
                "add brawn"-> addBrawn = jr.nextBoolean()
                "loaded"-> loaded = jr.nextBoolean()
                "limited ammo"-> limitedAmmo = jr.nextBoolean()
                "item state"-> itemState = jr.nextInt()
                "ammo"-> ammo = jr.nextInt()
                "firing arc"-> firingArc = jr.nextString()
                "encumbrance"-> encumbrance = jr.nextInt()
            }
        }
        jr.endObject()
    }
    override fun clone() = copy()
}