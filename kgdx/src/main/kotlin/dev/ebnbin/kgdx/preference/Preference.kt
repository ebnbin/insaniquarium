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

    var value: V
        get() = get()
        set(value) {
            put(value)
        }

    private fun getStoredValue(): SV {
        return preferences.get(key, defaultStoredValue)
    }

    private fun putStoredValue(storedValue: SV) {
        preferences.put(key, storedValue).flush()
    }

    private fun get(): V {
        return storedValueToValue(getStoredValue())
    }

    private fun put(value: V) {
        putStoredValue(valueToStoredValue(value))
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
