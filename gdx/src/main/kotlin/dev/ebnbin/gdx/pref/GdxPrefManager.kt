package dev.ebnbin.gdx.pref

object GdxPrefManager : PrefManager("gdx") {
    val use_fixed_delta: SimplePref<Boolean> = SimplePref(
        prefManager = this,
        key = "use_fixed_delta",
        defaultValue = false,
    )
    val show_dev_gdx_log: SimplePref<Boolean> = SimplePref(
        prefManager = this,
        key = "show_dev_gdx_log",
        defaultValue = false,
    )
    val show_dev_game_log: SimplePref<Boolean> = SimplePref(
        prefManager = this,
        key = "show_dev_game_log",
        defaultValue = false,
    )
    val is_debug_all: SimplePref<Boolean> = SimplePref(
        prefManager = this,
        key = "is_debug_all",
        defaultValue = false,
    )
    val music_volume: SimplePref<Float> = SimplePref(
        prefManager = this,
        key = "music_volume",
        defaultValue = 1f,
    )
}
