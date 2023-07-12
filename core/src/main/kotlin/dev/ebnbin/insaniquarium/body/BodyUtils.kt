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
        config.animations.swim,
        config.animations.turn,
        config.animations.eat,
        config.animations.hungry,
        config.animations.hungryTurn,
        config.animations.hungryEat,
        config.animations.actionA,
        config.animations.actionB,
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

fun BodyConfig.Hunger?.status(hunger: Int?): BodyHungerStatus? {
    if (this == null || hunger == null) {
        return null
    }
    return when {
        hunger == 0 -> {
            // NOT_FULL or HUNGRY.
            if (hungry == 0) {
                BodyHungerStatus.NOT_FULL
            } else {
                BodyHungerStatus.HUNGRY
            }
        }
        hunger >= full -> {
            // FULL or NOT_FULL or HUNGRY.
            if (max == full) {
                // NOT_FULL or HUNGRY.
                if (hungry == 1) {
                    BodyHungerStatus.HUNGRY
                } else {
                    BodyHungerStatus.NOT_FULL
                }
            } else {
                return BodyHungerStatus.FULL
            }
        }
        hunger >= hungry -> {
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
