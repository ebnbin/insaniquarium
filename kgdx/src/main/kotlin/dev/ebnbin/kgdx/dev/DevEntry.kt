package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.graphics.Color
import dev.ebnbin.kgdx.util.colorMarkup
import ktx.graphics.copy

data class DevEntry(
    val key: String?,
    val getValue: (delta: Float) -> String,
) {
    fun getText(delta: Float): String {
        if (key == null) {
            return ""
        }
        val keyText = if (key.isEmpty()) "" else "$key=".colorMarkup(Color.WHITE.copy(alpha = 0.75f))
        val valueText = getValue(delta)
        return " $keyText$valueText "
    }
}

infix fun String?.toDevEntry(getValue: (delta: Float) -> String): DevEntry {
    return DevEntry(this, getValue)
}
