package dev.ebnbin.kgdx

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.kgdx.dev.DevEntry
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.WorldScreenViewport
import ktx.assets.disposeSafely

abstract class LifecycleStage : Stage {
    constructor(viewport: Viewport = defaultViewport()) : super(viewport)

    constructor(viewport: Viewport = defaultViewport(), batch: Batch) : super(viewport, batch)

    protected open val isRendering: Boolean
        get() = true

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

    override fun dispose() {
        game.gameDevInfoStage.removeInfo(this)
        super.dispose()
    }

    @Deprecated("", ReplaceWith(""))
    final override fun act() {
        super.act()
    }

    protected open val hasDevMenu: Boolean
        get() = false

    protected open fun createDevMenu(menuBar: MenuBar, menu: Menu) {
    }

    fun putDevInfo(entry: DevEntry) {
        game.gameDevInfoStage.putInfo(this, entry)
    }

    fun removeDevInfo(entry: DevEntry) {
        game.gameDevInfoStage.removeInfo(this, entry)
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
                if (stage.hasDevMenu) {
                    val menu = Menu(stage::class.java.simpleName)
                    game.devMenuStage.createMenu(stage) { menuBar ->
                        stage.createDevMenu(menuBar, menu)
                        menu
                    }
                }
                stage.resume()
            }
        }

        internal fun List<LifecycleStage>.act(delta: Float) {
            filter { it.isRendering }.forEach { stage ->
                stage.isDebugAll = KgdxPreferenceManager.isDebugAll.value
                stage.act(delta)
            }
        }

        internal fun List<LifecycleStage>.draw() {
            filter { it.isRendering }.forEach { stage ->
                stage.viewport.apply(true)
                stage.draw()
            }
        }

        internal fun List<LifecycleStage>.pause() {
            reversed().forEach { stage ->
                stage.pause()
                game.devMenuStage.removeMenu(stage)
            }
        }

        internal fun List<LifecycleStage>.dispose() {
            reversed().forEach { stage ->
                stage.disposeSafely()
            }
        }
    }
}
