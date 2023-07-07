package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.World
import dev.ebnbin.gdx.utils.XY
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.magnitude
import dev.ebnbin.gdx.utils.unitToMeter
import kotlin.math.abs

data class BodyBox(
    val body: Body,
    val params: Params,
    val status: Status,
) {
    data class Params(
        val drivingTargetX: BodyDrivingTarget?,
        val drivingTargetY: BodyDrivingTarget?,
    )

    data class Status(
        val velocityX: Float = 0f,
        val velocityY: Float = 0f,
        val x: Float = 0f,
        val y: Float = 0f,
    )

    private val velocityX: Float = status.velocityX
    private val velocityY: Float = status.velocityY

    val x: Float = status.x
    val y: Float = status.y

    private val halfWidth: Float = body.config.box.width / 2f
    private val halfHeight: Float = body.config.box.height / 2f

    private val minX: Float = halfWidth
    private val maxX: Float = body.delegate.tankWidth - halfWidth

    private val minY: Float = halfHeight
    private val maxY: Float = Float.MAX_VALUE

    private val left: Float = x - halfWidth
    private val right: Float = left + body.config.box.width
    private val bottom: Float = y - halfHeight
    private val top: Float = bottom + body.config.box.height

    private val isInsideLeft: Boolean = left > 0f
    private val isInsideRight: Boolean = right < body.delegate.tankWidth
    private val isInsideBottom: Boolean = bottom > 0f

    /**
     * Percent of body inside water.
     */
    private val insideTopPercent: Float = ((body.config.box.height + body.delegate.tankHeight - top) /
        body.config.box.height).coerceIn(0f, 1f)

    private val area: Float = body.config.box.width * body.config.box.height

    //*****************************************************************************************************************

    private val halfDepth: Float = body.config.box.depth / 2f

    private val depthLeft: Float = x - halfDepth
    private val depthBottom: Float = y - halfDepth

    private val areaX: Float = body.config.box.height * body.config.box.depth
    private val areaY: Float = body.config.box.width * body.config.box.depth

    private val volume: Float = area * body.config.box.depth

    private val mass: Float = volume * body.config.box.density

    //*****************************************************************************************************************

    private val gravityY: Float = BodyForceHelper.gravityY(
        mass = mass,
    )

    private val buoyancyY: Float = BodyForceHelper.buoyancyY(
        volume = volume,
        insideTopPercent = insideTopPercent,
    )

    private val dragX: Float = BodyForceHelper.drag(
        dragCoefficient = body.config.box.dragCoefficient,
        velocity = velocityX,
        referenceArea = areaX,
    )
    private val dragY: Float = BodyForceHelper.drag(
        dragCoefficient = body.config.box.dragCoefficient,
        velocity = velocityY,
        referenceArea = areaY,
    )

    private val drivingX: Float = BodyForceHelper.driving(
        drivingTarget = params.drivingTargetX,
        position = x,
        velocity = velocityX,
        mass = mass,
    )
    private val drivingY: Float = BodyForceHelper.driving(
        drivingTarget = params.drivingTargetY,
        position = y,
        velocity = velocityY,
        mass = mass,
    )

    private val frictionReactionX: Float = dragX + drivingX
    private val frictionReactionY: Float = gravityY + buoyancyY + dragY + drivingY

    private val normalForFrictionX: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideLeft,
        isInsideRightOrTop = isInsideRight,
        normalReaction = frictionReactionX,
    )
    private val normalForFrictionY: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideBottom,
        isInsideRightOrTop = true,
        normalReaction = frictionReactionY,
    )

    private val waterStaticFrictionMagnitude: Float = BodyForceHelper.staticFrictionMagnitude(
        frictionCoefficient = body.config.box.waterFrictionCoefficient,
        normalMagnitude = buoyancyY.magnitude,
        isNormalValid = true,
    )

    private val bottomStaticFrictionMagnitude: Float = BodyForceHelper.staticFrictionMagnitude(
        frictionCoefficient = body.config.box.bottomFrictionCoefficient,
        normalMagnitude = normalForFrictionY.magnitude,
        isNormalValid = !isInsideBottom && normalForFrictionY > 0f,
    )

    private val leftRightStaticFrictionMagnitude: Float = BodyForceHelper.staticFrictionMagnitude(
        frictionCoefficient = body.config.box.leftRightFrictionCoefficient,
        normalMagnitude = normalForFrictionX.magnitude,
        isNormalValid = !isInsideLeft && normalForFrictionX > 0f || !isInsideRight && normalForFrictionX < 0f,
    )

    private val staticFrictionMagnitudeX: Float = waterStaticFrictionMagnitude + bottomStaticFrictionMagnitude
    private val staticFrictionMagnitudeY: Float = waterStaticFrictionMagnitude + leftRightStaticFrictionMagnitude

    private val frictionX: Float = BodyForceHelper.friction(
        velocity = velocityX,
        staticFrictionMagnitude = staticFrictionMagnitudeX,
        frictionReaction = frictionReactionX,
    )
    private val frictionY: Float = BodyForceHelper.friction(
        velocity = velocityY,
        staticFrictionMagnitude = staticFrictionMagnitudeY,
        frictionReaction = frictionReactionY,
    )

    private val normalReactionX: Float = frictionReactionX + frictionX
    private val normalReactionY: Float = frictionReactionY + frictionY

    private val normalX: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideLeft,
        isInsideRightOrTop = isInsideRight,
        normalReaction = normalReactionX,
    )
    private val normalY: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideBottom,
        isInsideRightOrTop = true,
        normalReaction = normalReactionY,
    )

    private val forceX: Float = normalReactionX + normalX
    private val forceY: Float = normalReactionY + normalY

    private val accelerationX: Float = BodyForceHelper.acceleration(
        force = forceX,
        mass = mass,
    )
    private val accelerationY: Float = BodyForceHelper.acceleration(
        force = forceY,
        mass = mass,
    )

    //*****************************************************************************************************************

    val isSinkingOrFloatingOutsideWater: Boolean = when {
        body.config.box.density == World.DENSITY_WATER -> false
        body.config.box.density > World.DENSITY_WATER -> !isInsideBottom
        else -> insideTopPercent < 1f
    }

    val expectedDirection: Direction = drivingX.direction.takeIf { it != Direction.ZERO } ?: velocityX.direction

    fun act(delta: Float, params: Params): BodyBox {
        val nextStatus = nextStatus(delta)
        return copy(
            params = params,
            status = nextStatus,
        )
    }

    fun nextStatus(delta: Float): Status {
        val nextVelocityX = nextVelocityX(delta)
        val nextVelocityY = nextVelocityY(delta)
        val nextX = nextX(delta)
        val nextY = nextY(delta)
        return Status(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            x = nextX,
            y = nextY,
        )
    }

    private fun nextVelocityX(delta: Float): Float {
        return BodyForceHelper.nextVelocity(
            velocity = velocityX,
            acceleration = accelerationX,
            isInsideLeftOrBottom = isInsideLeft,
            isInsideRightOrTop = isInsideRight,
            friction = frictionX,
            delta = delta,
        )
    }

    private fun nextVelocityY(delta: Float): Float {
        return BodyForceHelper.nextVelocity(
            velocity = velocityY,
            acceleration = accelerationY,
            isInsideLeftOrBottom = isInsideBottom,
            isInsideRightOrTop = true,
            friction = frictionY,
            delta = delta,
        )
    }

    private fun nextX(delta: Float): Float {
        return BodyForceHelper.nextPosition(
            position = x,
            velocity = velocityX, // TODO nextVelocityX
            minPosition = minX,
            maxPosition = maxX,
            delta = delta,
        )
    }

    private fun nextY(delta: Float): Float {
        return BodyForceHelper.nextPosition(
            position = y,
            velocity = velocityY, // TODO nextVelocityY
            minPosition = minY,
            maxPosition = maxY,
            delta = delta,
        )
    }

    //*****************************************************************************************************************

    private val vector2: Vector2 = Vector2(x, y)
    private val rectangle: Rectangle = Rectangle(left, bottom, body.config.box.width, body.config.box.height)

    fun distance(other: BodyBox): Float {
        return vector2.dst(other.vector2)
    }

    fun hit(touchPoint: Point): Boolean {
        return rectangle.contains(touchPoint.x, touchPoint.y)
    }

    fun relation(other: BodyBox?): BodyRelation {
        if (other == null) {
            return BodyRelation.DISJOINT
        }
        if (rectangle.contains(other.vector2)) {
            return BodyRelation.CONTAIN_CENTER
        }
        if (rectangle.overlaps(other.rectangle)) {
            return BodyRelation.OVERLAP
        }
        return BodyRelation.DISJOINT
    }

    val reachDrivingTargetX: Boolean = params.drivingTargetX?.position?.let { it in left..right } ?: false
    val reachDrivingTargetY: Boolean = params.drivingTargetY?.position?.let { it in bottom..top } ?: false

    val awayFromDrivingTargetX: Boolean = params.drivingTargetX != null &&
        abs(params.drivingTargetX.position - x) >= body.config.box.width / 12f

    //*****************************************************************************************************************

    fun postAct() {
        val textureRegion = body.config.renderer.animations.swim.getTextureRegion(0f)
        body.delegate.setSize(
            textureRegion.regionWidth.toFloat().unitToMeter,
            textureRegion.regionHeight.toFloat().unitToMeter,
        )
        body.delegate.setPosition(x, y)
    }

    //*****************************************************************************************************************

    fun actDebug() {
        baseGame.putLog("size            ") {
            "${body.config.box.width.devText()},${body.config.box.height.devText()}"
        }
        baseGame.putLog("lrbt            ") {
            "${left.devText()},${right.devText()},${bottom.devText()},${top.devText()}"
        }
        baseGame.putLog("depth           ") {
            body.config.box.depth.devText()
        }
        baseGame.putLog("density         ") {
            body.config.box.density.devText()
        }
        baseGame.putLog("gravity,buoyancy") {
            "${gravityY.devText(XY.Y)},${buoyancyY.devText(XY.Y)}"
        }
        baseGame.putLog("drag            ") {
            "${dragX.devText(XY.X)},${dragY.devText(XY.Y)}"
        }
        baseGame.putLog("driving         ") {
            "${drivingX.devText(XY.X)},${drivingY.devText(XY.Y)}"
        }
        baseGame.putLog("frictionReaction") {
            "${frictionReactionX.devText(XY.X)},${frictionReactionY.devText(XY.Y)}"
        }
        baseGame.putLog("normalForFriction") {
            "${normalForFrictionX.devText(XY.X)},${normalForFrictionY.devText(XY.Y)}"
        }
        baseGame.putLog("waterStaticFrictionMagnitude") {
            waterStaticFrictionMagnitude.devText()
        }
        baseGame.putLog("bottomStaticFrictionMagnitude") {
            bottomStaticFrictionMagnitude.devText()
        }
        baseGame.putLog("leftRightStaticFrictionMagnitude") {
            leftRightStaticFrictionMagnitude.devText()
        }
        baseGame.putLog("staticFrictionMagnitude") {
            "${staticFrictionMagnitudeX.devText(XY.X)},${staticFrictionMagnitudeY.devText(XY.Y)}"
        }
        baseGame.putLog("friction        ") {
            "${frictionX.devText(XY.X)},${frictionY.devText(XY.Y)}"
        }
        baseGame.putLog("normalReaction  ") {
            "${normalReactionX.devText(XY.X)},${normalReactionY.devText(XY.Y)}"
        }
        baseGame.putLog("normal          ") {
            "${normalX.devText(XY.X)},${normalY.devText(XY.Y)}"
        }
        baseGame.putLog("force           ") {
            "${forceX.devText(XY.X)},${forceY.devText(XY.Y)}"
        }
        baseGame.putLog("acceleration    ") {
            "${accelerationX.devText(XY.X)},${accelerationY.devText(XY.Y)}"
        }
        baseGame.putLog("velocity        ") {
            "${velocityX.devText(XY.X)},${velocityY.devText(XY.Y)}"
        }
        baseGame.putLog("position        ") {
            "${x.devText()},${y.devText()}"
        }
    }

    fun drawDebug(shapes: ShapeRenderer) {
        shapes.rect(left, bottom, body.config.box.width, body.config.box.height)
        shapes.line(left, bottom, right, top)
        shapes.line(left, top, right, bottom)
        shapes.rect(depthLeft, bottom, body.config.box.depth, body.config.box.height)
        shapes.rect(left, depthBottom, body.config.box.width, body.config.box.depth)
        params.drivingTargetX?.let {
            shapes.line(it.position, 0f, it.position, body.delegate.tankHeight)
        }
        params.drivingTargetY?.let {
            shapes.line(0f, it.position, body.delegate.tankWidth, it.position)
        }
    }
}
