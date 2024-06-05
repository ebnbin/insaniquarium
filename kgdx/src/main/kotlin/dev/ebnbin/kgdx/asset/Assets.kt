package dev.ebnbin.kgdx.asset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Assets(
    @Expose
    @SerializedName("free_type")
    private val freeType: Map<String, FreeTypeAsset> = emptyMap(),
    @Expose
    @SerializedName("json")
    private val json: Map<String, JsonAsset> = emptyMap(),
    @Expose
    @SerializedName("texture")
    private val texture: Map<String, TextureAsset> = emptyMap(),
) {
    fun freeType(name: String): FreeTypeAsset {
        return freeType.getValue(name)
    }

    fun json(name: String): JsonAsset {
        return json.getValue(name)
    }

    fun texture(name: String): TextureAsset {
        return texture.getValue(name)
    }

    internal fun all(): Set<Asset<*>> {
        val set = mutableSetOf<Asset<*>>()
        set.addAll(freeType.values)
        set.addAll(json.values)
        set.addAll(texture.values)
        return set
    }

    internal operator fun plus(other: Assets): Assets {
        return Assets(
            freeType = freeType + other.freeType,
            json = json + other.json,
            texture = texture + other.texture,
        )
    }
}
