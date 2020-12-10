package com.p1.picza

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class pickImage: AppCompatActivity() {
    private val SELECT_ACTIVITY = 10
    private var imageUri: Uri? = null
    val cargarFoto: ImageView = findViewById(R.id.imageSelect_iv);

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        cargarFoto.setOnClickListener{
            ImageController.selectPhotoFromGallery(this, SELECT_ACTIVITY)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when{
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK ->{
                imageUri = data!!.data

                cargarFoto.setImageURI(imageUri)
            }
        }
    }

}