package dev.ebnbin.insaniquarium.body

data class BodyInput(
    val healthDiff: Float = 0f,
    val hungerDiff: Float = 0f,
    val growthDiff: Float = 0f,
    val dropDiff: Float = 0f,
    val scaleTransform: BodyRenderer.ScaleTransform? = null,
) {
    val skipUpdate: Boolean = healthDiff == 0f &&
        hungerDiff == 0f &&
        growthDiff == 0f &&
        dropDiff == 0f &&
        scaleTransform == null
}
