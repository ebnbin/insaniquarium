package dev.ebnbin.insaniquarium.body

data class BodyDrivingTarget(
    val position: Float,
    val acceleration: Float,
    val oppositeAccelerationMultiplier: Float,
)
