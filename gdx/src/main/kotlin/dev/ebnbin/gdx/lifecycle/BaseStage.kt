package dev.ebnbin.gdx.lifecycle

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport

abstract class BaseStage : Stage {
    constructor(viewport: Viewport = defaultViewport()) : super(viewport)

    constructor(viewport: Viewport = defaultViewport(), batch: Batch) : super(viewport, batch)

    companion object {
        private fun defaultViewport(): Viewport {
            return StretchViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        }
    }
}
