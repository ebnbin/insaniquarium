package dev.ebnbin.kgdx.preference

internal object KgdxPreferenceManager {
    private const val NAME = "kgdx"

    val showDevInfo: SimplePreference<Boolean> = SimplePreference(
        name = NAME,
        key = "show_dev_info",
        defaultValue = false,
    )
}
