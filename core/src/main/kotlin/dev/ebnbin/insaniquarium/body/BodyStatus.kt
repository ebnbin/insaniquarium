package dev.ebnbin.insaniquarium.body

data class BodyStatus(
    val velocityX: Float = 0f,
    val velocityY: Float = 0f,

    val x: Float = 0f,
    val y: Float = 0f,

    val swimActX: SwimAct? = null,
    val swimActY: SwimAct? = null,

    val health: Float? = null,

    val hunger: Float? = null,

    val growth: Float? = null,

    val drop: Float? = null,

    val disappearAct: DisappearAct? = null,

    val drivingTargetX: BodyDrivingTarget? = null,
    val drivingTargetY: BodyDrivingTarget? = null,

    val animationData: BodyAnimationData = BodyAnimationData(),
) {
    data class SwimAct(
        val remainingTime: Float,
    )

    data class DisappearAct(
        /**
         * >= 0f: Delaying.
         * < 0f: Disappearing.
         */
        val time: Float = DELAY_DURATION,
    ) {
        companion object {
            const val DELAY_DURATION = 0f
            const val DISAPPEAR_DURATION = 1f
        }
    }
}
