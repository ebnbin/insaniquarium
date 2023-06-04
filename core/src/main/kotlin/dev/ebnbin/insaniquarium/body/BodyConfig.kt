package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose

data class BodyConfig(
    @Expose
    val id: String,
    @Expose
    val width: Float,
    @Expose
    val height: Float,
    @Expose
    val depth: Float,
    @Expose
    val density: Float?,
    @Expose
    val assetId: String,
)
