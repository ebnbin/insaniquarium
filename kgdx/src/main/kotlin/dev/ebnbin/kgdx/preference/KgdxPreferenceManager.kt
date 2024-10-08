package dev.ebnbin.kgdx.preference

import com.badlogic.gdx.graphics.Color
import dev.ebnbin.kgdx.util.Dpi
import dev.ebnbin.kgdx.util.TextureFilter
import dev.ebnbin.kgdx.util.toColor

object KgdxPreferenceManager {
    private const val NAME = "kgdx"

    val showKgdxDevInfo: SimplePreference<Boolean> = SimplePreference(
        name = NAME,
        key = "show_kgdx_dev_info",
        defaultValue = false,
    )

    val showGameDevInfo: SimplePreference<Boolean> = SimplePreference(
        name = NAME,
        key = "show_game_dev_info",
        defaultValue = false,
    )

    val showSafeInset: SimplePreference<Boolean> = SimplePreference(
        name = NAME,
        key = "show_safe_inset",
        defaultValue = false,
    )

    val isDebugAll: SimplePreference<Boolean> = SimplePreference(
        name = NAME,
        key = "is_debug_all",
        defaultValue = false,
    )

    val clearColor: Preference<Color, Int> = Preference(
        name = NAME,
        key = "clear_color",
        defaultValue = Color.CLEAR,
        valueToStoredValue = { Color.rgba8888(it) },
        storedValueToValue = { it.toColor() },
    )

    val dpi: Preference<Dpi, String> = Preference(
        name = NAME,
        key = "dpi",
        defaultValue = Dpi.M,
        valueToStoredValue = Dpi::id,
        storedValueToValue = Dpi::of,
    )

    val textureFilter: Preference<TextureFilter, String> = Preference(
        name = NAME,
        key = "texture_filter",
        defaultValue = TextureFilter.NEAREST,
        valueToStoredValue = TextureFilter::id,
        storedValueToValue = TextureFilter::of,
    )

    val gameSpeedFPS: SimplePreference<Float> = SimplePreference(
        name = NAME,
        key = "game_speed_fps",
        defaultValue = 20f,
    )
}
