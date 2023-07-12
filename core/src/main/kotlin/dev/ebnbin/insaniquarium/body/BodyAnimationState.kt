package dev.ebnbin.insaniquarium.body

data class BodyAnimationState(
    val action: BodyAnimations.Action = BodyAnimations.Action.SWIM,
    val status: BodyAnimations.Status = BodyAnimations.Status.NORMAL,
    val stateTick: Int = 0,
    val isFacingRight: Boolean = false,
)
