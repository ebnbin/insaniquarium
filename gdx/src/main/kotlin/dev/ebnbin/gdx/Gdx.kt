package dev.ebnbin.gdx

import dev.ebnbin.gdx.lifecycle.BaseGame
import dev.ebnbin.gdx.utils.World

lateinit var id: String
    internal set

object Gdx {
    fun init(
        id: String,
        gameGetter: () -> BaseGame,
        unitWidth: Float,
        unitHeight: Float,
        unitsPerMeter: Float = 1f,
    ) {
        dev.ebnbin.gdx.id = id
        dev.ebnbin.gdx.lifecycle.gameGetter = gameGetter
        World.unitWidth = unitWidth
        World.unitHeight = unitHeight
        World.unitsPerMeter = unitsPerMeter
    }
}
