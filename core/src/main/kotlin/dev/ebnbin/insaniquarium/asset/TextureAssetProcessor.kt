package dev.ebnbin.insaniquarium.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import dev.ebnbin.kgdx.asset.Asset
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.util.internalAsset
import dev.ebnbin.kgdx.util.localAsset
import ktx.assets.disposeSafely
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.ZipInputStream

object TextureAssetProcessor {
    private abstract class Processor(val name: String) {
        abstract fun process(): TextureAsset
    }

    private class Aquarium(
        name: String,
        private val imageFileName: String,
    ) : Processor(
        name = name,
    ) {
        override fun process(): TextureAsset {
            readPixmapFromZip(imageFileName).writeToLocal(name)
            return TextureAsset(
                name = name,
                extension = "png",
                fileType = Asset.FileType.LOCAL,
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
                        Pixmap(byteArray, 0, byteArray.size)
                    }
                }
                zipInputStream.closeEntry()
                zipEntry = zipInputStream.nextEntry
            }
        }
        error(Unit)
    }

    private fun Pixmap.writeToLocal(name: String) {
        PixmapIO.writePNG(Gdx.files.localAsset("texture/$name.png"), this, Deflater.NO_COMPRESSION, false)
        disposeSafely()
    }

    private val PROCESSOR_LIST: List<Processor> = listOf(
        Aquarium(
            name = "aquarium_a",
            imageFileName = "aquarium1.jpg",
        ),
        Aquarium(
            name = "aquarium_b",
            imageFileName = "aquarium4.jpg",
        ),
        Aquarium(
            name = "aquarium_c",
            imageFileName = "aquarium3.jpg",
        ),
        Aquarium(
            name = "aquarium_d",
            imageFileName = "aquarium2.jpg",
        ),
        Aquarium(
            name = "aquarium_e",
            imageFileName = "aquarium6.jpg",
        ),
        Aquarium(
            name = "aquarium_f",
            imageFileName = "Aquarium5.jpg",
        ),
    )

    fun process(fileName: String) {
        val processorMap = PROCESSOR_LIST.associateBy { processor ->
            "local:texture/${processor.name}.png"
        }
        processorMap[fileName]?.process()
    }

    fun process(): Map<String, TextureAsset> {
        return PROCESSOR_LIST
            .map { it.process() }
            .associateBy { it.name }
            .toSortedMap()
    }
}
