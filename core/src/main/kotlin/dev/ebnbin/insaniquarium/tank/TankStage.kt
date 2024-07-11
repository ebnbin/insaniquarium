package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.kgdx.LifecycleStage

class TankStage : LifecycleStage(TankViewport()) {
    private val tankGroup: TankGroup = TankGroup().also {
        addActor(it)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        tankGroup.setPosition(
            (width - tankGroup.width) / 2f,
            (height - tankGroup.height) / 3f,
            Align.bottomLeft,
        )
    }

    private var accumulator: Float = 0f

    var tickDelta: Float = 0f
        private set

    override fun act(delta: Float) {
        accumulator += delta
        tickDelta = if (accumulator >= TICK_DELTA) {
            accumulator -= TICK_DELTA
            TICK_DELTA
        } else {
            0f
        }
        super.act(delta)
    }

    override val hasDevMenu: Boolean
        get() = tankGroup.tank.devHelper.hasDevMenu

    override fun createDevMenu(menuBar: MenuBar, menu: Menu) {
        super.createDevMenu(menuBar, menu)
        tankGroup.tank.devHelper.createDevMenu(menuBar, menu)
    }

    companion object {
        private const val TICKS_PER_SECOND = 20
        private const val TICK_DELTA = 1f / TICKS_PER_SECOND
    }
}
