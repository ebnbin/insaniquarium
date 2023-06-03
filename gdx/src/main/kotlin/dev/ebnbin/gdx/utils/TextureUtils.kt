package dev.ebnbin.gdx.utils

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

fun Texture.split(row: Int = 1, column: Int = 1): List<TextureRegion> {
    require(row > 0 && column > 0)
    require(width % column == 0 && height % row == 0)
    return TextureRegion.split(this, width / column, height / row).flatten()
}
