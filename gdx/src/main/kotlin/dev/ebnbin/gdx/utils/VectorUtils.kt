package dev.ebnbin.gdx.utils

import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * Vector direction.
 */
enum class Direction(val value: Int) {
    ZERO(0),
    POSITIVE(1),
    NEGATIVE(-1),
    ;

    /**
     * Opposite direction.
     */
    operator fun unaryMinus(): Direction {
        return when (this) {
            ZERO -> ZERO
            POSITIVE -> NEGATIVE
            NEGATIVE -> POSITIVE
        }
    }

    /**
     * Vector = direction * magnitude.
     */
    operator fun times(magnitude: Float): Float {
        require(magnitude >= 0f)
        return value * magnitude
    }

    fun isOpposite(other: Direction): Boolean {
        return this == POSITIVE && other == NEGATIVE || this == NEGATIVE && other == POSITIVE
    }
}

/**
 * Direction of the vector.
 */
val Float.direction: Direction
    get() = Direction.values().single { it.value == sign.toInt() }

/**
 * Magnitude of the vector.
 */
val Float.magnitude: Float
    get() = absoluteValue

/**
 * Vector = direction * magnitude.
 */
operator fun Float.times(direction: Direction): Float {
    return direction * this
}
