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
        delta: Float,
    ): BodyData.TextureRegionData {
        fun createEatTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.EAT,
                animationStatus = BodyConfig.AnimationStatus.NORMAL,
                stateTime = 0f,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        fun createTurnTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.TURN,
                animationStatus = BodyConfig.AnimationStatus.NORMAL,
                stateTime = 0f,
                isFacingRight = expectedIsFacingRight,
            )
        }

        fun createSwimTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = BodyConfig.AnimationAction.SWIM,
                animationStatus = BodyConfig.AnimationStatus.NORMAL,
                stateTime = 0f,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        fun updateTextureRegionData(): BodyData.TextureRegionData {
            return BodyData.TextureRegionData(
                animationAction = textureRegionData.animationAction,
                animationStatus = BodyConfig.AnimationStatus.NORMAL,
                stateTime = textureRegionData.stateTime + delta,
                isFacingRight = textureRegionData.isFacingRight,
            )
        }

        return if (canAnimationActionChange) {
            if (config.eatAct?.hasAnimation == true && eatAct?.canPlayEatAnimation == true) {
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
                createSwimTextureRegionData()
            } else {
                updateTextureRegionData()
            }
        }
    }
}
