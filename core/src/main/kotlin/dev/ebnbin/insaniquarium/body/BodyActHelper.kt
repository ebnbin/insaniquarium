package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.World

object BodyActHelper {
    fun nextTouchAct(
        configTouchAct: BodyConfig.TouchAct?,
        input: BodyInput?,
    ): BodyData.TouchAct? {
        if (input == null) {
            return null
        }
        if (configTouchAct == null) {
            return null
        }
        val touchPoint = input.body.tank.touchPoint ?: return null
        return BodyData.TouchAct(
            drivingTargetX = BodyData.DrivingTarget(
                position = touchPoint.x,
                acceleration = configTouchAct.accelerationX,
            ),
            drivingTargetY = BodyData.DrivingTarget(
                position = touchPoint.y,
                acceleration = configTouchAct.accelerationY,
            ),
        )
    }

    fun nextSwimAct(
        enabled: Boolean,
        configSwimAct: BodyConfig.SwimAct?,
        swimAct: BodyData.SwimAct?,
        tankSize: Float,
        containDrivingTarget: Boolean,
        input: BodyInput?,
    ): BodyData.SwimAct? {
        if (input == null) {
            return swimAct
        }
        if (!enabled) {
            return null
        }
        if (configSwimAct == null) {
            return null
        }

        fun createTargetingSwimAct(): BodyData.SwimAct {
            return BodyData.SwimAct(
                drivingTarget = BodyData.DrivingTarget(
                    position = Random.nextFloat(0f, tankSize),
                    acceleration = configSwimAct.acceleration,
                ),
                remainingTime = Float.MAX_VALUE,
            )
        }

        fun createIdlingSwimAct(): BodyData.SwimAct {
            return BodyData.SwimAct(
                drivingTarget = null,
                remainingTime = Random.nextFloat(
                    configSwimAct.idlingTimeRandomStart,
                    configSwimAct.idlingTimeRandomEnd,
                ),
            )
        }

        fun updateSwimAct(swimAct: BodyData.SwimAct): BodyData.SwimAct {
            return swimAct.copy(
                remainingTime = swimAct.remainingTime - input.delta,
            )
        }

        if (swimAct == null) {
            return if (Random.nextBoolean()) {
                createTargetingSwimAct()
            } else {
                createIdlingSwimAct()
            }
        }
        val isRemainingTimeUp = swimAct.remainingTime - input.delta <= 0f
        return if (swimAct.drivingTarget == null) {
            if (isRemainingTimeUp) {
                createTargetingSwimAct()
            } else {
                updateSwimAct(swimAct)
            }
        } else {
            if (isRemainingTimeUp || containDrivingTarget) {
                createIdlingSwimAct()
            } else {
                updateSwimAct(swimAct)
            }
        }
    }

    fun nextDisappearAct(
        configDisappearAct: BodyConfig.DisappearAct?,
        disappearAct: BodyData.DisappearAct?,
        data: BodyData,
        input: BodyInput?,
    ): BodyData.DisappearAct? {
        if (input == null) {
            return disappearAct
        }
        if (configDisappearAct == null) {
            return null
        }
        return if (disappearAct == null) {
            val canDisappear = if (data.density == World.DENSITY_WATER) {
                false
            } else if (data.density > World.DENSITY_WATER) {
                !data.isInsideBottom
            } else {
                data.insideTopPercent < 1f
            }
            if (canDisappear) {
                BodyData.DisappearAct(
                    time = configDisappearAct.delay,
                )
            } else {
                null
            }
        } else {
            disappearAct.copy(
                time = disappearAct.time - input.delta,
            )
        }
    }

    fun nextEatAct(
        configEatAct: BodyConfig.EatAct?,
        data: BodyData,
        input: BodyInput?,
    ): BodyData.EatAct? {
        if (input == null) {
            return null
        }
        if (configEatAct == null) {
            return null
        }
        val foodSet = input.body.tank.findBodyByType(configEatAct.foodTypeSet)
        if (foodSet.isEmpty()) {
            return null
        }
        val food = foodSet.minBy {
            data.distance(it.data)
        }
        val isTurning = data.textureRegionData.animationAction == BodyConfig.AnimationAction.TURN
        if (!isTurning && data.containsCenter(food.data)) {
            food.act(
                input = BodyInput(
                    body = food,
                    damage = configEatAct.damagePerSecond * input.delta,
                ),
            )
        }
        return BodyData.EatAct(
            drivingTargetX = BodyData.DrivingTarget(
                position = food.data.x,
                acceleration = configEatAct.accelerationX,
            ),
            drivingTargetY = BodyData.DrivingTarget(
                position = food.data.y,
                acceleration = configEatAct.accelerationY,
            ),
            canPlayEatAnimation = !isTurning && data.overlaps(food.data),
        )
    }
}
