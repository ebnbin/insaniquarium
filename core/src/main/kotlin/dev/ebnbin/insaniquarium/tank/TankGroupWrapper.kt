package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class TankGroupWrapper(
    private val tankGroup: TankGroup,
) {
    val x: Float
        get() = tankGroup.x

    val y: Float
        get() = tankGroup.y

    val width: Float
        get() = tankGroup.width

    val height: Float
        get() = tankGroup.height

    val childrenCount: Int
        get() = tankGroup.children.size

    val batch: Batch
        get() = requireNotNull(tankGroup.stage).batch

    fun setSize(width: Float, height: Float) {
        tankGroup.setSize(width, height)
    }

    fun addActor(actor: Actor) {
        tankGroup.addActor(actor)
    }

    fun clearChildren() {
        tankGroup.clearChildren()
    }
}
