package com.apps.darkstorm.cdr.saveLoad

import com.apps.darkstorm.swrpg.assistant.saveLoad.JsonSavable
import com.google.android.gms.drive.DriveFile
import com.google.android.gms.drive.DriveResourceClient
import com.google.android.gms.tasks.Tasks
import java.io.File
import java.io.InputStreamReader

object Load{
    fun local(js: JsonSavable, filename: String){
        if(File(filename+".bak").exists()) {
            js.loadJson(File(filename).reader())
            Save.local(js,filename)
            File(filename+".bak").delete()
        }else
            js.loadJson(File(filename).reader())
    }
    fun drive(js: JsonSavable, drc: DriveResourceClient, df: DriveFile, blocking: Boolean){
        if(!blocking)
            drc.openFile(df, DriveFile.MODE_READ_ONLY).addOnSuccessListener { driveContents ->
                js.loadJson(InputStreamReader(driveContents.inputStream))
            }
        else{
            val res = drc.openFile(df, DriveFile.MODE_READ_ONLY)
            Tasks.await(res)
            val cont = res.result
            js.loadJson(InputStreamReader(cont.inputStream))
        }
    }
}