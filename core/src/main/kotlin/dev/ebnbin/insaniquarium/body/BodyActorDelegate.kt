package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.utils.Point

class BodyActorDelegate(
    private val bodyActor: BodyActor,
) {
    fun findNearestBodyByType(typeSet: Set<BodyType>): Body? {
        val bodies = bodyActor.tank.findBodyByType(typeSet)
        return bodies.minByOrNull { bodyActor.body.box.distance(it.box) }
    }

    fun removeFromTank() {
        bodyActor.tank.removeBody(bodyActor.body)
    }

    fun addBody(
        type: BodyType,
        boxStatus: BodyBox.Status = BodyBox.Status(),
        lifeStatus: BodyLife.Status = BodyLife.Status(),
    ): Body {
        return bodyActor.tank.addBody(
            type = type,
            boxStatus = boxStatus,
            lifeStatus = lifeStatus,
        )
    }

    fun replaceBody(
        type: BodyType,
        boxStatus: BodyBox.Status = BodyBox.Status(),
        lifeStatus: BodyLife.Status = BodyLife.Status(),
    ): Body {
        return bodyActor.tank.replaceBody(bodyActor.body, type, boxStatus, lifeStatus)
    }

    fun setSize(width: Float, height: Float) {
        bodyActor.setSize(width, height)
    }

    fun setPosition(x: Float, y: Float) {
        bodyActor.setPosition(x, y, Align.center)
    }

    val x: Float
        get() = bodyActor.x
    val y: Float
        get() = bodyActor.y

    val originX: Float
        get() = bodyActor.originX
    val originY: Float
        get() = bodyActor.originY

    val width: Float
        get() = bodyActor.width
    val height: Float
        get() = bodyActor.height

    val scaleX: Float
        get() = bodyActor.scaleX
    val scaleY: Float
        get() = bodyActor.scaleY

    val rotation: Float
        get() = bodyActor.rotation

    val touchPoint: Point?
        get() = bodyActor.tank.touchPoint

    val tankWidth: Float
        get() = bodyActor.tank.width
    val tankHeight: Float
        get() = bodyActor.tank.height

    val debug: Boolean
        get() = bodyActor.debug
}
