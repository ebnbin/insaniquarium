package dev.ebnbin.insaniquarium.body

data class BodyAnimationState(
    val action: BodyAnimations.Action = BodyAnimations.Action.SWIM,
    val isHungry: Boolean = false,
    val isCharged: Boolean = false,
    val stateTick: Int = 0,
    val isFacingRight: Boolean = false,
)
