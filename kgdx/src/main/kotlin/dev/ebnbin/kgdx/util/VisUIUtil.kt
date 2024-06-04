package dev.ebnbin.kgdx.util

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisCheckBox
import dev.ebnbin.kgdx.preference.Preference
import ktx.scene2d.Scene2dDsl
import kotlin.reflect.KMutableProperty0

@Scene2dDsl
fun PopupMenu.menuItem(
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
        }
    })
    addItem(menuItem)
}

@Scene2dDsl
fun PopupMenu.checkBoxMenuItem(
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
        }
    })
    addItem(menuItem)
}

@Scene2dDsl
fun PopupMenu.checkBoxMenuItem(
    preference: Preference<*, Boolean>,
    clicked: (() -> Unit)? = null,
) {
    checkBoxMenuItem(
        text = preference.key,
        valueProperty = preference::storedValue,
        clicked = clicked,
    )
}
