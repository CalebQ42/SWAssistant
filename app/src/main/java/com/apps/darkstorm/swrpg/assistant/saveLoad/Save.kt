package com.apps.darkstorm.cdr.saveLoad

import com.apps.darkstorm.swrpg.assistant.saveLoad.JsonSavable
import com.google.android.gms.drive.DriveFile
import com.google.android.gms.drive.DriveResourceClient
import com.google.android.gms.tasks.Tasks
import java.io.File
import java.io.FileWriter
import java.io.OutputStreamWriter

object Save{
    fun local(js: JsonSavable, filename: String){
        var tmp = File(filename)
        if(tmp.exists())
            tmp.renameTo(File(filename+".bak"))
        tmp = File(filename+".bak")
        File(filename).delete()
        js.saveJson(FileWriter(filename))
        File(filename).setReadable(true)
        File(filename).setWritable(true)
        tmp.delete()
    }
    fun drive(js: JsonSavable, drc: DriveResourceClient, df: DriveFile, blocking: Boolean){
        if(!blocking)
            drc.openFile(df,DriveFile.MODE_WRITE_ONLY).addOnSuccessListener {driveContents ->
                val os = driveContents.outputStream
                val osw = OutputStreamWriter(os)
                js.saveJson(osw)
                os.close()
                drc.commitContents(driveContents,null)
            }
        else {
            val res = drc.openFile(df, DriveFile.MODE_WRITE_ONLY)
            Tasks.await(res)
            val cont = res.result
            val os = cont.outputStream
            val osw = OutputStreamWriter(os)
            js.saveJson(osw)
            os.close()
            drc.commitContents(cont,null)
        }
    }
}