package dev.ebnbin.gdx.asset

import com.google.gson.annotations.Expose

data class Assets(
    @Expose
    val texture: Map<String, TextureAsset> = emptyMap(),
)
