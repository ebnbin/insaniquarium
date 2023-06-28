package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.utils.Point

class BodyDelegate(
    private val body: Body,
) {
    fun findBodyByType(typeSet: Set<BodyType>): List<Body> {
        return body.tank.findBodyByType(typeSet)
    }

    fun findNearestBodyByType(typeSet: Set<BodyType>): Body? {
        val bodies = body.tank.findBodyByType(typeSet)
        return bodies.minByOrNull { body.data.box.distance(it.data.box) }
    }

    fun removeFromTank() {
        body.tank.removeBody(body)
    }

    fun addBody(
        type: BodyType,
        createStatus: (body: Body) -> BodyStatus,
        delta: Float,
    ): Body {
        val newBody = body.tank.addBody(
            type = type,
            createStatus = createStatus,
        )
        newBody.act(delta = delta)
        return newBody
    }

    fun replaceBody(
        type: BodyType,
        createStatus: (body: Body) -> BodyStatus,
        delta: Float,
    ): Body {
        val newBody = body.tank.replaceBody(body, type, createStatus)
        newBody.act(delta = delta)
        return newBody
    }

    fun act(input: BodyInput): Body {
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
}
