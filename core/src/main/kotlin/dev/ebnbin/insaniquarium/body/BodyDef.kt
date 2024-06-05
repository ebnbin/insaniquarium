package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.ebnbin.kgdx.asset.JsonWrapper
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.game

data class BodyDefJsonWrapper(
    @Expose
    @SerializedName("data")
    override val data: Map<String, BodyDef>,
) : JsonWrapper<Map<String, BodyDef>>

data class BodyDef(
    @Expose
    @SerializedName("id")
    val id: String,
) {
    val textureAsset: TextureAsset
        get() = game.assets.texture(id)
}
