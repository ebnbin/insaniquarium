package dev.ebnbin.insaniquarium.aquarium

import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.asset.MusicAsset
import dev.ebnbin.gdx.asset.TextureAsset
import dev.ebnbin.gdx.lifecycle.baseGame

enum class Aquarium(
    val id: String,
) {
    A("a"),
    B("b"),
    C("c"),
    D("d"),
    E("e"),
    F("f"),
    ;

    val textureAsset: TextureAsset
        get() = baseGame.assets.texture.getValue("aquarium_$id")
    val musicAsset: MusicAsset
        get() = baseGame.assets.music.getValue("aquarium_$id")

    fun assets(): Set<Asset<*>> {
        return setOf(textureAsset, musicAsset)
    }
}
