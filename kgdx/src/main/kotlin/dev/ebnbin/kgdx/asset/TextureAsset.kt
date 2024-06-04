package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.util.split

class TextureAsset(
    name: String,
    extension: String,
    fileType: FileType,
    preload: Boolean,
    @Expose
    @SerializedName("region")
    val region: Region?,
) : Asset<Texture>(
    name = name,
    extension = extension,
    fileType = fileType,
    preload = preload,
) {
    class Region(
        @Expose
        @SerializedName("row")
        val row: Int,
        @Expose
        @SerializedName("column")
        val column: Int,
    )

    override val directory: String
        get() = "texture"

    override val type: Class<Texture>
        get() = Texture::class.java

    override val parameters: AssetLoaderParameters<Texture>
        get() = TextureLoader.TextureParameter()

    fun getTextureRegionList(): List<TextureRegion> {
        requireNotNull(region)
        return game.assetManager.getAssetExtraOrPut(this, "texture_region_list") { texture ->
            texture.split(region.row, region.column)
        }
    }
}
