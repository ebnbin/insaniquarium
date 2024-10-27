package dev.ebnbin.kgdx.scene

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.WorldScreenViewport

abstract class LifecycleStage : Stage, LifecycleScene {
    constructor(viewport: Viewport = defaultViewport()) : super(viewport)

    constructor(viewport: Viewport = defaultViewport(), batch: Batch) : super(viewport, batch)

    protected open val isRendering: Boolean
        get() = true

    private var resized: Boolean = false

    final override fun resize(width: Int, height: Int) {
        diffSize {
            viewport.update(width, height, true)
        }
    }

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

    override fun resume() {
        if (hasDevMenu) {
            val menu = Menu(this::class.java.simpleName)
            game.devMenuStage.createMenu(this) { menuBar ->
                createDevMenu(menuBar, menu)
                menu
            }
        }
    }

    override fun pause() {
        game.devMenuStage.removeMenu(this)
    }

    final override fun render(delta: Float) {
        if (!isRendering) {
            return
        }
        isDebugAll = KgdxPreferenceManager.isDebugAll.value
        act(delta)
        viewport.apply(true)
        draw()
    }

    protected open val enableTick: Boolean
        get() = false

    private var accumulator: Float = 0f

    var tickDelta: Float = 0f
        private set

    var ticks: Int = 0
        private set

    override fun act(delta: Float) {
        if (enableTick) {
            accumulator += delta
            tickDelta = if (accumulator >= TICK_DELTA) {
                accumulator -= TICK_DELTA
                ++ticks
                TICK_DELTA
            } else {
                0f
            }
        }
        super.act(delta)
    }

    @Deprecated("", ReplaceWith(""))
    final override fun act() {
        super.act()
    }

    protected open val hasDevMenu: Boolean
        get() = false

    protected open fun createDevMenu(menuBar: MenuBar, menu: Menu) {
    }

    companion object {
        private const val TICKS_PER_SECOND = 20
        private const val TICK_DELTA = 1f / TICKS_PER_SECOND

        fun defaultViewport(): Viewport {
            return WorldScreenViewport()
        }
    }
}
