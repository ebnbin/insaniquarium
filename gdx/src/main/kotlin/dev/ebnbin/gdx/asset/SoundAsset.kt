package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.SoundLoader
import com.badlogic.gdx.audio.Sound

class SoundAsset(
    name: String,
    extension: String = "ogg",
    preload: Boolean = false,
) : Asset<Sound>(
    name = name,
    extension = extension,
    preload = preload,
) {
    override val directory: String
        get() = "sound"

    override val type: Class<Sound>
        get() = Sound::class.java

    override val params: AssetLoaderParameters<Sound>
        get() = SoundLoader.SoundParameter()
}
