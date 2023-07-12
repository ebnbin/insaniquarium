package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.MusicLoader
import com.badlogic.gdx.audio.Music

class MusicAsset(
    name: String,
    extension: String = "mp3",
    preload: Boolean = false,
) : Asset<Music>(
    name = name,
    extension = extension,
    preload = preload,
) {
    override val directory: String
        get() = "music"

    override val type: Class<Music>
        get() = Music::class.java

    override val params: AssetLoaderParameters<Music>
        get() = MusicLoader.MusicParameter()
}
