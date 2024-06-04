package dev.ebnbin.kgdx.preference

import com.badlogic.gdx.graphics.Color
import dev.ebnbin.kgdx.util.toColor

internal object KgdxPreferenceManager {
    private const val NAME = "kgdx"

    val showDevInfo: SimplePreference<Boolean> = SimplePreference(
        name = NAME,
        key = "show_dev_info",
        defaultValue = false,
    )

    val clearColor: Preference<Color, Int> = Preference(
        name = NAME,
        key = "clear_color",
        defaultValue = Color.CLEAR,
        valueToStoredValue = { Color.rgba8888(it) },
        storedValueToValue = { it.toColor() },
    )
}
