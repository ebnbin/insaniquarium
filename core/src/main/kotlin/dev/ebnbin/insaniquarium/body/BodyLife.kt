package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Random

data class BodyLife(
    private val config: BodyConfig.Life,
    private val tankWidth: Float,
    private val tankHeight: Float,
    private val reachDrivingTargetX: Boolean,
    private val reachDrivingTargetY: Boolean,
    private val boxRelation: (other: BodyBox?) -> BodyRelation,
    private val canEat: Boolean,
    private val status: Status,
) {
    data class Status(
        /**
         * null: Neither targeting nor idling.
         * 0f: Targeting or finish idling.
         * > 0f: Idling.
         */
        val swimTimeX: Float? = null,
        val swimTimeY: Float? = null,

        val drivingTargetX: BodyDrivingTarget? = null,
        val drivingTargetY: BodyDrivingTarget? = null,

        val health: Float? = null,
        val hunger: Float? = null,
        val growth: Float? = null,
        val drop: Float? = null,
    )

    data class TmpStatus(
        val foodRelation: BodyRelation,
    )

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

    val isDead: Boolean = config.isDead

    val health: Float? = status.health
    private val hunger: Float? = status.hunger
    private val growth: Float? = status.growth
    private val drop: Float? = status.drop

    private val isDeadFromHealth: Boolean = config.health != null && health == 0f

    val hungerStatus: BodyHungerStatus? = config.hunger?.status(hunger)

    private val transformationFromHunger: BodyType? = config.hunger?.transformation?.takeIf { hunger == 0f }

    private val transformationFromGrowth: BodyType? = config.growth?.transformation?.takeIf { growth != null && growth <= 0f }

    private val productionFromDrop: BodyType? = config.drop?.production?.takeIf { drop != null && drop <= 0f }

    private val dropCount: Int = if (drop == null || drop > 0f) {
        0
    } else {
        -drop.toInt() + 1
    }

    fun canRemove(isSwimming: Boolean): Boolean {
        return isDeadFromHealth || (transformationFromHunger != null && isSwimming) || transformationFromGrowth != null
    }

    fun nextStatus(
        delegate: BodyDelegate,
        input: BodyInput,
        touchPoint: Point?,
    ): Pair<Status, TmpStatus> {
        val nextEatAct = nextEatAct(
            delegate = delegate,
            delta = input.delta,
        )

        val hasEatDrivingTarget = nextEatAct?.drivingTargetX != null || nextEatAct?.drivingTargetY != null

        val nextTouchAct = nextTouchAct(
            enabled = !hasEatDrivingTarget,
            touchPoint = touchPoint,
        )

        val hasTouchDrivingTarget = nextTouchAct != null

        val nextSwimActX = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = config.swimActX,
            swimAct = if (status.swimTimeX == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = status.drivingTargetX?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    time = status.swimTimeX,
                )
            },
            tankSize = tankWidth,
            reachDrivingTarget = reachDrivingTargetX,
            delta = input.delta,
        )
        val nextSwimActY = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = config.swimActY,
            swimAct = if (status.swimTimeY == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = status.drivingTargetY?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    time = status.swimTimeY,
                )
            },
            tankSize = tankHeight,
            reachDrivingTarget = reachDrivingTargetY,
            delta = input.delta,
        )

        val nextDrivingTargetX: BodyDrivingTarget? =
            nextEatAct?.drivingTargetX ?: nextTouchAct?.drivingTargetX ?: nextSwimActX?.drivingTarget
        val nextDrivingTargetY: BodyDrivingTarget? =
            nextEatAct?.drivingTargetY ?: nextTouchAct?.drivingTargetY ?: nextSwimActY?.drivingTarget

        val nextHealth = nextHealth(input, nextEatAct?.eatenFood)
        val nextHunger = nextHunger(input, nextEatAct?.eatenFood)
        val nextGrowth = nextGrowth(input, nextEatAct?.eatenFood)
        val nextDrop = nextDrop(input, nextEatAct?.eatenFood)
        return Status(
            swimTimeX = nextSwimActX?.time,
            swimTimeY = nextSwimActY?.time,
            drivingTargetX = nextDrivingTargetX,
            drivingTargetY = nextDrivingTargetY,
            health = nextHealth,
            hunger = nextHunger,
            growth = nextGrowth,
            drop = nextDrop,
        ) to TmpStatus(
            foodRelation = nextEatAct?.foodRelation ?: BodyRelation.DISJOINT,
        )
    }

    private fun nextEatAct(
        delegate: BodyDelegate,
        delta: Float,
    ): EatAct? {
        if (config.eatAct == null) {
            return null
        }

        fun targetFood(): BodyData? {
            if (hungerStatus == BodyHungerStatus.FULL) {
                return null
            }
            require(config.eatAct.foods.isNotEmpty())
            return delegate.findNearestBodyByType(config.eatAct.foods.keys)
        }

        var eatenFood: BodyConfig.Food? = null
        val targetFood = targetFood()
        val foodRelation = boxRelation(targetFood?.box)

        if (targetFood != null && canEat && foodRelation == BodyRelation.CONTAIN_CENTER) {
            val food = config.eatAct.foods.getValue(targetFood.type)
            val foodBody = targetFood.delegate.act(
                input = BodyInput(
                    healthDiff = food.healthDiffPerSecond * delta,
                ),
            )
            if (foodBody.canRemove) {
                eatenFood = food
            }
        }
        return EatAct(
            drivingTargetX = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.box.x,
                    acceleration = config.eatAct.drivingAccelerationX,
                )
            },
            drivingTargetY = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.box.y,
                    acceleration = config.eatAct.drivingAccelerationY,
                )
            },
            foodRelation = foodRelation,
            eatenFood = eatenFood,
        )
    }

    private fun nextTouchAct(
        enabled: Boolean,
        touchPoint: Point?,
    ): TouchAct? {
        if (!enabled) {
            return null
        }
        if (config.touchAct == null) {
            return null
        }
        touchPoint ?: return null
        return TouchAct(
            drivingTargetX = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.x,
                acceleration = config.touchAct.drivingAccelerationX,
            ),
            drivingTargetY = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.y,
                acceleration = config.touchAct.drivingAccelerationY,
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

    private fun nextHealth(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        config.health ?: return null
        return nextValue(
            value = health,
            initialThreshold = config.health.initialThreshold,
            diffPerSecond = config.health.diffPerSecond,
            inputDelta = input.delta,
            inputDiff = input.healthDiff,
            foodDiff = food?.health,
        ).coerceIn(0f, 1f)
    }

    private fun nextHunger(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        config.hunger ?: return null
        return nextValue(
            value = hunger,
            initialThreshold = config.hunger.initialThreshold,
            diffPerSecond = config.hunger.diffPerSecond,
            inputDelta = input.delta,
            inputDiff = input.hungerDiff,
            foodDiff = food?.hunger,
        ).coerceIn(0f, config.hunger.maxThreshold)
    }

    private fun nextGrowth(
        input: BodyInput,
        food: BodyConfig.Food?
    ): Float? {
        config.growth ?: return null
        return nextValue(
            value = growth,
            initialThreshold = config.growth.initialThreshold,
            diffPerSecond = config.growth.diffPerSecond,
            inputDelta = input.delta,
            inputDiff = input.growthDiff,
            foodDiff = food?.growth,
        )
    }

    private fun nextDrop(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        config.drop ?: return null
        return nextValue(
            value = drop,
            initialThreshold = config.drop.initialThreshold,
            diffPerSecond = config.drop.diffPerSecond,
            inputDelta = input.delta,
            inputDiff = input.dropDiff,
            foodDiff = food?.drop,
        )
    }

    private fun nextValue(
        value: Float?,
        initialThreshold: Float,
        diffPerSecond: Float,
        inputDelta: Float,
        inputDiff: Float,
        foodDiff: Float?,
    ): Float {
        var nextValue = value ?: initialThreshold
        nextValue += diffPerSecond * inputDelta
        nextValue += inputDiff
        nextValue += (foodDiff ?: 0f)
        return nextValue
    }

    /**
     * @return True if removed.
     */
    fun postUpdate(
        delegate: BodyDelegate,
        status: BodyStatus,
        delta: Float,
    ): Boolean {
        if (isDeadFromHealth) {
            delegate.removeFromTank()
            return true
        }
        if (transformationFromHunger != null &&
            status.renderer.animationData.action == BodyAnimationData.Action.SWIM) {
            delegate.replaceBody(
                type = transformationFromHunger,
                createStatus = {
                    status.copy(
                        life = Status(),
                        renderer = status.renderer.copy(
                            animationData = status.renderer.animationData.copy(
                                stateTime = 0f,
                            ),
                            alphaTime = null,
                        ),
                    )
                },
                input = BodyInput(
                    delta = delta,
                ),
            )
            return true
        }
        if (transformationFromGrowth != null) {
            val growth = status.life.growth
            require(growth != null)
            delegate.replaceBody(
                type = transformationFromGrowth,
                createStatus = {
                    status.copy(
                        life = status.life.copy(
                            growth = null,
                        ),
                    )
                },
                input = BodyInput(
                    delta = delta,
                    growthDiff = growth,
                ),
            )
        }
        if (productionFromDrop != null) {
            repeat(dropCount) {
                delegate.addBody(
                    type = productionFromDrop,
                    createStatus = {
                        BodyStatus(
                            box = BodyBox.Status(
                                x = status.box.x,
                                y = status.box.y,
                            ),
                        )
                    },
                    input = BodyInput(
                        delta = delta,
                    ),
                )
            }
            delegate.act(
                input = BodyInput(
                    dropDiff = dropCount.toFloat(),
                ),
            )
            return false
        }
        return false
    }

    fun devPutLogs() {
        baseGame.putLog("health") {
            if (health == null) "null" else "%.3f".format(health)
        }
        baseGame.putLog("hunger") {
            if (hunger == null) "null" else "%.3f,%s".format(hunger, hungerStatus)
        }
        baseGame.putLog("growth") {
            if (growth == null) "null" else "%.3f".format(growth)
        }
        baseGame.putLog("drop  ") {
            if (drop == null) "null" else "%.3f".format(drop)
        }
    }
}
