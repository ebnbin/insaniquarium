package dev.ebnbin.insaniquarium.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import dev.ebnbin.kgdx.util.internalAsset
import dev.ebnbin.kgdx.util.localAsset
import ktx.assets.disposeSafely
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.ZipInputStream

class InsaniquariumTextureLoader(resolver: FileHandleResolver) : TextureLoader(resolver) {
    override fun loadAsync(manager: AssetManager?, fileName: String, file: FileHandle?, parameter: TextureParameter?) {
        when (fileName) {
            "local:texture/aquarium_a.png" -> readFromZipAndWriteToLocal("aquarium_a", "aquarium1.jpg")
        }
        super.loadAsync(manager, fileName, file, parameter)
    }

    private fun readFromZipAndWriteToLocal(textureName: String, imageFileName: String) {
        val zipFileHandle = Gdx.files.internalAsset("Insaniquarium Deluxe.zip")
        ZipInputStream(zipFileHandle.read()).use { zipInputStream ->
            var zipEntry = zipInputStream.nextEntry
            while (zipEntry != null) {
                if (zipEntry.name == "images/$imageFileName") {
                    ByteArrayOutputStream().use { byteArrayOutputStream ->
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var len: Int
                        while (zipInputStream.read(buffer, 0, buffer.size).also { len = it } != -1) {
                            byteArrayOutputStream.write(buffer, 0, len)
                        }
                        val byteArray = byteArrayOutputStream.toByteArray()
                        val pixmap = Pixmap(byteArray, 0, byteArray.size)
                        val fileHandle = Gdx.files.localAsset("texture/$textureName.png")
                        PixmapIO.writePNG(fileHandle, pixmap, Deflater.NO_COMPRESSION, false)
                        pixmap.disposeSafely()
                    }
                    zipInputStream.closeEntry()
                    return
                }
                zipInputStream.closeEntry()
                zipEntry = zipInputStream.nextEntry
            }
        }
        error(Unit)
    }
}
