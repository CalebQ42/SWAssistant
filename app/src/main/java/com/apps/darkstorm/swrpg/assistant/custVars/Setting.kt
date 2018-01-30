package com.apps.darkstorm.swrpg.assistant.custVars

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.apps.darkstorm.swrpg.assistant.R
import org.jetbrains.anko.find
import org.jetbrains.anko.hintResource
import org.jetbrains.anko.textResource

class Setting(private val textID: Int, private val key: String, private val defaultValue: Any, private val prefs: SharedPreferences, val type: Int){
    lateinit var checkedChangeListener: ((Boolean) -> Unit)
    fun linkToSwitch(sw: Switch){
        sw.isChecked = prefs.getBoolean(key, defaultValue as Boolean)
        sw.textResource = textID
        sw.setOnCheckedChangeListener { _, b ->
            prefs.edit().putBoolean(key,b).apply()
            if(::checkedChangeListener.isInitialized)
                checkedChangeListener.invoke(b)
        }
    }
    lateinit var onItemSelectedListener: (Int)->Unit
    lateinit var array: Array<String>
    fun linkToSpinner(ac: Activity, v: View, itemViewID: Int = android.R.layout.simple_spinner_item){
        v.find<TextView>(R.id.name).textResource = textID
        val sp = v.find<Spinner>(R.id.spinner)
        sp.adapter = ArrayAdapter<String>(ac,itemViewID,array)
        sp.setSelection(prefs.getInt(key,defaultValue as Int))
        sp.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(s0: AdapterView<*>?) {}
            override fun onItemSelected(s0: AdapterView<*>?, s1: View?, position: Int, s2: Long) {
                prefs.edit().putInt(key,position).apply()
                if(::onItemSelectedListener.isInitialized)
                    onItemSelectedListener.invoke(position)
            }
        }
    }
    fun setSpinnerItems(items: Array<String>){
        array = items
    }
    lateinit var onStringConfirmed: (String)->Unit
    fun linkToString(ac: Activity,v: View){
        v.find<TextView>(R.id.setting_name).textResource = textID
        val value = v.find<TextView>(R.id.setting_value)
        value.text = prefs.getString(key,defaultValue as String)
        v.setOnClickListener {
            val build = AlertDialog.Builder(ac)
            val view = ac.layoutInflater.inflate(R.layout.settings_string_edit,null)
            build.setView(view)
            val txt = view.find<TextInputEditText>(R.id.editText)
            txt.hintResource = textID
            txt.setText(prefs.getString(key,defaultValue))
            build.setPositiveButton(android.R.string.ok,{dialog,_->
                prefs.edit().putString(key,txt.text.toString()).apply()
                value.text = txt.text
                dialog.cancel()
            }).setNegativeButton(android.R.string.cancel,{dialog,_->
                dialog.cancel()
            }).show()
        }
    }
    lateinit var onClickListener: ()->Unit
    fun linkToButton(ac: Activity, b: Button){
        b.textResource = textID
    }
    fun createView(inflater: LayoutInflater,parent: ViewGroup?,attach: Boolean):View = when(type){
        Setting.boolean->Setting.createSwitch(inflater,parent,attach)
        Setting.multipleChoice->Setting.createSpinner(inflater,parent,attach)
        Setting.stringValue->Setting.createStringEdit(inflater,parent,attach)
        else-> Setting.createSwitch(inflater,parent,attach)
    }

    companion object {
        val boolean = 0
        val multipleChoice = 1
        val stringValue = 2
        val button = 3
        val divider = 4
        fun createSwitch(inflater: LayoutInflater,parent: ViewGroup?,attach: Boolean):View = inflater.inflate(R.layout.settings_switch,parent,attach)
        fun createSpinner(inflater: LayoutInflater,parent: ViewGroup?,attach: Boolean):View = inflater.inflate(R.layout.settings_spinner,parent,attach)
        fun createStringEdit(inflater: LayoutInflater,parent: ViewGroup?,attach: Boolean):View = inflater.inflate(R.layout.settings_string,parent,attach)
        fun createDivider(inflater: LayoutInflater,parent: ViewGroup?,attach: Boolean):View = inflater.inflate(R.layout.settings_divider,parent,attach)
        class SettingsAdapter(private var activity: Activity,private var settings: Array<Setting>): RecyclerView.Adapter<Adapters.SimpleHolder>(){
            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Adapters.SimpleHolder? = when(viewType){
                Setting.boolean-> Adapters.SimpleHolder(Setting.createSwitch(LayoutInflater.from(parent?.context),parent,false))
                Setting.multipleChoice-> Adapters.SimpleHolder(Setting.createSpinner(LayoutInflater.from(parent?.context),parent,false))
                Setting.stringValue-> Adapters.SimpleHolder(Setting.createStringEdit(LayoutInflater.from(parent?.context),parent,false))
                Setting.divider-> Adapters.SimpleHolder(Setting.createDivider(LayoutInflater.from(parent?.context),parent,false))
                else -> null
            }
            override fun getItemCount() = settings.size*2
            override fun onBindViewHolder(holder: Adapters.SimpleHolder?, position: Int) {
                when(getItemViewType(position)){
                    Setting.boolean->settings[position/2].linkToSwitch(holder?.v as Switch)
                    Setting.multipleChoice-> if (holder != null) {
                        settings[position/2].linkToSpinner(activity,holder.v)
                    }
                    Setting.stringValue-> if (holder != null) {
                        settings[position/2].linkToString(activity,holder.v)
                    }
                }
            }
            override fun getItemViewType(position: Int) =
                if(position%2==1)
                    Setting.divider
                else
                    settings[position/2].type
        }
    }
}