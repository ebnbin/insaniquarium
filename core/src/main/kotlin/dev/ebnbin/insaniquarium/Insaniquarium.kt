package dev.ebnbin.insaniquarium

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.game
import ktx.assets.disposeSafely
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

val insaniquarium: Insaniquarium
    get() = game as Insaniquarium

class Insaniquarium : Game() {
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var texture: Texture

    override fun create() {
        super.create()
        spriteBatch = SpriteBatch()
        texture = readTextureFromZip("aquarium1.jpg")
    }

    private fun readTextureFromZip(imageFileName: String): Texture {
        val zipFileHandle = Gdx.files.internal("Insaniquarium Deluxe.zip")
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

    override fun render() {
        super.render()
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        spriteBatch.begin()
        spriteBatch.draw(texture, 0f, 0f)
        spriteBatch.end()
    }

    override fun dispose() {
        texture.dispose()
        spriteBatch.dispose()
        super.dispose()
    }
}
