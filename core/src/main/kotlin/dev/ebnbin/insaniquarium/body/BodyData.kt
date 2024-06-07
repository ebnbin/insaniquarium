package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

data class BodyData(
    val type: BodyType,
    private val tankWidth: Float,
    private val tankHeight: Float,
    val velocityX: Float,
    val velocityY: Float,
    val x: Float,
    val y: Float,
) {
    private val def: BodyDef = type.def

    val width: Float = def.width
    val height: Float = def.height

    val halfWidth: Float = width / 2f
    val halfHeight: Float = height / 2f

    val left: Float = x - halfWidth
    val right: Float = left + width
    val bottom: Float = y - halfHeight
    val top: Float = bottom + height

    val area: Float = width * height

    val areaInWater: Float = ((tankHeight - bottom) / height).coerceIn(0f, 1f) * area

    val density: Float = def.density

    val mass: Float = area * density

    val gravityY: Float = -(mass * G)

    val buoyancyY: Float = +(DENSITY_WATER * G * areaInWater)

    val forceX: Float = 0f
    val forceY: Float = gravityY + buoyancyY

    val accelerationX = forceX / mass
    val accelerationY = forceY / mass

    fun tick(tickDelta: Float): BodyData {
        val nextVelocityX = velocityX + accelerationX * tickDelta
        val nextVelocityY = velocityY + accelerationY * tickDelta
        val nextX = x + nextVelocityX * tickDelta
        val nextY = y + nextVelocityY * tickDelta
        return copy(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            x = nextX,
            y = nextY,
        )
    }

    fun drawDebugBounds(shapes: ShapeRenderer) {
        shapes.rect(left, bottom, width, height)
    }

    companion object {
        private const val G = 10f
        private const val DENSITY_WATER = 1000f
    }
}
