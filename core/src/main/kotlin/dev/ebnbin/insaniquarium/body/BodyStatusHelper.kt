package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.direction

object BodyStatusHelper {
    fun nextStatus(
        data: BodyData,
        config: BodyConfig,
        status: BodyStatus,
        input: BodyInput,
    ): BodyStatus {
        if (input.skipUpdate) {
            return status
        }

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

        val hasNextTouchAct = data.body.tank.touchPoint != null &&
            config.touchAct != null &&
            data.hungerStatus != HungerStatus.DYING

        val nextSwimActX = BodyActHelper.nextSwimAct(
            enabled = !hasNextTouchAct,
            configSwimAct = config.swimActX,
            swimAct = status.swimActX,
            tankSize = data.body.tank.width,
            containDrivingTarget = data.containDrivingTargetX,
            input = input,
            isDying = data.hungerStatus == HungerStatus.DYING,
        )
        val nextSwimActY = BodyActHelper.nextSwimAct(
            enabled = !hasNextTouchAct,
            configSwimAct = config.swimActY,
            swimAct = status.swimActY,
            tankSize = data.body.tank.height,
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
            tank = data.body.tank,
            configEatAct = config.eatAct,
            hungerStatus = data.hungerStatus,
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
