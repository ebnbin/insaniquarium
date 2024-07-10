package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

data class BodyData(
    val type: BodyType,
    private val tankWidth: Float,
    private val tankHeight: Float,
    val velocityX: Float,
    val velocityY: Float,
    val position: BodyPosition,
) {
    private val def: BodyDef = type.def

    val width: Float = def.width
    val height: Float = def.height

    val halfWidth: Float = width / 2f
    val halfHeight: Float = height / 2f

    val left: Float = position.x - halfWidth
    val right: Float = left + width
    val bottom: Float = position.y - halfHeight
    val top: Float = bottom + height

    val area: Float = width * height

    val areaInWater: Float = ((tankHeight - bottom) / height).coerceIn(0f, 1f) * area

    val density: Float = def.density

    val mass: Float = area * density

    val gravityY: Float = BodyHelper.gravityY(mass)

    val buoyancyY: Float = BodyHelper.buoyancyY(areaInWater)

    val forceX: Float = BodyHelper.force()
    val forceY: Float = BodyHelper.force(gravityY, buoyancyY)

    val accelerationX: Float = BodyHelper.acceleration(forceX, mass)
    val accelerationY: Float = BodyHelper.acceleration(forceY, mass)

    fun tick(tickDelta: Float): BodyData {
        val nextVelocityX = BodyHelper.velocity(velocityX, accelerationX, tickDelta)
        val nextVelocityY = BodyHelper.velocity(velocityY, accelerationY, tickDelta)
        val nextX = BodyHelper.position(position.x, nextVelocityX, tickDelta)
        val nextY = BodyHelper.position(position.y, nextVelocityY, tickDelta)
        return copy(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            position = BodyPosition(
                x = nextX,
                y = nextY,
            ),
        )
    }

    fun drawDebugBounds(shapes: ShapeRenderer) {
        shapes.rect(left, bottom, width, height)
    }
}
