package dev.ebnbin.gdx.utils

import kotlin.math.absoluteValue

fun Float.trim(threshold: Float = 1E-6f): Float {
    require(threshold >= 0f)
    return if (this.absoluteValue < threshold) 0f else this
}
