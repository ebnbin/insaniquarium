package dev.ebnbin.kgdx.asset

import dev.ebnbin.kgdx.util.SerializableEnum

enum class AssetFileType(val id: String) : SerializableEnum {
    INTERNAL("internal"),
    LOCAL("local"),
    ;

    override val serializedName: String
        get() = id

    companion object {
        fun of(id: String): AssetFileType {
            return entries.single { it.id == id }
        }
    }
}
