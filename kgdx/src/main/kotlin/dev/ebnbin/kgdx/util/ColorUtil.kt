package dev.ebnbin.kgdx.util

import com.badlogic.gdx.graphics.Color

fun Int.toColor(): Color {
    return Color(this)
}

val Color.isOpaque: Boolean
    get() = a == 1f

val Color.gray: Float
    get() {
        require(isOpaque && r == g && g == b)
        return r
    }

fun String.colorMarkup(color: Color?): String {
    if (color == null) return this
    return "[#$color]$this[]"
}
