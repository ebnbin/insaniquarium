package dev.ebnbin.insaniquarium.body

import dev.ebnbin.insaniquarium.tank.TankData

data class BodyData(
    val tankData: TankData,
    val type: BodyType,
    val velocityX: Float,
    val velocityY: Float,
    val position: BodyPosition,
    val swimBehaviorX: BodyHelper.SwimBehavior?,
    val swimBehaviorY: BodyHelper.SwimBehavior?,
) {
    private val def: BodyDef = type.def

    val width: Float = def.width
    val height: Float = def.height

    val halfWidth: Float = width / 2f
    val halfHeight: Float = height / 2f

    val minX: Float = halfWidth
    val maxX: Float = tankData.width - halfWidth
    val minY: Float = halfHeight
    val maxY: Float = Float.MAX_VALUE

    val left: Float = position.x - halfWidth
    val right: Float = left + width
    val bottom: Float = position.y - halfHeight
    val top: Float = bottom + height

    val isInsideLeft: Boolean = left > 0f
    val isInsideRight: Boolean = right < tankData.width
    val isInsideBottom: Boolean = bottom > 0f
    val isInsideTop: Boolean = true

    val area: Float = width * height

    val areaX: Float = height * height
    val areaY: Float = width * width

    val areaInWater: Float = ((tankData.height - bottom) / height).coerceIn(0f, 1f) * area

    val density: Float = def.density

    val mass: Float = area * density

    val gravityY: Float = BodyHelper.gravityY(mass)

    val buoyancyY: Float = BodyHelper.buoyancyY(areaInWater)

    val dragX: Float = BodyHelper.drag(
        velocity = velocityX,
        dragCoefficient = def.dragCoefficient,
        crossSectionalArea = areaX,
    )
    val dragY: Float = BodyHelper.drag(
        velocity = velocityY,
        dragCoefficient = def.dragCoefficient,
        crossSectionalArea = areaY,
    )

    val drivingTargetX: BodyDrivingTarget? = swimBehaviorX?.drivingTarget
    val drivingTargetY: BodyDrivingTarget? = swimBehaviorY?.drivingTarget

    val drivingForceX: Float = BodyHelper.drivingForce(
        drivingAcceleration = def.drivingAccelerationX,
        drivingTarget = drivingTargetX,
        position = position.x,
        mass = mass,
        velocity = velocityX,
    )
    val drivingForceY: Float = BodyHelper.drivingForce(
        drivingAcceleration = def.drivingAccelerationY,
        drivingTarget = drivingTargetY,
        position = position.y,
        mass = mass,
        velocity = velocityY,
    )

    val normalReactionForceX: Float = BodyHelper.force(dragX, drivingForceX)
    val normalReactionForceY: Float = BodyHelper.force(gravityY, drivingForceY, buoyancyY, dragY)

    val normalForceX: Float = BodyHelper.normalForce(
        isInsideLeftOrBottom = isInsideLeft,
        isInsideRightOrTop = isInsideRight,
        normalReactionForce = normalReactionForceX
    )
    val normalForceY: Float = BodyHelper.normalForce(
        isInsideLeftOrBottom = isInsideBottom,
        isInsideRightOrTop = isInsideTop,
        normalReactionForce = normalReactionForceY
    )

    val frictionReactionForceX: Float = BodyHelper.force(normalReactionForceX, normalForceX)

    val frictionX: Float = BodyHelper.frictionX(
        normalReactionForceY = normalReactionForceY,
        frictionCoefficient = def.frictionCoefficient,
        isInsideBottom = isInsideBottom,
        frictionReactionForceX = frictionReactionForceX,
        velocityX = velocityX,
    )

    val forceX: Float = BodyHelper.force(frictionReactionForceX, frictionX)
    val forceY: Float = BodyHelper.force(normalReactionForceY, normalForceY)

    val accelerationX: Float = BodyHelper.acceleration(forceX, mass)
    val accelerationY: Float = BodyHelper.acceleration(forceY, mass)

    fun tick(
        tickDelta: Float,
    ): BodyData {
        val nextVelocityX = BodyHelper.velocity(
            velocity = velocityX,
            acceleration = accelerationX,
            delta = tickDelta,
            isInsideLeftOrBottom = isInsideLeft,
            isInsideRightOrTop = isInsideRight,
            friction = frictionX,
        )
        val nextVelocityY = BodyHelper.velocity(
            velocity = velocityY,
            acceleration = accelerationY,
            delta = tickDelta,
            isInsideLeftOrBottom = isInsideBottom,
            isInsideRightOrTop = isInsideTop,
            friction = 0f,
        )
        val nextX = BodyHelper.position(
            position = position.x,
            velocity = nextVelocityX,
            delta = tickDelta,
            minPosition = minX,
            maxPosition = maxX,
        )
        val nextY = BodyHelper.position(
            position = position.y,
            velocity = nextVelocityY,
            delta = tickDelta,
            minPosition = minY,
            maxPosition = maxY,
        )
        return copy(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            position = BodyPosition(
                x = nextX,
                y = nextY,
            ),
            swimBehaviorX = BodyHelper.swimBehavior(
                swimBehavior = swimBehaviorX,
                tankSize = tankData.width,
                leftOrBottom = left,
                rightOrTop = right,
                defSwimBehavior = def.swimBehaviorX,
            ),
            swimBehaviorY = BodyHelper.swimBehavior(
                swimBehavior = swimBehaviorY,
                tankSize = tankData.height,
                leftOrBottom = bottom,
                rightOrTop = top,
                defSwimBehavior = def.swimBehaviorY,
            ),
        )
    }
}
