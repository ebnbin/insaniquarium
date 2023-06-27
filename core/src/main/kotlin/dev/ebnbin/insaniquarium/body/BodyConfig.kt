package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.World

data class BodyConfig(
    @Expose
    val group: BodyGroup,
    @Expose
    val width: Float,
    @Expose
    val height: Float,
    @Expose
    val depth: Float,
    @Expose
    val density: Float = World.DENSITY_WATER,
    @Expose
    val dragCoefficient: Float = DEFAULT_DRAG_COEFFICIENT,
    @Expose
    val waterFrictionCoefficient: Float = DEFAULT_WATER_FRICTION_COEFFICIENT,
    @Expose
    val bottomFrictionCoefficient: Float = DEFAULT_BOTTOM_FRICTION_COEFFICIENT,
    @Expose
    val leftRightFrictionCoefficient: Float = DEFAULT_LEFT_RIGHT_FRICTION_COEFFICIENT,
    @Expose
    val health: Health? = null,
    @Expose
    val hunger: Hunger? = null,
    @Expose
    val growth: Growth? = null,
    @Expose
    val drop: Drop? = null,
    @Expose
    val touchAct: TouchAct? = null,
    @Expose
    val swimActX: SwimAct? = null,
    @Expose
    val swimActY: SwimAct? = null,
    @Expose
    val animations: Animations,
) {
    data class Health(
        /**
         * > 0f.
         */
        @Expose
        val full: Float,
    )

    data class Hunger(
        @Expose
        /**
         * >= 0f.
         * 0f: Never full.
         */
        val full: Float = 0f,
        /**
         * >= 1f.
         * 1f: Never full.
         */
        @Expose
        val maxPercent: Float = 1f,
        /**
         * >= 0f && <= 1f.
         * 0f: Never hungry.
         * 1f: Always hungry.
         */
        @Expose
        val hungryPercent: Float = 0f,
        @Expose
        val diffPerSecond: Float = 0f,
        /**
         * Whether die when hunger == 0f.
         */
        @Expose
        val canDie: Boolean = false,
        @Expose
        val foods: Map<BodyType, Food> = emptyMap(),
        @Expose
        val drivingAccelerationX: Float = 0f,
        @Expose
        val drivingAccelerationY: Float = 0f,
        @Expose
        val corpseDensity: Float? = null,
    )

    data class Growth(
        @Expose
        val full: Float,
        @Expose
        val bodyType: BodyType,
    )

    data class Food(
        @Expose
        val healthDiffPerSecond: Float = 0f,
        @Expose
        val hungerDiffPerSecond: Float = 0f,
        @Expose
        val growthDiffPerSecond: Float = 0f,
        @Expose
        val dropDiffPerSecond: Float = 0f,
        @Expose
        val health: Float = 0f,
        @Expose
        val hunger: Float = 0f,
        @Expose
        val growth: Float = 0f,
        @Expose
        val drop: Float = 0f,
    )

    data class Drop(
        @Expose
        val full: Float,
        @Expose
        val incrementPerSecond: Float = 0f,
        @Expose
        val product: BodyType,
    )

    data class TouchAct(
        @Expose
        val drivingAccelerationX: Float,
        @Expose
        val drivingAccelerationY: Float,
    )

    data class SwimAct(
        @Expose
        val drivingAcceleration: Float,
        @Expose
        val idlingTimeRandomStart: Float,
        @Expose
        val idlingTimeRandomEnd: Float,
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

    companion object {
        const val DEFAULT_DRAG_COEFFICIENT = 1f
        const val DEFAULT_WATER_FRICTION_COEFFICIENT = 0.001f
        const val DEFAULT_BOTTOM_FRICTION_COEFFICIENT = 1f
        const val DEFAULT_LEFT_RIGHT_FRICTION_COEFFICIENT = 0f
    }
}
