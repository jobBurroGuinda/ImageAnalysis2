package spatialAnalysis

import open.OpenImage.toBufferedImage
import open.OpenImage.toImage
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage


object Convolucion {
    fun aplicarConvolucion(io: Image?, mascara: Array<IntArray>, div: Int, offset: Int): Image {
        val bi = toBufferedImage(io!!)
        val bnuevo = BufferedImage(bi.width, bi.height, BufferedImage.TYPE_INT_RGB)
        // recorres el buffer
        for (x in 0 until bi.width) {
            for (y in 0 until bi.height) {
                val rgb = calcularNuevoTono(x, y, bi, mascara, div, offset)
                bnuevo.setRGB(x, y, rgb)
            }
        }
        return toImage(bnuevo)
    }

    private fun calcularNuevoTono(
        x: Int,
        y: Int,
        bi: BufferedImage,
        mascara: Array<IntArray>,
        div: Int,
        offset: Int
    ): Int {
        // recorrer la mascara
        // int r = x -1;
        //int c = y -1;
        var auxR = 0
        var auxG = 0
        var auxB = 0
        var color: Color?
        var k = 0
        var i = 0
        var r = x - 1
        while (i < mascara.size) {
            var j = 0
            var c = y - 1
            while (j < mascara[0].size) {
                // todo: quitar el if
                if (mascara[i][j] != 0) {
                    try {
                        val rgb = bi.getRGB(r, c)
                        k++
                        color = Color(rgb)
                        // acomodar la multiplicación
                        auxR += color.red * mascara[i][j]
                        auxG += color.green * mascara[i][j]
                        auxB += color.blue * mascara[i][j]
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
        // quitar k
        // quitar el if
        if (k != 0) {
            auxR /= div
            auxG /= div
            auxB /= div
        }
        color = Color(auxR + offset, auxG + offset, auxB + offset)
        return color.rgb
    }
}