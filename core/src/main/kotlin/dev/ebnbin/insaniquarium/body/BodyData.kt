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

    val life: BodyLife = BodyLife(
        configHealth = body.config.health,
        configHunger = body.config.hunger,
        configGrowth = body.config.growth,
        configDrop = body.config.drop,
        health = status.health,
        hunger = status.hunger,
        growth = status.growth,
        drop = status.drop,
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

    val canRemove: Boolean = (status.disappearAct?.canRemove() == true) ||
        life.isDeadFromHealth ||
        (life.transformationFromHunger != null && status.animationData.action == BodyAnimationData.Action.SWIM) ||
        life.transformationFromGrowth != null

    fun validate(): Boolean {
        if (life.isDeadFromHealth) {
            body.tank.removeBody(body)
            return true
        }
        if (life.transformationFromHunger != null && status.animationData.action == BodyAnimationData.Action.SWIM) {
            val newBody = body.tank.replaceBody(
                oldBody = body,
                type = life.transformationFromHunger,
                createStatus = {
                    status.copy(
                        swimActX = null,
                        swimActY = null,
                        health = null,
                        hunger = null,
                        growth = null,
                        drop = null,
                        disappearAct = null,
                        drivingTargetX = null,
                        drivingTargetY = null,
                        animationData = status.animationData.copy(
                            stateTime = 0f,
                        ),
                    )
                },
            )
            newBody.act(delta = input.delta)
            return true
        }
        if (life.transformationFromGrowth != null) {
            val growth = status.growth
            require(growth != null)
            val newBody = body.tank.replaceBody(
                oldBody = body,
                type = life.transformationFromGrowth,
                createStatus = {
                    status.copy(
                        growth = growth + (it.config.growth?.initialThreshold ?: 0f),
                    )
                },
            )
            newBody.act(delta = input.delta)
            return true
        }
        if (life.productionFromDrop != null) {
            repeat(life.dropCount) {
                body.tank.addBody(
                    type = life.productionFromDrop,
                    createStatus = {
                        BodyStatus(
                            x = status.x,
                            y = status.y,
                        )
                    },
                )
            }
            body.act(
                input = BodyInput(
                    dropDiff = life.dropCount.toFloat(),
                )
            )
            return false
        }
        return false
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
