package dev.ebnbin.insaniquarium.body

object BodyDrawHelper {
    fun nextTextureRegionData(
        config: BodyConfig,
        hasTurnAnimation: Boolean,
        textureRegionData: BodyData.TextureRegionData,
        isAnimationFinished: Boolean,
        canAnimationActionChange: Boolean,
        expectedIsFacingRight: Boolean,
        eatAct: BodyData.EatAct?,
        input: BodyInput?,
    ): BodyData.TextureRegionData {
        if (input == null) {
            return textureRegionData
        }

        val isHungry = config.eatAct != null &&
            eatAct != null &&
            config.eatAct.fullHunger != 0f &&
            config.eatAct.hungryHungerPercent != BodyConfig.HUNGRY_NEVER &&
            eatAct.hunger < config.eatAct.fullHunger * config.eatAct.hungryHungerPercent
        val animationStatus = if (isHungry) {
            BodyConfig.AnimationStatus.HUNGRY
        } else {
            BodyConfig.AnimationStatus.NORMAL
        }

        fun createEatTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.EAT,
                animationStatus = animationStatus,
                stateTime = 0f,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        fun createTurnTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.TURN,
                animationStatus = animationStatus,
                stateTime = 0f,
                isFacingRight = expectedIsFacingRight,
            )
        }

        fun createSwimTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.SWIM,
                animationStatus = animationStatus,
                stateTime = 0f,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        fun createDieTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.DIE,
                animationStatus = BodyConfig.AnimationStatus.HUNGRY,
                stateTime = 0f,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        fun updateTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = textureRegionData.animationAction,
                animationStatus = animationStatus,
                stateTime = textureRegionData.stateTime + input.delta,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        return if (canAnimationActionChange) {
            if (eatAct?.isDying == true) {
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
                if (eatAct?.isDying == true) {
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
