package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader

class FreeTypeAsset(
    name: String,
    extension: String,
    val fontFileName: String,
) : Asset<BitmapFont>(
    name = name,
    extension = extension,
) {
    override val directory: String
        get() = "freetype"

    override val type: Class<BitmapFont>
        get() = BitmapFont::class.java

    override val parameters: AssetLoaderParameters<BitmapFont>
        get() = FreetypeFontLoader.FreeTypeFontLoaderParameter().also { parameters ->
            parameters.fontFileName = "$directory/$fontFileName.$extension"
        }
}
