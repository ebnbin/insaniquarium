package dev.ebnbin.kgdx.asset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Assets(
    @Expose
    @SerializedName("free_type")
    private val freeType: Map<String, FreeTypeAsset> = emptyMap(),
) {
    fun freeType(name: String): FreeTypeAsset {
        return freeType.getValue(name)
    }

    internal fun all(): Set<Asset<*>> {
        val set = mutableSetOf<Asset<*>>()
        set.addAll(freeType.values)
        return set
    }

    internal operator fun plus(other: Assets): Assets {
        return Assets(
            freeType = freeType + other.freeType,
        )
    }
}
