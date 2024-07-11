package dev.ebnbin.insaniquarium.body

import dev.ebnbin.kgdx.util.VectorDirection
import dev.ebnbin.kgdx.util.direction
import dev.ebnbin.kgdx.util.isNegative
import dev.ebnbin.kgdx.util.isPositive
import dev.ebnbin.kgdx.util.isZero
import dev.ebnbin.kgdx.util.magnitude
import kotlin.math.min

object BodyHelper {
    private const val G = 10f
    private const val DENSITY_WATER = 1f
    private const val DRAG_COEFFICIENT_SCALE = 0.5f
    private const val FRICTION_COEFFICIENT_SCALE = 0.1f

    fun gravityY(mass: Float): Float {
        return VectorDirection.NEGATIVE * (mass * G)
    }

    fun buoyancyY(areaInWater: Float): Float {
        return VectorDirection.POSITIVE * (DENSITY_WATER * G * areaInWater)
    }

    fun drag(
        velocity: Float,
        dragCoefficient: Float,
        crossSectionalArea: Float,
    ): Float {
        return -velocity.direction *
            (0.5f * DENSITY_WATER * velocity * velocity * dragCoefficient * DRAG_COEFFICIENT_SCALE *
                crossSectionalArea)
    }

    fun drivingForce(
        drivingTarget: BodyDrivingTarget?,
        position: Float,
        mass: Float,
        velocity: Float,
    ): Float {
        if (drivingTarget == null) return 0f
        val direction = (drivingTarget.position - position).direction
        val magnitude = drivingTarget.acceleration * mass
        return direction * if (direction.isOpposite(velocity.direction)) {
            magnitude * drivingTarget.oppositeAccelerationMultiplier
        } else {
            magnitude
        }
    }

    fun normalForce(
        isInsideLeftOrBottom: Boolean,
        isInsideRightOrTop: Boolean,
        normalReactionForce: Float,
    ): Float {
        return if (!isInsideLeftOrBottom && normalReactionForce.isNegative ||
            !isInsideRightOrTop && normalReactionForce.isPositive) {
            -normalReactionForce
        } else {
            0f
        }
    }

    fun frictionX(
        normalReactionForceY: Float,
        frictionCoefficient: Float,
        isInsideBottom: Boolean,
        frictionReactionForceX: Float,
        velocityX: Float,
    ): Float {
        if (isInsideBottom || normalReactionForceY.isPositive) {
            return 0f
        }
        val maxFrictionMagnitude = frictionCoefficient * FRICTION_COEFFICIENT_SCALE * normalReactionForceY.magnitude
        if (velocityX.isZero) {
            return -frictionReactionForceX.direction * min(maxFrictionMagnitude, frictionReactionForceX.magnitude)
        }
        return -velocityX.direction * maxFrictionMagnitude
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
        friction: Float,
    ): Float {
        val nextVelocity = velocity + acceleration * delta
        return if (!isInsideLeftOrBottom && nextVelocity.isNegative ||
            !isInsideRightOrTop && nextVelocity.isPositive ||
            nextVelocity.direction == friction.direction) {
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
