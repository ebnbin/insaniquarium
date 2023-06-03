package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose

data class BodyConfig(
    @Expose
    val id: String,
    @Expose
    val assetId: String,
)
