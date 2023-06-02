import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun createImage(width: Int, height: Int): BufferedImage {
    return BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
}

fun File.readImage(): BufferedImage {
    return ImageIO.read(this)
}

fun BufferedImage.write(file: File) {
    require(file.extension == "png")
    val dir = file.parentFile
    if (!dir.exists()) {
        require(dir.mkdirs())
    }
    require(ImageIO.write(this, "png", file))
}

fun BufferedImage.useGraphics(block: Graphics2D.() -> Unit): BufferedImage {
    val graphics = createGraphics()
    graphics.block()
    graphics.dispose()
    return this
}

fun Graphics2D.drawScaledImage(
    srcImage: BufferedImage,
    srcX: Int,
    srcY: Int,
    srcWidth: Int,
    srcHeight: Int,
    dstX: Int,
    dstY: Int,
    dstWidth: Int,
    dstHeight: Int,
): Graphics2D {
    val sub = srcImage.getSubimage(srcX, srcY, srcWidth, srcHeight)
    val scaled = sub.getScaledInstance(dstWidth, dstHeight, BufferedImage.SCALE_DEFAULT)
    drawImage(scaled, dstX, dstY, null)
    return this
}
