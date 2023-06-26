package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
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

    val width: Float = body.config.width
    val height: Float = body.config.height

    val halfWidth: Float = width / 2f
    val halfHeight: Float = height / 2f

    val minX: Float = halfWidth
    val maxX: Float = body.tank.width - halfWidth

    val minY: Float = halfHeight
    val maxY: Float = Float.MAX_VALUE

    val left: Float = status.x - halfWidth
    val right: Float = left + width
    val bottom: Float = status.y - halfHeight
    val top: Float = bottom + height

    val isInsideLeft: Boolean = left > 0f
    val isInsideRight: Boolean = right < body.tank.width
    val isInsideBottom: Boolean = bottom > 0f

    //*****************************************************************************************************************

    val vector2: Vector2 = Vector2(status.x, status.y)
    val rectangle: Rectangle = Rectangle(left, bottom, width, height)

    fun distance(other: BodyData): Float {
        return vector2.dst(other.vector2)
    }

    fun containsCenter(other: BodyData): Boolean {
        return rectangle.contains(other.vector2)
    }

    fun overlaps(other: BodyData): Boolean {
        return rectangle.overlaps(other.rectangle)
    }

    fun relation(other: BodyData?): BodyRelation {
        if (other == null) {
            return BodyRelation.DISJOINT
        }
        if (containsCenter(other)) {
            return BodyRelation.CONTAIN_CENTER
        }
        if (overlaps(other)) {
            return BodyRelation.OVERLAP
        }
        return BodyRelation.DISJOINT
    }

    fun hit(touchPoint: Point): Boolean {
        val hit = rectangle.contains(touchPoint.x, touchPoint.y)
        if (hit) {
            body.act(
                input = BodyInput(
                    damage = status.health ?: 0f,
                ),
            )
        }
        return hit
    }

    //*****************************************************************************************************************

    val force: BodyForce = BodyForce(
        dragCoefficient = body.config.dragCoefficient,
        tankWidth = body.tank.width,
        tankHeight = body.tank.height,
        width = width,
        height = height,
        depth = body.config.depth,
        density = if (hungerStatus == BodyHungerStatus.DYING) {
            body.config.hunger?.corpseDensity ?: body.config.density
        } else {
            body.config.density
        },
        drivingTargetX = status.drivingTargetX,
        drivingTargetY = status.drivingTargetY,
        velocityX = status.velocityX,
        velocityY = status.velocityY,
        x = status.x,
        y = status.y,
    )

    //*****************************************************************************************************************

    val animation: TextureRegionAnimation = status.animationData.getAnimation(body.config)

    val isAnimationFinished: Boolean = status.animationData.action != BodyStatus.AnimationData.Action.SWIM &&
        status.animationData.stateTime >= animation.duration

    val canAnimationActionChange: Boolean = status.animationData.action == BodyStatus.AnimationData.Action.SWIM

    val textureRegion: TextureRegion = animation.getTextureRegion(status.animationData.stateTime)

    val actorWidth: Float = textureRegion.regionWidth.toFloat().unitToMeter
    val actorHeight: Float = textureRegion.regionHeight.toFloat().unitToMeter

    val isFlipX: Boolean = if (status.animationData.action == BodyStatus.AnimationData.Action.TURN) {
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

    val canGrowth: Boolean = body.config.growth != null && status.growth != null &&
        status.growth >= body.config.growth.full

    val canRemove: Boolean = (status.disappearAct?.canRemove() == true) ||
        (status.health == 0f) ||
        (canGrowth)

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
        body.setPosition(status.x, status.y, Align.center)
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
