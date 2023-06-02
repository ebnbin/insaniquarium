package dev.ebnbin.gdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.utils.UnitFitViewport

class DevMenuStage : BaseStage(UnitFitViewport()) {
    init {
        Gdx.input.inputProcessor = this
        VisUI.load(VisUI.SkinScale.X2)
    }

    private val menuBar: MenuBar = MenuBar().also {
        it.table.align(Align.topLeft)
        it.addMenu(Menu("DEV"))
        addActor(it.table)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        menuBar.table.setPosition(0f, height)
    }

    override fun dispose() {
        VisUI.dispose()
        super.dispose()
    }
}
