package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.google.gson.annotations.Expose

class FreeTypeAsset(
    @Expose
    val fontFileName: String = "",
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
        }
}
