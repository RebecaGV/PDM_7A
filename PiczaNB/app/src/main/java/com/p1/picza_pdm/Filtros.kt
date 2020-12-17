package com.p1.picza_pdm

import android.graphics.*
import kotlinx.android.synthetic.main.abrir_galeria.*
import kotlin.math.pow


class Filtros {

    fun EscalaGrises(src: Bitmap?): Bitmap? {
        var src = src
        val GrisArray = floatArrayOf(
                0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
        val colorMatrixGray = ColorMatrix(GrisArray)
        val w = src!!.width
        val h = src.height
        val bitmapResult = Bitmap
                .createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvasResult = Canvas(bitmapResult)
        val paint = Paint()
        val filter = ColorMatrixColorFilter(colorMatrixGray)
        paint.colorFilter = filter
        canvasResult.drawBitmap(src, 0f, 0f, paint)
        src.recycle()
        src = null
        return bitmapResult
    }
    fun invertir(src: Bitmap?): Bitmap? {
        var src = src
        val output = Bitmap.createBitmap(src!!.width, src.height, src.config)
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
                output.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        src.recycle()
        src = null
        return output
    }
    fun brillo(src: Bitmap?, value: Int): Bitmap? {

        var src = src
        val width = src!!.width
        val height = src.height

        val bmOut = Bitmap.createBitmap(width, height, src.config)

        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
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

                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        src.recycle()
        src = null
        return bmOut
    }
    fun contraste(src: Bitmap?, value: Double): Bitmap? {

        var src = src
        val width = src!!.width
        val height = src.height

        val bmOut = Bitmap.createBitmap(width, height, src.config)


        val c = Canvas()
        c.setBitmap(bmOut)


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

                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        src.recycle()
        src = null
        return bmOut
    }
    fun gamma(src: Bitmap?, red: Double, green: Double, blue: Double): Bitmap? {
        var src = src
        var red = red
        var green = green
        var blue = blue
        red = (red + 2) / 10.0
        green = (green + 2) / 10.0
        blue = (blue + 2) / 10.0

        val bmOut = Bitmap.createBitmap(src!!.width, src.height, src.config)

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

        // gamma arrays
        val gammaR = IntArray(MAX_SIZE)
        val gammaG = IntArray(MAX_SIZE)
        val gammaB = IntArray(MAX_SIZE)

        // setting values for every gamma channels
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

                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        src.recycle()
        src = null


        return bmOut
    }
    fun canales(src: Bitmap?, red: Double, green: Double, blue: Double): Bitmap? {
        var src = src
        var red = red
        var green = green
        var blue = blue
        red = red / 100
        green = green / 100
        blue = blue / 100


        val width = src!!.width
        val height = src.height

        val bmOut = Bitmap.createBitmap(width, height, src.config)

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

                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        src.recycle()
        src = null

        // return final image
        return bmOut
    }
    //Convolucion
    fun smoothing(src: Bitmap): Bitmap {
        val SmootConfig = arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1))
        val convMatrix: ConvolucionMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(SmootConfig)
        convMatrix.Factor = 9
        convMatrix.Offset = 0
        return convMatrix.Convolucion(src)
    }
    fun edge(src: Bitmap): Bitmap {
        val EdgeConfig = arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(0, 0, 0),
                intArrayOf(-1, -1, -1))
        val convMatrix: ConvolucionMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(EdgeConfig)
        convMatrix.Factor = 1
        convMatrix.Offset = 127
        return convMatrix.Convolucion(src)
    }
    fun mean(src: Bitmap): Bitmap {
        val MeanConfig = arrayOf(
                intArrayOf(-1, -1, -1),
                intArrayOf(-1, 9, -1),
                intArrayOf(-1, -1, -1))
        val convMatrix: ConvolucionMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(MeanConfig)
        convMatrix.Factor = 1
        convMatrix.Offset = 0
        return convMatrix.Convolucion(src)
    }
    fun sharpen(src: Bitmap): Bitmap {
        val SharpConfig = arrayOf(
                intArrayOf(0, -2, 0),
                intArrayOf(-2, 11, -2),
                intArrayOf(0, -2, 0))
        val convMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(SharpConfig)
        convMatrix.Factor = 3
        return convMatrix.Convolucion(src)
    }
    fun emboss(src: Bitmap): Bitmap {
        val EmbossConfig = arrayOf(
                intArrayOf(-1, 0, -1),
                intArrayOf(0, 4, 0),
                intArrayOf(-1, 0, -1))
        val convMatrix: ConvolucionMatrix = ConvolucionMatrix()
        convMatrix.aplicarConfig(EmbossConfig)
        convMatrix.Factor = 1
        convMatrix.Offset = 127
        return convMatrix.Convolucion(src)
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
        src.recycle()
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