package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Point

data class BodyInput(
    val delta: Float,
    val touchPoint: Point?,
    val damage: Float = 0f,
)
