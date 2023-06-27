package dev.ebnbin.insaniquarium.body

data class BodyStatus(
    val box: BodyBox.Status = BodyBox.Status(),

    val swimActX: SwimAct? = null,
    val swimActY: SwimAct? = null,

    val life: BodyLife.Status = BodyLife.Status(),

    val drivingTargetX: BodyDrivingTarget? = null,
    val drivingTargetY: BodyDrivingTarget? = null,

    val renderer: BodyRenderer.Status = BodyRenderer.Status(),
) {
    data class SwimAct(
        val remainingTime: Float,
    )
}
