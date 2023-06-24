package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.minMax
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.tank.Tank
import dev.ebnbin.insaniquarium.game
import java.util.UUID
import kotlin.math.max

data class BodyData(
    val type: BodyType,

    val id: String,

    val tankWidth: Float,
    val tankHeight: Float,

    val config: BodyConfig,

    val status: BodyStatus,

    val input: BodyInput?,
) {
    val width: Float = config.width
    val height: Float = config.height

    val halfWidth: Float = width / 2f
    val halfHeight: Float = height / 2f

    val minX: Float = halfWidth
    val maxX: Float = tankWidth - halfWidth

    val minY: Float = halfHeight
    val maxY: Float = Float.MAX_VALUE

    val left: Float = status.x - halfWidth
    val right: Float = left + width
    val bottom: Float = status.y - halfHeight
    val top: Float = bottom + height

    val isInsideLeft: Boolean = left > 0f
    val isInsideRight: Boolean = right < tankWidth
    val isInsideBottom: Boolean = bottom > 0f

    /**
     * Percent of body inside tank.
     */
    val insideTopPercent: Float = ((height + tankHeight - top) / height).minMax(0f, 1f)

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

    val depth: Float = config.depth

    val halfDepth: Float = depth / 2f

    val depthLeft: Float = status.x - halfDepth
    val depthRight: Float = depthLeft + depth
    val depthBottom: Float = status.y - halfDepth
    val depthTop: Float = depthBottom + depth

    val area: Float = width * height

    val areaX: Float = height * depth
    val areaY: Float = width * depth

    val volume: Float = area * depth

    val density: Float = if (status.eatAct?.isDying == true) {
        CORPSE_DENSITY
    } else {
        config.density
    }

    val mass: Float = volume * density

    //*****************************************************************************************************************

    val drivingTargetX: BodyStatus.DrivingTarget? = if (status.eatAct?.isDying == true) {
        null
    } else {
        status.eatAct?.drivingTargetX ?: status.touchAct?.drivingTargetX ?: status.swimActX?.drivingTarget
    }
    val drivingTargetY: BodyStatus.DrivingTarget? = if (status.eatAct?.isDying == true) {
        null
    } else {
        status.eatAct?.drivingTargetY ?: status.touchAct?.drivingTargetY ?: status.swimActY?.drivingTarget
    }

    val containDrivingTargetX: Boolean = drivingTargetX?.position?.let { it in left..right } ?: false
    val containDrivingTargetY: Boolean = drivingTargetY?.position?.let { it in bottom..top } ?: false

    //*****************************************************************************************************************

    val dragCoefficient: Float = config.dragCoefficient

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
        drivingTarget = drivingTargetX,
        position = status.x,
        velocity = status.velocityX,
        mass = mass,
    )
    val drivingY: Float = BodyForceHelper.driving(
        drivingTarget = drivingTargetY,
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

    val hasTurnAnimation: Boolean = config.animations.containsKey(BodyConfig.AnimationType.TURN)

    val animation: TextureRegionAnimation = config.animations.getValue(status.textureRegionData.animationType)

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

    private val canRemove: Boolean = (status.disappearAct?.canRemove == true) || (status.health == 0f)

    //*****************************************************************************************************************

    val nextVelocityX = BodyForceHelper.nextVelocity(
        velocity = status.velocityX,
        acceleration = accelerationX,
        isInsideLeftOrBottom = isInsideLeft,
        isInsideRightOrTop = isInsideRight,
        input = input,
    )
    val nextVelocityY = BodyForceHelper.nextVelocity(
        velocity = status.velocityY,
        acceleration = accelerationY,
        isInsideLeftOrBottom = isInsideBottom,
        isInsideRightOrTop = true,
        input = input,
    )

    val nextX = BodyForceHelper.nextPosition(
        position = status.x,
        velocity = nextVelocityX,
        minPosition = minX,
        maxPosition = maxX,
        input = input,
    )
    val nextY = BodyForceHelper.nextPosition(
        position = status.y,
        velocity = nextVelocityY,
        minPosition = minY,
        maxPosition = maxY,
        input = input,
    )

    //*****************************************************************************************************************

    val nextTouchAct = BodyActHelper.nextTouchAct(
        configTouchAct = config.touchAct,
        input = input,
        isDying = status.eatAct?.isDying == true,
    )

    val nextSwimActX = BodyActHelper.nextSwimAct(
        enabled = nextTouchAct == null,
        configSwimAct = config.swimActX,
        swimAct = status.swimActX,
        tankSize = tankWidth,
        containDrivingTarget = containDrivingTargetX,
        input = input,
        isDying = status.eatAct?.isDying == true,
    )
    val nextSwimActY = BodyActHelper.nextSwimAct(
        enabled = nextTouchAct == null,
        configSwimAct = config.swimActY,
        swimAct = status.swimActY,
        tankSize = tankHeight,
        containDrivingTarget = containDrivingTargetY,
        input = input,
        isDying = status.eatAct?.isDying == true,
    )

    val nextDisappearAct = BodyActHelper.nextDisappearAct(
        canDisappear = config.canDisappear ||
            (status.textureRegionData.animationType == BodyConfig.AnimationType.DIE && isAnimationFinished),
        disappearAct = status.disappearAct,
        data = this,
        input = input,
    )

    val nextEatAct = BodyActHelper.nextEatAct(
        configEatAct = config.eatAct,
        eatAct = status.eatAct,
        data = this,
        input = input,
        isDying = status.eatAct?.isDying == true,
    )

    //*****************************************************************************************************************

    val nextExpectedIsFacingRight = if (hasTurnAnimation) {
        when (drivingX.direction) {
            Direction.ZERO -> when (status.velocityX.direction) {
                Direction.ZERO -> status.expectedIsFacingRight
                Direction.POSITIVE -> true
                Direction.NEGATIVE -> false
            }
            Direction.POSITIVE -> true
            Direction.NEGATIVE -> false
        }
    } else {
        false
    }

    val nextTextureRegionData = BodyDrawHelper.nextTextureRegionData(
        config = config,
        hasTurnAnimation = hasTurnAnimation,
        textureRegionData = status.textureRegionData,
        isAnimationFinished = isAnimationFinished,
        canAnimationActionChange = canAnimationActionChange,
        expectedIsFacingRight = status.expectedIsFacingRight,
        eatAct = nextEatAct,
        input = input,
    )

    //*****************************************************************************************************************

    val nextHealth = if (input == null) {
        status.health
    } else {
        if (status.health == BodyConfig.HEALTH_MAX) {
            status.health
        } else {
            max(0f, status.health - input.damage)
        }
    }

    //*****************************************************************************************************************

    fun update(input: BodyInput): BodyData? {
        val nextStatus = BodyStatus(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            x = nextX,
            y = nextY,
            touchAct = nextTouchAct,
            swimActX = nextSwimActX,
            swimActY = nextSwimActY,
            disappearAct = nextDisappearAct,
            eatAct = nextEatAct,
            expectedIsFacingRight = nextExpectedIsFacingRight,
            textureRegionData = nextTextureRegionData,
            health = nextHealth,
        )
        return copy(
            status = nextStatus,
            input = input,
        ).takeIf { !canRemove }
    }

    fun act(body: Body) {
        body.setSize(actorWidth, actorHeight)
        body.setPosition(status.x, status.y, Align.center)
    }

    fun draw(body: Body, batch: Batch, parentAlpha: Float) {
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

    fun actDebug(body: Body, delta: Float) {
        BodyDevHelper.act(this, body, delta)
    }

    fun drawDebug(body: Body, shapes: ShapeRenderer) {
        BodyDevHelper.draw(this, body, shapes)
    }

    //*****************************************************************************************************************

    companion object {
        const val CORPSE_DENSITY = 1020f

        fun create(
            tank: Tank,
            params: BodyParams,
        ): BodyData {
            val config = game.config.body.getValue(params.type)

            val bodyStatus = BodyStatus(
                velocityX = 0f,
                velocityY = 0f,
                x = params.x ?: (tank.width / 2f),
                y = params.y ?: (tank.height / 2f),
                touchAct = null,
                swimActX = null,
                swimActY = null,
                disappearAct = null,
                eatAct = null,
                expectedIsFacingRight = false,
                textureRegionData = BodyStatus.TextureRegionData(
                    animationAction = BodyConfig.AnimationAction.SWIM,
                    animationStatus = BodyConfig.AnimationStatus.NORMAL,
                    stateTime = 0f,
                    isFacingRight = false,
                ),
                health = config.health,
            )

            return BodyData(
                type = params.type,
                id = "${UUID.randomUUID()}",
                tankWidth = tank.width,
                tankHeight = tank.height,
                config = config,
                status = bodyStatus,
                input = null,
            )
        }
    }
}
