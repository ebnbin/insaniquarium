package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.lifecycle.baseGame

data class BodyLife(
    private val configHealth: BodyConfig.Health?,
    private val configHunger: BodyConfig.Hunger?,
    private val configGrowth: BodyConfig.Growth?,
    private val configDrop: BodyConfig.Drop?,
    private val health: Float?,
    private val hunger: Float?,
    private val growth: Float?,
    private val drop: Float?,
) {
    val isDeadFromHealth: Boolean = configHealth != null && health == 0f

    val hungerStatus: BodyHungerStatus? = configHunger?.status(hunger)

    val transformationFromHunger: BodyType? = configHunger?.transformation?.takeIf { hunger == 0f }

    val transformationFromGrowth: BodyType? = configGrowth?.transformation?.takeIf { growth != null && growth <= 0f }

    val productionFromDrop: BodyType? = configDrop?.production?.takeIf { drop != null && drop <= 0f }

    val dropCount: Int = if (drop == null || drop > 0f) {
        0
    } else {
        -drop.toInt() + 1
    }

    fun nextHealth(
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

    fun nextHunger(
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

    fun nextGrowth(
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

    fun nextDrop(
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
