package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.World
import kotlin.math.max
import kotlin.math.min

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
        eatAct: BodyStatus.EatAct?,
        data: BodyData,
        input: BodyInput?,
        isDying: Boolean,
    ): BodyStatus.EatAct? {
        if (input == null) {
            return eatAct
        }
        if (configEatAct == null) {
            return null
        }

        val prevEatAct = eatAct ?: BodyStatus.EatAct(
            drivingTargetX = null,
            drivingTargetY = null,
            canPlayEatAnimation = false,
            hunger = configEatAct.fullHunger,
        )

        val isNotFull = configEatAct.fullHunger == 0f || prevEatAct.hunger < configEatAct.fullHunger

        fun targetFood(): Body? {
            if (isDying) {
                return null
            }
            if (!isNotFull) {
                return null
            }
            require(configEatAct.foods.isNotEmpty())
            val foodSet = input.body.tank.findBodyByType(configEatAct.foods.keys)
            if (foodSet.isEmpty()) {
                return null
            }
            return foodSet.minBy {
                data.distance(it.data)
            }
        }

        fun calcHunger(): Float {
            return max(0f, prevEatAct.hunger - configEatAct.hungerRatePerSecond * input.delta)
        }

        var hunger = calcHunger()

        val targetFood = targetFood()
        val isTurning = data.status.textureRegionData.animationAction == BodyConfig.AnimationAction.TURN
        if (targetFood != null) {
            if (!isTurning && data.containsCenter(targetFood.data)) {
                val food = configEatAct.foods.getValue(targetFood.data.type)
                val removed = targetFood.act(
                    input = BodyInput(
                        body = targetFood,
                        damage = food.damagePerSecond * input.delta,
                    ),
                )
                if (removed) {
                    val maxHunger = configEatAct.fullHunger * configEatAct.maxHungerPercent
                    hunger = min(maxHunger, hunger + food.hunger)
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
            hunger = hunger,
        )
    }
}
