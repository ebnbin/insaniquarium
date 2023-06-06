package dev.ebnbin.gdx.utils

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * @param startIndex Rearrange the list.
 */
fun Texture.split(
    row: Int,
    column: Int,
    startIndex: Int = 0,
): List<TextureRegion> {
    require(row > 0 && column > 0)
    require(width % column == 0 && height % row == 0)
    val list = TextureRegion.split(this, width / column, height / row).flatten()
    if (startIndex == 0) return list
    require(startIndex in list.indices)
    return list.subList(startIndex, list.size) + list.subList(0, startIndex)
}
