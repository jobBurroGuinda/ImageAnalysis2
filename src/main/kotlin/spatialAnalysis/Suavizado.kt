package spatialAnalysis

import open.OpenImage.toBufferedImage
import open.OpenImage.toImage
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.util.*


object Suavizado {
    fun agregarRuidoAditivo(io: Image, p: Int): Image {
        val dim = io.getWidth(null) * io.getHeight(null)
        val cp = dim / 100 * p
        val bi = toBufferedImage(io)
        val ran = Random()
        for (x in 0 until cp) {
            val r = ran.nextInt(bi.height)
            val c = ran.nextInt(bi.width)
            bi.setRGB(c, r, Color.WHITE.rgb)
        }
        return toImage(bi)
    }

    fun agregarRuidoSustractivo(io: Image, p: Int): Image {
        val dim = io.getWidth(null) * io.getHeight(null)
        val cp = dim / 100 * p
        val bi = toBufferedImage(io)
        val ran = Random()
        for (x in 0 until cp) {
            val r = ran.nextInt(bi.height)
            val c = ran.nextInt(bi.width)
            bi.setRGB(c, r, Color.BLACK.rgb)
        }
        return toImage(bi)
    }

    fun suavizar(io: Image?, mascara: Array<IntArray>): Image {
        val bi = toBufferedImage(io!!)
        val bnuevo = BufferedImage(bi.width, bi.height, BufferedImage.TYPE_INT_RGB)
        // recorres el buffer
        for (x in 0 until bi.width) {
            for (y in 0 until bi.height) {
                val rgb = calcularNuevoTono(x, y, bi, mascara)
                bnuevo.setRGB(x, y, rgb)
            }
        }
        return toImage(bnuevo)
    }

    private fun calcularNuevoTono(x: Int, y: Int, bi: BufferedImage, mascara: Array<IntArray>): Int {
        // recorrer la mascara
        // int r = x -1;
        //int c = y -1;
        var auxR = 0
        var auxG = 0
        var auxB = 0
        var color: Color? = null
        var k = 0
        var i = 0
        var r = x - 1
        while (i < mascara.size) {
            var j = 0
            var c = y - 1
            while (j < mascara[0].size) {
                if (mascara[i][j] != 0) {
                    try {
                        val rgb = bi.getRGB(r, c)
                        k++
                        color = Color(rgb)
                        auxR += color.red
                        auxG += color.green
                        auxB += color.blue
                    } catch (e: Exception) {
                        // nada de nada
                    }
                }
                j++
                c++
            }
            i++
            r++
        }
        if (k != 0) {
            auxR /= k
            auxG /= k
            auxB /= k
        }
        color = Color(auxR, auxG, auxB)
        return color.rgb
    }
}