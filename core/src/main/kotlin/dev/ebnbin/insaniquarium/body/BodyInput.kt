package dev.ebnbin.insaniquarium.body

data class BodyInput(
    val delta: Float = 0f,
    val healthDiff: Float = 0f,
    val hungerDiff: Float = 0f,
    val growthDiff: Float = 0f,
    val dropDiff: Float = 0f,
) {
    val skipUpdate: Boolean = delta == 0f && healthDiff == 0f && hungerDiff == 0f && growthDiff == 0f && dropDiff == 0f
}
