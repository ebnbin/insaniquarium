package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FreeTypeAsset(
    name: String,
    extension: String,
    fileType: AssetFileType,
    preload: Boolean,
    @Expose
    @SerializedName("font_file_name")
    val fontFileName: String,
) : Asset<BitmapFont>(
    name = name,
    extension = extension,
    fileType = fileType,
    preload = preload,
) {
    override val type: AssetType
        get() = AssetType.FREETYPE

    override val parameters: AssetLoaderParameters<BitmapFont>
        get() = FreetypeFontLoader.FreeTypeFontLoaderParameter().also { parameters ->
            parameters.fontFileName = AssetId(
                fileType = fileType,
                type = type,
                nameWithExtension = "$fontFileName.$extension",
            ).id
        }

    override fun loaded(asset: BitmapFont) {
        super.loaded(asset)
        asset.data.markupEnabled = true
    }
}
