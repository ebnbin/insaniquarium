package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.utils.Point

data class BodyData(
    val type: BodyType,
    val id: String,
    val config: BodyConfig,
    val delegate: BodyDelegate,
    val tankWidth: Float,
    val tankHeight: Float,
    val status: BodyStatus,
    val input: BodyInput,
) {
    val box: BodyBox = BodyBox(
        config = config.box,
        tankWidth = tankWidth,
        tankHeight = tankHeight,
        drivingTargetX = status.life.drivingTargetX,
        drivingTargetY = status.life.drivingTargetY,
        status = status.box,
    )

    //*****************************************************************************************************************

    val life: BodyLife = BodyLife(
        config = config.life,
        tankWidth = tankWidth,
        tankHeight = tankHeight,
        reachDrivingTargetX = box.reachDrivingTargetX,
        reachDrivingTargetY = box.reachDrivingTargetY,
        boxRelation = box::relation,
        canEat = status.renderer.animationData.action == BodyAnimationData.Action.SWIM ||
            status.renderer.animationData.action == BodyAnimationData.Action.EAT,
        status = status.life,
    )

    //*****************************************************************************************************************

    val renderer: BodyRenderer = BodyRenderer(
        config = config.renderer,
        isDead = life.isDead,
        isSinkingOrFloatingOutsideWater = box.isSinkingOrFloatingOutsideWater,
        expectedDirection = box.expectedDirection,
        isHungry = life.hungerStatus == BodyHungerStatus.HUNGRY,
        status = status.renderer,
    )

    //*****************************************************************************************************************

    fun hit(touchPoint: Point): Boolean {
        val hit = box.hit(touchPoint)
        if (hit) {
            delegate.act(
                input = BodyInput(
                    healthDiff = -(life.health ?: 0f),
                ),
            )
        }
        return hit
    }

    //*****************************************************************************************************************

    val canRemove: Boolean = (renderer.canRemove) ||
        (life.canRemove(isSwimming = status.renderer.animationData.action == BodyAnimationData.Action.SWIM))

    fun postUpdate(): Boolean {
        if (life.postUpdate(
            delegate = delegate,
            status = status,
            delta = input.delta,
        )) {
            return true
        }
        return renderer.postUpdate(
            delegate = delegate,
        )
    }

    //*****************************************************************************************************************

    fun update(input: BodyInput): BodyData {
        if (input.skipUpdate) {
            return this
        }
        return copy(
            status = BodyStatusHelper.nextStatus(
                data = this,
                delegate = delegate,
                input = input,
                touchPoint = delegate.touchPoint,
            ),
            input = input,
        )
    }

    fun act() {
        delegate.setSize(renderer.actorWidth, renderer.actorHeight)
        delegate.setPosition(box.x, box.y)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        renderer.draw(delegate, batch, parentAlpha)
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
            createStatus: (body: Body) -> BodyStatus,
        ): BodyData {
            return BodyData(
                type = body.type,
                id = body.id,
                config = body.config,
                delegate = BodyDelegate(body),
                tankWidth = body.tank.width,
                tankHeight = body.tank.height,
                status = createStatus(body),
                input = BodyInput(),
            )
        }
    }
}
