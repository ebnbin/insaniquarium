package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.utils.AnimMode
import dev.ebnbin.gdx.utils.Interpolation
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
    val touchAct: TouchAct? = null,
    @Expose
    val swimActX: SwimAct? = null,
    @Expose
    val swimActY: SwimAct? = null,
    @Expose
    val disappearAct: DisappearAct? = null,
    @Expose
    val anim: Anim,
) {
    enum class Group(override val serializedName: String) : SerializableEnum {
        PET("pet"),
        MONEY("money"),
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

    data class Anim(
        @Expose
        val assetId: String,
        @Expose
        val duration: Float = 1f,
        @Expose
        val mode: AnimMode = AnimMode.NORMAL,
        @Expose
        val interpolation: Interpolation = Interpolation.LINEAR,
    )
}
