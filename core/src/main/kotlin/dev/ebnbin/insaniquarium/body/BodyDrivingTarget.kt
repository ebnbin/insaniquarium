package dev.ebnbin.insaniquarium.body

data class BodyDrivingTarget(
    val position: Float,
    val accelerationMultiplier: Float,
) {
    companion object {
        const val OPPOSITE_ACCELERATION_MULTIPLIER = 1.5f
    }
}
