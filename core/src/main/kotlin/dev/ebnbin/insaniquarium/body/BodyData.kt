package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.unitToMeter

data class BodyData(
    val type: BodyType,
    val id: String,
    val config: BodyConfig,
    val delegate: BodyDelegate,
    val status: BodyStatus,
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
            delegate.tick(
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

    //*****************************************************************************************************************

    fun tick(input: BodyInput): BodyData {
        return copy(
            status = BodyStatusHelper.nextTick(
                data = this,
                delegate = delegate,
                input = input,
                touchPoint = delegate.touchPoint,
            ),
        )
    }

    fun postTick(): Boolean {
        if (life.postTick(
                delegate = delegate,
                status = status,
            )) {
            return true
        }
        return renderer.postTick(
            delegate = delegate,
        )
    }

    fun update(delta: Float): BodyData {
        return copy(
            status = BodyStatusHelper.nextStatus(
                data = this,
                delta = delta,
            ),
        )
    }

    fun act() {
        val textureRegion = config.renderer.animations.swim.getTextureRegion(0f)
        delegate.setSize(
            textureRegion.regionWidth.toFloat().unitToMeter,
            textureRegion.regionHeight.toFloat().unitToMeter,
        )
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
            )
        }
    }
}
