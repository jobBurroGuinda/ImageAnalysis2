package spatialAnalysis

import open.OpenImage.toBufferedImage
import open.OpenImage.toImage
import java.awt.Color
import java.awt.Image


object FiltrosEspaciales {
    fun generarImagenGrises(io: Image?): Image {
        val bi = toBufferedImage(io!!)
        var color: Color
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            val prom = (color.red + color.green + color.blue) / 3
            color = Color(prom, prom, prom)
            bi.setRGB(x, y, color.rgb)
        }
        return toImage(bi)
    }

    fun generarImagenNegativa(io: Image?): Image {
        val bi = toBufferedImage(io!!)
        var color: Color
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            color = Color(
                255 - color.red, 255 - color.green, 255 - color.blue
            )
            bi.setRGB(x, y, color.rgb)
        }
        return toImage(bi)
    }

    fun iluminarImagen(imagen: Image?, alpha: Int): Image {
        val bi = toBufferedImage(imagen!!)
        var color: Color
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            val r = color.red + alpha
            val g = color.green + alpha
            val b = color.blue + alpha
            color = Color(
                validarLimites(r),
                validarLimites(g),
                validarLimites(b)
            )
            bi.setRGB(x, y, color.rgb)
        }
        return toImage(bi)
    }

    fun expansionLineal(r1: Int, r2: Int, imagen: Image?): Image {
        val bi = toBufferedImage(imagen!!)
        var color: Color
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            val r = (color.red - r1) * (255 / r2 - r1)
            val g = (color.green - r1) * (255 / r2 - r1)
            val b = (color.blue - r1) * (255 / r2 - r1)
            color = Color(
                validarLimites(r),
                validarLimites(g),
                validarLimites(b)
            )
            bi.setRGB(x, y, color.rgb)
        }
        return toImage(bi)
    }

    fun expansionLineal(h: Histogramas, imagen: Image?): Image {
        val bi = toBufferedImage(imagen!!)
        var color: Color
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            val r = (color.red - h.getMinR()) * (255 / (h.getMaxR() - h.getMinR()))
            val g = (color.green - h.getMinG()) * (255 / (h.getMaxG() - h.getMinG()))
            val b = (color.blue - h.getMinB()) * (255 / (h.getMaxB() - h.getMinB()))
            color = Color(
                validarLimites(r),
                validarLimites(g),
                validarLimites(b)
            )
            bi.setRGB(x, y, color.rgb)
        }
        return toImage(bi)
    }

    fun modificarTemperatura(imagen: Image?, alpha: Int): Image {
        val bi = toBufferedImage(imagen!!)
        var color: Color
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            val r = color.red + alpha
            val g = color.green
            val b = color.blue - alpha
            color = Color(
                validarLimites(r),
                validarLimites(g),
                validarLimites(b)
            )
            bi.setRGB(x, y, color.rgb)
        }
        return toImage(bi)
    }

    fun validarLimites(aux: Int): Int {
        if (aux < 0) return 0
        return if (aux > 255) 255 else aux
    }

    fun obtenerMin(h: DoubleArray): Int {
        for (x in h.indices) {
            if (h[x] != 0.0) return x
        }
        return -1
    }

    fun obtenerMax(h: DoubleArray): Int {
        for (x in h.indices.reversed()) {
            if (h[x] != 0.0) return x
        }
        return -1
    }

    fun segmentarImagen(imagen: Image?, umbral: Int): Image {
        val bi = toBufferedImage(imagen!!)
        var color: Color
        val colorFondo: Color
        colorFondo = Color(255, 255, 255)
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            val prom = (color.red + color.green + color.blue) / 3
            if (prom > umbral) {
                bi.setRGB(x, y, colorFondo.rgb)
            }
        }
        return toImage(bi)
    }

    fun segmentarImagen(imagen: Image?, u1: Int, u2: Int): Image {
        // TODO: garantizar  que el u2>u1
        val bi = toBufferedImage(imagen!!)
        var color: Color
        val colorFondo: Color
        colorFondo = Color(255, 255, 255)
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            val prom = (color.red + color.green + color.blue) / 3
            if (!(prom >= u1 && prom <= u2)) {
                bi.setRGB(x, y, colorFondo.rgb)
            }
        }
        return toImage(bi)
    }

    fun ecualizarImagen(imagen: Image): Image {
        val nxm = imagen.getWidth(null) * imagen.getHeight(null)
        val h = Histogramas(imagen)
        val ho: DoubleArray = h.hRed
        val daf = DoubleArray(256)
        val nt = IntArray(256)
        daf[0] = ho[0]
        nt[0] = Math.round(daf[0] / nxm * 255).toInt()
        // recorremos el histograma para acumular
        for (x in 1 until ho.size) {
            daf[x] = (ho[x] + daf[x - 1])
            val aux = daf[x] / nxm
            val tmp = Math.round(aux * 255).toInt()
            nt[x] = tmp
        }
        val bi = toBufferedImage(imagen)
        var color: Color
        for (x in 0 until bi.width) for (y in 0 until bi.height) {
            color = Color(bi.getRGB(x, y))
            val t = color.red
            val t2 = nt[t]
            color = Color(t2, t2, t2)
            bi.setRGB(x, y, color.rgb)
        }
        return toImage(bi)
    }
}