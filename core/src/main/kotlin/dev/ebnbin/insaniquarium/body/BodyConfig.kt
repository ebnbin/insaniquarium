package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.utils.AnimMode
import dev.ebnbin.gdx.utils.Interpolation

data class BodyConfig(
    @Expose
    val id: String = "",
    @Expose
    val width: Float = 0f,
    @Expose
    val height: Float = 0f,
    @Expose
    val depth: Float = 0f,
    @Expose
    val density: Float? = null,
    @Expose
    val dragCoefficient: Float? = null,
    @Expose
    val touchAct: TouchAct? = null,
    @Expose
    val swimActX: SwimAct? = null,
    @Expose
    val swimActY: SwimAct? = null,
    @Expose
    val anim: Anim = Anim(),
) {
    data class TouchAct(
        @Expose
        val accelerationX: Float = 0f,
        @Expose
        val accelerationY: Float = 0f,
    )

    data class SwimAct(
        @Expose
        val acceleration: Float = 0f,
        @Expose
        val idlingTimeRandomStart: Float = 0f,
        @Expose
        val idlingTimeRandomEnd: Float = 0f,
    )

    data class Anim(
        @Expose
        val assetId: String = "",
        @Expose
        val duration: Float = 0f,
        @Expose
        val mode: AnimMode = AnimMode.NORMAL,
        @Expose
        val interpolation: Interpolation = Interpolation.LINEAR,
    )
}
