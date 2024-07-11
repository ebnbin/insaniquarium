package dev.ebnbin.kgdx.util

import kotlin.math.absoluteValue

enum class VectorDirection(val value: Int) {
    ZERO(0),
    POSITIVE(1),
    NEGATIVE(-1),
    ;

    operator fun unaryMinus(): VectorDirection {
        return when (this) {
            ZERO -> ZERO
            POSITIVE -> NEGATIVE
            NEGATIVE -> POSITIVE
        }
    }

    operator fun times(magnitude: Float): Float {
        require(magnitude >= 0f)
        return value * magnitude
    }

    fun isOpposite(other: VectorDirection): Boolean {
        return this == POSITIVE && other == NEGATIVE || this == NEGATIVE && other == POSITIVE
    }
}

val Float.direction: VectorDirection
    get() = when {
        this > 0f -> VectorDirection.POSITIVE
        this < 0f -> VectorDirection.NEGATIVE
        else -> VectorDirection.ZERO
    }

val Float.magnitude: Float
    get() = absoluteValue

val Float.isPositive: Boolean
    get() = direction == VectorDirection.POSITIVE

val Float.isNegative: Boolean
    get() = direction == VectorDirection.NEGATIVE

operator fun Float.times(direction: VectorDirection): Float {
    return direction * this
}
