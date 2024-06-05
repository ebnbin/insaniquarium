package dev.ebnbin.kgdx.util

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import ktx.assets.disposeSafely
import ktx.graphics.copy
import java.util.zip.Deflater
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun createPixmap(width: Int, height: Int): Pixmap {
    return Pixmap(width, height, Pixmap.Format.RGBA8888)
}

fun createPixmap(byteArray: ByteArray): Pixmap {
    return Pixmap(byteArray, 0, byteArray.size)
}

fun Pixmap.copy(): Pixmap {
    val pixmap = createPixmap(width, height)
    pixmap.drawPixmap(this, 0, 0)
    return pixmap
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
) {
    val subPixmap = Pixmap(srcWidth, srcHeight, srcPixmap.format)
    subPixmap.drawPixmap(srcPixmap, 0, 0, srcX, srcY, srcWidth, srcHeight)
    drawPixmap(subPixmap, 0, 0, subPixmap.width, subPixmap.height, dstX, dstY, dstWidth, dstHeight)
    subPixmap.disposeSafely()
}

fun Pixmap.mask(maskPixmap: Pixmap): Pixmap {
    require(width == maskPixmap.width && height == maskPixmap.height)
    val maskedPixmap = createPixmap(width, height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val color = getPixel(x, y).toColor()
            require(color.isOpaque)
            val maskColor = maskPixmap.getPixel(x, y).toColor()
            val blendColor = color.copy(alpha = maskColor.gray)
            maskedPixmap.drawPixel(x, y, Color.rgba8888(blendColor))
        }
    }
    return maskedPixmap
}

fun Pixmap.tile(row: Int, column: Int, tileStart: Int, tileCount: Int): List<Pixmap> {
    require(width % column == 0 && height % row == 0)
    val tileWidth = width / column
    val tileHeight = height / row
    var tileIndex = 0
    val tiledPixmapList = mutableListOf<Pixmap>()
    for (rowIndex in 0 until row) {
        for (columnIndex in 0 until column) {
            if (tileIndex in tileStart until tileStart + tileCount) {
                val srcX = columnIndex * tileWidth
                val srcY = rowIndex * tileHeight
                val tiledPixmap = createPixmap(tileWidth, tileHeight)
                tiledPixmap.drawPixmap(this, 0, 0, srcX, srcY, tileWidth, tileHeight)
                tiledPixmapList.add(tiledPixmap)
            }
            ++tileIndex
        }
    }
    return tiledPixmapList
}

/**
 * @return (packedPixmap, packedRow, packedColumn).
 */
fun List<Pixmap>.pack(): Triple<Pixmap, Int, Int> {
    val first = first()
    val tileWidth = first.width
    val tileHeight = first.height
    require(drop(1).all { it.width == tileWidth && it.height == tileHeight })
    var packRow = 1
    var packColumn = size
    var minPackDiff = abs(size * tileWidth - tileHeight)
    for (currentPackRow in 2..size) {
        if (size % currentPackRow != 0) continue
        val currentPackColumn = size / currentPackRow
        val packWidth = currentPackColumn * tileWidth
        val packHeight = currentPackRow * tileHeight
        val packDiff = abs(packWidth - packHeight)
        if (packDiff < minPackDiff) {
            packRow = currentPackRow
            packColumn = currentPackColumn
            minPackDiff = packDiff
        }
    }
    val packedPixmap = createPixmap(packColumn * tileWidth, packRow * tileHeight)
    for (rowIndex in 0 until packRow) {
        for (columnIndex in 0 until packColumn) {
            val index = rowIndex * packColumn + columnIndex
            val tiledPixmap = get(index)
            val dstX = columnIndex * tileWidth
            val dstY = rowIndex * tileHeight
            packedPixmap.drawPixmap(tiledPixmap, 0, 0, tileWidth, tileHeight, dstX, dstY, tileWidth, tileHeight)
        }
    }
    return Triple(packedPixmap, packRow, packColumn)
}

fun Pixmap.scale(width: Int, height: Int): Pixmap {
    val scaledPixmap = createPixmap(width, height)
    scaledPixmap.drawPixmap(this, 0, 0, this.width, this.height, 0, 0, width, height)
    return scaledPixmap
}

fun Pixmap.scale(scale: Float): Pixmap {
    if (scale == 1f) return copy()
    val scaleWidth = width * scale
    require(scaleWidth.toInt().toFloat() == scaleWidth)
    val scaleHeight = height * scale
    require(scaleHeight.toInt().toFloat() == scaleHeight)
    return scale(scaleWidth.toInt(), scaleHeight.toInt())
}

fun Pixmap.nonTransparentSize(): Pair<Int, Int> {
    var minX = width - 1
    var minY = height - 1
    var maxX = 0
    var maxY = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (getPixel(x, y).toColor().isTransparent) continue
            minX = min(minX, x)
            minY = min(minY, y)
            maxX = max(maxX, x)
            maxY = max(maxY, y)
        }
    }
    require(minX <= maxX && minY <= maxY)
    return maxX - minX + 1 to maxY - minY + 1
}

fun Pixmap.write(fileHandle: FileHandle) {
    PixmapIO.writePNG(fileHandle, this, Deflater.NO_COMPRESSION, false)
}
