package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.insaniquarium.tank.Tank
import kotlin.math.max

object BodyStatusHelper {
    fun createStatus(
        tank: Tank,
        params: BodyParams,
        config: BodyConfig,
    ): BodyStatus {
        return BodyStatus(
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
    }

    fun nextStatus(
        data: BodyData,
        config: BodyConfig,
        status: BodyStatus,
        input: BodyInput?,
    ): BodyStatus {
        val nextVelocityX = BodyForceHelper.nextVelocity(
            velocity = status.velocityX,
            acceleration = data.accelerationX,
            isInsideLeftOrBottom = data.isInsideLeft,
            isInsideRightOrTop = data.isInsideRight,
            input = input,
        )
        val nextVelocityY = BodyForceHelper.nextVelocity(
            velocity = status.velocityY,
            acceleration = data.accelerationY,
            isInsideLeftOrBottom = data.isInsideBottom,
            isInsideRightOrTop = true,
            input = input,
        )

        val nextX = BodyForceHelper.nextPosition(
            position = status.x,
            velocity = nextVelocityX,
            minPosition = data.minX,
            maxPosition = data.maxX,
            input = input,
        )
        val nextY = BodyForceHelper.nextPosition(
            position = status.y,
            velocity = nextVelocityY,
            minPosition = data.minY,
            maxPosition = data.maxY,
            input = input,
        )

        val nextTouchAct = BodyActHelper.nextTouchAct(
            configTouchAct = config.touchAct,
            input = input,
            isDying = data.isDying,
        )

        val nextSwimActX = BodyActHelper.nextSwimAct(
            enabled = nextTouchAct == null,
            configSwimAct = config.swimActX,
            swimAct = status.swimActX,
            tankSize = data.tankWidth,
            containDrivingTarget = data.containDrivingTargetX,
            input = input,
            isDying = data.isDying,
        )
        val nextSwimActY = BodyActHelper.nextSwimAct(
            enabled = nextTouchAct == null,
            configSwimAct = config.swimActY,
            swimAct = status.swimActY,
            tankSize = data.tankHeight,
            containDrivingTarget = data.containDrivingTargetY,
            input = input,
            isDying = data.isDying,
        )

        val nextDisappearAct = BodyActHelper.nextDisappearAct(
            canDisappear = config.canDisappear ||
                (status.textureRegionData.animationType == BodyConfig.AnimationType.DIE && data.isAnimationFinished),
            disappearAct = status.disappearAct,
            data = data,
            input = input,
        )

        val nextEatAct = BodyActHelper.nextEatAct(
            configEatAct = config.eatAct,
            eatAct = status.eatAct,
            data = data,
            input = input,
            isDying = data.isDying,
        )

        val nextExpectedIsFacingRight = if (data.hasTurnAnimation) {
            when (data.drivingX.direction) {
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
            hasTurnAnimation = data.hasTurnAnimation,
            textureRegionData = status.textureRegionData,
            isAnimationFinished = data.isAnimationFinished,
            canAnimationActionChange = data.canAnimationActionChange,
            expectedIsFacingRight = status.expectedIsFacingRight,
            eatAct = nextEatAct,
            isDying = data.isDying,
            input = input,
        )

        val nextHealth = if (input == null) {
            status.health
        } else {
            if (status.health == BodyConfig.HEALTH_MAX) {
                status.health
            } else {
                max(0f, status.health - input.damage)
            }
        }

        return BodyStatus(
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
    }
}
