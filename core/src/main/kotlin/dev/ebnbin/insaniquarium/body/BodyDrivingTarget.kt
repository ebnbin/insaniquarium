package dev.ebnbin.insaniquarium.body

data class BodyDrivingTarget(
    val type: Type,
    val position: Float,
    val acceleration: Float,
    /**
     * The acceleration multiplier when the direction of velocity is opposite to the direction of the target.
     */
    val oppositeAccelerationMultiplier: Float = DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER,
) {
    enum class Type {
        EAT,
        TOUCH,
        SWIM,
        ;
    }

    companion object {
        private const val DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER = 1.5f
    }
}
