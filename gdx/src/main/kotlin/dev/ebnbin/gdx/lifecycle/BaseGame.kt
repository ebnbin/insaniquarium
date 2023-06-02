package dev.ebnbin.gdx.lifecycle

import com.badlogic.gdx.ApplicationListener

internal lateinit var gameGetter: () -> BaseGame

val baseGame: BaseGame
    get() = gameGetter()

abstract class BaseGame : ApplicationListener {
    override fun create() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun render() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }
}
