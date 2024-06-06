package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.Screen
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.CLEAR_COLOR_MAP
import dev.ebnbin.kgdx.util.Dpi
import dev.ebnbin.kgdx.util.TextureFilter
import dev.ebnbin.kgdx.util.WorldScreenViewport
import dev.ebnbin.kgdx.util.checkBoxMenuItem
import dev.ebnbin.kgdx.util.listMenuItem
import dev.ebnbin.kgdx.util.menuItem
import ktx.scene2d.vis.menu

internal class DevMenuStage : LifecycleStage(WorldScreenViewport()) {
    init {
        VisUI.load(VisUI.SkinScale.X2)
    }

    private val menuBar: MenuBar = MenuBar().also {
        it.table.align(Align.topLeft)
        it.createDevMenu()
        addActor(it.table)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        menuBar.table.setPosition(
            (width - Game.WORLD_WIDTH) / 2f,
            height - (height - Game.WORLD_HEIGHT) / 2f,
        )
    }

    private fun MenuBar.createDevMenu() {
        menu(
            title = "dev",
        ) {
            menuItem(
                menuBar = this@createDevMenu,
                text = "exit",
            ) {
                Gdx.app.exit()
            }
            menuItem(
                menuBar = this@createDevMenu,
                text = "recreate",
            ) {
                game.recreate()
            }
            checkBoxMenuItem(
                menuBar = this@createDevMenu,
                text = KgdxPreferenceManager.showKgdxDevInfo.key,
                valueProperty = KgdxPreferenceManager.showKgdxDevInfo::value,
            )
            checkBoxMenuItem(
                menuBar = this@createDevMenu,
                text = KgdxPreferenceManager.showGameDevInfo.key,
                valueProperty = KgdxPreferenceManager.showGameDevInfo::value,
            )
            checkBoxMenuItem(
                menuBar = this@createDevMenu,
                text = KgdxPreferenceManager.isDebugAll.key,
                valueProperty = KgdxPreferenceManager.isDebugAll::value,
            )
            listMenuItem(
                menuBar = this@createDevMenu,
                text = KgdxPreferenceManager.clearColor.key,
                valueList = CLEAR_COLOR_MAP.values.toList(),
                valueProperty = KgdxPreferenceManager.clearColor::value,
                valueToString = { color ->
                    CLEAR_COLOR_MAP.entries.firstOrNull { it.value == color }?.key ?: "$color"
                },
            )
            listMenuItem(
                menuBar = this@createDevMenu,
                text = KgdxPreferenceManager.dpi.key,
                valueList = Dpi.entries,
                valueProperty = KgdxPreferenceManager.dpi::value,
                valueToString = Dpi::id,
            ) {
                game.recreate()
            }
            listMenuItem(
                menuBar = this@createDevMenu,
                text = KgdxPreferenceManager.textureFilter.key,
                valueList = TextureFilter.entries,
                valueProperty = KgdxPreferenceManager.textureFilter::value,
                valueToString = TextureFilter::id,
            ) {
                game.recreate()
            }
            listMenuItem(
                menuBar = this@createDevMenu,
                text = "screen",
                valueList = mutableListOf<Screen.Creator>().also { list ->
                    list.add(Screen.Creator.EMPTY)
                    list.addAll(game.devScreenList())
                },
                valueToString = { it.name },
            ) {
                game.loadScreen(it)
            }
        }
    }

    override fun dispose() {
        VisUI.dispose()
        super.dispose()
    }
}
