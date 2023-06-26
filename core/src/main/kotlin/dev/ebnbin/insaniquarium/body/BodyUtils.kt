package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.minMax
import dev.ebnbin.insaniquarium.game

fun BodyType.assets(): Set<Asset<*>> {
    val config = game.config.body.getValue(this)
    return listOfNotNull(
        config.animations.swim,
        config.animations.turn,
        config.animations.eat,
        config.animations.hungry,
        config.animations.hungryTurn,
        config.animations.hungryEat,
        config.animations.die,
    ).mapTo(mutableSetOf()) {
        baseGame.assets.texture.getValue(it.assetId)
    }
}

data class DrivingTarget(
    val type: Type,
    val position: Float,
    val acceleration: Float,
    /**
     * The acceleration multiplier when the direction of velocity is opposite to the direction of the target.
     */
    val oppositeAccelerationMultiplier: Float = DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER,
) {
    enum class Type {
        EAT,
        TOUCH,
        SWIM,
        ;
    }

    companion object {
        private const val DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER = 1.5f
    }
}

enum class BodyRelation {
    DISJOINT,
    OVERLAP,
    CONTAIN_CENTER,
    ;
}

enum class HungerStatus {
    FULL,
    NOT_FULL,
    HUNGRY,
    DYING,
    ;
}

fun BodyConfig.Hunger?.status(hunger: Float?): HungerStatus? {
    if (this == null || hunger == null) {
        return null
    }
    return when {
        hunger == 0f -> {
            // NOT_FULL or HUNGRY or DYING.
            if (canDie) {
                HungerStatus.DYING
            } else {
                // NOT_FULL or HUNGRY.
                if (hungryPercent == 0f) {
                    HungerStatus.NOT_FULL
                } else {
                    HungerStatus.HUNGRY
                }
            }
        }
        hunger >= full -> {
            // FULL or NOT_FULL or HUNGRY.
            if (maxPercent == 1f) {
                // NOT_FULL or HUNGRY.
                if (hungryPercent == 1f) {
                    HungerStatus.HUNGRY
                } else {
                    HungerStatus.NOT_FULL
                }
            } else {
                return HungerStatus.FULL
            }
        }
        hunger >= full * hungryPercent -> {
            // NOT_FULL or HUNGRY.
            if (hungryPercent == 1f) {
                HungerStatus.HUNGRY
            } else {
                HungerStatus.NOT_FULL
            }
        }
        else -> {
            HungerStatus.HUNGRY
        }
    }
}

fun BodyConfig.Hunger.minMax(hunger: Float): Float {
    val maxHunger = full * maxPercent
    return hunger.minMax(0f, maxHunger)
}
