package dev.ebnbin.gdx.pref

import com.badlogic.gdx.Preferences

open class Pref<D, V : Any>(
    val prefManager: PrefManager,
    val key: String,
    val defaultData: D,
    val dataToValue: (D) -> V,
    val valueToData: (V) -> D,
) {
    var data: D
        get() = get()
        set(value) = put(value)

    private fun get(): D {
        return valueToData(prefManager.preferences.get(key, dataToValue(defaultData)))
    }

    private fun put(data: D) {
        prefManager.preferences.put(key, dataToValue(data)).flush()
    }
}

private fun <T : Any> Preferences.get(key: String, defaultValue: T): T {
    @Suppress("UNCHECKED_CAST")
    return when (defaultValue) {
        is Boolean -> getBoolean(key, defaultValue)
        is Int -> getInteger(key, defaultValue)
        is Long -> getLong(key, defaultValue)
        is Float -> getFloat(key, defaultValue)
        is String -> getString(key, defaultValue)
        else -> error("")
    } as T
}

private fun <T : Any> Preferences.put(key: String, value: T): Preferences {
    return when (value) {
        is Boolean -> putBoolean(key, value)
        is Int -> putInteger(key, value)
        is Long -> putLong(key, value)
        is Float -> putFloat(key, value)
        is String -> putString(key, value)
        else -> error("")
    }
}
