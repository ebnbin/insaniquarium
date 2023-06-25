package dev.ebnbin.insaniquarium.body

object BodyDrawHelper {
    fun nextTextureRegionData(
        config: BodyConfig,
        hasTurnAnimation: Boolean,
        textureRegionData: BodyStatus.TextureRegionData,
        isAnimationFinished: Boolean,
        canAnimationActionChange: Boolean,
        expectedIsFacingRight: Boolean,
        eatAct: BodyStatus.EatAct?,
        hunger: Float,
        isDying: Boolean,
        input: BodyInput?,
    ): BodyStatus.TextureRegionData {
        if (input == null) {
            return textureRegionData
        }

        val isHungry = config.eatAct != null &&
            eatAct != null &&
            config.hunger.full != 0f &&
            config.hunger.hungryPercent != BodyConfig.HUNGRY_NEVER &&
            hunger < config.hunger.full * config.hunger.hungryPercent
        val animationStatus = if (isHungry) {
            BodyConfig.AnimationStatus.HUNGRY
        } else {
            BodyConfig.AnimationStatus.NORMAL
        }

        fun createEatTextureRegionData(): BodyStatus.TextureRegionData {
            return BodyStatus.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.EAT,
                animationStatus = animationStatus,
                stateTime = 0f,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        fun createTurnTextureRegionData(): BodyStatus.TextureRegionData {
            return BodyStatus.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.TURN,
                animationStatus = animationStatus,
                stateTime = 0f,
                isFacingRight = expectedIsFacingRight,
            )
        }

        fun createSwimTextureRegionData(): BodyStatus.TextureRegionData {
            return BodyStatus.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.SWIM,
                animationStatus = animationStatus,
                stateTime = 0f,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        fun createDieTextureRegionData(): BodyStatus.TextureRegionData {
            return BodyStatus.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.DIE,
                animationStatus = BodyConfig.AnimationStatus.HUNGRY,
                stateTime = 0f,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        fun updateTextureRegionData(): BodyStatus.TextureRegionData {
            return BodyStatus.TextureRegionData(
                animationAction = textureRegionData.animationAction,
                animationStatus = if (textureRegionData.animationAction == BodyConfig.AnimationAction.DIE) {
                    BodyConfig.AnimationStatus.HUNGRY
                } else {
                    animationStatus
                },
                stateTime = textureRegionData.stateTime + input.delta,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        return if (canAnimationActionChange) {
            if (isDying) {
                createDieTextureRegionData()
            } else if (config.eatAct?.hasAnimation == true && eatAct?.canPlayEatAnimation == true) {
                createEatTextureRegionData()
            } else {
                if (hasTurnAnimation && textureRegionData.isFacingRight != expectedIsFacingRight) {
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
