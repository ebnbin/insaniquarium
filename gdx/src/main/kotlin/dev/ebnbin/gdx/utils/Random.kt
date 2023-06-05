package dev.ebnbin.gdx.utils

import kotlin.random.Random

object Random {
    private val random: Random = Random.Default

    fun nextBoolean(): Boolean {
        return random.nextBoolean()
    }

    fun nextFloat(start: Float, end: Float): Float {
        require(end >= start)
        return start + random.nextFloat() * (end - start)
    }
}
