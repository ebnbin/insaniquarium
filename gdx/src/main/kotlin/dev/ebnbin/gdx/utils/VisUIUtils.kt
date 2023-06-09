package dev.ebnbin.gdx.utils

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import dev.ebnbin.gdx.pref.SimplePref
import kotlin.reflect.KMutableProperty0

fun PopupMenu.createMenuItem(
    title: String,
    clicked: ((MenuItem) -> Unit)? = null,
): MenuItem {
    val menuItem = MenuItem(title)
    menuItem.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            clicked?.invoke(menuItem)
        }
    })
    addItem(menuItem)
    return menuItem
}

fun <T> PopupMenu.createListMenuItem(
    title: String,
    dataList: List<T>,
    dataToString: (T) -> String = { "$it" },
    property: KMutableProperty0<T>? = null,
    clicked: ((MenuItem, T) -> Unit)? = null,
): MenuItem {
    val menuItem = MenuItem(title)
    if (property != null) {
        menuItem.setShortcut(dataToString(property.get()))
    }
    val popupMenu = PopupMenu()
    dataList.forEach { data ->
        popupMenu.createMenuItem(dataToString(data)) {
            if (property != null) {
                property.set(data)
                menuItem.setShortcut(dataToString(data))
            }
            clicked?.invoke(menuItem, data)
        }
    }
    menuItem.subMenu = popupMenu
    addItem(menuItem)
    return menuItem
}

fun PopupMenu.createBooleanMenuItem(
    pref: SimplePref<Boolean>,
): MenuItem {
    val menuItem = MenuItem(pref.key)
    menuItem.setShortcut("${pref.data}")
    menuItem.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            val newData = !pref.data
            pref.data = newData
            menuItem.setShortcut("$newData")
        }
    })
    addItem(menuItem)
    return menuItem
}
