package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.Color
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.XY
import dev.ebnbin.gdx.utils.colorMarkup
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.magnitude
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

enum class BodyRelation {
    DISJOINT,
    OVERLAP,
    CONTAIN_CENTER,
    ;
}

enum class BodyHungerStatus {
    FULL,
    NOT_FULL,
    HUNGRY,
    DYING,
    ;
}

fun BodyConfig.Hunger?.status(hunger: Float?): BodyHungerStatus? {
    if (this == null || hunger == null) {
        return null
    }
    return when {
        hunger == 0f -> {
            // NOT_FULL or HUNGRY or DYING.
            if (canDie) {
                BodyHungerStatus.DYING
            } else {
                // NOT_FULL or HUNGRY.
                if (hungryPercent == 0f) {
                    BodyHungerStatus.NOT_FULL
                } else {
                    BodyHungerStatus.HUNGRY
                }
            }
        }
        hunger >= full -> {
            // FULL or NOT_FULL or HUNGRY.
            if (maxPercent == 1f) {
                // NOT_FULL or HUNGRY.
                if (hungryPercent == 1f) {
                    BodyHungerStatus.HUNGRY
                } else {
                    BodyHungerStatus.NOT_FULL
                }
            } else {
                return BodyHungerStatus.FULL
            }
        }
        hunger >= full * hungryPercent -> {
            // NOT_FULL or HUNGRY.
            if (hungryPercent == 1f) {
                BodyHungerStatus.HUNGRY
            } else {
                BodyHungerStatus.NOT_FULL
            }
        }
        else -> {
            BodyHungerStatus.HUNGRY
        }
    }
}

fun BodyConfig.Hunger.minMax(hunger: Float): Float {
    val maxHunger = full * maxPercent
    return hunger.minMax(0f, maxHunger)
}

fun BodyStatus.AnimationData.getAnimation(config: BodyConfig): TextureRegionAnimation {
    return when (action) {
        BodyStatus.AnimationData.Action.SWIM -> {
            when (status) {
                BodyStatus.AnimationData.Status.NORMAL -> {
                    config.animations.swim
                }
                BodyStatus.AnimationData.Status.HUNGRY -> {
                    config.animations.hungry ?: config.animations.swim
                }
            }
        }
        BodyStatus.AnimationData.Action.TURN -> {
            when (status) {
                BodyStatus.AnimationData.Status.NORMAL -> {
                    requireNotNull(config.animations.turn)
                }
                BodyStatus.AnimationData.Status.HUNGRY -> {
                    config.animations.hungryTurn ?: requireNotNull(config.animations.turn)
                }
            }
        }
        BodyStatus.AnimationData.Action.EAT -> {
            when (status) {
                BodyStatus.AnimationData.Status.NORMAL -> {
                    requireNotNull(config.animations.eat)
                }
                BodyStatus.AnimationData.Status.HUNGRY -> {
                    config.animations.hungryEat ?: requireNotNull(config.animations.eat)
                }
            }
        }
        BodyStatus.AnimationData.Action.DIE -> {
            requireNotNull(status == BodyStatus.AnimationData.Status.HUNGRY) // FIXME
            return config.animations.die ?: config.animations.swim
        }
    }
}

fun BodyStatus.DisappearAct.canRemove(): Boolean {
    return time <= -BodyStatus.DisappearAct.DISAPPEAR_DURATION
}

fun Float.devText(xy: XY? = null): String {
    val directionText = when (direction) {
        Direction.ZERO -> " "
        Direction.POSITIVE -> when (xy) {
            XY.X -> "►"
            XY.Y -> "▲"
            null -> "+"
        }.colorMarkup(Color.GREEN)
        Direction.NEGATIVE -> when (xy) {
            XY.X -> "◄"
            XY.Y -> "▼"
            null -> "-"
        }.colorMarkup(Color.RED)
    }
    var nonZero = false
    val magnitudeText = "%11.6f".format(magnitude)
        .reversed()
        .map {
            if (nonZero) {
                "$it"
            } else {
                when (it) {
                    '0' -> {
                        "$it".colorMarkup(Color.GRAY)
                    }
                    '.' -> {
                        nonZero = true
                        "$it".colorMarkup(Color.GRAY)
                    }
                    else -> {
                        nonZero = true
                        "$it"
                    }
                }
            }
        }
        .reversed()
        .joinToString("")
    return "$directionText$magnitudeText"
}
