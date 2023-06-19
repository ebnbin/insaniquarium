package dev.ebnbin.insaniquarium

import com.google.gson.annotations.Expose
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyType

data class Config(
    @Expose
    val body: Map<BodyType, BodyConfig> = emptyMap(),
)
