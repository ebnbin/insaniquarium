package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.World
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.insaniquarium.tank.Tank
import kotlin.math.max

object BodyStatusHelper {
    private data class EatAct(
        val drivingTargetX: BodyStatus.DrivingTarget?,
        val drivingTargetY: BodyStatus.DrivingTarget?,
        val foodRelation: BodyRelation,
        val hungerDiff: Float,
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

        val nextEatAct = nextEatAct(
            tank = data.body.tank,
            configEatAct = data.body.config.eatAct,
            hungerStatus = data.hungerStatus,
            data = data,
            input = input,
        )

        val hasEatDrivingTarget = nextEatAct?.drivingTargetX != null || nextEatAct?.drivingTargetY != null

        val nextTouchAct = nextTouchAct(
            enabled = !hasEatDrivingTarget,
            tank = data.body.tank,
            configTouchAct = data.body.config.touchAct,
            isDying = data.hungerStatus == HungerStatus.DYING,
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
            leftOrBottom = data.left,
            rightOrTop = data.right,
            drivingTarget = data.status.drivingTargetX,
            input = input,
            isDying = data.hungerStatus == HungerStatus.DYING,
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
            leftOrBottom = data.bottom,
            rightOrTop = data.top,
            drivingTarget = data.status.drivingTargetY,
            input = input,
            isDying = data.hungerStatus == HungerStatus.DYING,
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

        val nextHungerStatus = data.body.config.hunger?.status(nextHunger)

        val nextDisappearAct = nextDisappearAct(
            canDisappear = status.animationData.action == BodyStatus.AnimationData.Action.DIE &&
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

        val nextAnimationData = nextAnimationData(
            config = data.body.config,
            hasTurnAnimation = data.hasTurnAnimation,
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
            disappearAct = nextDisappearAct,
            drivingTargetX = nextDrivingTargetX,
            drivingTargetY = nextDrivingTargetY,
            expectedIsFacingRight = nextExpectedIsFacingRight,
            animationData = nextAnimationData,
        )
    }

    private fun nextEatAct(
        tank: Tank,
        configEatAct: BodyConfig.EatAct?,
        hungerStatus: HungerStatus?,
        data: BodyData,
        input: BodyInput,
    ): EatAct? {
        if (configEatAct == null) {
            return null
        }

        fun targetFood(): Body? {
            if (hungerStatus == HungerStatus.FULL || hungerStatus == HungerStatus.DYING) {
                return null
            }
            val foodSet = tank.findBodyByType(configEatAct.foods.keys)
            if (foodSet.isEmpty()) {
                return null
            }
            return foodSet.minBy {
                data.distance(it.data)
            }
        }

        var hungerDiff = 0f
        val targetFood = targetFood()
        val foodRelation = data.relation(targetFood?.data)

        val isTurning = data.status.animationData.action == BodyStatus.AnimationData.Action.TURN
        if (targetFood != null && !isTurning && foodRelation == BodyRelation.CONTAIN_CENTER) {
            val food = configEatAct.foods.getValue(targetFood.data.body.type)
            val foodBody = targetFood.act(
                input = BodyInput(
                    damage = food.damagePerSecond * input.delta,
                ),
            )
            if (foodBody.data.canRemove) {
                hungerDiff = food.hunger
            }
        }
        return EatAct(
            drivingTargetX = if (targetFood == null) {
                null
            } else {
                BodyStatus.DrivingTarget(
                    type = BodyStatus.DrivingTarget.Type.EAT,
                    position = targetFood.data.status.x,
                    acceleration = configEatAct.accelerationX,
                )
            },
            drivingTargetY = if (targetFood == null) {
                null
            } else {
                BodyStatus.DrivingTarget(
                    type = BodyStatus.DrivingTarget.Type.EAT,
                    position = targetFood.data.status.y,
                    acceleration = configEatAct.accelerationY,
                )
            },
            foodRelation = foodRelation,
            hungerDiff = hungerDiff,
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
                acceleration = configTouchAct.accelerationX,
            ),
            drivingTargetY = BodyStatus.DrivingTarget(
                type = BodyStatus.DrivingTarget.Type.TOUCH,
                position = touchPoint.y,
                acceleration = configTouchAct.accelerationY,
            ),
        )
    }

    private fun nextSwimAct(
        enabled: Boolean,
        configSwimAct: BodyConfig.SwimAct?,
        swimAct: SwimAct?,
        tankSize: Float,
        leftOrBottom: Float,
        rightOrTop: Float,
        drivingTarget: BodyStatus.DrivingTarget?,
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

        val containDrivingTarget = drivingTarget?.position?.let { it in leftOrBottom..rightOrTop } ?: false

        fun createTargetingSwimAct(): SwimAct {
            return SwimAct(
                drivingTarget = BodyStatus.DrivingTarget(
                    type = BodyStatus.DrivingTarget.Type.SWIM,
                    position = Random.nextFloat(0f, tankSize),
                    acceleration = configSwimAct.acceleration,
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
            if (isRemainingTimeUp || containDrivingTarget) {
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

    private fun nextAnimationData(
        config: BodyConfig,
        hasTurnAnimation: Boolean,
        animationData: BodyStatus.AnimationData,
        isAnimationFinished: Boolean,
        canAnimationActionChange: Boolean,
        expectedIsFacingRight: Boolean,
        canPlayEatAnimation: Boolean,
        hungerStatus: HungerStatus?,
        input: BodyInput,
    ): BodyStatus.AnimationData {
        val isDying = hungerStatus == HungerStatus.DYING

        val animationStatus = if (hungerStatus == HungerStatus.HUNGRY || isDying) {
            BodyStatus.AnimationData.Status.HUNGRY
        } else {
            BodyStatus.AnimationData.Status.NORMAL
        }

        fun createEatTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.EAT,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createTurnTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.TURN,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = expectedIsFacingRight,
            )
        }

        fun createSwimTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.SWIM,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createDieTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.DIE,
                status = BodyStatus.AnimationData.Status.HUNGRY,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun updateTextureRegionData(): BodyStatus.AnimationData {
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
                createDieTextureRegionData()
            } else if (config.animations.eat != null && canPlayEatAnimation) {
                createEatTextureRegionData()
            } else {
                if (hasTurnAnimation && animationData.isFacingRight != expectedIsFacingRight) {
                    createTurnTextureRegionData()
                } else {
                    updateTextureRegionData()
                }
            }
        } else {
            if (isAnimationFinished) {
                if (isDying) {
                    updateTextureRegionData()
                } else {
                    createSwimTextureRegionData()
                }
            } else {
                updateTextureRegionData()
            }
        }
    }
}
