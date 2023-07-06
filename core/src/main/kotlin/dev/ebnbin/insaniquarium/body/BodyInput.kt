package dev.ebnbin.insaniquarium.body

data class BodyInput(
    val healthDiff: Float = 0f,
    val hungerDiff: Float = 0f,
    val growthDiff: Float = 0f,
    val dropDiff: Float = 0f,
    val scaleTransform: BodyLife.ScaleTransform? = null,
)
