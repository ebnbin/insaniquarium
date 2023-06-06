package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.split

class TextureAsset(
    name: String,
    extension: String = "png",
    preload: Boolean = false,
    @Expose
    val region: Region? = null,
) : Asset<Texture>(
    name = name,
    extension = extension,
    preload = preload,
) {
    data class Region(
        @Expose
        val row: Int,
        @Expose
        val column: Int,
        @Expose
        val startIndex: Int = 0,
    )

    override val directory: String
        get() = "texture"

    override val type: Class<Texture>
        get() = Texture::class.java

    override val params: AssetLoaderParameters<Texture>
        get() = TextureLoader.TextureParameter()

    fun getTextureRegionList(): List<TextureRegion> {
        requireNotNull(region)
        return baseGame.assetHelper.getAssetExtraOrPut(this, "textureRegionList") {
            val texture = it.get(this)
            texture.split(
                row = region.row,
                column = region.column,
                startIndex = region.startIndex,
            )
        }
    }
}
