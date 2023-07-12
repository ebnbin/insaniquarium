package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.SerializableEnum

data class BodyAnimations(
    @Expose
    val swim: TextureRegionAnimation,
    @Expose
    val turn: TextureRegionAnimation? = null,
    @Expose
    val eat: TextureRegionAnimation? = null,
    @Expose
    val hungry: TextureRegionAnimation? = null,
    @Expose
    val hungryTurn: TextureRegionAnimation? = null,
    @Expose
    val hungryEat: TextureRegionAnimation? = null,
    @Expose
    val charged: TextureRegionAnimation? = null,
    @Expose
    val charge: TextureRegionAnimation? = null,
    @Expose
    val discharge: TextureRegionAnimation? = null,
) {
    enum class Action(override val serializedName: String) : SerializableEnum {
        SWIM("swim"),
        TURN("turn"),
        EAT("eat"),
        CHARGE("charge"),
        DISCHARGE("discharge"),
        ;
    }

    enum class Status(override val serializedName: String) : SerializableEnum {
        NORMAL("normal"),
        HUNGRY("hungry"),
        CHARGED("charged"),
        ;
    }

    fun get(action: Action, status: Status): TextureRegionAnimation {
        return when (action) {
            Action.SWIM -> {
                when (status) {
                    Status.NORMAL -> {
                        swim
                    }
                    Status.HUNGRY -> {
                        hungry ?: swim
                    }
                    Status.CHARGED -> {
                        charged ?: swim
                    }
                }
            }
            Action.TURN -> {
                when (status) {
                    Status.NORMAL -> {
                        requireNotNull(turn)
                    }
                    Status.HUNGRY -> {
                        hungryTurn ?: requireNotNull(turn)
                    }
                    Status.CHARGED -> {
                        requireNotNull(turn)
                    }
                }
            }
            Action.EAT -> {
                when (status) {
                    Status.NORMAL -> {
                        requireNotNull(eat)
                    }
                    Status.HUNGRY -> {
                        hungryEat ?: requireNotNull(eat)
                    }
                    Status.CHARGED -> {
                        requireNotNull(eat)
                    }
                }
            }
            Action.CHARGE -> {
                requireNotNull(charge)
            }
            Action.DISCHARGE -> {
                requireNotNull(discharge)
            }
        }
    }
}
