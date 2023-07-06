package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.utils.Point

data class BodyData(
    val type: BodyType,
    val id: String,
    val config: BodyConfig,
    val delegate: BodyDelegate,
    val status: BodyStatus,
    val delta: Float,
    val input: BodyInput,
) {
    val box: BodyBox = BodyBox(
        config = config.box,
        tankWidth = delegate.tankWidth,
        tankHeight = delegate.tankHeight,
        drivingTargetX = status.life.drivingTargetX,
        drivingTargetY = status.life.drivingTargetY,
        status = status.box,
    )

    //*****************************************************************************************************************

    val life: BodyLife = BodyLife(
        boxConfig = config.box,
        config = config.life,
        tankWidth = delegate.tankWidth,
        tankHeight = delegate.tankHeight,
        reachDrivingTargetX = box.reachDrivingTargetX,
        reachDrivingTargetY = box.reachDrivingTargetY,
        boxRelation = box::relation,
        canEat = status.renderer.animationData.canEat,
        status = status.life,
    )

    //*****************************************************************************************************************

    val renderer: BodyRenderer = BodyRenderer(
        config = config.renderer,
        delegate = delegate,
        isDead = life.isDead,
        isSinkingOrFloatingOutsideWater = box.isSinkingOrFloatingOutsideWater,
        expectedDirection = box.expectedDirection,
        isHungry = life.hungerStatus == BodyHungerStatus.HUNGRY,
        awayFromDrivingTargetX = box.awayFromDrivingTargetX,
        status = status.renderer,
    )

    //*****************************************************************************************************************

    fun hit(touchPoint: Point): Boolean {
        val hit = box.hit(touchPoint)
        if (hit) {
            delegate.act(
                delta = 0f,
                input = BodyInput(
                    healthDiff = -(life.health ?: 0f),
                ),
            )
        }
        return hit
    }

    //*****************************************************************************************************************

    val canRemove: Boolean = (renderer.canRemove) ||
        (life.canRemove(isSwimming = status.renderer.animationData.isSwimming))

    fun postUpdate(): Boolean {
        if (life.postUpdate(
            delegate = delegate,
            status = status,
            delta = delta,
        )) {
            return true
        }
        return renderer.postUpdate(
            delegate = delegate,
        )
    }

    //*****************************************************************************************************************

    fun update(delta: Float, input: BodyInput): BodyData {
        return copy(
            status = BodyStatusHelper.nextStatus(
                data = this,
                delegate = delegate,
                delta = delta,
                input = input,
                touchPoint = delegate.touchPoint,
            ),
            delta = delta,
            input = input,
        )
    }

    fun act() {
        delegate.setSize(renderer.width, renderer.height)
        delegate.setPosition(box.x, box.y)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        renderer.draw(batch, parentAlpha)
    }

    //*****************************************************************************************************************

    fun actDebug() {
        BodyDevHelper.act(this)
    }

    fun drawDebug(shapes: ShapeRenderer) {
        BodyDevHelper.draw(this, shapes)
    }

    //*****************************************************************************************************************

    companion object {
        fun create(
            body: Body,
            status: BodyStatus
        ): BodyData {
            return BodyData(
                type = body.type,
                id = body.id,
                config = body.config,
                delegate = BodyDelegate(body),
                status = status,
                delta = 0f,
                input = BodyInput(),
            )
        }
    }
}
