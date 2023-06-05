package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose

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
    val assetId: String = "",
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
}
