package dev.ebnbin.gdx.utils

import com.badlogic.gdx.graphics.Color

fun String.colorMarkup(color: Color): String {
    return "[#$color]$this[]"
}
