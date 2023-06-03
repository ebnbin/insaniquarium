package dev.ebnbin.gdx.asset

import com.google.gson.annotations.Expose

data class Assets(
    @Expose
    val texture: Map<String, TextureAsset> = emptyMap(),
) {
    /**
     * Should only be called by [AssetHelper].
     */
    internal fun all(): Set<Asset<*>> {
        val set = mutableSetOf<Asset<*>>()
        set.addAll(texture.values)
        return set
    }

    operator fun plus(other: Assets): Assets {
        return Assets(
            texture = texture + other.texture,
        )
    }
}
