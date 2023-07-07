package dev.ebnbin.insaniquarium.body

data class BodyInput(
    val healthDiff: Int = 0,
    val hungerDiff: Int = 0,
    val growthDiff: Int = 0,
    val dropDiff: Int = 0,
    val energyDiff: Int = 0,
    val scaleTransform: BodyLife.ScaleTransform? = null,
)
