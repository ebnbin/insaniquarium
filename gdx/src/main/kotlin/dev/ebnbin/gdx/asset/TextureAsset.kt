package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture

class TextureAsset : Asset<Texture>() {
    override val directory: String
        get() = "texture"

    override val defaultExtension: String
        get() = "png"

    override val type: Class<Texture>
        get() = Texture::class.java

    override val params: AssetLoaderParameters<Texture>
        get() = TextureLoader.TextureParameter()
}
