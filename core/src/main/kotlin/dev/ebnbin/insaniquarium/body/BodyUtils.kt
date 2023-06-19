package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.insaniquarium.game

fun BodyType.assets(): Set<Asset<*>> {
    val config = game.config.body.getValue(this)
    return config.animations.mapTo(mutableSetOf()) {
        baseGame.assets.texture.getValue(it.value.assetId)
    }
}
