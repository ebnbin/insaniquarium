package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.minMax
import dev.ebnbin.gdx.utils.unitToMeter

data class BodyData(
    val body: Body,
    val status: BodyStatus,
    val input: BodyInput,
) {
    val hungerStatus: BodyHungerStatus? = body.config.hunger?.status(status.hunger)

    //*****************************************************************************************************************

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
        velocityX = status.velocityX,
        velocityY = status.velocityY,
        x = status.x,
        y = status.y,
    )

    //*****************************************************************************************************************

    fun hit(touchPoint: Point): Boolean {
        val hit = box.hit(touchPoint)
        if (hit) {
            body.act(
                input = BodyInput(
                    healthDiff = -(status.health ?: 0f),
                ),
            )
        }
        return hit
    }

    //*****************************************************************************************************************

    val animation: TextureRegionAnimation = status.animationData.getAnimation(body.config)

    val isAnimationFinished: Boolean = status.animationData.action != BodyAnimationData.Action.SWIM &&
        status.animationData.stateTime >= animation.duration

    val canAnimationActionChange: Boolean = status.animationData.action == BodyAnimationData.Action.SWIM

    val textureRegion: TextureRegion = animation.getTextureRegion(status.animationData.stateTime)

    val actorWidth: Float = textureRegion.regionWidth.toFloat().unitToMeter
    val actorHeight: Float = textureRegion.regionHeight.toFloat().unitToMeter

    val isFlipX: Boolean = if (status.animationData.action == BodyAnimationData.Action.TURN) {
        !status.animationData.isFacingRight
    } else {
        status.animationData.isFacingRight
    }

    val alpha: Float = if (status.disappearAct == null || status.disappearAct.time >= 0f) {
        1f
    } else {
        ((BodyStatus.DisappearAct.DISAPPEAR_DURATION + status.disappearAct.time) /
            BodyStatus.DisappearAct.DISAPPEAR_DURATION).minMax(0f, 1f)
    }

    //*****************************************************************************************************************

    val canTransformByHunger: Boolean = body.config.hunger != null && status.hunger != null &&
        status.hunger == 0f && body.config.hunger.transformation != null &&
        status.animationData.action == BodyAnimationData.Action.SWIM

    val canTransformByGrowth: Boolean = body.config.growth != null && status.growth != null &&
        status.growth >= body.config.growth.full

    val canRemove: Boolean = (status.disappearAct?.canRemove() == true) ||
        (status.health == 0f) ||
        canTransformByHunger ||
        canTransformByGrowth

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
            ),
            input = input,
        )
    }

    fun act() {
        body.setSize(actorWidth, actorHeight)
        body.setPosition(box.x, box.y, Align.center)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        val oldColor = batch.color.cpy()
        batch.color = batch.color.cpy().also { it.a = alpha * parentAlpha }
        batch.draw(
            textureRegion.texture,
            body.x,
            body.y,
            body.originX,
            body.originY,
            body.width,
            body.height,
            body.scaleX,
            body.scaleY,
            body.rotation,
            textureRegion.regionX,
            textureRegion.regionY,
            textureRegion.regionWidth,
            textureRegion.regionHeight,
            isFlipX,
            false,
        )
        batch.color = oldColor
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
