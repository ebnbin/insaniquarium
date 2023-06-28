package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.utils.Point

class BodyDelegate(
    private val body: Body,
) {
    fun findNearestBodyByType(typeSet: Set<BodyType>): BodyData? {
        val bodies = body.tank.findBodyByType(typeSet)
        return bodies.minByOrNull { body.data.box.distance(it.box) }
    }

    fun removeFromTank() {
        body.tank.removeBody(body.data)
    }

    fun addBody(
        type: BodyType,
        createStatus: (body: Body) -> BodyStatus,
        delta: Float,
    ): BodyData {
        val newBody = body.tank.addBody(
            type = type,
            createStatus = createStatus,
        )
        newBody.delegate.act(
            input = BodyInput(
                delta = delta,
            ),
        )
        return newBody
    }

    fun replaceBody(
        type: BodyType,
        createStatus: (body: Body) -> BodyStatus,
        delta: Float,
    ): BodyData {
        val newBody = body.tank.replaceBody(body.data, type, createStatus)
        newBody.delegate.act(
            input = BodyInput(
                delta = delta,
            ),
        )
        return newBody
    }

    fun act(input: BodyInput): BodyData {
        return body.act(input)
    }

    fun setSize(width: Float, height: Float) {
        body.setSize(width, height)
    }

    fun setPosition(x: Float, y: Float) {
        body.setPosition(x, y, Align.center)
    }

    val x: Float
        get() = body.x
    val y: Float
        get() = body.y

    val originX: Float
        get() = body.originX
    val originY: Float
        get() = body.originY

    val width: Float
        get() = body.width
    val height: Float
        get() = body.height

    val scaleX: Float
        get() = body.scaleX
    val scaleY: Float
        get() = body.scaleY

    val rotation: Float
        get() = body.rotation

    val touchPoint: Point?
        get() = body.tank.touchPoint

    val tankWidth: Float
        get() = body.tank.width
    val tankHeight: Float
        get() = body.tank.height
}
