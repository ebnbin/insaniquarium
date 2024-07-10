package dev.ebnbin.insaniquarium.body

import kotlin.math.sign

object BodyHelper {
    private const val G = 10f
    private const val DENSITY_WATER = 1000f

    fun gravityY(mass: Float): Float {
        return -(mass * G)
    }

    fun buoyancyY(areaInWater: Float): Float {
        return +(DENSITY_WATER * G * areaInWater)
    }

    fun drag(velocity: Float, dragCoefficient: Float, crossSectionalArea: Float): Float {
        return -velocity.sign.toInt() *
            (0.5f * DENSITY_WATER * velocity * velocity * dragCoefficient * crossSectionalArea)
    }

    fun normalForce(
        isInsideLeftOrBottom: Boolean,
        isInsideRightOrTop: Boolean,
        reactionForce: Float,
    ): Float {
        return if (!isInsideLeftOrBottom && reactionForce < 0f || !isInsideRightOrTop && reactionForce > 0f) {
            -reactionForce
        } else {
            0f
        }
    }

    fun force(vararg forces: Float): Float {
        return forces.sum()
    }

    fun acceleration(force: Float, mass: Float): Float {
        return force / mass
    }

    fun velocity(
        velocity: Float,
        acceleration: Float,
        delta: Float,
        isInsideLeftOrBottom: Boolean,
        isInsideRightOrTop: Boolean,
    ): Float {
        val nextVelocity = velocity + acceleration * delta
        return if (!isInsideLeftOrBottom && nextVelocity < 0f || !isInsideRightOrTop && nextVelocity > 0f) {
            0f
        } else {
            nextVelocity
        }
    }

    fun position(
        position: Float,
        velocity: Float,
        delta: Float,
        minPosition: Float,
        maxPosition: Float,
    ): Float {
        val nextPosition = position + velocity * delta
        return nextPosition.coerceIn(minPosition, maxPosition)
    }
}
