package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.animation.TextureRegionAnimation

data class BodyStatus(
    val velocityX: Float,
    val velocityY: Float,

    val x: Float,
    val y: Float,

    val touchAct: TouchAct?,

    val swimActX: SwimAct?,
    val swimActY: SwimAct?,

    val disappearAct: DisappearAct?,

    val eatAct: EatAct?,

    val expectedIsFacingRight: Boolean,

    val textureRegionData: TextureRegionData,

    val health: Float,

    val hunger: Float,
) {
    data class DrivingTarget(
        val position: Float,
        val acceleration: Float,
        /**
         * The acceleration multiplier when the direction of velocity is opposite to the direction of the target.
         */
        val oppositeAccelerationMultiplier: Float = DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER,
    ) {
        companion object {
            private const val DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER = 1.5f
        }
    }

    data class TouchAct(
        val drivingTargetX: DrivingTarget,
        val drivingTargetY: DrivingTarget,
    )

    data class SwimAct(
        val drivingTarget: DrivingTarget?,
        val remainingTime: Float,
    )

    data class DisappearAct(
        /**
         * >= 0f: Delaying.
         * < 0f: Disappearing.
         */
        val time: Float = DELAY_DURATION,
    ) {
        val canRemove: Boolean = time <= -DISAPPEAR_DURATION

        companion object {
            const val DELAY_DURATION = 0f
            const val DISAPPEAR_DURATION = 1f
        }
    }

    data class EatAct(
        val drivingTargetX: DrivingTarget?,
        val drivingTargetY: DrivingTarget?,
        val canPlayEatAnimation: Boolean,
        val hungerDiff: Float,
    )

    data class TextureRegionData(
        val animationAction: BodyConfig.AnimationAction,
        val animationStatus: BodyConfig.AnimationStatus,
        val stateTime: Float,
        val isFacingRight: Boolean,
    ) {
        val animationType: BodyConfig.AnimationType = BodyConfig.AnimationType.of(animationAction, animationStatus)

        fun getAnimation(animations: BodyConfig.Animations): TextureRegionAnimation {
            return when (animationType) {
                BodyConfig.AnimationType.SWIM -> {
                    animations.swim
                }
                BodyConfig.AnimationType.TURN -> {
                    requireNotNull(animations.turn)
                }
                BodyConfig.AnimationType.EAT -> {
                    requireNotNull(animations.eat)
                }
                BodyConfig.AnimationType.HUNGRY_SWIM -> {
                    animations.hungry ?: animations.swim
                }
                BodyConfig.AnimationType.HUNGRY_TURN -> {
                    animations.hungryTurn ?: requireNotNull(animations.turn)
                }
                BodyConfig.AnimationType.HUNGRY_EAT -> {
                    animations.hungryEat ?: requireNotNull(animations.eat)
                }
                BodyConfig.AnimationType.DIE -> {
                    animations.die ?: animations.swim
                }
            }
        }
    }
}
