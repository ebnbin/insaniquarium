package dev.ebnbin.insaniquarium.body

data class BodyStatus(
    val box: BodyBox.Status = BodyBox.Status(),

    val swimActX: SwimAct? = null,
    val swimActY: SwimAct? = null,

    val life: BodyLife.Status = BodyLife.Status(),

    val alphaTime: Float? = null,

    val drivingTargetX: BodyDrivingTarget? = null,
    val drivingTargetY: BodyDrivingTarget? = null,

    val animationData: BodyAnimationData = BodyAnimationData(),
) {
    data class SwimAct(
        val remainingTime: Float,
    )
}
