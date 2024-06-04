package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont

enum class AssetType(
    val id: String,
    private val type: Class<*>,
) {
    FREETYPE(
        id = "freetype",
        type = BitmapFont::class.java,
    ),
    TEXTURE(
        id = "texture",
        type = Texture::class.java,
    ),
    ;

    val directory: String = id

    fun <T> type(): Class<T> {
        @Suppress("UNCHECKED_CAST")
        return type as Class<T>
    }

    companion object {
        fun of(id: String): AssetType {
            return entries.single { it.id == id }
        }
    }
}
