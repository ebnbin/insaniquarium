package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.dpToPxRound

class FreeTypeAsset(
    name: String,
    extension: String,
    fileType: AssetFileType,
    preload: Boolean,
    @Expose
    @SerializedName("font_file_name")
    val fontFileName: String,
    @Expose
    @SerializedName("default_characters")
    val defaultCharacters: Boolean,
    @Expose
    @SerializedName("characters")
    val characters: String?,
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
            parameters.fontParameters.size = 16f.dpToPxRound
            parameters.fontParameters.characters = if (defaultCharacters) {
                FreeTypeFontGenerator.DEFAULT_CHARS
            } else {
                ""
            } + (characters ?: "")
            parameters.fontParameters.minFilter = KgdxPreferenceManager.textureFilter.value.value
            parameters.fontParameters.magFilter = KgdxPreferenceManager.textureFilter.value.value
        }

    override fun loaded(asset: BitmapFont) {
        super.loaded(asset)
        asset.data.markupEnabled = true
        asset.data.setScale(KgdxPreferenceManager.dpi.value.dpsPerPx)
    }
}
