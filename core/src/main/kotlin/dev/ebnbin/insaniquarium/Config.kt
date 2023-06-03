package dev.ebnbin.insaniquarium

import com.google.gson.annotations.Expose
import dev.ebnbin.insaniquarium.body.BodyConfig

data class Config(
    @Expose
    val body: Map<String, BodyConfig>,
)
