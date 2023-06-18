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
    val animations: Map<String, TextureRegionAnimation>,
    @Expose
    val touchAct: TouchAct? = null,
    @Expose
    val swimActX: SwimAct? = null,
    @Expose
    val swimActY: SwimAct? = null,
    @Expose
    val turnAct: TurnAct? = null,
    @Expose
    val disappearAct: DisappearAct? = null,
    @Expose
    val eatAct: EatAct? = null,
) {
    enum class Group(override val serializedName: String) : SerializableEnum {
        PET("pet"),
        MONEY("money"),
        ;
    }

    enum class AnimationType(
        override val serializedName: String,
        val canInterrupt: Boolean,
    ) : SerializableEnum {
        IDLE("idle", canInterrupt = true),
        TURN("turn", canInterrupt = false),
        EAT("eat", canInterrupt = false),
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

    data class DisappearAct(
        @Expose
        val delay: Float = 0f,
    )

    data class EatAct(
        @Expose
        val foodTypeSet: Set<BodyType>,
        @Expose
        val accelerationX: Float,
        @Expose
        val accelerationY: Float,
        @Expose
        val animationType: AnimationType? = null,
    )

    data class TurnAct(
        @Expose
        val animationType: AnimationType,
    )
}
