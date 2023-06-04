package dev.ebnbin.gdx.utils

import kotlin.math.max
import kotlin.math.min

fun Float.minMax(min: Float, max: Float): Float {
    require(min <= max)
    return max(min, min(max, this))
}
