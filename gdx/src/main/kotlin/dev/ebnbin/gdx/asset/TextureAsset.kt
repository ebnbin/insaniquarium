package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture

class TextureAsset(name: String) : Asset<Texture>(name) {
    override fun directory(): String {
        return "texture"
    }

    override fun type(): Class<Texture> {
        return Texture::class.java
    }

    override fun params(): AssetLoaderParameters<Texture> {
        return TextureLoader.TextureParameter()
    }
}
