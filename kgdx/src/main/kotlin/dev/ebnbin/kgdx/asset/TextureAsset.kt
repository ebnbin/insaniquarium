package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture

class TextureAsset(
    name: String,
    extension: String,
) : Asset<Texture>(
    name = name,
    extension = extension,
) {
    override val directory: String
        get() = "texture"

    override val type: Class<Texture>
        get() = Texture::class.java

    override val parameters: AssetLoaderParameters<Texture>
        get() = TextureLoader.TextureParameter()
}