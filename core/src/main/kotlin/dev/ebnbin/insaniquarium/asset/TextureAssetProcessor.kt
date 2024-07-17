package dev.ebnbin.insaniquarium.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import dev.ebnbin.insaniquarium.aquarium.AquariumType
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

    private class Region(
        name: String,
        private val input: Input,
        private val tileStart: Int,
        private val tileCount: Int,
    ) : Processor(
        name = name,
    ) {
        class Input(
            val inputFileName: String,
            val inputMaskFileName: String,
            val row: Int,
            val column: Int,
        )

        override fun process(fromTool: Boolean): TextureAsset {
            val inputPixmap = readPixmapFromZip(input.inputFileName)
            val inputMaskPixmap = readPixmapFromZip(input.inputMaskFileName)
            val maskedPixmap = inputPixmap.mask(inputMaskPixmap)
            inputPixmap.disposeSafely()
            inputMaskPixmap.disposeSafely()
            val tiledPixmapList = maskedPixmap.tile(input.row, input.column, tileStart, tileCount)
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
                    animation = when (name) {
                        "stinky",
                        "niko",
                        "itchy",
                        "prego",
                        "zorf",
                        "clyde",
                        "vert",
                        "rufus",
                        "meryl",
                        "wadsworth",
                        "seymour",
                        "shrapnel",
                        "gumbo",
                        "blip",
                        "rhubarb",
                        "nimbus",
                        "amp",
                        "gash",
                        "angie",
                        "presto",
                        "brinkley",
                        "nostradamus",
                        "stanley",
                        "walter" -> TextureAsset.Region.Animation(
                            duration = when (name) {
                                "clyde",
                                "seymour",
                                "shrapnel",
                                "rhubarb",
                                "brinkley" -> 1f
                                else -> 0.5f
                            },
                            mode = when (name) {
                                "niko",
                                "vert",
                                "walter" -> AnimationMode.LOOP_PINGPONG
                                else -> AnimationMode.LOOP
                            },
                        )
                        else -> null
                    },
                ),
                stretchable = null,
            )
        }

        companion object {
            private val STINKY = Input(
                inputFileName = "stinky.gif",
                inputMaskFileName = "_stinky.gif",
                row = 3,
                column = 10,
            )
            private val NIKO = Input(
                inputFileName = "niko.gif",
                inputMaskFileName = "_niko.gif",
                row = 3,
                column = 10,
            )
            private val ITCHY = Input(
                inputFileName = "itchy.gif",
                inputMaskFileName = "_itchy.gif",
                row = 4,
                column = 10,
            )
            private val PREGO = Input(
                inputFileName = "prego.gif",
                inputMaskFileName = "_prego.gif",
                row = 4,
                column = 10,
            )
            private val ZORF = Input(
                inputFileName = "zorf.gif",
                inputMaskFileName = "_zorf.gif",
                row = 3,
                column = 10,
            )
            private val CLYDE = Input(
                inputFileName = "clyde.gif",
                inputMaskFileName = "_clyde.gif",
                row = 1,
                column = 10,
            )
            private val VERT = Input(
                inputFileName = "vert.gif",
                inputMaskFileName = "_vert.gif",
                row = 2,
                column = 10,
            )
            private val RUFUS = Input(
                inputFileName = "rufus.gif",
                inputMaskFileName = "_rufus.gif",
                row = 2,
                column = 10,
            )
            private val MERYL = Input(
                inputFileName = "meryl.gif",
                inputMaskFileName = "_meryl.gif",
                row = 4,
                column = 10,
            )
            private val WADSWORTH = Input(
                inputFileName = "wadsworth.gif",
                inputMaskFileName = "_wadsworth.gif",
                row = 3,
                column = 10,
            )
            private val SEYMOUR = Input(
                inputFileName = "seymour.gif",
                inputMaskFileName = "_seymour.gif",
                row = 2,
                column = 10,
            )
            private val SHRAPNEL = Input(
                inputFileName = "shrapnel.gif",
                inputMaskFileName = "_shrapnel.gif",
                row = 2,
                column = 10,
            )
            private val GUMBO = Input(
                inputFileName = "gumbo.gif",
                inputMaskFileName = "_gumbo.gif",
                row = 2,
                column = 10,
            )
            private val BLIP = Input(
                inputFileName = "blip.gif",
                inputMaskFileName = "_blip.gif",
                row = 2,
                column = 10,
            )
            private val RHUBARB = Input(
                inputFileName = "rhubarb.gif",
                inputMaskFileName = "_rhubarb.gif",
                row = 2,
                column = 10,
            )
            private val NIMBUS = Input(
                inputFileName = "nimbus.gif",
                inputMaskFileName = "_nimbus.gif",
                row = 2,
                column = 10,
            )
            private val AMP = Input(
                inputFileName = "amp.gif",
                inputMaskFileName = "_amp.gif",
                row = 2,
                column = 10,
            )
            private val GASH = Input(
                inputFileName = "gash.gif",
                inputMaskFileName = "_gash.gif",
                row = 3,
                column = 10,
            )
            private val ANGIE = Input(
                inputFileName = "angie.gif",
                inputMaskFileName = "_angie.gif",
                row = 2,
                column = 10,
            )
            private val PRESTO = Input(
                inputFileName = "presto.gif",
                inputMaskFileName = "_presto.gif",
                row = 3,
                column = 10,
            )
            private val BRINKLEY = Input(
                inputFileName = "brink.gif",
                inputMaskFileName = "_brink.gif",
                row = 2,
                column = 10,
            )
            private val NOSTRADAMUS = Input(
                inputFileName = "nostradamus.gif",
                inputMaskFileName = "_nostradamus.gif",
                row = 2,
                column = 10,
            )
            private val STANLEY = Input(
                inputFileName = "stanley.gif",
                inputMaskFileName = "_stanley.gif",
                row = 6,
                column = 10,
            )
            private val WALTER = Input(
                inputFileName = "walter.gif",
                inputMaskFileName = "_walter.gif",
                row = 3,
                column = 10,
            )

            val ALL: List<Region> = listOf(
                Region(
                    name = "stinky",
                    input = STINKY,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "niko",
                    input = NIKO,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "itchy",
                    input = ITCHY,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "prego",
                    input = PREGO,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "zorf",
                    input = ZORF,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "clyde",
                    input = CLYDE,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "vert",
                    input = VERT,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "rufus",
                    input = RUFUS,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "meryl",
                    input = MERYL,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "wadsworth",
                    input = WADSWORTH,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "seymour",
                    input = SEYMOUR,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "shrapnel",
                    input = SHRAPNEL,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "gumbo",
                    input = GUMBO,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "blip",
                    input = BLIP,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "rhubarb",
                    input = RHUBARB,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "nimbus",
                    input = NIMBUS,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "amp",
                    input = AMP,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "gash",
                    input = GASH,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "angie",
                    input = ANGIE,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "presto",
                    input = PRESTO,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "brinkley",
                    input = BRINKLEY,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "nostradamus",
                    input = NOSTRADAMUS,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "stanley",
                    input = STANLEY,
                    tileStart = 0,
                    tileCount = 10,
                ),
                Region(
                    name = "walter",
                    input = WALTER,
                    tileStart = 0,
                    tileCount = 10,
                ),
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
        LoadingBackground,
        *Aquarium.ALL.toTypedArray(),
        *Region.ALL.toTypedArray(),
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
