package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import dev.ebnbin.kgdx.util.Dpi

enum class AssetType(
    val id: String,
    private val type: Class<*>,
) {
    FREETYPE(
        id = "freetype",
        type = BitmapFont::class.java,
    ),
    JSON(
        id = "json",
        type = Json::class.java,
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

    fun dpiSuffix(dpi: Dpi): String {
        return when (this) {
            TEXTURE -> dpi.suffix
            else -> ""
        }
    }

    companion object {
        fun of(id: String): AssetType {
            return entries.single { it.id == id }
        }
    }
}
