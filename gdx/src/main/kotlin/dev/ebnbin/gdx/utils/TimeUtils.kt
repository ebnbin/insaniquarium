package dev.ebnbin.gdx.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toTimestampString(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}
