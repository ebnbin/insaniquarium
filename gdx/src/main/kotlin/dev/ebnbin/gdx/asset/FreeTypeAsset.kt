package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.google.gson.annotations.Expose
import kotlin.math.roundToInt

class FreeTypeAsset(
    @Expose
    val fontFileName: String = "",
    @Expose
    val size: Float = 16f,
    @Expose
    val shadowOffsetX: Float = 0f,
    @Expose
    val shadowOffsetY: Float = 0f,
    @Expose
    val includeDefaultChars: Boolean = true,
    @Expose
    val characters: String = "",
) : Asset<BitmapFont>() {
    override val directory: String
        get() = "freetype"

    override val defaultExtension: String
        get() = "ttf"

    override val type: Class<BitmapFont>
        get() = BitmapFont::class.java

    override val params: AssetLoaderParameters<BitmapFont>
        get() = FreetypeFontLoader.FreeTypeFontLoaderParameter().also {
            val extension = extension ?: defaultExtension
            it.fontFileName = "$directory/$fontFileName${if (extension == "") "" else ".$extension"}"
            it.fontParameters.size = size.roundToInt()
            it.fontParameters.shadowOffsetX = shadowOffsetX.roundToInt()
            it.fontParameters.shadowOffsetY = shadowOffsetY.roundToInt()
            it.fontParameters.characters =
                "${if (includeDefaultChars) FreeTypeFontGenerator.DEFAULT_CHARS else ""}$characters"
        }

    override fun loaded(assetHelper: AssetHelper) {
        super.loaded(assetHelper)
        val bitmapFont = assetHelper.get(this)
        bitmapFont.data.markupEnabled = true
    }
}
