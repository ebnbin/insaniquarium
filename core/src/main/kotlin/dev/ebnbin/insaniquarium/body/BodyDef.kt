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
    @Expose
    @SerializedName("width")
    val width: Float,
    @Expose
    @SerializedName("height")
    val height: Float,
    @Expose
    @SerializedName("density")
    val density: Float,
    @Expose
    @SerializedName("drag_coefficient")
    val dragCoefficient: Float,
    @Expose
    @SerializedName("driving_acceleration_x")
    val drivingAccelerationX: Float,
    @Expose
    @SerializedName("driving_acceleration_y")
    val drivingAccelerationY: Float,
    @Expose
    @SerializedName("friction_coefficient")
    val frictionCoefficient: Float,
) {
    val textureAsset: TextureAsset
        get() = game.assets.texture(id)
}
