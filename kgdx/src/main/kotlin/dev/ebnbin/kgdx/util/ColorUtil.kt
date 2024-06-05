package dev.ebnbin.kgdx.util

import com.badlogic.gdx.graphics.Color

val CLEAR_COLOR_MAP: Map<String, Color> = mapOf(
    "clear" to Color.CLEAR,
    "black" to Color.BLACK,
    "white" to Color.WHITE,
    "gray" to Color.GRAY,
    "red" to Color.RED,
    "green" to Color.GREEN,
    "blue" to Color.BLUE,
)

fun Int.toColor(): Color {
    return Color(this)
}

val Color.isTransparent: Boolean
    get() = a == 0f

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
