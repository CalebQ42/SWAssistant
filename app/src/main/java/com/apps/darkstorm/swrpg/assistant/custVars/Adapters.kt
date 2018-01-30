package com.apps.darkstorm.swrpg.assistant.custVars

import android.support.v7.widget.RecyclerView
import android.view.View

object Adapters {
    class SimpleHolder(var v: View): RecyclerView.ViewHolder(v)
    abstract class SearchableAdapter: RecyclerView.Adapter<SimpleHolder>(){
        abstract fun search(str: String)
    }
}