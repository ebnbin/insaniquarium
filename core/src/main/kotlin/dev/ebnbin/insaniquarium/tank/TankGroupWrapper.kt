package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener

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

    fun addListener(listener: EventListener): Boolean {
        return tankGroup.addListener(listener)
    }

    fun addActor(actor: Actor) {
        tankGroup.addActor(actor)
    }

    fun clearChildren() {
        tankGroup.clearChildren()
    }
}
