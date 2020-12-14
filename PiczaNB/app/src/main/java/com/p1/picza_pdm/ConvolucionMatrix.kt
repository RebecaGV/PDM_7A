package com.p1.picza_pdm

import android.graphics.Bitmap
import android.graphics.Color


class ConvolucionMatrix {
    constructor(size: Int) {
        Matrix = Array(size) { DoubleArray(size) }
    }

    constructor() {

    }
    val SIZE = 3

    lateinit var Matrix: Array<DoubleArray>
    var Factor = 1.0
    var Offset = 1.0

    fun ConvolucionMatrix(size: Int) {
        Matrix = Array(size) { DoubleArray(size) }
    }

    fun setAll(value: Double) {
        for (x in 0 until SIZE) {
            for (y in 0 until SIZE) {
                Matrix[x][y] = value
            }
        }
    }

    fun aplicarConfig(config: Array<DoubleArray>) {
        for (x in 0 until SIZE) {
            for (y in 0 until SIZE) {
                Matrix[x][y] = config[x][y]
            }
        }
    }

    fun Convolucion(src: Bitmap?, matrix: ConvolucionMatrix): Bitmap? {
        var src = src
        val width = src!!.width
        val height = src.height
        val result = Bitmap.createBitmap(width, height, src.config)
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var sumR: Int
        var sumG: Int
        var sumB: Int
        val pixels = Array(SIZE) { IntArray(SIZE) }
        for (y in 0 until height - 2) {
            for (x in 0 until width - 2) {

                for (i in 0 until SIZE) {
                    for (j in 0 until SIZE) {
                        pixels[i][j] = src?.getPixel(x + i, y + j)!!
                    }
                }

                A = Color.alpha(pixels[1][1])

                sumB = 0
                sumG = sumB
                sumR = sumG

                for (i in 0 until SIZE) {
                    for (j in 0 until SIZE) {
                        sumR += (Color.red(pixels[i][j]) * matrix.Matrix.get(i).get(j)).toInt()
                        sumG += (Color.green(pixels[i][j]) * matrix.Matrix.get(i).get(j)).toInt()
                        sumB += (Color.blue(pixels[i][j]) * matrix.Matrix.get(i).get(j)).toInt()
                    }
                }

                R = ((sumR / matrix.Factor + matrix.Offset).toInt())
                if (R < 0) {
                    R = 0
                } else if (R > 255) {
                    R = 255
                }

                G = ((sumG / matrix.Factor + matrix.Offset).toInt())
                if (G < 0) {
                    G = 0
                } else if (G > 255) {
                    G = 255
                }

                B = ((sumB / matrix.Factor + matrix.Offset).toInt())
                if (B < 0) {
                    B = 0
                } else if (B > 255) {
                    B = 255
                    result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B))
                }
            }
        }
        src?.recycle()
        src = null

        // final image
        return result
    }


    }

