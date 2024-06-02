package dev.ebnbin.kgdx.util

import com.badlogic.gdx.graphics.Color

fun String.colorMarkup(color: Color?): String {
    if (color == null) return this
    return "[#$color]$this[]"
}
