package dev.ebnbin.kgdx

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import dev.ebnbin.kgdx.util.WorldScreenViewport
import ktx.assets.disposeSafely

abstract class LifecycleStage : Stage {
    constructor(viewport: Viewport = defaultViewport()) : super(viewport)

    constructor(viewport: Viewport = defaultViewport(), batch: Batch) : super(viewport, batch)

    private var resized: Boolean = false

    protected open fun resize(width: Float, height: Float) {
    }

    private fun diffSize(updateViewport: () -> Unit) {
        val oldWidth = width
        val oldHeight = height
        updateViewport()
        val newWidth = width
        val newHeight = height
        if (!resized || oldWidth != newWidth || oldHeight != newHeight) {
            if (!resized) {
                resized = true
            }
            resize(newWidth, newHeight)
        }
    }

    override fun setViewport(viewport: Viewport) {
        diffSize {
            super.setViewport(viewport)
        }
    }

    protected open fun resume() {
    }

    protected open fun pause() {
    }

    companion object {
        private fun defaultViewport(): Viewport {
            return WorldScreenViewport()
        }

        internal fun List<LifecycleStage>.resize(width: Int, height: Int) {
            forEach { stage ->
                stage.diffSize {
                    stage.viewport.update(width, height, true)
                }
            }
        }

        internal fun List<LifecycleStage>.resume() {
            forEach { stage ->
                stage.resume()
            }
        }

        internal fun List<LifecycleStage>.act(delta: Float) {
            forEach { stage ->
                stage.act(delta)
            }
        }

        internal fun List<LifecycleStage>.draw() {
            forEach { stage ->
                stage.viewport.apply(true)
                stage.draw()
            }
        }

        internal fun List<LifecycleStage>.pause() {
            reversed().forEach { stage ->
                stage.pause()
            }
        }

        internal fun List<LifecycleStage>.dispose() {
            reversed().forEach { stage ->
                stage.disposeSafely()
            }
        }
    }
}
