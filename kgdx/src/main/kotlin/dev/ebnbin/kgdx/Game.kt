package dev.ebnbin.kgdx

import com.badlogic.gdx.ApplicationListener

private var singleton: Game? = null

val game: Game
    get() = requireNotNull(singleton)

abstract class Game : ApplicationListener {
    override fun create() {
        singleton = this
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
        singleton = null
    }
}
