package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.insaniquarium.tank.Tank
import kotlin.math.max

object BodyStatusHelper {
    private data class EatAct(
        val drivingTargetX: BodyStatus.DrivingTarget?,
        val drivingTargetY: BodyStatus.DrivingTarget?,
        val foodRelation: BodyRelation,
        val hungerDiff: Float,
        val growthDiff: Float,
        val dropDiff: Float,
    )

    private data class TouchAct(
        val drivingTargetX: BodyStatus.DrivingTarget,
        val drivingTargetY: BodyStatus.DrivingTarget,
    )

    private data class SwimAct(
        val drivingTarget: BodyStatus.DrivingTarget?,
        val remainingTime: Float,
    )

    fun nextStatus(
        data: BodyData,
        status: BodyStatus,
        input: BodyInput,
    ): BodyStatus {
        val nextVelocityX = data.box.nextVelocityX(input.delta)
        val nextVelocityY = data.box.nextVelocityY(input.delta)

        val nextX = data.box.nextX(input.delta)
        val nextY = data.box.nextY(input.delta)

        val nextEatAct = nextEatAct(
            tank = data.body.tank,
            configHunger = data.body.config.hunger,
            hungerStatus = data.hungerStatus,
            data = data,
            input = input,
        )

        val hasEatDrivingTarget = nextEatAct?.drivingTargetX != null || nextEatAct?.drivingTargetY != null

        val nextTouchAct = nextTouchAct(
            enabled = !hasEatDrivingTarget,
            tank = data.body.tank,
            configTouchAct = data.body.config.touchAct,
            isDying = data.hungerStatus == BodyHungerStatus.DYING,
        )

        val hasTouchDrivingTarget = nextTouchAct != null

        val nextSwimActX = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = data.body.config.swimActX,
            swimAct = if (status.swimActX == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = data.status.drivingTargetX
                        ?.takeIf { it.type == BodyStatus.DrivingTarget.Type.SWIM },
                    remainingTime = status.swimActX.remainingTime,
                )
            },
            tankSize = data.body.tank.width,
            reachDrivingTarget = data.box.reachDrivingTargetX,
            input = input,
            isDying = data.hungerStatus == BodyHungerStatus.DYING,
        )
        val nextSwimActY = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            configSwimAct = data.body.config.swimActY,
            swimAct = if (status.swimActY == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = data.status.drivingTargetY
                        ?.takeIf { it.type == BodyStatus.DrivingTarget.Type.SWIM },
                    remainingTime = status.swimActY.remainingTime,
                )
            },
            tankSize = data.body.tank.height,
            reachDrivingTarget = data.box.reachDrivingTargetY,
            input = input,
            isDying = data.hungerStatus == BodyHungerStatus.DYING,
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

        val nextHealth = nextHealth(
            configHealth = data.body.config.health,
            health = status.health,
            input = input,
        )

        val nextHunger = nextHunger(
            configHunger = data.body.config.hunger,
            hunger = status.hunger,
            eatAct = nextEatAct,
            input = input,
        )

        val nextGrowth = nextGrowth(
            configGrowth = data.body.config.growth,
            growth = status.growth,
            eatAct = nextEatAct,
        )

        val nextDrop = nextDrop(
            body = data.body,
            configDrop = data.body.config.drop,
            drop = status.drop,
            eatAct = nextEatAct,
            input = input,
            isDying = data.hungerStatus == BodyHungerStatus.DYING,
        )

        val nextHungerStatus = data.body.config.hunger?.status(nextHunger)

        val nextDisappearAct = nextDisappearAct(
            canDisappear = status.animationData.action == BodyStatus.AnimationData.Action.DIE &&
                data.isAnimationFinished,
            disappearAct = status.disappearAct,
            data = data,
            input = input,
        )

        val nextDrivingTargetX: BodyStatus.DrivingTarget? = if (nextHungerStatus == BodyHungerStatus.DYING) {
            null
        } else {
            nextEatAct?.drivingTargetX ?: nextTouchAct?.drivingTargetX ?: nextSwimActX?.drivingTarget
        }
        val nextDrivingTargetY: BodyStatus.DrivingTarget? = if (nextHungerStatus == BodyHungerStatus.DYING) {
            null
        } else {
            nextEatAct?.drivingTargetY ?: nextTouchAct?.drivingTargetY ?: nextSwimActY?.drivingTarget
        }

        val hasTurnAnimation = data.body.config.animations.turn != null
        val nextExpectedIsFacingRight = if (hasTurnAnimation) {
            when (data.box.expectedDirection) {
                Direction.ZERO -> status.expectedIsFacingRight
                Direction.POSITIVE -> true
                Direction.NEGATIVE -> false
            }
        } else {
            false
        }

        val nextAnimationData = nextAnimationData(
            config = data.body.config,
            hasTurnAnimation = hasTurnAnimation,
            animationData = status.animationData,
            isAnimationFinished = data.isAnimationFinished,
            canAnimationActionChange = data.canAnimationActionChange,
            expectedIsFacingRight = status.expectedIsFacingRight,
            canPlayEatAnimation = nextEatAct?.foodRelation == BodyRelation.OVERLAP ||
                nextEatAct?.foodRelation == BodyRelation.CONTAIN_CENTER,
            hungerStatus = nextHungerStatus,
            input = input,
        )

        return BodyStatus(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            x = nextX,
            y = nextY,
            swimActX = nextStatusSwimActX,
            swimActY = nextStatusSwimActY,
            health = nextHealth,
            hunger = nextHunger,
            growth = nextGrowth,
            drop = nextDrop,
            disappearAct = nextDisappearAct,
            drivingTargetX = nextDrivingTargetX,
            drivingTargetY = nextDrivingTargetY,
            expectedIsFacingRight = nextExpectedIsFacingRight,
            animationData = nextAnimationData,
        )
    }

    private fun nextEatAct(
        tank: Tank,
        configHunger: BodyConfig.Hunger?,
        hungerStatus: BodyHungerStatus?,
        data: BodyData,
        input: BodyInput,
    ): EatAct? {
        if (configHunger == null) {
            return null
        }

        fun targetFood(): Body? {
            if (hungerStatus == BodyHungerStatus.FULL || hungerStatus == BodyHungerStatus.DYING) {
                return null
            }
            val foodSet = tank.findBodyByType(configHunger.foods.keys)
            if (foodSet.isEmpty()) {
                return null
            }
            return foodSet.minBy {
                data.box.distance(it.data.box)
            }
        }

        var hungerDiff = 0f
        var growthDiff = 0f
        var dropDiff = 0f
        val targetFood = targetFood()
        val foodRelation = data.box.relation(targetFood?.data?.box)

        val isTurning = data.status.animationData.action == BodyStatus.AnimationData.Action.TURN
        if (targetFood != null && !isTurning && foodRelation == BodyRelation.CONTAIN_CENTER) {
            val food = configHunger.foods.getValue(targetFood.data.body.type)
            val foodBody = targetFood.act(
                input = BodyInput(
                    damage = food.damagePerSecond * input.delta,
                ),
            )
            if (foodBody.data.canRemove) {
                hungerDiff = food.hunger
                growthDiff = food.growth
                dropDiff = food.drop
            }
        }
        return EatAct(
            drivingTargetX = if (targetFood == null) {
                null
            } else {
                BodyStatus.DrivingTarget(
                    type = BodyStatus.DrivingTarget.Type.EAT,
                    position = targetFood.data.status.x,
                    acceleration = configHunger.drivingAccelerationX,
                )
            },
            drivingTargetY = if (targetFood == null) {
                null
            } else {
                BodyStatus.DrivingTarget(
                    type = BodyStatus.DrivingTarget.Type.EAT,
                    position = targetFood.data.status.y,
                    acceleration = configHunger.drivingAccelerationY,
                )
            },
            foodRelation = foodRelation,
            hungerDiff = hungerDiff,
            growthDiff = growthDiff,
            dropDiff = dropDiff,
        )
    }

    private fun nextTouchAct(
        enabled: Boolean,
        tank: Tank,
        configTouchAct: BodyConfig.TouchAct?,
        isDying: Boolean,
    ): TouchAct? {
        if (!enabled) {
            return null
        }
        if (configTouchAct == null) {
            return null
        }
        if (isDying) {
            return null
        }
        val touchPoint = tank.touchPoint ?: return null
        return TouchAct(
            drivingTargetX = BodyStatus.DrivingTarget(
                type = BodyStatus.DrivingTarget.Type.TOUCH,
                position = touchPoint.x,
                acceleration = configTouchAct.drivingAccelerationX,
            ),
            drivingTargetY = BodyStatus.DrivingTarget(
                type = BodyStatus.DrivingTarget.Type.TOUCH,
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
        isDying: Boolean,
    ): SwimAct? {
        if (!enabled) {
            return null
        }
        if (configSwimAct == null) {
            return null
        }
        if (isDying) {
            return null
        }

        fun createTargetingSwimAct(): SwimAct {
            return SwimAct(
                drivingTarget = BodyStatus.DrivingTarget(
                    type = BodyStatus.DrivingTarget.Type.SWIM,
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

    private fun nextHealth(
        configHealth: BodyConfig.Health?,
        health: Float?,
        input: BodyInput,
    ): Float? {
        if (configHealth == null) {
            return null
        }
        if (health == null) {
            return configHealth.full
        }
        return max(0f, health - input.damage)
    }

    private fun nextHunger(
        configHunger: BodyConfig.Hunger?,
        hunger: Float?,
        eatAct: EatAct?,
        input: BodyInput,
    ): Float? {
        if (configHunger == null) {
            return null
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

    private fun nextGrowth(
        configGrowth: BodyConfig.Growth?,
        growth: Float?,
        eatAct: EatAct?,
    ): Float? {
        if (configGrowth == null) {
            return null
        }
        if (growth == null) {
            return 0f
        }

        var nextGrowth = growth
        if (eatAct != null) {
            nextGrowth += eatAct.growthDiff
        }
        return nextGrowth
    }

    private fun nextDrop(
        body: Body,
        configDrop: BodyConfig.Drop?,
        drop: Float?,
        eatAct: EatAct?,
        input: BodyInput,
        isDying: Boolean,
    ): Float? {
        if (configDrop == null) {
            return null
        }
        if (drop == null) {
            return 0f
        }
        if (isDying) {
            return drop
        }

        var nextDrop = drop + configDrop.incrementPerSecond * input.delta
        if (eatAct != null) {
            nextDrop += eatAct.dropDiff
        }
        while (nextDrop >= configDrop.full) {
            nextDrop -= configDrop.full
            body.tank.addBody(
                type = configDrop.product,
                createStatus = {
                    BodyStatus(
                        x = body.data.status.x,
                        y = body.data.status.y,
                    )
                },
            )
        }
        return nextDrop
    }

    private fun nextDisappearAct(
        canDisappear: Boolean,
        disappearAct: BodyStatus.DisappearAct?,
        data: BodyData,
        input: BodyInput,
    ): BodyStatus.DisappearAct? {
        if (!canDisappear) {
            return null
        }
        return if (disappearAct == null) {
            if (data.box.isSinkingOrFloatingOutsideWater) {
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

    private fun nextAnimationData(
        config: BodyConfig,
        hasTurnAnimation: Boolean,
        animationData: BodyStatus.AnimationData,
        isAnimationFinished: Boolean,
        canAnimationActionChange: Boolean,
        expectedIsFacingRight: Boolean,
        canPlayEatAnimation: Boolean,
        hungerStatus: BodyHungerStatus?,
        input: BodyInput,
    ): BodyStatus.AnimationData {
        val isDying = hungerStatus == BodyHungerStatus.DYING

        val animationStatus = if (hungerStatus == BodyHungerStatus.HUNGRY || isDying) {
            BodyStatus.AnimationData.Status.HUNGRY
        } else {
            BodyStatus.AnimationData.Status.NORMAL
        }

        fun createEat(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.EAT,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createTurn(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.TURN,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = expectedIsFacingRight,
            )
        }

        fun createSwim(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.SWIM,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createDie(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.DIE,
                status = BodyStatus.AnimationData.Status.HUNGRY,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun update(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = animationData.action,
                status = if (animationData.action == BodyStatus.AnimationData.Action.DIE) {
                    BodyStatus.AnimationData.Status.HUNGRY
                } else {
                    animationStatus
                },
                stateTime = animationData.stateTime + input.delta,
                isFacingRight = animationData.isFacingRight,
            )
        }

        return if (canAnimationActionChange) {
            if (isDying) {
                createDie()
            } else if (config.animations.eat != null && canPlayEatAnimation) {
                createEat()
            } else {
                if (hasTurnAnimation && animationData.isFacingRight != expectedIsFacingRight) {
                    createTurn()
                } else {
                    update()
                }
            }
        } else {
            if (isAnimationFinished) {
                if (isDying && animationData.action == BodyStatus.AnimationData.Action.DIE) {
                    update()
                } else {
                    createSwim()
                }
            } else {
                update()
            }
        }
    }
}
