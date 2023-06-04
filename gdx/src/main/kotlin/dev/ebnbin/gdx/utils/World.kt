package dev.ebnbin.gdx.utils

object World {
    const val DENSITY_WATER = 1000f

    const val G = 10f

    var unitWidth: Float = 0f
        internal set
    var unitHeight: Float = 0f
        internal set

    var unitsPerMeter: Float = 1f
        internal set
}

val Float.unitToMeter: Float
    get() = this / World.unitsPerMeter

val Float.meterToUnit: Float
    get() = this * World.unitsPerMeter
