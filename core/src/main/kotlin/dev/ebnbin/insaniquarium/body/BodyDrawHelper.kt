package dev.ebnbin.insaniquarium.body

object BodyDrawHelper {
    fun nextAnimationData(
        config: BodyConfig,
        hasTurnAnimation: Boolean,
        animationData: BodyStatus.AnimationData,
        isAnimationFinished: Boolean,
        canAnimationActionChange: Boolean,
        expectedIsFacingRight: Boolean,
        canPlayEatAnimation: Boolean,
        hungerStatus: HungerStatus?,
        input: BodyInput,
    ): BodyStatus.AnimationData {
        val isDying = hungerStatus == HungerStatus.DYING

        val animationStatus = if (hungerStatus == HungerStatus.HUNGRY || isDying) {
            BodyStatus.AnimationData.Status.HUNGRY
        } else {
            BodyStatus.AnimationData.Status.NORMAL
        }

        fun createEatTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.EAT,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createTurnTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.TURN,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = expectedIsFacingRight,
            )
        }

        fun createSwimTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.SWIM,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createDieTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = BodyStatus.AnimationData.Action.DIE,
                status = BodyStatus.AnimationData.Status.HUNGRY,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun updateTextureRegionData(): BodyStatus.AnimationData {
            return BodyStatus.AnimationData(
                action = animationData.action,
                status = if (animationData.action == BodyStatus.AnimationData.Action.DIE) {
                    BodyStatus.AnimationData.Status.HUNGRY
                } else {
                    animationStatus
                },
                stateTime = animationData.stateTime + input.delta,
                isFacingRight = animationData.isFacingRight,
            )
        }

        return if (canAnimationActionChange) {
            if (isDying) {
                createDieTextureRegionData()
            } else if (config.animations.eat != null && canPlayEatAnimation) {
                createEatTextureRegionData()
            } else {
                if (hasTurnAnimation && animationData.isFacingRight != expectedIsFacingRight) {
                    createTurnTextureRegionData()
                } else {
                    updateTextureRegionData()
                }
            }
        } else {
            if (isAnimationFinished) {
                if (isDying) {
                    updateTextureRegionData()
                } else {
                    createSwimTextureRegionData()
                }
            } else {
                updateTextureRegionData()
            }
        }
    }
}
