package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.minMax
import dev.ebnbin.gdx.utils.unitToMeter

data class BodyData(
    val body: Body,
    val status: BodyStatus,
    val input: BodyInput,
) {
    val hungerStatus: HungerStatus? = body.config.hunger?.status(status.hunger)

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

    /**
     * Percent of body inside tank.
     */
    val insideTopPercent: Float = ((height + body.tank.height - top) / height).minMax(0f, 1f)

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

    //*****************************************************************************************************************

    val depth: Float = body.config.depth

    val halfDepth: Float = depth / 2f

    val depthLeft: Float = status.x - halfDepth
    val depthRight: Float = depthLeft + depth
    val depthBottom: Float = status.y - halfDepth
    val depthTop: Float = depthBottom + depth

    val area: Float = width * height

    val areaX: Float = height * depth
    val areaY: Float = width * depth

    val volume: Float = area * depth

    val density: Float = if (hungerStatus == HungerStatus.DYING) {
        body.config.hunger?.corpseDensity ?: body.config.density
    } else {
        body.config.density
    }

    val mass: Float = volume * density

    //*****************************************************************************************************************

    val dragCoefficient: Float = body.config.dragCoefficient

    val gravityY: Float = BodyForceHelper.gravityY(
        mass = mass,
    )

    val buoyancyY: Float = BodyForceHelper.buoyancyY(
        volume = volume,
        insideTopPercent = insideTopPercent,
    )

    val dragX: Float = BodyForceHelper.drag(
        dragCoefficient = dragCoefficient,
        velocity = status.velocityX,
        referenceArea = areaX,
    )
    val dragY: Float = BodyForceHelper.drag(
        dragCoefficient = dragCoefficient,
        velocity = status.velocityY,
        referenceArea = areaY,
    )

    val drivingX: Float = BodyForceHelper.driving(
        drivingTarget = status.drivingTargetX,
        position = status.x,
        velocity = status.velocityX,
        mass = mass,
    )
    val drivingY: Float = BodyForceHelper.driving(
        drivingTarget = status.drivingTargetY,
        position = status.y,
        velocity = status.velocityY,
        mass = mass,
    )

    val normalReactionX: Float = dragX + drivingX
    val normalReactionY: Float = gravityY + buoyancyY + dragY + drivingY

    val normalX: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideLeft,
        isInsideRightOrTop = isInsideRight,
        normalReaction = normalReactionX,
    )
    val normalY: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideBottom,
        isInsideRightOrTop = true,
        normalReaction = normalReactionY,
    )

    val forceX: Float = normalReactionX + normalX
    val forceY: Float = normalReactionY + normalY

    val accelerationX: Float = BodyForceHelper.acceleration(
        force = forceX,
        mass = mass,
    )
    val accelerationY: Float = BodyForceHelper.acceleration(
        force = forceY,
        mass = mass,
    )

    //*****************************************************************************************************************

    val hasTurnAnimation: Boolean = body.config.animations.turn != null

    val animation: TextureRegionAnimation = status.textureRegionData.getAnimation(body.config.animations)

    val isAnimationFinished: Boolean = status.textureRegionData.animationAction != BodyConfig.AnimationAction.SWIM &&
        status.textureRegionData.stateTime >= animation.duration

    val canAnimationActionChange: Boolean = status.textureRegionData.animationAction == BodyConfig.AnimationAction.SWIM

    val textureRegion: TextureRegion = animation.getTextureRegion(status.textureRegionData.stateTime)

    val actorWidth: Float = textureRegion.regionWidth.toFloat().unitToMeter
    val actorHeight: Float = textureRegion.regionHeight.toFloat().unitToMeter

    val isFlipX: Boolean = if (status.textureRegionData.animationAction == BodyConfig.AnimationAction.TURN) {
        !status.textureRegionData.isFacingRight
    } else {
        status.textureRegionData.isFacingRight
    }

    val alpha: Float = if (status.disappearAct == null || status.disappearAct.time >= 0f) {
        1f
    } else {
        ((BodyStatus.DisappearAct.DISAPPEAR_DURATION + status.disappearAct.time) /
            BodyStatus.DisappearAct.DISAPPEAR_DURATION).minMax(0f, 1f)
    }

    //*****************************************************************************************************************

    val canRemove: Boolean = (status.disappearAct?.canRemove == true) || (status.health == 0f)

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
