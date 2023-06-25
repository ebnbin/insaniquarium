package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.World
import kotlin.math.max

object BodyActHelper {
    fun nextTouchAct(
        configTouchAct: BodyConfig.TouchAct?,
        input: BodyInput?,
        isDying: Boolean,
    ): BodyStatus.TouchAct? {
        if (input == null) {
            return null
        }
        if (configTouchAct == null) {
            return null
        }
        if (isDying) {
            return null
        }
        val touchPoint = input.body.tank.touchPoint ?: return null
        return BodyStatus.TouchAct(
            drivingTargetX = BodyStatus.DrivingTarget(
                position = touchPoint.x,
                acceleration = configTouchAct.accelerationX,
            ),
            drivingTargetY = BodyStatus.DrivingTarget(
                position = touchPoint.y,
                acceleration = configTouchAct.accelerationY,
            ),
        )
    }

    fun nextSwimAct(
        enabled: Boolean,
        configSwimAct: BodyConfig.SwimAct?,
        swimAct: BodyStatus.SwimAct?,
        tankSize: Float,
        containDrivingTarget: Boolean,
        input: BodyInput?,
        isDying: Boolean,
    ): BodyStatus.SwimAct? {
        if (input == null) {
            return swimAct
        }
        if (!enabled) {
            return null
        }
        if (configSwimAct == null) {
            return null
        }
        if (isDying) {
            return null
        }

        fun createTargetingSwimAct(): BodyStatus.SwimAct {
            return BodyStatus.SwimAct(
                drivingTarget = BodyStatus.DrivingTarget(
                    position = Random.nextFloat(0f, tankSize),
                    acceleration = configSwimAct.acceleration,
                ),
                remainingTime = Float.MAX_VALUE,
            )
        }

        fun createIdlingSwimAct(): BodyStatus.SwimAct {
            return BodyStatus.SwimAct(
                drivingTarget = null,
                remainingTime = Random.nextFloat(
                    configSwimAct.idlingTimeRandomStart,
                    configSwimAct.idlingTimeRandomEnd,
                ),
            )
        }

        fun updateSwimAct(swimAct: BodyStatus.SwimAct): BodyStatus.SwimAct {
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
        canDisappear: Boolean,
        disappearAct: BodyStatus.DisappearAct?,
        data: BodyData,
        input: BodyInput?,
    ): BodyStatus.DisappearAct? {
        if (input == null) {
            return disappearAct
        }
        if (!canDisappear) {
            return null
        }
        return if (disappearAct == null) {
            val isNotInsideTank = if (data.density == World.DENSITY_WATER) {
                false
            } else if (data.density > World.DENSITY_WATER) {
                !data.isInsideBottom
            } else {
                data.insideTopPercent < 1f
            }
            if (isNotInsideTank) {
                BodyStatus.DisappearAct()
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
        hungerStatus: HungerStatus?,
        eatAct: BodyStatus.EatAct?,
        data: BodyData,
        input: BodyInput?,
    ): BodyStatus.EatAct? {
        if (input == null) {
            return eatAct
        }
        if (configEatAct == null) {
            return null
        }

        fun targetFood(): Body? {
            if (hungerStatus == HungerStatus.FULL || hungerStatus == HungerStatus.DYING) {
                return null
            }
            val foodSet = input.body.tank.findBodyByType(configEatAct.foods.keys)
            if (foodSet.isEmpty()) {
                return null
            }
            return foodSet.minBy {
                data.distance(it.data)
            }
        }

        var hungerDiff = 0f
        val targetFood = targetFood()
        val isTurning = data.status.textureRegionData.animationAction == BodyConfig.AnimationAction.TURN
        if (targetFood != null) {
            if (!isTurning && data.containsCenter(targetFood.data)) {
                val food = configEatAct.foods.getValue(targetFood.data.type)
                val foodBody = targetFood.act(
                    input = BodyInput(
                        body = targetFood,
                        damage = food.damagePerSecond * input.delta,
                    ),
                )
                if (foodBody.data.canRemove) {
                    hungerDiff = food.hunger
                }
            }
        }
        return BodyStatus.EatAct(
            drivingTargetX = if (targetFood == null) {
                null
            } else {
                BodyStatus.DrivingTarget(
                    position = targetFood.data.status.x,
                    acceleration = configEatAct.accelerationX,
                )
            },
            drivingTargetY = if (targetFood == null) {
                null
            } else {
                BodyStatus.DrivingTarget(
                    position = targetFood.data.status.y,
                    acceleration = configEatAct.accelerationY,
                )
            },
            canPlayEatAnimation = !isTurning && targetFood != null && data.overlaps(targetFood.data),
            hungerDiff = hungerDiff,
        )
    }

    fun nextHunger(
        configHunger: BodyConfig.Hunger?,
        hunger: Float?,
        eatAct: BodyStatus.EatAct?,
        input: BodyInput?,
    ): Float? {
        if (configHunger == null) {
            return null
        }
        if (input == null) {
            return hunger
        }
        if (hunger == null) {
            return configHunger.full
        }

        var nextHunger = hunger - configHunger.exhaustionPerSecond * input.delta
        nextHunger -= input.exhaustion
        if (eatAct != null) {
            nextHunger += eatAct.hungerDiff
        }
        return configHunger.minMax(nextHunger)
    }

    fun nextHealth(
        configHealth: BodyConfig.Health?,
        health: Float?,
        input: BodyInput?,
    ): Float? {
        if (input == null) {
            return health
        }
        if (configHealth == null) {
            return null
        }
        if (health == null) {
            return configHealth.full
        }
        return max(0f, health - input.damage)
    }
}
