package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose

data class BodyConfig(
    @Expose
    val id: String = "",
    @Expose
    val width: Float = 0f,
    @Expose
    val height: Float = 0f,
    @Expose
    val depth: Float = 0f,
    @Expose
    val density: Float? = null,
    @Expose
    val dragCoefficient: Float? = null,
    @Expose
    val assetId: String = "",
)
