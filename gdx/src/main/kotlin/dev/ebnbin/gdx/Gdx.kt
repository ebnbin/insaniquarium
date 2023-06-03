package dev.ebnbin.gdx

import dev.ebnbin.gdx.lifecycle.BaseGame
import dev.ebnbin.gdx.utils.World

object Gdx {
    fun init(
        gameGetter: () -> BaseGame,
        unitWidth: Float,
        unitHeight: Float,
        unitsPerMeter: Float = 1f,
    ) {
        dev.ebnbin.gdx.lifecycle.gameGetter = gameGetter
        World.unitWidth = unitWidth
        World.unitHeight = unitHeight
        World.unitsPerMeter = unitsPerMeter
    }
}
