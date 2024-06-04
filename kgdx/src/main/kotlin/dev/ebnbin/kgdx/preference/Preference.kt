package dev.ebnbin.kgdx.preference

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import dev.ebnbin.kgdx.Game

open class Preference<V, SV : Any>(
    val name: String,
    val key: String,
    val defaultValue: V,
    val valueToStoredValue: (V) -> SV,
    val storedValueToValue: (SV) -> V,
) {
    private val preferences: Preferences = Gdx.app.getPreferences("${Game.ID}.$name")

    private val defaultStoredValue: SV = valueToStoredValue(defaultValue)

    private var storedValue: SV
        get() = preferences.get(key, defaultStoredValue)
        set(value) {
            preferences.put(key, value).flush()
        }

    var value: V
        get() = storedValueToValue(storedValue)
        set(value) {
            storedValue = valueToStoredValue(value)
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
        else -> error(Unit)
    } as T
}

private fun <T : Any> Preferences.put(key: String, value: T): Preferences {
    return when (value) {
        is Boolean -> putBoolean(key, value)
        is Int -> putInteger(key, value)
        is Long -> putLong(key, value)
        is Float -> putFloat(key, value)
        is String -> putString(key, value)
        else -> error(Unit)
    }
}
