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
        hungerStatus: HungerStatus?,
        input: BodyInput,
    ): BodyStatus.TextureRegionData {
        val isDying = hungerStatus == HungerStatus.DYING

        val animationStatus = if (hungerStatus == HungerStatus.HUNGRY || isDying) {
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
            } else if (config.animations.eat != null && (eatAct?.foodRelation == BodyStatus.Relation.OVERLAP ||
                    eatAct?.foodRelation == BodyStatus.Relation.CONTAIN_CENTER)) {
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
