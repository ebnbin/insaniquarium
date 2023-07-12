package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Position

data class BodyState(
    val position: Position = Position(),

    /**
     * null: Neither targeting nor idling.
     * 0: Targeting.
     * > 0: Idling.
     */
    val swimTicksX: Int? = null,
    val swimTicksY: Int? = null,

    val drivingTargetX: BodyDrivingTarget? = null,
    val drivingTargetY: BodyDrivingTarget? = null,

    val health: Int? = null,
    val hunger: Int? = null,
    val growth: Int? = null,
    val drop: Int? = null,
    val energy: Int? = null,
    val isCharging: Boolean? = null,

    val animationData: BodyAnimationState = BodyAnimationState(),
    /**
     * >= 0f: Delaying.
     * < 0f: Changing alpha.
     */
    val alphaTime: Float? = null,
    val scaleTransform: BodyData.ScaleTransform? = null,

    val velocityX: Float = 0f,
    val velocityY: Float = 0f,
)
