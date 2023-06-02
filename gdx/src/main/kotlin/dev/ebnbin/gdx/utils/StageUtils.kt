package dev.ebnbin.gdx.utils

import com.badlogic.gdx.Gdx
import dev.ebnbin.gdx.lifecycle.BaseStage

fun List<BaseStage>.resize(width: Int = Gdx.graphics.width, height: Int = Gdx.graphics.height) {
    forEach {
        val viewport = it.viewport
        viewport.update(width, height, true)
        it.resize(viewport.worldWidth, viewport.worldHeight)
    }
}

fun List<BaseStage>.resume() {
    forEach {
        it.resume()
    }
}

fun List<BaseStage>.act(delta: Float) {
    forEach {
        it.act(delta)
    }
}

fun List<BaseStage>.draw() {
    forEach {
        it.viewport.apply(true)
        it.draw()
    }
}

fun List<BaseStage>.pause() {
    reversed().forEach {
        it.pause()
    }
}

fun List<BaseStage>.dispose() {
    reversed().forEach {
        it.dispose()
    }
}
