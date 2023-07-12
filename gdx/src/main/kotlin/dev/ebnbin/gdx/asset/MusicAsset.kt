package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.MusicLoader
import com.badlogic.gdx.audio.Music
import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.pref.GdxPrefManager

class MusicAsset(
    name: String,
    extension: String = "mp3",
    preload: Boolean = false,
    @Expose
    val isLooping: Boolean = true,
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

    override fun loaded(assetHelper: AssetHelper) {
        super.loaded(assetHelper)
        val music = assetHelper.get(this)
        music.volume = GdxPrefManager.music_volume.data
        music.isLooping = isLooping
    }
}
