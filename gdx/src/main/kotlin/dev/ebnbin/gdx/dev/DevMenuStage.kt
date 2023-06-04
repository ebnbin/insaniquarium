package dev.ebnbin.gdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.pref.GdxPrefManager
import dev.ebnbin.gdx.utils.UnitFitViewport
import dev.ebnbin.gdx.utils.createBooleanMenuItem
import dev.ebnbin.gdx.utils.createMenuItem

class DevMenuStage : BaseStage(UnitFitViewport()) {
    init {
        Gdx.input.inputProcessor = this
        VisUI.load(VisUI.SkinScale.X2)
    }

    private val menuBar: MenuBar = MenuBar().also {
        it.table.align(Align.topLeft)
        addActor(it.table)
    }

    fun addMenu(menu: Menu) {
        menuBar.addMenu(menu)
    }

    fun removeMenu(menu: Menu) {
        menuBar.removeMenu(menu)
    }

    init {
        addMenu(createDevMenu())
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        menuBar.table.setPosition(0f, height)
    }

    override fun dispose() {
        VisUI.dispose()
        super.dispose()
    }

    companion object {
        private fun createDevMenu(): Menu {
            val menu = Menu("DEV")
            menu.createMenuItem("restart") {
                baseGame.restart()
            }
            menu.createBooleanMenuItem(GdxPrefManager.show_dev_gdx_log)
            menu.createBooleanMenuItem(GdxPrefManager.show_dev_game_log)
            return menu
        }
    }
}
