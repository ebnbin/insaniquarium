package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.utils.Point

data class BodyData(
    val body: Body,
    val status: BodyStatus,
    val input: BodyInput,
) {
    val box: BodyBox = BodyBox(
        dragCoefficient = body.config.dragCoefficient,
        waterFrictionCoefficient = body.config.waterFrictionCoefficient,
        leftRightFrictionCoefficient = body.config.leftRightFrictionCoefficient,
        bottomFrictionCoefficient = body.config.bottomFrictionCoefficient,
        tankWidth = body.tank.width,
        tankHeight = body.tank.height,
        width = body.config.width,
        height = body.config.height,
        depth = body.config.depth,
        density = body.config.density,
        drivingTargetX = status.drivingTargetX,
        drivingTargetY = status.drivingTargetY,
        status = status.box,
    )

    //*****************************************************************************************************************

    val life: BodyLife = BodyLife(
        configHealth = body.config.health,
        configHunger = body.config.hunger,
        configGrowth = body.config.growth,
        configDrop = body.config.drop,
        status = status.life,
    )

    //*****************************************************************************************************************

    val renderer: BodyRenderer = BodyRenderer(
        isDead = body.config.isDead,
        configAnimations = body.config.animations,
        isSinkingOrFloatingOutsideWater = box.isSinkingOrFloatingOutsideWater,
        expectedDirection = box.expectedDirection,
        isHungry = life.hungerStatus == BodyHungerStatus.HUNGRY,
        status = status.renderer,
    )

    //*****************************************************************************************************************

    fun hit(touchPoint: Point): Boolean {
        val hit = box.hit(touchPoint)
        if (hit) {
            body.act(
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
        val bodyManager = BodyManager(body)
        if (life.postUpdate(
            bodyManager = bodyManager,
            status = status,
            delta = input.delta,
        )) {
            return true
        }
        return renderer.postUpdate(
            bodyManager = bodyManager,
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
                status = status,
                input = input,
                bodyManager = BodyManager(body),
            ),
            input = input,
        )
    }

    fun act() {
        body.setSize(renderer.actorWidth, renderer.actorHeight)
        body.setPosition(box.x, box.y, Align.center)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        renderer.draw(body, batch, parentAlpha)
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
                body = body,
                status = createStatus(body),
                input = BodyInput(),
            )
        }
    }
}
