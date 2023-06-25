package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.SerializableEnum
import dev.ebnbin.gdx.utils.World

data class BodyConfig(
    @Expose
    val type: BodyType,
    @Expose
    val group: Group,
    @Expose
    val width: Float,
    @Expose
    val height: Float,
    @Expose
    val depth: Float,
    @Expose
    val density: Float = World.DENSITY_WATER,
    @Expose
    val dragCoefficient: Float = World.DEFAULT_DRAG_COEFFICIENT,
    @Expose
    val health: Float = HEALTH_MAX,
    @Expose
    val hunger: Hunger,
    @Expose
    val animations: Animations,
    @Expose
    val touchAct: TouchAct? = null,
    @Expose
    val swimActX: SwimAct? = null,
    @Expose
    val swimActY: SwimAct? = null,
    @Expose
    val eatAct: EatAct? = null,
) {
    enum class Group(override val serializedName: String) : SerializableEnum {
        FOOD("food"),
        FISH("fish"),
        PET("pet"),
        MONEY("money"),
        ;
    }

    /**
     * Max
     * Full
     * Not full (can eat)
     * Hungry (animation changed)
     * Die
     */
    data class Hunger(
        @Expose
        val full: Float = 0f,
        @Expose
        val maxPercent: Float = 1f,
        @Expose
        val hungryPercent: Float = HUNGRY_NEVER,
        @Expose
        val exhaustionPerSecond: Float = 0f,
        @Expose
        val canDie: Boolean = false,
        @Expose
        val corpseDensity: Float? = null,
    )

    data class Animations(
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
        val die: TextureRegionAnimation? = null,
    )

    enum class AnimationType(
        override val serializedName: String,
        val action: AnimationAction,
        val status: AnimationStatus,
    ) : SerializableEnum {
        SWIM("swim", action = AnimationAction.SWIM, status = AnimationStatus.NORMAL),
        TURN("turn", action = AnimationAction.TURN, status = AnimationStatus.NORMAL),
        EAT("eat", action = AnimationAction.EAT, status = AnimationStatus.NORMAL),
        HUNGRY_SWIM("hungry_swim", action = AnimationAction.SWIM, status = AnimationStatus.HUNGRY),
        HUNGRY_TURN("hungry_turn", action = AnimationAction.TURN, status = AnimationStatus.HUNGRY),
        HUNGRY_EAT("hungry_eat", action = AnimationAction.EAT, status = AnimationStatus.HUNGRY),
        DIE("die", action = AnimationAction.DIE, status = AnimationStatus.HUNGRY),
        ;

        companion object {
            fun of(action: AnimationAction, status: AnimationStatus): AnimationType {
                return values().single { it.action == action && it.status == status }
            }
        }
    }

    enum class AnimationAction(override val serializedName: String) : SerializableEnum {
        SWIM("swim"),
        TURN("turn"),
        EAT("eat"),
        DIE("die"),
        ;
    }

    enum class AnimationStatus(override val serializedName: String) : SerializableEnum {
        NORMAL("normal"),
        HUNGRY("hungry"),
        ;
    }

    data class TouchAct(
        @Expose
        val accelerationX: Float,
        @Expose
        val accelerationY: Float,
    )

    data class SwimAct(
        @Expose
        val acceleration: Float,
        @Expose
        val idlingTimeRandomStart: Float,
        @Expose
        val idlingTimeRandomEnd: Float,
    )

    data class EatAct(
        @Expose
        val foods: Map<BodyType, Food>,
        @Expose
        val accelerationX: Float,
        @Expose
        val accelerationY: Float,
        @Expose
        val hasAnimation: Boolean = false,
    )

    data class Food(
        @Expose
        val damagePerSecond: Float = 0f,
        @Expose
        val hunger: Float = 0f,
    )

    companion object {
        const val HEALTH_MAX = -1f
        const val HUNGRY_NEVER = -1f
    }
}
