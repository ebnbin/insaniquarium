package dev.ebnbin.insaniquarium.tank

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
