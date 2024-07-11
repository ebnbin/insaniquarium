package dev.ebnbin.insaniquarium.preference

import dev.ebnbin.kgdx.preference.SimplePreference

object PreferenceManager {
    private const val NAME = "insaniquarium"

    val enableBodySmoothPosition: SimplePreference<Boolean> = SimplePreference(
        name = NAME,
        key = "enable_body_smooth_position",
        defaultValue = true,
    )
}
