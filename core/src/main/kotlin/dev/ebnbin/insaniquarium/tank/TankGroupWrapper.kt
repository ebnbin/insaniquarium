package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch

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

    val stage: TankStage
        get() = requireNotNull(tankGroup.stage) as TankStage

    val batch: Batch
        get() = stage.batch

    fun setSize(width: Float, height: Float) {
        tankGroup.setSize(width, height)
    }

    fun applyTransform(batch: Batch) {
        tankGroup.applyTransform(batch)
    }

    fun resetTransform(batch: Batch) {
        tankGroup.resetTransform(batch)
    }
}
