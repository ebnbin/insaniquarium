package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.ebnbin.kgdx.asset.JsonWrapper

data class BodyDefJsonWrapper(
    @Expose
    @SerializedName("data")
    override val data: Map<String, BodyDef>,
) : JsonWrapper<Map<String, BodyDef>>

data class BodyDef(
    @Expose
    @SerializedName("id")
    val id: String,
)
