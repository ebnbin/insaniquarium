package dev.ebnbin.insaniquarium.body

data class BodyStatus(
    val box: BodyBox.Status = BodyBox.Status(),

    /**
     * null: Neither targeting nor idling.
     * 0f: Targeting or finish idling.
     * > 0f: Idling.
     */
    val swimTimeX: Float? = null,
    val swimTimeY: Float? = null,

    val life: BodyLife.Status = BodyLife.Status(),

    val drivingTargetX: BodyDrivingTarget? = null,
    val drivingTargetY: BodyDrivingTarget? = null,

    val renderer: BodyRenderer.Status = BodyRenderer.Status(),
)
