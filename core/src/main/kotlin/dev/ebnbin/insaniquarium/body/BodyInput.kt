package dev.ebnbin.insaniquarium.body

data class BodyInput(
    val healthDiff: Int = 0,
    val hungerDiff: Int = 0,
    val growthDiff: Float = 0f,
    val dropDiff: Float = 0f,
    val scaleTransform: BodyLife.ScaleTransform? = null,
)
