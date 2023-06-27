package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Random

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
        val time: Float,
    )

    fun nextStatus(
        data: BodyData,
        status: BodyStatus,
        input: BodyInput,
        bodyManager: BodyManager,
        touchPoint: Point?,
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
            touchPoint = touchPoint,
            configTouchAct = data.body.config.touchAct,
        )

        val hasTouchDrivingTarget = nextTouchAct != null

        val nextSwimActX = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = data.body.config.swimActX,
            swimAct = if (status.swimTimeX == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = data.status.drivingTargetX?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    time = status.swimTimeX,
                )
            },
            tankSize = data.body.tank.width,
            reachDrivingTarget = data.box.reachDrivingTargetX,
            delta = input.delta,
        )
        val nextSwimActY = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = data.body.config.swimActY,
            swimAct = if (status.swimTimeY == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = data.status.drivingTargetY?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    time = status.swimTimeY,
                )
            },
            tankSize = data.body.tank.height,
            reachDrivingTarget = data.box.reachDrivingTargetY,
            delta = input.delta,
        )

        val nextLife = data.life.nextStatus(
            input = input,
            food = nextEatAct?.eatenFood,
        )

        val nextDrivingTargetX: BodyDrivingTarget? =
            nextEatAct?.drivingTargetX ?: nextTouchAct?.drivingTargetX ?: nextSwimActX?.drivingTarget
        val nextDrivingTargetY: BodyDrivingTarget? =
            nextEatAct?.drivingTargetY ?: nextTouchAct?.drivingTargetY ?: nextSwimActY?.drivingTarget

        val nextRenderer = data.renderer.nextStatus(
            delta = input.delta,
            eatenFoodRelation = nextEatAct?.foodRelation,
        )

        return BodyStatus(
            box = nextBox,
            swimTimeX = nextSwimActX?.time,
            swimTimeY = nextSwimActY?.time,
            life = nextLife,
            drivingTargetX = nextDrivingTargetX,
            drivingTargetY = nextDrivingTargetY,
            renderer = nextRenderer,
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

        if (targetFood != null && renderer.canEat && foodRelation == BodyRelation.CONTAIN_CENTER) {
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
        touchPoint: Point?,
        configTouchAct: BodyConfig.TouchAct?,
    ): TouchAct? {
        if (!enabled) {
            return null
        }
        if (configTouchAct == null) {
            return null
        }
        touchPoint ?: return null
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
        delta: Float,
    ): SwimAct? {
        if (!enabled) {
            return null
        }
        if (configSwimAct == null) {
            return null
        }

        fun createTargeting(): SwimAct {
            return SwimAct(
                drivingTarget = BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.SWIM,
                    position = Random.nextFloat(0f, tankSize),
                    acceleration = configSwimAct.drivingAcceleration,
                ),
                time = 0f,
            )
        }

        fun createIdling(): SwimAct {
            return SwimAct(
                drivingTarget = null,
                time = Random.nextFloat(
                    configSwimAct.idlingTimeRandomStart,
                    configSwimAct.idlingTimeRandomEnd,
                ),
            )
        }

        fun updateIdling(swimAct: SwimAct): SwimAct {
            return swimAct.copy(
                time = swimAct.time - delta,
            )
        }

        if (swimAct == null) {
            return if (Random.nextBoolean()) {
                createTargeting()
            } else {
                createIdling()
            }
        }
        return if (swimAct.drivingTarget == null) {
            // Idling
            val isRemainingTimeUp = swimAct.time - delta <= 0f
            if (isRemainingTimeUp) {
                createTargeting()
            } else {
                updateIdling(swimAct)
            }
        } else {
            // Targeting
            if (reachDrivingTarget) {
                createIdling()
            } else {
                swimAct
            }
        }
    }
}
