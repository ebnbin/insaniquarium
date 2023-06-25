package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.insaniquarium.game

fun BodyType.assets(): Set<Asset<*>> {
    val config = game.config.body.getValue(this)
    return listOfNotNull(
        config.animations.swim,
        config.animations.turn,
        config.animations.eat,
        config.animations.hungry,
        config.animations.hungryTurn,
        config.animations.hungryEat,
        config.animations.die,
    ).mapTo(mutableSetOf()) {
        baseGame.assets.texture.getValue(it.assetId)
    }
}
