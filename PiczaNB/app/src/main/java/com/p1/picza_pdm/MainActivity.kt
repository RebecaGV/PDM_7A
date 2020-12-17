package com.p1.picza_pdm

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.abrir_galeria.*
import kotlin.math.pow


class MainActivity : AppCompatActivity() {
    private val SELECT_ACTIVITY = 50
    private var imageUri : Uri? = null
    private val id = 4
    private lateinit var img: Bitmap
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
            ImageController.selectPhoto(this, SELECT_ACTIVITY)
        }
        btnCamara.setOnClickListener{
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }
        Undo.setOnClickListener(){
            mostrar_im.setImageBitmap(img)
        }
        FiltroNegativo.setOnClickListener(){
            invertir(img)
        }
        FiltroEscalaGrises.setOnClickListener(){
            EscalaGrises(img)
        }
        FiltroBrillo.setOnClickListener(){
            brillo(img, 50)
        }
        FiltroContraste.setOnClickListener(){
            contraste(img, 50.0)
        }
        FiltroGamma.setOnClickListener(){
            gamma(img, 20.0, 30.0, 100.0)
        }
        FiltroCanales.setOnClickListener(){
            canales(img, 20.0, 30.0, 100.0)
        }
        FiltroSmoot.setOnClickListener(){
            smoothing(img)
        }
        FiltroEdge.setOnClickListener(){
            edge(img)
        }
        FiltroMean.setOnClickListener(){
            mean(img)
        }
        FiltroSharpen.setOnClickListener(){
            sharpen(img)
        }
        FiltroEmbo.setOnClickListener(){
            emboss(img)
        }
        FiltroGauss.setOnClickListener(){
            gaussian(img)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode== SELECT_ACTIVITY && resultCode==Activity.RESULT_OK ->{
                imageUri=data!!.data

                mostrar_im.setImageURI(imageUri)
                img= (mostrar_im.drawable as BitmapDrawable).bitmap

                imageUri?.let {
                    ImageController.saveImage(this, id.toLong(), it)
                }
            }
        }
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    img = data.extras?.get("data") as Bitmap
                    mostrar_im.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
               // Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }


    }
    //Programacion del boton
    fun EscalaGrises(src: Bitmap) {
        var src=src
        val GrisArray = floatArrayOf(
                0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
        val colorMatrixGray = ColorMatrix(GrisArray)
        val w = src.width
        val h = src.height
        val res = Bitmap
                .createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvasResult = Canvas(res)
        val paint = Paint()
        val filter = ColorMatrixColorFilter(colorMatrixGray)
        paint.colorFilter = filter
        canvasResult.drawBitmap(src, 0f, 0f, paint)
        mostrar_im.setImageBitmap(res)
    }
    fun invertir(src: Bitmap) {
        var src = src
        val res = Bitmap.createBitmap(src!!.width, src.height, src.config)
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int
        val height = src.height
        val width = src.width
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixelColor = src.getPixel(x, y)
                A = Color.alpha(pixelColor)
                R = 255 - Color.red(pixelColor)
                G = 255 - Color.green(pixelColor)
                B = 255 - Color.blue(pixelColor)
                res.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        mostrar_im.setImageBitmap(res)

    }
    fun brillo(src: Bitmap, value: Int) {

        var src = src
        val width = src!!.width
        val height = src.height

        val res = Bitmap.createBitmap(width, height, src.config)

        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        for (x in 0 until width) {
            for (y in 0 until height) {

                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)

                R += value
                if (R > 255) {
                    R = 255
                } else if (R < 0) {
                    R = 0
                }
                G += value
                if (G > 255) {
                    G = 255
                } else if (G < 0) {
                    G = 0
                }
                B += value
                if (B > 255) {
                    B = 255
                } else if (B < 0) {
                    B = 0
                }

                res.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        mostrar_im.setImageBitmap(res)
    }
    fun contraste(src: Bitmap, value: Double){
        var src = src
        val width = src!!.width
        val height = src.height
        val res = Bitmap.createBitmap(width, height, src.config)

        val c = Canvas()
        c.setBitmap(res)


        c.drawBitmap(src, 0f, 0f, Paint(Color.BLACK))

        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        val contrast = Math.pow((100 + value) / 100, 2.0)

        for (x in 0 until width) {
            for (y in 0 until height) {

                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)

                R = Color.red(pixel)
                R = (((R / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (R < 0) {
                    R = 0
                } else if (R > 255) {
                    R = 255
                }
                G = Color.green(pixel)
                G = (((G / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (G < 0) {
                    G = 0
                } else if (G > 255) {
                    G = 255
                }
                B = Color.blue(pixel)
                B = (((B / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (B < 0) {
                    B = 0
                } else if (B > 255) {
                    B = 255
                }

                res.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        mostrar_im.setImageBitmap(res)
    }
    fun gamma(src: Bitmap, red: Double, green: Double, blue: Double) {
        var src = src
        var red = red
        var green = green
        var blue = blue
        red = (red + 2) / 10.0
        green = (green + 2) / 10.0
        blue = (blue + 2) / 10.0

        val res = Bitmap.createBitmap(src.width, src.height, src.config)

        val width = src.width
        val height = src.height

        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        val MAX_SIZE = 256
        val MAX_VALUE_DBL = 255.0
        val MAX_VALUE_INT = 255
        val REVERSE = 1.0

        val gammaR = IntArray(MAX_SIZE)
        val gammaG = IntArray(MAX_SIZE)
        val gammaB = IntArray(MAX_SIZE)


        for (i in 0 until MAX_SIZE) {
            gammaR[i] = MAX_VALUE_INT.coerceAtMost((MAX_VALUE_DBL * (i / MAX_VALUE_DBL).pow(REVERSE / red) + 0.5).toInt())
            gammaG[i] = MAX_VALUE_INT.coerceAtMost((MAX_VALUE_DBL * (i / MAX_VALUE_DBL).pow(REVERSE / green) + 0.5).toInt())
            gammaB[i] = MAX_VALUE_INT.coerceAtMost((MAX_VALUE_DBL * (i / MAX_VALUE_DBL).pow(REVERSE / blue) + 0.5).toInt())
        }


        for (x in 0 until width) {
            for (y in 0 until height) {

                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)

                R = gammaR[Color.red(pixel)]
                G = gammaG[Color.green(pixel)]
                B = gammaB[Color.blue(pixel)]

                res.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        mostrar_im.setImageBitmap(res)
    }
    fun canales(src: Bitmap, red: Double, green: Double, blue: Double) {
        var src = src
        var red = red
        var green = green
        var blue = blue
        red = red / 100
        green = green / 100
        blue = blue / 100


        val width = src.width
        val height = src.height

        val res = Bitmap.createBitmap(width, height, src.config)

        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int


        for (x in 0 until width) {
            for (y in 0 until height) {

                pixel = src.getPixel(x, y)

                A = Color.alpha(pixel)
                R = (Color.red(pixel) * red).toInt()
                G = (Color.green(pixel) * green).toInt()
                B = (Color.blue(pixel) * blue).toInt()

                res.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        mostrar_im.setImageBitmap(res)
    }
    //Convolucion
    fun smoothing(src: Bitmap){
        val SmootConfig = arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1))
        val convMatrix: ConvolucionMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(SmootConfig)
        convMatrix.Factor = 9
        convMatrix.Offset = 0
        var src2= convMatrix.Convolucion(src)
        mostrar_im.setImageBitmap(src2)
    }
    fun edge(src: Bitmap){
        val EdgeConfig = arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(0, 0, 0),
                intArrayOf(-1, -1, -1))
        val convMatrix: ConvolucionMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(EdgeConfig)
        convMatrix.Factor = 1
        convMatrix.Offset = 127
        var src2= convMatrix.Convolucion(src)
        mostrar_im.setImageBitmap(src2)
    }
    fun mean(src: Bitmap){
        val MeanConfig = arrayOf(
                intArrayOf(-1, -1, -1),
                intArrayOf(-1, 9, -1),
                intArrayOf(-1, -1, -1))
        val convMatrix: ConvolucionMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(MeanConfig)
        convMatrix.Factor = 1
        convMatrix.Offset = 0
        var src2= convMatrix.Convolucion(src)
        mostrar_im.setImageBitmap(src2)
    }
    fun sharpen(src: Bitmap){
        val SharpConfig = arrayOf(
                intArrayOf(0, -2, 0),
                intArrayOf(-2, 11, -2),
                intArrayOf(0, -2, 0))
        val convMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(SharpConfig)
        convMatrix.Factor = 3
        convMatrix.Offset = 0
        var src2= convMatrix.Convolucion(src)
        mostrar_im.setImageBitmap(src2)
    }
    fun emboss(src: Bitmap) {
        val EmbossConfig = arrayOf(
                intArrayOf(-1, 0, -1),
                intArrayOf(0, 4, 0),
                intArrayOf(-1, 0, -1))
        val convMatrix: ConvolucionMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(EmbossConfig)
        convMatrix.Factor = 1
        convMatrix.Offset = 127
        var src2= convMatrix.Convolucion(src)
        mostrar_im.setImageBitmap(src2)
    }
    fun gaussian(src: Bitmap){
        val GaussianBlurConfig = arrayOf(
                intArrayOf(1, 2, 1),
                intArrayOf(2, 4, 2),
                intArrayOf(1, 2, 1))
        val convMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(GaussianBlurConfig)
        convMatrix.Factor = 16
        convMatrix.Offset = 0
        var src2= convMatrix.Convolucion(src)
        mostrar_im.setImageBitmap(src2)
    }
    //extras 5
    fun sepia(src: Bitmap): Bitmap{

        var src = src
        val width = src!!.width
        val height = src.height

        val bmOut = Bitmap.createBitmap(width, height, src.config)

        val GS_RED = 0.3
        val GS_GREEN = 0.59
        val GS_BLUE = 0.11

        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int


        for (x in 0 until width) {
            for (y in 0 until height) {

                pixel = src.getPixel(x, y)

                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)

                R = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
                G = R
                B = G


                R += 110
                if (R > 255) {
                    R = 255
                }
                G += 65
                if (G > 255) {
                    G = 255
                }
                B += 20
                if (B > 255) {
                    B = 255
                }

                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        return bmOut
    }
    fun tint(src: Bitmap, color: Int): Bitmap {

        var src = src
        val width = src!!.width
        val height = src.height

        val bmOut = Bitmap.createBitmap(width, height, src.config)
        val p = Paint(Color.RED)
        val filter: ColorFilter = LightingColorFilter(color, 1)
        p.colorFilter = filter
        val c = Canvas()
        c.setBitmap(bmOut)
        c.drawBitmap(src, 0f, 0f, p)
        src.recycle()
        return bmOut
    }
    fun hue(bitmap: Bitmap, hue: Float): Bitmap{
        var bitmap = bitmap
        val newBitmap = bitmap!!.copy(bitmap.config, true)
        val width = newBitmap.width
        val height = newBitmap.height
        val hsv = FloatArray(3)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = newBitmap.getPixel(x, y)
                Color.colorToHSV(pixel, hsv)
                hsv[0] = hue
                newBitmap.setPixel(x, y, Color.HSVToColor(Color.alpha(pixel), hsv))
            }
        }
        bitmap.recycle()

        return newBitmap
    }
    fun vignette(image: Bitmap): Bitmap {
        val width = image.width
        val height = image.height
        val radius = (width / 1.2).toFloat()
        val colors = intArrayOf(0, 0x55000000, -0x1000000)
        val positions = floatArrayOf(0.0f, 0.5f, 1.0f)
        val gradient: RadialGradient
        gradient = RadialGradient((width / 2).toFloat(), (height / 2).toFloat(), radius, colors, positions, Shader.TileMode.CLAMP)


        val canvas = Canvas(image)
        canvas.drawARGB(1, 0, 0, 0)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.shader = gradient
        val rect = Rect(0, 0, image.width, image.height)
        val rectf = RectF(rect)
        canvas.drawRect(rectf, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(image, rect, rect, paint)
        return image
    }
    fun saturacion(src: Bitmap, value: Int): Bitmap {
        var src = src
        val f_value = (value / 100.0).toFloat()
        val w = src!!.width
        val h = src.height
        val bitmapResult = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvasResult = Canvas(bitmapResult)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(f_value)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        canvasResult.drawBitmap(src, 0f, 0f, paint)
        src.recycle()

        return bitmapResult
    }





}