import dev.ebnbin.gdx.utils.IntSize
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun Int.toColor(): Color {
    return Color(this, true)
}

val Int.colorIsTransparent: Boolean
    get() = toColor().alpha == 0

val Int.colorIsOpaque: Boolean
    get() = toColor().alpha == 255

val Int.colorGray: Int
    get() {
        val color = toColor()
        require(color.alpha == 255 && color.red == color.green && color.green == color.blue)
        return color.red
    }

fun Int.colorCopy(
    red: Int? = null,
    green: Int? = null,
    blue: Int? = null,
    alpha: Int? = null,
): Int {
    val color = toColor()
    return Color(
        red ?: color.red,
        green ?: color.green,
        blue ?: color.blue,
        alpha ?: color.alpha,
    ).rgb
}

fun createImage(width: Int, height: Int): BufferedImage {
    return BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
}

fun File.readImage(): BufferedImage {
    return ImageIO.read(this)
}

fun BufferedImage.write(file: File) {
    require(file.extension == "png")
    val dir = file.parentFile
    if (dir != null && !dir.exists()) {
        require(dir.mkdirs())
    }
    require(ImageIO.write(this, "png", file))
}

fun BufferedImage.mapPixel(transform: (x: Int, y: Int, color: Int) -> Int): BufferedImage {
    return createImage(width, height).also {
        for (y in 0 until height) {
            for (x in 0 until width) {
                it.setRGB(x, y, transform(x, y, getRGB(x, y)))
            }
        }
    }
}

fun BufferedImage.mask(maskImage: BufferedImage): BufferedImage {
    require(width == maskImage.width && height == maskImage.height)
    return mapPixel { x, y, color ->
        require(color.colorIsOpaque)
        color.colorCopy(alpha = maskImage.getRGB(x, y).colorGray)
    }
}

fun BufferedImage.scale(width: Int, height: Int): BufferedImage {
    return createImage(width, height).useGraphics {
        drawImage(getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT), 0, 0, null)
    }
}

fun BufferedImage.scale(scale: Float): BufferedImage {
    val scaledWidth = width * scale
    require(scaledWidth.toInt().toFloat() == scaledWidth)
    val scaledHeight = height * scale
    require(scaledHeight.toInt().toFloat() == scaledHeight)
    return scale(scaledWidth.toInt(), scaledHeight.toInt())
}

fun BufferedImage.split(row: Int, column: Int): List<BufferedImage> {
    require(row > 0 && column > 0)
    require(width % column == 0 && height % row == 0)
    val tileWidth = width / column
    val tileHeight = height / row
    val list = mutableListOf<BufferedImage>()
    for (rowIndex in 0 until row) {
        for (columnIndex in 0 until column) {
            list.add(getSubimage(columnIndex * tileWidth, rowIndex * tileHeight, tileWidth, tileHeight))
        }
    }
    return list
}

data class PackResult(
    val row: Int,
    val column: Int,
    val image: BufferedImage,
    val nonTransparentSizeList: List<IntSize>,
)

fun List<BufferedImage>.pack(): PackResult {
    val first = first()
    val tileWidth = first.width
    val tileHeight = first.height
    require(all { it.width == tileWidth && it.height == tileHeight })

    fun rowColumn(): Pair<Int, Int> {
        var minDiff = Int.MAX_VALUE
        var row = 1
        var column = size
        for (currentRow in 1..size) {
            if (size % currentRow != 0) {
                continue
            }
            val currentColumn = size / currentRow
            val width = currentColumn * tileWidth
            val height = currentRow * tileHeight
            val diff = abs(width - height)
            if (diff < minDiff) {
                minDiff = diff
                row = currentRow
                column = currentColumn
            }
        }
        return row to column
    }

    val (row, column) = rowColumn()
    val nonTransparentSizeList = mutableListOf<IntSize>()
    val image = createImage(column * tileWidth, row * tileHeight).useGraphics {
        for (rowIndex in 0 until row) {
            for (columnIndex in 0 until column) {
                val img = get(rowIndex * column + columnIndex)
                val x = columnIndex * tileWidth
                val y = rowIndex * tileHeight
                drawImage(img, x, y, null)
                nonTransparentSizeList.add(img.nonTransparentSize())
            }
        }
    }
    return PackResult(
        row = row,
        column = column,
        image = image,
        nonTransparentSizeList = nonTransparentSizeList,
    )
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

fun BufferedImage.nonTransparentSize(): IntSize {
    var xFirst = width - 1
    var xLast = 0
    var yFirst = height - 1
    var yLast = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (getRGB(x, y).colorIsTransparent) {
                continue
            }
            xFirst = min(xFirst, x)
            xLast = max(xLast, x)
            yFirst = min(yFirst, y)
            yLast = max(yLast, y)
        }
    }
    require(xFirst <= xLast && yFirst <= yLast)
    return IntSize(
        width = xLast - xFirst + 1,
        height = yLast - yFirst + 1,
    )
}
