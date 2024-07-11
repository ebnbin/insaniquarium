package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.utils.Align
import dev.ebnbin.insaniquarium.tank.TankStage

class BodyActorWrapper(
    private val bodyActor: BodyActor,
) {
    val x: Float
        get() = bodyActor.x

    val y: Float
        get() = bodyActor.y

    val width: Float
        get() = bodyActor.width

    val height: Float
        get() = bodyActor.height

    fun setSize(width: Float, height: Float) {
        bodyActor.setSize(width, height)
    }

    fun setPosition(x: Float, y: Float) {
        bodyActor.setPosition(x, y, Align.center)
    }

    val tankStage: TankStage?
        get() = bodyActor.stage as TankStage?
}
