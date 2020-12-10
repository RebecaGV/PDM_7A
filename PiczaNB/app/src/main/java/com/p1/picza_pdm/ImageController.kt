package com.p1.picza_pdm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File

object ImageController {
    fun selectPhoto (activity: Activity,code: Int){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type= "image/*"
        activity.startActivityForResult(intent,code)

    }
    fun saveImage(context: Context, id:Long, uri: Uri){

        val file = File(context.filesDir,id.toString())
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(bytes)


    }
}