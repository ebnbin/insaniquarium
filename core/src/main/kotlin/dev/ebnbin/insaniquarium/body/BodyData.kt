package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

data class BodyData(
    private val type: BodyType,
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

    fun act(delta: Float): BodyData {
        return copy()
    }

    fun drawDebugBounds(shapes: ShapeRenderer) {
        shapes.rect(left, bottom, width, height)
    }
}
