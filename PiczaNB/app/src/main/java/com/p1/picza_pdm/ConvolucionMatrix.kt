package com.p1.picza_pdm

import android.graphics.Bitmap
import android.graphics.Color


class ConvolucionMatrix {

    val SIZE = 3
    private var Matrix: Array<IntArray> = Array(SIZE) { IntArray(SIZE) }
    var Factor = 1
    var Offset = 1

    fun aplicarConfig(config: Array<IntArray>) {
        for (x in 0 until SIZE) {
            for (y in 0 until SIZE) Matrix[x][y] = config[x][y]
        }
    }

    fun Convolucion(src: Bitmap): Bitmap {
        var picw : Int = src.width
        var pich : Int = src.height
        val bitmap : Bitmap = src.copy(src.config, true)
        val pixel = IntArray(picw * pich)
        bitmap.getPixels(pixel, 0, picw, 0, 0, picw, pich)

        var sumaR = 0
        var sumaG = 0
        var sumaB = 0
        var index = 0
        var R = 0
        var G = 0
        var B = 0

        for (x in 1 until picw - 1) {
            for (y in 1 until pich - 1) {

                sumaR = 0
                sumaG = 0
                sumaB = 0
                for (i in -1 until 2) {
                    for (j in -1 until 2) {
                        index = (y + j) * picw + (x + i)
                        sumaR += (pixel[index] shr 16 and 0xff) * Matrix[i +1][j + 1]
                        sumaG += (pixel[index] shr 8 and 0xff) * Matrix[i + 1][j + 1]
                        sumaB += (pixel[index] and 0xff) * Matrix[i + 1][j + 1]
                    }
                }

                index = (y) * picw + (x)
                R = validar(sumaR)
                G = validar(sumaG)
                B = validar(sumaB)

                pixel[index] = -0x1000000 or (R shl 16) or (G shl 8) or B
            }
        }

        bitmap.setPixels(pixel, 0, picw, 0, 0, picw, pich);
        return bitmap
    }

    private fun validar(sumatoria: Int) : Int {
        var suma: Int= sumatoria / Factor + Offset
        if (suma < 0.0) {
            suma = -suma
        }
        if (suma < 0) {
            suma = 0
        } else if (suma > 255) {
            suma = 255
        }
        return suma
    }
    }

