package dev.ebnbin.insaniquarium.aquarium

import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.game

enum class AquariumType(val id: String) {
    A("aquarium_a"),
    B("aquarium_b"),
    C("aquarium_c"),
    D("aquarium_d"),
    E("aquarium_e"),
    F("aquarium_f"),
    ;

    val textureAsset: TextureAsset
        get() = game.assets.texture(id)
}
