package open

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


object OpenImage {
    fun openImage(): Image? {
        try {
            // definir los filtros para lectura
            val filtro = FileNameExtensionFilter("Imagenes", "jpg", "jpeg", "png", "bmp")
            // crear un selector de archivos
            val selector = JFileChooser()
            // agregar el filtro al selector
            selector.addChoosableFileFilter(filtro)
            // especificar que solo se puedan abrir archivos
            selector.fileSelectionMode = JFileChooser.FILES_ONLY

            //ejecutar el selector de imagenes
            val res = selector.showOpenDialog(null)
            if (res == 1) {
                return null
            }
            val archivo = selector.selectedFile
            val bi = ImageIO.read(archivo)
            return toImage(bi)
        } catch (ex: IOException) {
            //Logger.getLogger(ImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null
    }

    fun toImage(bi: BufferedImage): Image {
        return bi.getScaledInstance(bi.width, bi.height, BufferedImage.TYPE_INT_RGB)
    }

    fun toBufferedImage(imagen: Image): BufferedImage {
        // imagen es un objeto de tipo BufferedImage
        if (imagen is BufferedImage) {
            return imagen
        }
        val bi = BufferedImage(imagen.getWidth(null), imagen.getHeight(null), BufferedImage.TYPE_INT_RGB)
        val nueva = bi.createGraphics()
        nueva.drawImage(imagen, 0, 0, null)
        nueva.dispose()
        return bi
    }
}

