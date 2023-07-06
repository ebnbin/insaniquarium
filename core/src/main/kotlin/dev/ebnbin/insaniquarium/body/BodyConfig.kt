package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.World

data class BodyConfig(
    @Expose
    val group: BodyGroup,
    @Expose
    val box: Box,
    @Expose
    val life: Life,
    @Expose
    val renderer: Renderer,
) {
    data class Box(
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
    ) {
        companion object {
            const val DEFAULT_DRAG_COEFFICIENT = 1f
            const val DEFAULT_WATER_FRICTION_COEFFICIENT = 0.001f
            const val DEFAULT_BOTTOM_FRICTION_COEFFICIENT = 1f
            const val DEFAULT_LEFT_RIGHT_FRICTION_COEFFICIENT = 0f
        }
    }

    data class Life(
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
    )

    data class Renderer(
        @Expose
        val animations: Animations,
    )

    data class Health(
        @Expose
        val initialThreshold: Float = 1f,
        @Expose
        val diffPerTick: Float = 0f,
    )

    data class Hunger(
        @Expose
        val initialThreshold: Float = 1f,
        /**
         * >= 1f.
         * 1f: Never full.
         */
        @Expose
        val maxThreshold: Float = 1f,
        /**
         * >= 0f && <= 1f.
         * 0f: Never hungry.
         * 1f: Always hungry.
         */
        @Expose
        val hungryThreshold: Float = 0f,
        @Expose
        val diffPerTick: Float = 0f,
        @Expose
        val transformation: BodyType? = null,
    )

    data class Growth(
        @Expose
        val initialThreshold: Float = 1f,
        @Expose
        val diffPerTick: Float = 0f,
        @Expose
        val transformation: BodyType,
    )

    data class Drop(
        @Expose
        val initialThreshold: Float = 1f,
        @Expose
        val diffPerTick: Float = 0f,
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
        val healthDiffPerTick: Float = 0f,
        @Expose
        val hungerDiffPerTick: Float = 0f,
        @Expose
        val growthDiffPerTick: Float = 0f,
        @Expose
        val dropDiffPerTick: Float = 0f,
        @Expose
        val health: Float = 0f,
        @Expose
        val hunger: Float = 0f,
        @Expose
        val growth: Float = 0f,
        @Expose
        val drop: Float = 0f,
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
    )
}
