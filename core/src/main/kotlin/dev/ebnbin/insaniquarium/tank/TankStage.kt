package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.util.menuItem

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

    override val hasDevMenu: Boolean
        get() = true

    override fun createDevMenu(menuBar: MenuBar, menu: Menu) {
        super.createDevMenu(menuBar, menu)
        menu.apply {
            menuItem(
                menuBar = menuBar,
                text = "create body",
            ) {
                tankGroup.devCreateBody(count = 1)
            }
            menuItem(
                menuBar = menuBar,
                text = "create 10 bodies",
            ) {
                tankGroup.devCreateBody(count = 10)
            }
            menuItem(
                menuBar = menuBar,
                text = "create 100 bodies",
            ) {
                tankGroup.devCreateBody(count = 100)
            }
            menuItem(
                menuBar = menuBar,
                text = "create 1000 bodies",
            ) {
                tankGroup.devCreateBody(count = 1000)
            }
            menuItem(
                menuBar = menuBar,
                text = "clear bodies",
            ) {
                tankGroup.devClearBodies()
            }
        }
    }
}
