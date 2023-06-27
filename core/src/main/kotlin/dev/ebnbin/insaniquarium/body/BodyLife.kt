package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.lifecycle.baseGame

data class BodyLife(
    private val configHealth: BodyConfig.Health?,
    private val configHunger: BodyConfig.Hunger?,
    private val configGrowth: BodyConfig.Growth?,
    private val configDrop: BodyConfig.Drop?,
    private val status: Status,
) {
    data class Status(
        val health: Float? = null,
        val hunger: Float? = null,
        val growth: Float? = null,
        val drop: Float? = null,
    )

    val health: Float? = status.health
    private val hunger: Float? = status.hunger
    private val growth: Float? = status.growth
    private val drop: Float? = status.drop

    private val isDeadFromHealth: Boolean = configHealth != null && health == 0f

    val hungerStatus: BodyHungerStatus? = configHunger?.status(hunger)

    private val transformationFromHunger: BodyType? = configHunger?.transformation?.takeIf { hunger == 0f }

    private val transformationFromGrowth: BodyType? = configGrowth?.transformation?.takeIf { growth != null && growth <= 0f }

    private val productionFromDrop: BodyType? = configDrop?.production?.takeIf { drop != null && drop <= 0f }

    private val dropCount: Int = if (drop == null || drop > 0f) {
        0
    } else {
        -drop.toInt() + 1
    }

    fun canRemove(isSwimming: Boolean): Boolean {
        return isDeadFromHealth || (transformationFromHunger != null && isSwimming) || transformationFromGrowth != null
    }

    fun nextStatus(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Status {
        val nextHealth = nextHealth(input, food)
        val nextHunger = nextHunger(input, food)
        val nextGrowth = nextGrowth(input, food)
        val nextDrop = nextDrop(input, food)
        return Status(
            health = nextHealth,
            hunger = nextHunger,
            growth = nextGrowth,
            drop = nextDrop,
        )
    }

    private fun nextHealth(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        configHealth ?: return null
        return nextValue(
            value = health,
            initialThreshold = configHealth.initialThreshold,
            diffPerSecond = configHealth.diffPerSecond,
            inputDelta = input.delta,
            inputDiff = input.healthDiff,
            foodDiff = food?.health,
        ).coerceIn(0f, 1f)
    }

    private fun nextHunger(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        configHunger ?: return null
        return nextValue(
            value = hunger,
            initialThreshold = configHunger.initialThreshold,
            diffPerSecond = configHunger.diffPerSecond,
            inputDelta = input.delta,
            inputDiff = input.hungerDiff,
            foodDiff = food?.hunger,
        ).coerceIn(0f, configHunger.maxThreshold)
    }

    private fun nextGrowth(
        input: BodyInput,
        food: BodyConfig.Food?
    ): Float? {
        configGrowth ?: return null
        return nextValue(
            value = growth,
            initialThreshold = configGrowth.initialThreshold,
            diffPerSecond = configGrowth.diffPerSecond,
            inputDelta = input.delta,
            inputDiff = input.growthDiff,
            foodDiff = food?.growth,
        )
    }

    private fun nextDrop(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        configDrop ?: return null
        return nextValue(
            value = drop,
            initialThreshold = configDrop.initialThreshold,
            diffPerSecond = configDrop.diffPerSecond,
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
        bodyManager: BodyManager,
        status: BodyStatus,
        delta: Float,
    ): Boolean {
        if (isDeadFromHealth) {
            bodyManager.removeSelf()
            return true
        }
        if (transformationFromHunger != null &&
            status.renderer.animationData.action == BodyAnimationData.Action.SWIM) {
            bodyManager.replaceBody(
                type = transformationFromHunger,
                createStatus = {
                    status.copy(
                        swimActX = null,
                        swimActY = null,
                        life = Status(),
                        drivingTargetX = null,
                        drivingTargetY = null,
                        renderer = status.renderer.copy(
                            animationData = status.renderer.animationData.copy(
                                stateTime = 0f,
                            ),
                            alphaTime = null,
                        ),
                    )
                },
                delta = delta,
            )
            return true
        }
        if (transformationFromGrowth != null) {
            val growth = status.life.growth
            require(growth != null)
            bodyManager.replaceBody(
                type = transformationFromGrowth,
                createStatus = {
                    status.copy(
                        life = status.life.copy(
                            growth = if (it.config.growth == null) {
                                null
                            } else {
                                growth + it.config.growth.initialThreshold
                            },
                        ),
                    )
                },
                delta = delta,
            )
        }
        if (productionFromDrop != null) {
            repeat(dropCount) {
                bodyManager.addBody(
                    type = productionFromDrop,
                    createStatus = {
                        BodyStatus(
                            box = BodyBox.Status(
                                x = status.box.x,
                                y = status.box.y,
                            ),
                        )
                    },
                    delta = delta,
                )
            }
            bodyManager.actSelf(
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
