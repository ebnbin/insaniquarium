package dev.ebnbin.gdx.pref

object GdxPrefManager : PrefManager("gdx") {
    val show_dev_log: SimplePref<Boolean> = SimplePref(
        prefManager = this,
        key = "show_dev_log",
        defaultValue = false,
    )
}
