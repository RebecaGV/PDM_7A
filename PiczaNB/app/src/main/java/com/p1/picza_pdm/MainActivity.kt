package com.p1.picza_pdm

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.abrir_galeria.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private val SELECT_ACTIVITY = 50
    private var imageUri : Uri? = null
    private val id = 4

    val CAMERA_REQUEST_CODE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.abrir_galeria)
        abrirG.setOnClickListener(){
            ImageController.selectPhoto(this,SELECT_ACTIVITY)
        }
        btnCamara.setOnClickListener{
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode== SELECT_ACTIVITY && resultCode==Activity.RESULT_OK ->{
                imageUri=data!!.data

                mostrar_im.setImageURI(imageUri)
                imageUri?.let {
                    ImageController.saveImage(this,id.toLong(),it)
                }
            }
        }
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    mostrar_im.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Programacion del boton

}