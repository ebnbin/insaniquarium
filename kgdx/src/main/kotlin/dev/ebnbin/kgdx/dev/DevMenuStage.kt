package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.util.WorldFitViewport
import ktx.scene2d.vis.menu
import ktx.scene2d.vis.menuItem

internal class DevMenuStage : LifecycleStage(WorldFitViewport()) {
    init {
        Gdx.input.inputProcessor = this
        VisUI.load(VisUI.SkinScale.X2)
        MenuBar().also {
            it.table.setPosition(0f, height)
            it.table.align(Align.topLeft)
            it.createDevMenu()
            addActor(it.table)
        }
    }

    private fun MenuBar.createDevMenu() {
        menu(
            title = "dev",
        ) {
            menuItem(
                text = "exit",
            ) {
                addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        Gdx.app.exit()
                    }
                })
            }
        }
    }

    override fun dispose() {
        VisUI.dispose()
        Gdx.input.inputProcessor = null
        super.dispose()
    }
}
