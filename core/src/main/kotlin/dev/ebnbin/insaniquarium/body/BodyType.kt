package dev.ebnbin.insaniquarium.body

import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.game

enum class BodyType(
    val id: String,
) {
    STINKY("stinky"),
    ;

    val textureAsset: TextureAsset
        get() = game.assets.texture(id)
}
