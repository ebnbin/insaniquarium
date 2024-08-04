package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch

class TankActorWrapper(
    private val tankActor: TankActor,
) {
    val x: Float
        get() = tankActor.x

    val y: Float
        get() = tankActor.y

    val width: Float
        get() = tankActor.width

    val height: Float
        get() = tankActor.height

    val batch: Batch
        get() = requireNotNull(tankActor.stage).batch

    fun setSize(width: Float, height: Float) {
        tankActor.setSize(width, height)
    }
}
