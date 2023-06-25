package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.insaniquarium.tank.Tank

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
            health = config.health?.full,
            hunger = config.hunger?.full,
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
            isDying = data.hungerStatus == HungerStatus.DYING,
        )

        val nextSwimActX = BodyActHelper.nextSwimAct(
            enabled = nextTouchAct == null,
            configSwimAct = config.swimActX,
            swimAct = status.swimActX,
            tankSize = data.tankWidth,
            containDrivingTarget = data.containDrivingTargetX,
            input = input,
            isDying = data.hungerStatus == HungerStatus.DYING,
        )
        val nextSwimActY = BodyActHelper.nextSwimAct(
            enabled = nextTouchAct == null,
            configSwimAct = config.swimActY,
            swimAct = status.swimActY,
            tankSize = data.tankHeight,
            containDrivingTarget = data.containDrivingTargetY,
            input = input,
            isDying = data.hungerStatus == HungerStatus.DYING,
        )

        val nextDisappearAct = BodyActHelper.nextDisappearAct(
            canDisappear = status.textureRegionData.animationAction == BodyConfig.AnimationAction.DIE &&
                data.isAnimationFinished,
            disappearAct = status.disappearAct,
            data = data,
            input = input,
        )

        val nextEatAct = BodyActHelper.nextEatAct(
            configEatAct = config.eatAct,
            hungerStatus = data.hungerStatus,
            eatAct = status.eatAct,
            data = data,
            input = input,
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
            hungerStatus = data.hungerStatus,
            input = input,
        )

        val nextHealth = BodyActHelper.nextHealth(
            configHealth = config.health,
            health = status.health,
            input = input,
        )

        val nextHunger = BodyActHelper.nextHunger(
            configHunger = config.hunger,
            hunger = status.hunger,
            eatAct = nextEatAct,
            input = input,
        )

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
            hunger = nextHunger,
        )
    }
}
