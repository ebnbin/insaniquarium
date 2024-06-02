package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.util.internalAsset
import ktx.assets.disposeSafely
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

class AquariumStage : LifecycleStage() {
    private val texture: Texture = readTextureFromZip("aquarium1.jpg")

    init {
        Image(texture).also {
            it.setFillParent(true)
            addActor(it)
        }
    }

    private fun readTextureFromZip(imageFileName: String): Texture {
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
                        val byteArray = byteArrayOutputStream.toByteArray()
                        val pixmap = Pixmap(byteArray, 0, byteArray.size)
                        Texture(pixmap).also {
                            pixmap.disposeSafely()
                        }
                    }
                }
                zipInputStream.closeEntry()
                zipEntry = zipInputStream.nextEntry
            }
        }
        error(Unit)
    }

    override fun dispose() {
        texture.disposeSafely()
        super.dispose()
    }
}
