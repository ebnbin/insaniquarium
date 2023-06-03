package dev.ebnbin.gdx.utils

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu

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
