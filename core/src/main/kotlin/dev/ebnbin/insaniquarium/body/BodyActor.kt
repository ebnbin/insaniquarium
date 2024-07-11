package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import dev.ebnbin.insaniquarium.tank.Tank
import dev.ebnbin.insaniquarium.tank.TankGroup
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.kgdx.util.diffParent
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

    override fun setParent(parent: Group?) {
        diffParent<TankGroup>(
            parent = parent,
            updateParent = { super.setParent(it) },
            addedToParent = ::addedToParent,
            removedFromParent = ::removedFromParent,
        )
    }

    private fun addedToParent(parent: TankGroup) {
        body.addedToParent()
    }

    private fun removedFromParent(parent: TankGroup) {
        body.removedFromParent()
    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor? {
        if (touchable && this.touchable != Touchable.enabled) return null
        if (!isVisible) return null
        val left = body.data.left - this.x
        val right = left + body.data.width
        val bottom = body.data.bottom - this.y
        val top = bottom + body.data.height
        return if (x >= left && x < right && y >= bottom && y < top) this else null
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

    override fun drawDebugBounds(shapes: ShapeRenderer) {
        super.drawDebugBounds(shapes)
        body.data.drawDebugBounds(shapes)
    }
}
