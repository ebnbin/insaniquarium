package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import dev.ebnbin.insaniquarium.body.BodyPosition

class Tank(
    val groupWrapper: TankGroupWrapper,
) {
    val data: TankData = TankData()

    init {
        groupWrapper.setSize(data.width, data.height)
    }

    val devHelper: TankDevHelper = TankDevHelper(this)

    var touchPosition: BodyPosition? = null
        private set

    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (devHelper.touchDown(event, x, y, pointer, button)) return false
        touchPosition = BodyPosition(x, y)
        return true
    }

    fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        touchPosition = BodyPosition(x, y)
    }

    fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        touchPosition = null
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        devHelper.draw(batch, parentAlpha)
    }

    fun addedToStage(stage: TankStage) {
        devHelper.addedToStage(stage)
    }

    fun removedFromStage(stage: TankStage) {
        devHelper.removedFromStage(stage)
    }
}
