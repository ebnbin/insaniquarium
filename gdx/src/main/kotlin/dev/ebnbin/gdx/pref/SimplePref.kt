package dev.ebnbin.gdx.pref

open class SimplePref<V : Any>(
    prefManager: PrefManager,
    key: String,
    defaultValue: V,
) : Pref<V, V>(
    prefManager = prefManager,
    key = key,
    defaultData = defaultValue,
    dataToValue = { it },
    valueToData = { it },
)
