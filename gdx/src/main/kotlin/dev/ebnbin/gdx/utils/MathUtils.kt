package dev.ebnbin.gdx.utils

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun Float.minMax(min: Float, max: Float): Float {
    require(min <= max)
    return max(min, min(max, this))
}

fun Float.trim(threshold: Float = 1E-6f): Float {
    require(threshold >= 0f)
    return if (this.absoluteValue < threshold) 0f else this
}
