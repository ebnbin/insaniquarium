package dev.ebnbin.kgdx.preference

class SimplePreference<V : Any>(
    name: String,
    key: String,
    defaultValue: V,
) : Preference<V, V>(
    name = name,
    key = key,
    defaultValue = defaultValue,
    valueToStoredValue = { it },
    storedValueToValue = { it },
)
