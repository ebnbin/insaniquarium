package dev.ebnbin.gdx.utils

import dev.ebnbin.gdx.lifecycle.BaseStage

fun List<BaseStage>.resize(width: Int, height: Int) {
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
