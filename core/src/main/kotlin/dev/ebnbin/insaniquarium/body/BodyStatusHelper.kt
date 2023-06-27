package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.insaniquarium.tank.Tank

object BodyStatusHelper {
    private data class EatAct(
        val drivingTargetX: BodyDrivingTarget?,
        val drivingTargetY: BodyDrivingTarget?,
        val foodRelation: BodyRelation,
        val eatenFood: BodyConfig.Food?,
    )

    private data class TouchAct(
        val drivingTargetX: BodyDrivingTarget,
        val drivingTargetY: BodyDrivingTarget,
    )

    private data class SwimAct(
        val drivingTarget: BodyDrivingTarget?,
        val remainingTime: Float,
    )

    fun nextStatus(
        data: BodyData,
        status: BodyStatus,
        input: BodyInput,
        bodyManager: BodyManager,
    ): BodyStatus {
        val nextBox = data.box.nextStatus(input.delta)

        val nextEatAct = nextEatAct(
            bodyManager = bodyManager,
            configEatAct = data.body.config.eatAct,
            hungerStatus = data.life.hungerStatus,
            box = data.box,
            renderer = data.renderer,
            delta = input.delta,
        )

        val hasEatDrivingTarget = nextEatAct?.drivingTargetX != null || nextEatAct?.drivingTargetY != null

        val nextTouchAct = nextTouchAct(
            enabled = !hasEatDrivingTarget,
            tank = data.body.tank,
            configTouchAct = data.body.config.touchAct,
        )

        val hasTouchDrivingTarget = nextTouchAct != null

        val nextSwimActX = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = data.body.config.swimActX,
            swimAct = if (status.swimActX == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = data.status.drivingTargetX?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    remainingTime = status.swimActX.remainingTime,
                )
            },
            tankSize = data.body.tank.width,
            reachDrivingTarget = data.box.reachDrivingTargetX,
            input = input,
        )
        val nextSwimActY = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = data.body.config.swimActY,
            swimAct = if (status.swimActY == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = data.status.drivingTargetY?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    remainingTime = status.swimActY.remainingTime,
                )
            },
            tankSize = data.body.tank.height,
            reachDrivingTarget = data.box.reachDrivingTargetY,
            input = input,
        )

        val nextStatusSwimActX = if (nextSwimActX == null) {
            null
        } else {
            BodyStatus.SwimAct(
                remainingTime = nextSwimActX.remainingTime,
            )
        }
        val nextStatusSwimActY = if (nextSwimActY == null) {
            null
        } else {
            BodyStatus.SwimAct(
                remainingTime = nextSwimActY.remainingTime,
            )
        }

        val nextLife = data.life.nextStatus(
            input = input,
            food = nextEatAct?.eatenFood,
        )

        val nextAlphaTime = data.renderer.nextAlphaTime(
            delta = input.delta,
        )

        val nextDrivingTargetX: BodyDrivingTarget? =
            nextEatAct?.drivingTargetX ?: nextTouchAct?.drivingTargetX ?: nextSwimActX?.drivingTarget
        val nextDrivingTargetY: BodyDrivingTarget? =
            nextEatAct?.drivingTargetY ?: nextTouchAct?.drivingTargetY ?: nextSwimActY?.drivingTarget

        val nextAnimationData = data.renderer.nextAnimationData(
            delta = input.delta,
            eatenFoodRelation = nextEatAct?.foodRelation,
        )

        return BodyStatus(
            box = nextBox,
            swimActX = nextStatusSwimActX,
            swimActY = nextStatusSwimActY,
            life = nextLife,
            alphaTime = nextAlphaTime,
            drivingTargetX = nextDrivingTargetX,
            drivingTargetY = nextDrivingTargetY,
            animationData = nextAnimationData,
        )
    }

    private fun nextEatAct(
        bodyManager: BodyManager,
        configEatAct: BodyConfig.EatAct?,
        hungerStatus: BodyHungerStatus?,
        box: BodyBox,
        renderer: BodyRenderer,
        delta: Float,
    ): EatAct? {
        if (configEatAct == null) {
            return null
        }

        fun targetFood(): Body? {
            if (hungerStatus == BodyHungerStatus.FULL) {
                return null
            }
            require(configEatAct.foods.isNotEmpty())
            return bodyManager.findNearestBodyByType(configEatAct.foods.keys)
        }

        var eatenFood: BodyConfig.Food? = null
        val targetFood = targetFood()
        val foodRelation = box.relation(targetFood?.data?.box)

        val isEating = renderer.animationData.action == BodyAnimationData.Action.SWIM ||
            renderer.animationData.action == BodyAnimationData.Action.EAT
        if (targetFood != null && isEating && foodRelation == BodyRelation.CONTAIN_CENTER) {
            val food = configEatAct.foods.getValue(targetFood.data.body.type)
            val foodBody = targetFood.act(
                input = BodyInput(
                    healthDiff = food.healthDiffPerSecond * delta,
                ),
            )
            if (foodBody.data.canRemove) {
                eatenFood = food
            }
        }
        return EatAct(
            drivingTargetX = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.data.box.x,
                    acceleration = configEatAct.drivingAccelerationX,
                )
            },
            drivingTargetY = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.data.box.y,
                    acceleration = configEatAct.drivingAccelerationY,
                )
            },
            foodRelation = foodRelation,
            eatenFood = eatenFood,
        )
    }

    private fun nextTouchAct(
        enabled: Boolean,
        tank: Tank,
        configTouchAct: BodyConfig.TouchAct?,
    ): TouchAct? {
        if (!enabled) {
            return null
        }
        if (configTouchAct == null) {
            return null
        }
        val touchPoint = tank.touchPoint ?: return null
        return TouchAct(
            drivingTargetX = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.x,
                acceleration = configTouchAct.drivingAccelerationX,
            ),
            drivingTargetY = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.y,
                acceleration = configTouchAct.drivingAccelerationY,
            ),
        )
    }

    private fun nextSwimAct(
        enabled: Boolean,
        configSwimAct: BodyConfig.SwimAct?,
        swimAct: SwimAct?,
        tankSize: Float,
        reachDrivingTarget: Boolean,
        input: BodyInput,
    ): SwimAct? {
        if (!enabled) {
            return null
        }
        if (configSwimAct == null) {
            return null
        }

        fun createTargetingSwimAct(): SwimAct {
            return SwimAct(
                drivingTarget = BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.SWIM,
                    position = Random.nextFloat(0f, tankSize),
                    acceleration = configSwimAct.drivingAcceleration,
                ),
                remainingTime = Float.MAX_VALUE,
            )
        }

        fun createIdlingSwimAct(): SwimAct {
            return SwimAct(
                drivingTarget = null,
                remainingTime = Random.nextFloat(
                    configSwimAct.idlingTimeRandomStart,
                    configSwimAct.idlingTimeRandomEnd,
                ),
            )
        }

        fun updateSwimAct(swimAct: SwimAct): SwimAct {
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
            if (isRemainingTimeUp || reachDrivingTarget) {
                createIdlingSwimAct()
            } else {
                updateSwimAct(swimAct)
            }
        }
    }
}
