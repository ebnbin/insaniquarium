package dev.ebnbin.kgdx.util

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.MenuBar
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisCheckBox
import ktx.scene2d.Scene2dDsl
import kotlin.reflect.KMutableProperty0

@Scene2dDsl
fun PopupMenu.menuItem(
    menuBar: MenuBar? = null,
    text: String,
    clicked: (() -> Unit)? = null,
) {
    val menuItem = MenuItem(text)
    menuItem.imageCell.size(0f)
    menuItem.shortcutCell.padLeft(0f)
    menuItem.subMenuIconCell.padLeft(0f).padRight(0f).size(0f)
    menuItem.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            clicked?.invoke()
            menuBar?.closeMenu()
        }
    })
    addItem(menuItem)
}

@Scene2dDsl
fun PopupMenu.checkBoxMenuItem(
    menuBar: MenuBar? = null,
    text: String,
    valueProperty: KMutableProperty0<Boolean>,
    clicked: (() -> Unit)? = null,
) {
    val menuItem = MenuItem(text)
    menuItem.imageCell.size(0f)
    menuItem.shortcutCell.padLeft(0f)
    menuItem.subMenuIconCell.padLeft(0f).padRight(0f).size(0f)
    val checkBox = VisCheckBox(null)
    checkBox.backgroundImage.color = Color.LIGHT_GRAY
    checkBox.isChecked = valueProperty.get()
    menuItem.add(checkBox)
    menuItem.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            if (actor is VisCheckBox) return
            val newValue = !valueProperty.get()
            valueProperty.set(newValue)
            checkBox.isChecked = newValue
            clicked?.invoke()
            menuBar?.closeMenu()
        }
    })
    addItem(menuItem)
}

@Scene2dDsl
fun <T> PopupMenu.listMenuItem(
    menuBar: MenuBar? = null,
    text: String,
    valueList: List<T>,
    valueProperty: KMutableProperty0<T>? = null,
    valueToString: (T) -> String = { "$it" },
    clicked: ((T) -> Unit)? = null,
) {
    val menuItem = MenuItem(text)
    menuItem.imageCell.size(0f)
    if (valueProperty == null) {
        menuItem.shortcutCell.padLeft(0f)
    } else {
        menuItem.subMenuIconCell.padLeft(0f).padRight(0f).size(0f)
        menuItem.setShortcut(valueToString(valueProperty.get()))
    }
    val subPopupMenu = PopupMenu()
    valueList.forEach { value ->
        subPopupMenu.menuItem(
            menuBar = menuBar,
            text = valueToString(value),
        ) {
            if (valueProperty != null) {
                valueProperty.set(value)
                menuItem.setShortcut(valueToString(value))
            }
            clicked?.invoke(value)
        }
    }
    menuItem.subMenu = subPopupMenu
    addItem(menuItem)
}

@Scene2dDsl
fun PopupMenu.subPopupMenu(
    text: String,
    init: (@Scene2dDsl PopupMenu).() -> Unit = {},
) {
    val menuItem = MenuItem(text)
    menuItem.imageCell.size(0f)
    menuItem.shortcutCell.padLeft(0f)
    val subPopupMenu = PopupMenu()
    subPopupMenu.init()
    menuItem.subMenu = subPopupMenu
    addItem(menuItem)
}
