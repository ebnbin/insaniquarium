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

    val isSwimming: Boolean = action == Action.SWIM
    val canEat: Boolean = action == Action.SWIM || action == Action.EAT

    fun getAnimation(configAnimations: BodyConfig.Animations): TextureRegionAnimation {
        return when (action) {
            Action.SWIM -> {
                when (status) {
                    Status.NORMAL -> {
                        configAnimations.swim
                    }
                    Status.HUNGRY -> {
                        configAnimations.hungry ?: configAnimations.swim
                    }
                }
            }
            Action.TURN -> {
                when (status) {
                    Status.NORMAL -> {
                        requireNotNull(configAnimations.turn)
                    }
                    Status.HUNGRY -> {
                        configAnimations.hungryTurn ?: requireNotNull(configAnimations.turn)
                    }
                }
            }
            Action.EAT -> {
                when (status) {
                    Status.NORMAL -> {
                        requireNotNull(configAnimations.eat)
                    }
                    Status.HUNGRY -> {
                        configAnimations.hungryEat ?: requireNotNull(configAnimations.eat)
                    }
                }
            }
        }
    }
}
