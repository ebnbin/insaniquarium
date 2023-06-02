package dev.ebnbin.gdx

import dev.ebnbin.gdx.lifecycle.BaseGame

object Gdx {
    fun init(
        gameGetter: () -> BaseGame,
    ) {
        dev.ebnbin.gdx.lifecycle.gameGetter = gameGetter
    }
}
