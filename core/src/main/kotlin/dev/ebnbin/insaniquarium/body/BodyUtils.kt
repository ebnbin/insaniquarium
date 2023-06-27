package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.Color
import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.XY
import dev.ebnbin.gdx.utils.colorMarkup
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.magnitude
import dev.ebnbin.insaniquarium.game

fun BodyType.assets(): Set<Asset<*>> {
    val config = game.config.body.getValue(this)
    return listOfNotNull(
        config.renderer.animations.swim,
        config.renderer.animations.turn,
        config.renderer.animations.eat,
        config.renderer.animations.hungry,
        config.renderer.animations.hungryTurn,
        config.renderer.animations.hungryEat,
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
    ;
}

fun BodyConfig.Hunger?.status(hunger: Float?): BodyHungerStatus? {
    if (this == null || hunger == null) {
        return null
    }
    return when {
        hunger == 0f -> {
            // NOT_FULL or HUNGRY.
            if (hungryThreshold == 0f) {
                BodyHungerStatus.NOT_FULL
            } else {
                BodyHungerStatus.HUNGRY
            }
        }
        hunger >= 1f -> {
            // FULL or NOT_FULL or HUNGRY.
            if (maxThreshold == 1f) {
                // NOT_FULL or HUNGRY.
                if (hungryThreshold == 1f) {
                    BodyHungerStatus.HUNGRY
                } else {
                    BodyHungerStatus.NOT_FULL
                }
            } else {
                return BodyHungerStatus.FULL
            }
        }
        hunger >= hungryThreshold -> {
            BodyHungerStatus.NOT_FULL
        }
        else -> {
            BodyHungerStatus.HUNGRY
        }
    }
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
