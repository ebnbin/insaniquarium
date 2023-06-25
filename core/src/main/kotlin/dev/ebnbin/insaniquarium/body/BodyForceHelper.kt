package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.World
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.minMax
import dev.ebnbin.gdx.utils.trim
import kotlin.math.pow

object BodyForceHelper {
    fun gravityY(
        mass: Float,
    ): Float {
        return -(mass * World.G)
    }

    fun buoyancyY(
        volume: Float,
        insideTopPercent: Float,
    ): Float {
        return +(World.DENSITY_WATER * World.G * volume * insideTopPercent)
    }

    fun drag(
        dragCoefficient: Float,
        velocity: Float,
        referenceArea: Float,
    ): Float {
        return -velocity.direction * (0.5f * World.DENSITY_WATER * velocity.pow(2) * dragCoefficient * referenceArea)
    }

    fun driving(
        drivingTarget: BodyStatus.DrivingTarget?,
        position: Float,
        velocity: Float,
        mass: Float,
    ): Float {
        if (drivingTarget == null) {
            return 0f
        }
        val direction = (drivingTarget.position - position).direction
        var magnitude = drivingTarget.acceleration * mass
        if (direction.isOpposite(velocity.direction)) {
            magnitude *= drivingTarget.oppositeAccelerationMultiplier
        }
        return direction * magnitude
    }

    fun normal(
        isInsideLeftOrBottom: Boolean,
        isInsideRightOrTop: Boolean,
        normalReaction: Float,
    ): Float {
        return if (!isInsideLeftOrBottom && normalReaction < 0f || !isInsideRightOrTop && normalReaction > 0f) {
            -normalReaction
        } else {
            0f
        }
    }

    fun acceleration(
        force: Float,
        mass: Float,
    ): Float {
        return (force / mass).trim() // float number error
    }

    fun nextVelocity(
        velocity: Float,
        acceleration: Float,
        isInsideLeftOrBottom: Boolean,
        isInsideRightOrTop: Boolean,
        input: BodyInput,
    ): Float {
        val nextVelocity = velocity + acceleration * input.delta
        return if (!isInsideLeftOrBottom && nextVelocity < 0f || !isInsideRightOrTop && nextVelocity > 0f) {
            0f
        } else {
            nextVelocity
        }
    }

    fun nextPosition(
        position: Float,
        velocity: Float,
        minPosition: Float,
        maxPosition: Float,
        input: BodyInput,
    ): Float {
        val nextPosition = position + velocity * input.delta
        return nextPosition.minMax(minPosition, maxPosition)
    }
}
