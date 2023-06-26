package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.direction

object BodyStatusHelper {
    fun nextStatus(
        data: BodyData,
        status: BodyStatus,
        input: BodyInput,
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

        val nextEatAct = BodyActHelper.nextEatAct(
            tank = data.body.tank,
            configEatAct = data.body.config.eatAct,
            hungerStatus = data.hungerStatus,
            data = data,
            input = input,
        )

        val hasEatDrivingTarget = nextEatAct?.drivingTargetX != null || nextEatAct?.drivingTargetY != null

        val nextTouchAct = BodyActHelper.nextTouchAct(
            enabled = !hasEatDrivingTarget,
            tank = data.body.tank,
            configTouchAct = data.body.config.touchAct,
            isDying = data.hungerStatus == HungerStatus.DYING,
        )

        val hasTouchDrivingTarget = nextTouchAct != null

        val nextSwimActX = BodyActHelper.nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = data.body.config.swimActX,
            swimAct = status.swimActX,
            tankSize = data.body.tank.width,
            leftOrBottom = data.left,
            rightOrTop = data.right,
            drivingTarget = data.status.drivingTargetX,
            input = input,
            isDying = data.hungerStatus == HungerStatus.DYING,
        )
        val nextSwimActY = BodyActHelper.nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = data.body.config.swimActY,
            swimAct = status.swimActY,
            tankSize = data.body.tank.height,
            leftOrBottom = data.bottom,
            rightOrTop = data.top,
            drivingTarget = data.status.drivingTargetY,
            input = input,
            isDying = data.hungerStatus == HungerStatus.DYING,
        )

        val nextHealth = BodyActHelper.nextHealth(
            configHealth = data.body.config.health,
            health = status.health,
            input = input,
        )

        val nextHunger = BodyActHelper.nextHunger(
            configHunger = data.body.config.hunger,
            hunger = status.hunger,
            eatAct = nextEatAct,
            input = input,
        )

        val nextHungerStatus = data.body.config.hunger?.status(nextHunger)

        val nextDisappearAct = BodyActHelper.nextDisappearAct(
            canDisappear = status.textureRegionData.animationAction == BodyConfig.AnimationAction.DIE &&
                data.isAnimationFinished,
            disappearAct = status.disappearAct,
            data = data,
            input = input,
        )

        val nextDrivingTargetX: BodyStatus.DrivingTarget? = if (nextHungerStatus == HungerStatus.DYING) {
            null
        } else {
            nextEatAct?.drivingTargetX ?: nextTouchAct?.drivingTargetX ?: nextSwimActX?.drivingTarget
        }
        val nextDrivingTargetY: BodyStatus.DrivingTarget? = if (nextHungerStatus == HungerStatus.DYING) {
            null
        } else {
            nextEatAct?.drivingTargetY ?: nextTouchAct?.drivingTargetY ?: nextSwimActY?.drivingTarget
        }

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
            config = data.body.config,
            hasTurnAnimation = data.hasTurnAnimation,
            textureRegionData = status.textureRegionData,
            isAnimationFinished = data.isAnimationFinished,
            canAnimationActionChange = data.canAnimationActionChange,
            expectedIsFacingRight = status.expectedIsFacingRight,
            eatAct = nextEatAct,
            hungerStatus = nextHungerStatus,
            input = input,
        )

        return BodyStatus(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            x = nextX,
            y = nextY,
            swimActX = nextSwimActX,
            swimActY = nextSwimActY,
            health = nextHealth,
            hunger = nextHunger,
            disappearAct = nextDisappearAct,
            drivingTargetX = nextDrivingTargetX,
            drivingTargetY = nextDrivingTargetY,
            expectedIsFacingRight = nextExpectedIsFacingRight,
            textureRegionData = nextTextureRegionData,
        )
    }
}
