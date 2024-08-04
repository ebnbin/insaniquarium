package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import dev.ebnbin.insaniquarium.tank.Tank
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.kgdx.util.diffStage

class BodyActor(
    tank: Tank,
    type: BodyType,
    position: BodyPosition,
) : Actor() {
    val body: Body = Body(
        actorWrapper = BodyActorWrapper(this),
        tank = tank,
        type = type,
        position = position,
    )

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return body.touchDown(event, x, y, pointer, button)
            }
        })
    }

    override fun setStage(stage: Stage?) {
        diffStage<TankStage>(
            stage = stage,
            updateStage = { super.setStage(it) },
            addedToStage = ::addedToStage,
            removedFromStage = ::removedFromStage,
        )
    }

    private fun addedToStage(stage: TankStage) {
        body.addedToStage(stage)
    }

    private fun removedFromStage(stage: TankStage) {
        body.removedFromStage(stage)
    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor? {
        if (touchable && this.touchable != Touchable.enabled) return null
        if (!isVisible) return null
        return if (body.hit(x, y)) this else null
    }

    override fun act(delta: Float) {
        super.act(delta)
        val tankStage = stage as TankStage? ?: return
        val tickDelta = tankStage.tickDelta
        body.act(
            actDelta = delta,
            tickDelta = tickDelta,
        )
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        body.draw(batch, parentAlpha)
    }
}
