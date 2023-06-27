package dev.ebnbin.insaniquarium.body

data class BodyStatus(
    val box: BodyBox.Status = BodyBox.Status(),

    val swimActX: SwimAct? = null,
    val swimActY: SwimAct? = null,

    val health: Float? = null,

    val hunger: Float? = null,

    val growth: Float? = null,

    val drop: Float? = null,

    val alphaTime: Float? = null,

    val drivingTargetX: BodyDrivingTarget? = null,
    val drivingTargetY: BodyDrivingTarget? = null,

    val animationData: BodyAnimationData = BodyAnimationData(),
) {
    data class SwimAct(
        val remainingTime: Float,
    )
}
