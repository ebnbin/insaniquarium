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
    val actionA: TextureRegionAnimation? = null,
    @Expose
    val actionB: TextureRegionAnimation? = null,
) {
    enum class Action(override val serializedName: String) : SerializableEnum {
        SWIM("swim"),
        TURN("turn"),
        EAT("eat"),
        A("a"),
        B("b"),
        ;
    }

    enum class Status(override val serializedName: String) : SerializableEnum {
        NORMAL("normal"),
        HUNGRY("hungry"),
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
                }
            }
            Action.A -> {
                requireNotNull(actionA)
            }
            Action.B -> {
                requireNotNull(actionB)
            }
        }
    }
}
