package dev.ebnbin.insaniquarium.aquarium

import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.game

enum class Aquarium(val id: String) {
    A("a"),
    B("b"),
    C("c"),
    D("d"),
    E("e"),
    F("f"),
    ;

    val textureAsset: TextureAsset
        get() = game.assets.texture("aquarium_$id")
}
