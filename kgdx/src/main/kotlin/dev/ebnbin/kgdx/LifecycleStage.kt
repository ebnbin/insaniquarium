package dev.ebnbin.kgdx

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import dev.ebnbin.kgdx.util.WorldScreenViewport

abstract class LifecycleStage : Stage {
    constructor(viewport: Viewport = defaultViewport()) : super(viewport)

    constructor(viewport: Viewport = defaultViewport(), batch: Batch) : super(viewport, batch)

    open fun resume() {
    }

    open fun pause() {
    }

    companion object {
        private fun defaultViewport(): Viewport {
            return WorldScreenViewport()
        }
    }
}
