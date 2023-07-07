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
    val animations: Animations,
    /**
     * If true, the body will disappear when sinking or floating outside water.
     */
    @Expose
    val isDead: Boolean = false,
    @Expose
    val health: Health? = null,
    @Expose
    val hunger: Hunger? = null,
    @Expose
    val growth: Growth? = null,
    @Expose
    val drop: Drop? = null,
    @Expose
    val eatAct: EatAct? = null,
    @Expose
    val touchAct: TouchAct? = null,
    @Expose
    val swimActX: SwimAct? = null,
    @Expose
    val swimActY: SwimAct? = null,
) {
    data class Health(
        @Expose
        val full: Int,
        @Expose
        val init: Int = full,
        @Expose
        val diffPerTick: Int = 0,
    )

    data class Hunger(
        @Expose
        val full: Int,
        /**
         * Range: >= full.
         * == full: Never full.
         */
        @Expose
        val max: Int = full,
        /**
         * Range: >= 0 && <= max.
         */
        @Expose
        val init: Int = full,
        /**
         * Range: >= 0 && <= full.
         * == 0: Never hungry.
         * == full: Always hungry.
         */
        @Expose
        val hungry: Int = 0,
        @Expose
        val diffPerTick: Int = 0,
        @Expose
        val transformation: BodyType? = null,
    )

    data class Growth(
        @Expose
        val full: Int,
        @Expose
        val init: Int = 0,
        @Expose
        val diffPerTick: Int = 0,
        @Expose
        val transformation: BodyType,
    )

    data class Drop(
        @Expose
        val full: Int,
        @Expose
        val init: Int = 0,
        @Expose
        val diffPerTick: Int = 0,
        @Expose
        val production: BodyType,
    )

    data class EatAct(
        @Expose
        val foods: Map<BodyType, Food>,
        @Expose
        val drivingAccelerationX: Float = 0f,
        @Expose
        val drivingAccelerationY: Float = 0f,
    )

    data class Food(
        @Expose
        val healthDiffPerTick: Int = 0,
        @Expose
        val hungerDiffPerTick: Int = 0,
        @Expose
        val growthDiffPerTick: Int = 0,
        @Expose
        val dropDiffPerTick: Float = 0f,
        @Expose
        val health: Int = 0,
        @Expose
        val hunger: Int = 0,
        @Expose
        val growth: Int = 0,
        @Expose
        val drop: Int = 0,
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
        val idlingTicksMin: Int,
        @Expose
        val idlingTicksMax: Int,
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
    )

    companion object {
        const val DEFAULT_DRAG_COEFFICIENT = 1f
        const val DEFAULT_WATER_FRICTION_COEFFICIENT = 0.001f
        const val DEFAULT_BOTTOM_FRICTION_COEFFICIENT = 1f
        const val DEFAULT_LEFT_RIGHT_FRICTION_COEFFICIENT = 0f
    }
}
