package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.util.dialog.Dialogs
import com.kotcrab.vis.ui.util.dialog.InputDialogListener
import com.kotcrab.vis.ui.widget.Menu
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
import dev.ebnbin.kgdx.util.subPopupMenu
import ktx.app.Platform
import ktx.scene2d.vis.menu

internal class DevMenuStage : LifecycleStage(WorldScreenViewport()) {
    init {
        VisUI.load(VisUI.SkinScale.X2)
    }

    private val menuBar: MenuBar = MenuBar().also {
        it.table.align(Align.topLeft)
        it.createKgdxMenu()
        addActor(it.table)
    }

    private val menuMap: MutableMap<LifecycleStage, Menu> = mutableMapOf()

    internal fun createMenu(stage: LifecycleStage, createMenu: (menuBar: MenuBar) -> Menu) {
        val menu = createMenu(menuBar)
        menuBar.addMenu(menu)
        menuMap[stage] = menu
    }

    internal fun removeMenu(stage: LifecycleStage) {
        val menu = menuMap.remove(stage) ?: return
        menuBar.removeMenu(menu)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        menuBar.table.setPosition(
            (width - Game.WORLD_WIDTH) / 2f,
            height - (height - Game.WORLD_HEIGHT) / 2f,
        )
    }

    private fun MenuBar.createKgdxMenu() {
        menu(
            title = "kgdx",
        ) {
            menuItem(
                menuBar = this@createKgdxMenu,
                text = "exit",
            ) {
                Gdx.app.exit()
            }
            menuItem(
                menuBar = this@createKgdxMenu,
                text = "recreate",
            ) {
                game.recreate()
            }
            listMenuItem(
                menuBar = this@createKgdxMenu,
                text = "screen",
                valueList = mutableListOf<Screen.Creator>().also { list ->
                    list.add(Screen.Creator.EMPTY)
                    list.addAll(game.devScreenList())
                },
                valueToText = { it.name },
            ) {
                game.loadScreen(it)
            }
            subPopupMenu(
                text = "preference",
            ) {
                checkBoxMenuItem(
                    menuBar = this@createKgdxMenu,
                    text = KgdxPreferenceManager.showKgdxDevInfo.key,
                    valueProperty = KgdxPreferenceManager.showKgdxDevInfo::value,
                )
                checkBoxMenuItem(
                    menuBar = this@createKgdxMenu,
                    text = KgdxPreferenceManager.showGameDevInfo.key,
                    valueProperty = KgdxPreferenceManager.showGameDevInfo::value,
                )
                checkBoxMenuItem(
                    menuBar = this@createKgdxMenu,
                    text = KgdxPreferenceManager.showSafeInset.key,
                    valueProperty = KgdxPreferenceManager.showSafeInset::value,
                )
                checkBoxMenuItem(
                    menuBar = this@createKgdxMenu,
                    text = KgdxPreferenceManager.isDebugAll.key,
                    valueProperty = KgdxPreferenceManager.isDebugAll::value,
                )
                listMenuItem(
                    menuBar = this@createKgdxMenu,
                    text = KgdxPreferenceManager.clearColor.key,
                    valueList = CLEAR_COLOR_MAP.values.toList(),
                    valueProperty = KgdxPreferenceManager.clearColor::value,
                    valueToText = { color ->
                        CLEAR_COLOR_MAP.entries.firstOrNull { it.value == color }?.key ?: "$color"
                    },
                )
                listMenuItem(
                    menuBar = this@createKgdxMenu,
                    text = KgdxPreferenceManager.dpi.key,
                    valueList = Dpi.entries,
                    valueProperty = KgdxPreferenceManager.dpi::value,
                    valueToText = Dpi::id,
                ) {
                    game.recreate()
                }
                listMenuItem(
                    menuBar = this@createKgdxMenu,
                    text = KgdxPreferenceManager.textureFilter.key,
                    valueList = TextureFilter.entries,
                    valueProperty = KgdxPreferenceManager.textureFilter::value,
                    valueToText = TextureFilter::id,
                ) {
                    game.recreate()
                }
            }
            menuItem(
                menuBar = this@createKgdxMenu,
                text = "input",
            ) {
                val onInput = { text: String ->
                    game.addDevMessage(text)
                }
                val onCanceled = {
                }
                Platform.runOnMobile {
                    Gdx.input.getTextInput(object : Input.TextInputListener {
                        override fun input(text: String?) {
                            onInput(text ?: "")
                        }

                        override fun canceled() {
                            onCanceled()
                        }
                    }, "input", null, null)
                }
                Platform.runOnDesktop {
                    Dialogs.showInputDialog(this@DevMenuStage, "input", null, object : InputDialogListener {
                        override fun finished(input: String?) {
                            onInput(input ?: "")
                        }

                        override fun canceled() {
                            onCanceled()
                        }
                    })
                }
            }
        }
    }

    override fun dispose() {
        VisUI.dispose()
        super.dispose()
    }
}
