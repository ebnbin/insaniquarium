package dev.ebnbin.kgdx.util

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import ktx.assets.disposeSafely
import java.util.zip.Deflater

fun createPixmap(width: Int, height: Int): Pixmap {
    return Pixmap(width, height, Pixmap.Format.RGBA8888)
}

fun createPixmap(byteArray: ByteArray): Pixmap {
    return Pixmap(byteArray, 0, byteArray.size)
}

fun Pixmap.drawSubPixmap(
    srcPixmap: Pixmap,
    srcX: Int,
    srcY: Int,
    srcWidth: Int,
    srcHeight: Int,
    dstX: Int,
    dstY: Int,
    dstWidth: Int,
    dstHeight: Int,
): Pixmap {
    val subPixmap = Pixmap(srcWidth, srcHeight, srcPixmap.format)
    subPixmap.drawPixmap(srcPixmap, 0, 0, srcX, srcY, srcWidth, srcHeight)
    drawPixmap(subPixmap, 0, 0, subPixmap.width, subPixmap.height, dstX, dstY, dstWidth, dstHeight)
    subPixmap.disposeSafely()
    return this
}

fun Pixmap.write(fileHandle: FileHandle): Pixmap {
    PixmapIO.writePNG(fileHandle, this, Deflater.NO_COMPRESSION, false)
    return this
}
