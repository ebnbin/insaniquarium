package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.SerializableEnum

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

    val animationData: AnimationData = AnimationData(),
) {
    data class DrivingTarget(
        val type: Type,
        val position: Float,
        val acceleration: Float,
        /**
         * The acceleration multiplier when the direction of velocity is opposite to the direction of the target.
         */
        val oppositeAccelerationMultiplier: Float = DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER,
    ) {
        enum class Type {
            EAT,
            TOUCH,
            SWIM,
            ;
        }

        companion object {
            private const val DEFAULT_OPPOSITE_ACCELERATION_MULTIPLIER = 1.5f
        }
    }

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

    data class AnimationData(
        val action: Action = Action.SWIM,
        val status: Status = Status.NORMAL,
        val stateTime: Float = 0f,
        val isFacingRight: Boolean = false,
    ) {
        enum class Action(override val serializedName: String) : SerializableEnum {
            SWIM("swim"),
            TURN("turn"),
            EAT("eat"),
            DIE("die"),
            ;
        }

        enum class Status(override val serializedName: String) : SerializableEnum {
            NORMAL("normal"),
            HUNGRY("hungry"),
            ;
        }
    }
}
