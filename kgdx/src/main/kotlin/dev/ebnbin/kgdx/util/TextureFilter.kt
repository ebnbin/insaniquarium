package dev.ebnbin.kgdx.util

import com.badlogic.gdx.graphics.Texture

enum class TextureFilter(
    val id: String,
    val value: Texture.TextureFilter,
) {
    NEAREST(
        id = "nearest",
        value = Texture.TextureFilter.Nearest,
    ),
    LINEAR(
        id = "linear",
        value = Texture.TextureFilter.Linear,
    ),
    ;

    companion object {
        fun of(id: String): TextureFilter {
            return entries.single { it.id == id }
        }
    }
}
