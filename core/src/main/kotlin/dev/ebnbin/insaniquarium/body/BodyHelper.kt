package dev.ebnbin.insaniquarium.body

object BodyHelper {
    private const val G = 10f
    private const val DENSITY_WATER = 1000f

    fun gravityY(mass: Float): Float {
        return -(mass * G)
    }

    fun buoyancyY(areaInWater: Float): Float {
        return +(DENSITY_WATER * G * areaInWater)
    }

    fun force(vararg forces: Float): Float {
        return forces.sum()
    }

    fun acceleration(force: Float, mass: Float): Float {
        return force / mass
    }

    fun velocity(velocity: Float, acceleration: Float, delta: Float): Float {
        return velocity + acceleration * delta
    }

    fun position(position: Float, velocity: Float, delta: Float): Float {
        return position + velocity * delta
    }
}
