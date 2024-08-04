package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.kgdx.scene.LifecycleStage

class TankStage : LifecycleStage(TankViewport()) {
    private val tankActor: TankActor = TankActor().also {
        addActor(it)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        tankActor.setPosition(
            (width - tankActor.width) / 2f,
            (height - tankActor.height) / 3f,
            Align.bottomLeft,
        )
    }

    override val enableTick: Boolean
        get() = true

    override val hasDevMenu: Boolean
        get() = tankActor.tank.devHelper.hasDevMenu

    override fun createDevMenu(menuBar: MenuBar, menu: Menu) {
        super.createDevMenu(menuBar, menu)
        tankActor.tank.devHelper.createDevMenu(menuBar, menu)
    }
}
