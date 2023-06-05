package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.utils.World
import dev.ebnbin.gdx.utils.minMax
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.aquarium.Tank
import dev.ebnbin.insaniquarium.game

data class BodyData(
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
) {
    data class TouchAct(
        val drivingTargetX: DrivingTarget,
        val drivingTargetY: DrivingTarget,
    )

    data class SwimAct(
        val drivingTarget: DrivingTarget?,
        val remainingTime: Float,
    )

    //*****************************************************************************************************************

    data class DrivingTarget(
        val position: Float,
        val acceleration: Float,
    )

    val drivingTargetX: DrivingTarget? = touchAct?.drivingTargetX ?: swimActX?.drivingTarget
    val drivingTargetY: DrivingTarget? = touchAct?.drivingTargetY ?: swimActY?.drivingTarget

    //*****************************************************************************************************************

    val config: BodyConfig = game.config.body.getValue(id)

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

    val area: Float = width * height

    val areaX: Float = height * depth
    val areaY: Float = width * depth

    val volume: Float = area * depth

    val density: Float = config.density ?: World.DENSITY_WATER

    val mass: Float = volume * density

    //*****************************************************************************************************************

    val dragCoefficient: Float = config.dragCoefficient ?: World.DEFAULT_DRAG_COEFFICIENT

    val gravityX: Float = 0f
    val gravityY: Float = BodyForceHelper.gravityY(
        mass = mass,
    )

    val buoyancyX: Float = 0f
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
        mass = mass,
    )
    val drivingY: Float = BodyForceHelper.driving(
        drivingTarget = drivingTargetY,
        position = y,
        mass = mass,
    )

    val normalReactionX: Float = gravityX + buoyancyX + dragX + drivingX
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

    private val textureRegion: TextureRegion =
        game.assets.texture.getValue(config.assetId).getTextureRegionList().first()

    private val actorWidth: Float = textureRegion.regionWidth.toFloat().unitToMeter
    private val actorHeight: Float = textureRegion.regionHeight.toFloat().unitToMeter

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

        return copy(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            x = nextX,
            y = nextY,
            touchAct = nextTouchAct,
            swimActX = nextSwimActX,
            swimActY = nextSwimActY,
        )
    }

    fun act(body: Body) {
        body.setSize(actorWidth, actorHeight)
        body.setPosition(x, y, Align.center)
    }

    fun draw(body: Body, batch: Batch, parentAlpha: Float) {
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
            textureRegion.isFlipX,
            textureRegion.isFlipY,
        )
    }

    //*****************************************************************************************************************

    fun actDebug(body: Body, delta: Float) {
        BodyDebugHelper.act(this, body, delta)
    }

    fun drawDebug(body: Body, shapes: ShapeRenderer) {
        BodyDebugHelper.draw(this, body, shapes)
    }

    //*****************************************************************************************************************

    companion object {
        fun create(
            tank: Tank,
            id: String,
            initX: Float? = null,
            initY: Float? = null,
        ): BodyData {
            return BodyData(
                id = id,
                tankWidth = tank.width,
                tankHeight = tank.height,
                velocityX = 0f,
                velocityY = 0f,
                x = initX ?: (tank.width / 2f),
                y = initY ?: (tank.height / 2f),
                touchAct = null,
                swimActX = null,
                swimActY = null,
            )
        }
    }
}
