package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
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

    init {
        groupWrapper.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                touchPosition = BodyPosition(x, y)
                devHelper.touchDown(event, x, y, pointer, button)
                return true
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                super.touchDragged(event, x, y, pointer)
                touchPosition = BodyPosition(x, y)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                touchPosition = null
            }
        })
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
