package dev.ebnbin.insaniquarium.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import dev.ebnbin.insaniquarium.aquarium.AquariumType
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.kgdx.asset.AssetFileType
import dev.ebnbin.kgdx.asset.AssetId
import dev.ebnbin.kgdx.asset.AssetType
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.AnimationMode
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
import ktx.graphics.copy
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

object TextureAssetProcessor {
    private abstract class Processor(val name: String) {
        abstract fun process(fromTool: Boolean): TextureAsset
    }

    private object LoadingBackground : Processor(name = "loading_background") {
        override fun process(fromTool: Boolean): TextureAsset {
            val inputFile = Gdx.files.internalAsset("insaniquarium_desktop.jpg")
            val inputPixmap = createPixmap(inputFile.readBytes())
            val primaryColor = Color(inputPixmap.getPixel(0, inputPixmap.height - 1))
            val outputPixmap = createPixmap(1504, 846)
            outputPixmap.setColor(primaryColor)
            outputPixmap.fill()
            outputPixmap.drawPixmap(inputPixmap, 2, 92)
            inputPixmap.disposeSafely()
            for (y in 0..47) {
                val alpha = (47f - y) / 47f
                outputPixmap.setColor(primaryColor.copy(alpha = alpha))
                outputPixmap.fillRectangle(2, 92 + y, 1500, 1)
            }
            val scaledPixmap = outputPixmap.scale(1280, 720)
            outputPixmap.disposeSafely()
            scaledPixmap.writeToLocal(name, fromTool)
            return TextureAsset(
                name = name,
                extension = "png",
                fileType = AssetFileType.LOCAL,
                preload = false,
                region = null,
                stretchable = TextureAsset.Stretchable(
                    x = listOf(0, 1, 1279, 1),
                    y = listOf(0, 2, 719, 1),
                ),
            )
        }
    }

    private class Aquarium(
        type: AquariumType,
        private val inputFileName: String,
    ) : Processor(
        name = type.id,
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

        companion object {
            private fun processor(type: AquariumType): Aquarium {
                return when (type) {
                    AquariumType.A -> Aquarium(
                        type = type,
                        inputFileName = "aquarium1.jpg",
                    )
                    AquariumType.B -> Aquarium(
                        type = type,
                        inputFileName = "aquarium4.jpg",
                    )
                    AquariumType.C -> Aquarium(
                        type = type,
                        inputFileName = "aquarium3.jpg",
                    )
                    AquariumType.D -> Aquarium(
                        type = type,
                        inputFileName = "aquarium2.jpg",
                    )
                    AquariumType.E -> Aquarium(
                        type = type,
                        inputFileName = "aquarium6.jpg",
                    )
                    AquariumType.F -> Aquarium(
                        type = type,
                        inputFileName = "Aquarium5.jpg",
                    )
                }
            }

            val ALL: List<Aquarium> = AquariumType.entries.map { aquariumType ->
                processor(aquariumType)
            }
        }
    }

    private class Body(
        private val type: BodyType,
        private val inputFileName: String,
        private val inputMaskFileName: String,
        private val row: Int,
        private val column: Int,
        private val tileStart: Int,
        private val tileCount: Int,
    ) : Processor(
        name = type.id,
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
                    animation = TextureAsset.Region.Animation(
                        duration = when (type) {
                            BodyType.CLYDE,
                            BodyType.SEYMOUR,
                            BodyType.SHRAPNEL,
                            BodyType.RHUBARB,
                            BodyType.BRINKLEY -> 1f
                            else -> 0.5f
                        },
                        mode = when (type) {
                            BodyType.NIKO,
                            BodyType.VERT,
                            BodyType.WALTER -> AnimationMode.LOOP_PINGPONG
                            else -> AnimationMode.LOOP
                        },
                    ),
                ),
                stretchable = null,
            )
        }

        companion object {
            private fun processor(type: BodyType): Body {
                return when (type) {
                    BodyType.STINKY -> Body(
                        type = type,
                        inputFileName = "stinky.gif",
                        inputMaskFileName = "_stinky.gif",
                        row = 3,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.NIKO -> Body(
                        type = type,
                        inputFileName = "niko.gif",
                        inputMaskFileName = "_niko.gif",
                        row = 3,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.ITCHY -> Body(
                        type = type,
                        inputFileName = "itchy.gif",
                        inputMaskFileName = "_itchy.gif",
                        row = 4,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.PREGO -> Body(
                        type = type,
                        inputFileName = "prego.gif",
                        inputMaskFileName = "_prego.gif",
                        row = 4,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.ZORF -> Body(
                        type = type,
                        inputFileName = "zorf.gif",
                        inputMaskFileName = "_zorf.gif",
                        row = 3,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.CLYDE -> Body(
                        type = type,
                        inputFileName = "clyde.gif",
                        inputMaskFileName = "_clyde.gif",
                        row = 1,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.VERT -> Body(
                        type = type,
                        inputFileName = "vert.gif",
                        inputMaskFileName = "_vert.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.RUFUS -> Body(
                        type = type,
                        inputFileName = "rufus.gif",
                        inputMaskFileName = "_rufus.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.MERYL -> Body(
                        type = type,
                        inputFileName = "meryl.gif",
                        inputMaskFileName = "_meryl.gif",
                        row = 4,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.WADSWORTH -> Body(
                        type = type,
                        inputFileName = "wadsworth.gif",
                        inputMaskFileName = "_wadsworth.gif",
                        row = 3,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.SEYMOUR -> Body(
                        type = type,
                        inputFileName = "seymour.gif",
                        inputMaskFileName = "_seymour.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.SHRAPNEL -> Body(
                        type = type,
                        inputFileName = "shrapnel.gif",
                        inputMaskFileName = "_shrapnel.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.GUMBO -> Body(
                        type = type,
                        inputFileName = "gumbo.gif",
                        inputMaskFileName = "_gumbo.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.BLIP -> Body(
                        type = type,
                        inputFileName = "blip.gif",
                        inputMaskFileName = "_blip.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.RHUBARB -> Body(
                        type = type,
                        inputFileName = "rhubarb.gif",
                        inputMaskFileName = "_rhubarb.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.NIMBUS -> Body(
                        type = type,
                        inputFileName = "nimbus.gif",
                        inputMaskFileName = "_nimbus.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.AMP -> Body(
                        type = type,
                        inputFileName = "amp.gif",
                        inputMaskFileName = "_amp.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.GASH -> Body(
                        type = type,
                        inputFileName = "gash.gif",
                        inputMaskFileName = "_gash.gif",
                        row = 3,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.ANGIE -> Body(
                        type = type,
                        inputFileName = "angie.gif",
                        inputMaskFileName = "_angie.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.PRESTO -> Body(
                        type = type,
                        inputFileName = "presto.gif",
                        inputMaskFileName = "_presto.gif",
                        row = 3,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.BRINKLEY -> Body(
                        type = type,
                        inputFileName = "brink.gif",
                        inputMaskFileName = "_brink.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.NOSTRADAMUS -> Body(
                        type = type,
                        inputFileName = "nostradamus.gif",
                        inputMaskFileName = "_nostradamus.gif",
                        row = 2,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.STANLEY -> Body(
                        type = type,
                        inputFileName = "stanley.gif",
                        inputMaskFileName = "_stanley.gif",
                        row = 6,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                    BodyType.WALTER -> Body(
                        type = type,
                        inputFileName = "walter.gif",
                        inputMaskFileName = "_walter.gif",
                        row = 3,
                        column = 10,
                        tileStart = 0,
                        tileCount = 10,
                    )
                }
            }

            val ALL: List<Body> = BodyType.entries.map { type ->
                processor(type)
            }
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
        LoadingBackground,
        *Aquarium.ALL.toTypedArray(),
        *Body.ALL.toTypedArray(),
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
