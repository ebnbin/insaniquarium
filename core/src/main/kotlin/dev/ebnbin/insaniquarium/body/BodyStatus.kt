package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.animation.TextureRegionAnimation

data class BodyStatus(
    val velocityX: Float = 0f,
    val velocityY: Float = 0f,

    val x: Float = 0f,
    val y: Float = 0f,

    val swimActX: SwimAct? = null,
    val swimActY: SwimAct? = null,

    val health: Float? = null,

    val hunger: Float? = null,

    val disappearAct: DisappearAct? = null,

    val drivingTargetX: DrivingTarget? = null,
    val drivingTargetY: DrivingTarget? = null,

    val expectedIsFacingRight: Boolean = false,

    val textureRegionData: TextureRegionData = TextureRegionData(
        animationAction = BodyConfig.AnimationAction.SWIM,
        animationStatus = BodyConfig.AnimationStatus.NORMAL,
        stateTime = 0f,
        isFacingRight = false,
    ),
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

    enum class Relation {
        DISJOINT,
        OVERLAP,
        CONTAIN_CENTER,
        ;
    }

    data class EatAct(
        val drivingTargetX: DrivingTarget?,
        val drivingTargetY: DrivingTarget?,
        val foodRelation: Relation,
        val hungerDiff: Float,
    )

    data class TextureRegionData(
        val animationAction: BodyConfig.AnimationAction,
        val animationStatus: BodyConfig.AnimationStatus,
        val stateTime: Float,
        val isFacingRight: Boolean,
    ) {
        fun getAnimation(animations: BodyConfig.Animations): TextureRegionAnimation {
            return when (animationAction) {
                BodyConfig.AnimationAction.SWIM -> {
                    when (animationStatus) {
                        BodyConfig.AnimationStatus.NORMAL -> animations.swim
                        BodyConfig.AnimationStatus.HUNGRY -> animations.hungry ?: animations.swim
                    }
                }
                BodyConfig.AnimationAction.TURN -> {
                    when (animationStatus) {
                        BodyConfig.AnimationStatus.NORMAL -> requireNotNull(animations.turn)
                        BodyConfig.AnimationStatus.HUNGRY -> animations.hungryTurn ?: requireNotNull(animations.turn)
                    }
                }
                BodyConfig.AnimationAction.EAT -> {
                    when (animationStatus) {
                        BodyConfig.AnimationStatus.NORMAL -> requireNotNull(animations.eat)
                        BodyConfig.AnimationStatus.HUNGRY -> animations.hungryEat ?: requireNotNull(animations.eat)
                    }
                }
                BodyConfig.AnimationAction.DIE -> {
                    requireNotNull(animationStatus == BodyConfig.AnimationStatus.HUNGRY) // FIXME
                    return animations.die ?: animations.swim
                }
            }
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
}
