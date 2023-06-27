package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.minMax

data class BodyLife(
    val configHealth: BodyConfig.Health?,
    val configHunger: BodyConfig.Hunger?,
    val configGrowth: BodyConfig.Growth?,
    val configDrop: BodyConfig.Drop?,
    val health: Float?,
    val hunger: Float?,
    val growth: Float?,
    val drop: Float?,
) {
    val isDeadFromHealth: Boolean = configHealth != null && health == 0f

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
        ).minMax(0f, 1f)
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
        ).minMax(0f, configHunger.maxThreshold)
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
}
