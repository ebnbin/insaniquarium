package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.minMax
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.aquarium.Tank
import dev.ebnbin.insaniquarium.game
import java.util.UUID

data class BodyData(
    val type: BodyType,

    val id: String,

    val tankWidth: Float,
    val tankHeight: Float,

    val velocityX: Float,
    val velocityY: Float,

    val x: Float,
    val y: Float,

    val touchAct: TouchAct?,

    val swimActX: SwimAct?,
    val swimActY: SwimAct?,

    val disappearAct: DisappearAct?,

    val eatAct: EatAct?,

    val expectedIsFacingRight: Boolean,

    val textureRegionData: TextureRegionData,
) {
    data class TouchAct(
        val drivingTargetX: DrivingTarget,
        val drivingTargetY: DrivingTarget,
    )

    data class SwimAct(
        val drivingTarget: DrivingTarget?,
        val remainingTime: Float,
    )

    data class DisappearAct(
        /**
         * >= 0f: Delaying.
         * < 0f: Disappearing.
         */
        val time: Float,
    ) {
        companion object {
            const val DISAPPEAR_DURATION = 1f
        }
    }

    data class EatAct(
        val drivingTargetX: DrivingTarget,
        val drivingTargetY: DrivingTarget,
    )

    //*****************************************************************************************************************

    data class DrivingTarget(
        val position: Float,
        val acceleration: Float,
        /**
         * The acceleration multiplier when the direction of velocity is opposite to the direction of the target.
         */
        val oppositeAccelerationMultiplier: Float = DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER,
    ) {
        companion object {
            private const val DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER = 1.5f
        }
    }

    val drivingTargetX: DrivingTarget? = eatAct?.drivingTargetX ?: touchAct?.drivingTargetX ?: swimActX?.drivingTarget
    val drivingTargetY: DrivingTarget? = eatAct?.drivingTargetY ?: touchAct?.drivingTargetY ?: swimActY?.drivingTarget

    //*****************************************************************************************************************

    val config: BodyConfig = game.config.body.getValue(type.serializedName)

    //*****************************************************************************************************************

    val width: Float = config.width
    val height: Float = config.height

    val halfWidth: Float = width / 2f
    val halfHeight: Float = height / 2f

    val minX: Float = halfWidth
    val maxX: Float = tankWidth - halfWidth

    val minY: Float = halfHeight
    val maxY: Float = Float.MAX_VALUE

    val left: Float = x - halfWidth
    val right: Float = left + width
    val bottom: Float = y - halfHeight
    val top: Float = bottom + height

    val isInsideLeft: Boolean = left > 0f
    val isInsideRight: Boolean = right < tankWidth
    val isInsideBottom: Boolean = bottom > 0f

    /**
     * Percent of body inside tank.
     */
    val insideTopPercent: Float = ((height + tankHeight - top) / height).minMax(0f, 1f)

    val containDrivingTargetX: Boolean = drivingTargetX?.position?.let { it in left..right } ?: false
    val containDrivingTargetY: Boolean = drivingTargetY?.position?.let { it in bottom..top } ?: false

    val depth: Float = config.depth

    val halfDepth: Float = depth / 2f

    val depthLeft: Float = x - halfDepth
    val depthRight: Float = depthLeft + depth
    val depthBottom: Float = y - halfDepth
    val depthTop: Float = depthBottom + depth

    val area: Float = width * height

    val areaX: Float = height * depth
    val areaY: Float = width * depth

    val volume: Float = area * depth

    val density: Float = config.density

    val mass: Float = volume * density

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
        velocity = velocityX,
        referenceArea = areaX,
    )
    val dragY: Float = BodyForceHelper.drag(
        dragCoefficient = dragCoefficient,
        velocity = velocityY,
        referenceArea = areaY,
    )

    val drivingX: Float = BodyForceHelper.driving(
        drivingTarget = drivingTargetX,
        position = x,
        velocity = velocityX,
        mass = mass,
    )
    val drivingY: Float = BodyForceHelper.driving(
        drivingTarget = drivingTargetY,
        position = y,
        velocity = velocityY,
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

    val vector2: Vector2 = Vector2(x, y)
    val rectangle: Rectangle = Rectangle(left, bottom, width, height)

    fun distance(other: BodyData): Float {
        return vector2.dst(other.vector2)
    }

    fun containsCenter(other: BodyData): Boolean {
        return rectangle.contains(other.vector2)
    }

    //*****************************************************************************************************************

    data class TextureRegionData(
        val anim: BodyConfig.Anim,
        val stateTime: Float,
        val isFacingRight: Boolean,
    ) {
        val canAnimChange: Boolean = anim.type.canInterrupt || stateTime >= anim.duration

        val textureRegion: TextureRegion = anim.getFrame(stateTime)

        val width: Float = textureRegion.regionWidth.toFloat().unitToMeter
        val height: Float = textureRegion.regionHeight.toFloat().unitToMeter

        val isFlipX: Boolean = if (anim.type == BodyConfig.AnimType.TURN) {
            !isFacingRight
        } else {
            isFacingRight
        }
    }

    val alpha: Float = if (disappearAct == null || disappearAct.time >= 0f) {
        1f
    } else {
        ((DisappearAct.DISAPPEAR_DURATION + disappearAct.time) / DisappearAct.DISAPPEAR_DURATION).minMax(0f, 1f)
    }

    //*****************************************************************************************************************

    val canRemove: Boolean = disappearAct != null && disappearAct.time <= -DisappearAct.DISAPPEAR_DURATION

    //*****************************************************************************************************************

    fun update(body: Body, delta: Float): BodyData {
        val nextTouchAct = BodyActHelper.nextTouchAct(
            configTouchAct = config.touchAct,
            touchPoint = body.tank.touchPoint,
        )

        val nextSwimActX = BodyActHelper.nextSwimAct(
            enabled = nextTouchAct == null,
            configSwimAct = config.swimActX,
            swimAct = swimActX,
            tankSize = tankWidth,
            containDrivingTarget = containDrivingTargetX,
            delta = delta,
        )
        val nextSwimActY = BodyActHelper.nextSwimAct(
            enabled = nextTouchAct == null,
            configSwimAct = config.swimActY,
            swimAct = swimActY,
            tankSize = tankHeight,
            containDrivingTarget = containDrivingTargetY,
            delta = delta,
        )

        val nextDisappearAct = BodyActHelper.nextDisappearAct(
            configDisappearAct = config.disappearAct,
            disappearAct = disappearAct,
            data = this,
            delta = delta,
        )

        val nextEatAct = BodyActHelper.nextEatAct(
            configEatAct = config.eatAct,
            data = this,
            body = body,
        )

        val nextVelocityX = BodyForceHelper.nextVelocity(
            velocity = velocityX,
            acceleration = accelerationX,
            isInsideLeftOrBottom = isInsideLeft,
            isInsideRightOrTop = isInsideRight,
            delta = delta,
        )
        val nextVelocityY = BodyForceHelper.nextVelocity(
            velocity = velocityY,
            acceleration = accelerationY,
            isInsideLeftOrBottom = isInsideBottom,
            isInsideRightOrTop = true,
            delta = delta,
        )

        val nextX = BodyForceHelper.nextPosition(
            position = x,
            velocity = nextVelocityX,
            minPosition = minX,
            maxPosition = maxX,
            delta = delta,
        )
        val nextY = BodyForceHelper.nextPosition(
            position = y,
            velocity = nextVelocityY,
            minPosition = minY,
            maxPosition = maxY,
            delta = delta,
        )

        val nextExpectedIsFacingRight = if (config.turnAct == null) {
            false
        } else {
            when (drivingX.direction) {
                Direction.ZERO -> when (velocityX.direction) {
                    Direction.ZERO -> expectedIsFacingRight
                    Direction.POSITIVE -> true
                    Direction.NEGATIVE -> false
                }
                Direction.POSITIVE -> true
                Direction.NEGATIVE -> false
            }
        }

        val nextTextureRegionData = if (textureRegionData.canAnimChange) {
            if (textureRegionData.isFacingRight != expectedIsFacingRight) {
                TextureRegionData(
                    anim = config.turnAct?.anim ?: config.anim,
                    stateTime = 0f,
                    isFacingRight = expectedIsFacingRight,
                )
            } else {
                TextureRegionData(
                    anim = config.anim,
                    stateTime = if (textureRegionData.anim.type == config.anim.type) {
                        textureRegionData.stateTime + delta
                    } else {
                        0f
                    },
                    isFacingRight = expectedIsFacingRight,
                )
            }
        } else {
            TextureRegionData(
                anim = textureRegionData.anim,
                stateTime = textureRegionData.stateTime + delta,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        return copy(
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
        )
    }

    fun act(body: Body) {
        body.setSize(textureRegionData.width, textureRegionData.height)
        body.setPosition(x, y, Align.center)
    }

    fun draw(body: Body, batch: Batch, parentAlpha: Float) {
        val oldColor = batch.color.cpy()
        batch.color = batch.color.cpy().also { it.a = alpha * parentAlpha }
        batch.draw(
            textureRegionData.textureRegion.texture,
            body.x,
            body.y,
            body.originX,
            body.originY,
            body.width,
            body.height,
            body.scaleX,
            body.scaleY,
            body.rotation,
            textureRegionData.textureRegion.regionX,
            textureRegionData.textureRegion.regionY,
            textureRegionData.textureRegion.regionWidth,
            textureRegionData.textureRegion.regionHeight,
            textureRegionData.isFlipX,
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
        fun create(
            tank: Tank,
            params: BodyParams,
        ): BodyData {
            val config: BodyConfig = game.config.body.getValue(params.type.serializedName)

            return BodyData(
                type = params.type,
                id = "${UUID.randomUUID()}",
                tankWidth = tank.width,
                tankHeight = tank.height,
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
                textureRegionData = TextureRegionData(
                    anim = config.anim,
                    stateTime = 0f,
                    isFacingRight = false,
                ),
            )
        }
    }
}
