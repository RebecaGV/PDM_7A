package com.p1.picza_pdm

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.abrir_galeria.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private val SELECT_ACTIVITY = 50
    private var imageUri : Uri? = null
    private val id = 4
    private var img: Bitmap?=null
    private var img1: Bitmap?=null
    private var img2: Bitmap?=null
    lateinit var Undo:Button
    lateinit var FiltroNegativo:Button
    lateinit var FiltroEscalaGrises:Button
    lateinit var FiltroBrillo:Button
    lateinit var FiltroContraste:Button
    lateinit var FiltroGamma:Button
    lateinit var FiltroCanales:Button
    lateinit var FiltroSmoot:Button
    lateinit var FiltroGauss:Button
    lateinit var FiltroSharpen:Button
    lateinit var FiltroMean:Button
    lateinit var FiltroEmbo:Button
    lateinit var FiltroEdge:Button



    val CAMERA_REQUEST_CODE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.abrir_galeria)
        FiltroNegativo = findViewById(R.id.btnNegativo)
        FiltroEscalaGrises = findViewById(R.id.btnGris)
        FiltroBrillo = findViewById(R.id.btnBrillo)
        FiltroContraste = findViewById(R.id.btnCont)
        FiltroGamma = findViewById(R.id.btnGamma)
        FiltroCanales = findViewById(R.id.btnCanales)
        FiltroSmoot = findViewById(R.id.btnSmoot)
        FiltroGauss = findViewById(R.id.btnGaussian)
        FiltroSharpen = findViewById(R.id.btnSharpen)
        FiltroMean = findViewById(R.id.btnMean)
        FiltroEmbo = findViewById(R.id.btnEmbo)
        FiltroEdge = findViewById(R.id.btnEdge)
        Undo = findViewById(R.id.btnUndo)


        abrirG.setOnClickListener(){
            ImageController.selectPhoto(this,SELECT_ACTIVITY)
        }
        btnCamara.setOnClickListener{
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }
        Undo.setOnClickListener(){

        }
        FiltroNegativo.setOnClickListener(){

        }
        FiltroEscalaGrises.setOnClickListener(){

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
                    img=data.extras?.get("data") as Bitmap
                    mostrar_im.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }


    }
    //Programacion del boton
    fun revertir(src: Bitmap?): Bitmap? {
        var src = src
        return src
    }





}