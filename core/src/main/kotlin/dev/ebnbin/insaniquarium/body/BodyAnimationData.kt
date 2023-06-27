package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.SerializableEnum

data class BodyAnimationData(
    val action: Action = Action.SWIM,
    val status: Status = Status.NORMAL,
    val stateTime: Float = 0f,
    val isFacingRight: Boolean = false,
) {
    enum class Action(override val serializedName: String) : SerializableEnum {
        SWIM("swim"),
        TURN("turn"),
        EAT("eat"),
        ;
    }

    enum class Status(override val serializedName: String) : SerializableEnum {
        NORMAL("normal"),
        HUNGRY("hungry"),
        ;
    }

    fun getAnimation(config: BodyConfig): TextureRegionAnimation {
        return when (action) {
            Action.SWIM -> {
                when (status) {
                    Status.NORMAL -> {
                        config.animations.swim
                    }
                    Status.HUNGRY -> {
                        config.animations.hungry ?: config.animations.swim
                    }
                }
            }
            Action.TURN -> {
                when (status) {
                    Status.NORMAL -> {
                        requireNotNull(config.animations.turn)
                    }
                    Status.HUNGRY -> {
                        config.animations.hungryTurn ?: requireNotNull(config.animations.turn)
                    }
                }
            }
            Action.EAT -> {
                when (status) {
                    Status.NORMAL -> {
                        requireNotNull(config.animations.eat)
                    }
                    Status.HUNGRY -> {
                        config.animations.hungryEat ?: requireNotNull(config.animations.eat)
                    }
                }
            }
        }
    }
}
