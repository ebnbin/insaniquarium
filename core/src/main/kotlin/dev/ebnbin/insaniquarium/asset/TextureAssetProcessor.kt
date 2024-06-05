package dev.ebnbin.insaniquarium.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import dev.ebnbin.kgdx.asset.AssetFileType
import dev.ebnbin.kgdx.asset.AssetId
import dev.ebnbin.kgdx.asset.AssetType
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.Dpi
import dev.ebnbin.kgdx.util.createPixmap
import dev.ebnbin.kgdx.util.drawSubPixmap
import dev.ebnbin.kgdx.util.internalAsset
import dev.ebnbin.kgdx.util.mask
import dev.ebnbin.kgdx.util.pack
import dev.ebnbin.kgdx.util.scale
import dev.ebnbin.kgdx.util.tile
import dev.ebnbin.kgdx.util.write
import ktx.assets.disposeSafely
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

object TextureAssetProcessor {
    private abstract class Processor(val name: String) {
        abstract fun process(fromTool: Boolean): TextureAsset
    }

    private class Aquarium(
        name: String,
        private val inputFileName: String,
    ) : Processor(
        name = name,
    ) {
        override fun process(fromTool: Boolean): TextureAsset {
            val inputPixmap = readPixmapFromZip(inputFileName)
            val outputPixmap = createPixmap(1280, 720)
            outputPixmap.drawSubPixmap(inputPixmap,    0,    0,   32,  480,    0,    0,  160,  720)
            outputPixmap.drawSubPixmap(inputPixmap,   32,    0,  576,  480,  160,    0,  960,  720)
            outputPixmap.drawSubPixmap(inputPixmap,  608,    0,   32,  480, 1120,    0,  160,  720)
            inputPixmap.disposeSafely()
            outputPixmap.writeToLocal(name, fromTool)
            return TextureAsset(
                name = name,
                extension = "png",
                fileType = AssetFileType.LOCAL,
                preload = false,
                region = null,
                stretchable = TextureAsset.Stretchable(
                    x = listOf(0, 160, 1120, 160),
                    y = listOf(0, 80, 680, 40),
                ),
            )
        }
    }

    private class Body(
        name: String,
        private val inputFileName: String,
        private val inputMaskFileName: String,
        private val row: Int,
        private val column: Int,
        private val tileStart: Int,
        private val tileCount: Int,
    ) : Processor(
        name = name,
    ) {
        override fun process(fromTool: Boolean): TextureAsset {
            val inputPixmap = readPixmapFromZip(inputFileName)
            val inputMaskPixmap = readPixmapFromZip(inputMaskFileName)
            val maskedPixmap = inputPixmap.mask(inputMaskPixmap)
            inputPixmap.disposeSafely()
            inputMaskPixmap.disposeSafely()
            val tiledPixmapList = maskedPixmap.tile(row, column, tileStart, tileCount)
            maskedPixmap.disposeSafely()
            val scaledPixmapList = tiledPixmapList.map { tiledPixmap ->
                tiledPixmap.scale(1.5f).also {
                    tiledPixmap.disposeSafely()
                }
            }
            val (packedPixmap, packedRow, packedColumn) = scaledPixmapList.pack()
            scaledPixmapList.forEach { it.disposeSafely() }
            packedPixmap.writeToLocal(name, fromTool)
            packedPixmap.disposeSafely()
            return TextureAsset(
                name = name,
                extension = "png",
                fileType = AssetFileType.LOCAL,
                preload = false,
                region = TextureAsset.Region(
                    row = packedRow,
                    column = packedColumn,
                ),
                stretchable = null,
            )
        }
    }

    private fun readPixmapFromZip(imageFileName: String): Pixmap {
        val zipFileHandle = Gdx.files.internalAsset("Insaniquarium Deluxe.zip")
        ZipInputStream(zipFileHandle.read()).use { zipInputStream ->
            var zipEntry = zipInputStream.nextEntry
            while (zipEntry != null) {
                if (zipEntry.name == "images/$imageFileName") {
                    return ByteArrayOutputStream().use { byteArrayOutputStream ->
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var len: Int
                        while (zipInputStream.read(buffer, 0, buffer.size).also { len = it } != -1) {
                            byteArrayOutputStream.write(buffer, 0, len)
                        }
                        zipInputStream.closeEntry()
                        val byteArray = byteArrayOutputStream.toByteArray()
                        createPixmap(byteArray)
                    }
                }
                zipInputStream.closeEntry()
                zipEntry = zipInputStream.nextEntry
            }
        }
        error(Unit)
    }

    private fun Pixmap.writeToLocal(name: String, fromTool: Boolean) {
        val assetId = AssetId(
            fileType = AssetFileType.LOCAL,
            type = AssetType.TEXTURE,
            nameWithExtension = "$name.png",
        )
        val dpi = if (fromTool) Dpi.M else KgdxPreferenceManager.dpi.value
        val dpiPixmap = scale(dpi.pxsPerDp)
        disposeSafely()
        dpiPixmap.write(assetId.fileHandle(dpi = dpi))
        dpiPixmap.disposeSafely()
    }

    private val PROCESSOR_LIST: List<Processor> = listOf(
        Aquarium(
            name = "aquarium_a",
            inputFileName = "aquarium1.jpg",
        ),
        Aquarium(
            name = "aquarium_b",
            inputFileName = "aquarium4.jpg",
        ),
        Aquarium(
            name = "aquarium_c",
            inputFileName = "aquarium3.jpg",
        ),
        Aquarium(
            name = "aquarium_d",
            inputFileName = "aquarium2.jpg",
        ),
        Aquarium(
            name = "aquarium_e",
            inputFileName = "aquarium6.jpg",
        ),
        Aquarium(
            name = "aquarium_f",
            inputFileName = "Aquarium5.jpg",
        ),
        Body(
            name = "stinky",
            inputFileName = "stinky.gif",
            inputMaskFileName = "_stinky.gif",
            row = 3,
            column = 10,
            tileStart = 0,
            tileCount = 10,
        ),
    )

    fun process(fileName: String) {
        val processorMap = PROCESSOR_LIST.associateBy { processor ->
            AssetId(
                fileType = AssetFileType.LOCAL,
                type = AssetType.TEXTURE,
                nameWithExtension = "${processor.name}.png",
            ).id
        }
        processorMap[fileName]?.process(fromTool = false)
    }

    fun process(): Map<String, TextureAsset> {
        return PROCESSOR_LIST
            .map { it.process(fromTool = true) }
            .associateBy { it.name }
            .toSortedMap()
    }
}
