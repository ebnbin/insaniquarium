package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.unitToMeter

data class BodyRenderer(
    private val isDead: Boolean,
    private val configAnimations: BodyConfig.Animations,
    private val isSinkingOrFloatingOutsideWater: Boolean,
    private val expectedDirection: Direction,
    private val isHungry: Boolean,
    private val status: Status,
) {
    data class Status(
        val animationData: BodyAnimationData = BodyAnimationData(),
        /**
         * >= 0f: Delaying.
         * < 0f: Changing alpha.
         */
        val alphaTime: Float? = null,
    )

    private val animationData: BodyAnimationData = status.animationData
    private val alphaTime: Float? = status.alphaTime

    val isSwimming: Boolean = animationData.action == BodyAnimationData.Action.SWIM
    val canEat: Boolean = animationData.action == BodyAnimationData.Action.SWIM ||
        animationData.action == BodyAnimationData.Action.EAT

    private val animation: TextureRegionAnimation = animationData.getAnimation(configAnimations)

    private val textureRegion: TextureRegion = animation.getTextureRegion(animationData.stateTime)

    val actorWidth: Float = textureRegion.regionWidth.toFloat().unitToMeter
    val actorHeight: Float = textureRegion.regionHeight.toFloat().unitToMeter

    private val isFlipX: Boolean = if (animationData.action == BodyAnimationData.Action.TURN) {
        !animationData.isFacingRight
    } else {
        animationData.isFacingRight
    }

    private val alpha: Float = if (alphaTime == null || alphaTime >= 0f) {
        1f
    } else {
        ((ALPHA_DURATION + alphaTime) / ALPHA_DURATION).coerceIn(0f, 1f)
    }

    val canRemove: Boolean = alphaTime != null && alphaTime <= -ALPHA_DURATION

    fun nextStatus(
        delta: Float,
        eatenFoodRelation: BodyRelation?,
    ): Status {
        val nextAnimationData = nextAnimationData(delta, eatenFoodRelation)
        val nextAlphaTime = nextAlphaTime(delta)
        return Status(
            animationData = nextAnimationData,
            alphaTime = nextAlphaTime,
        )
    }

    private fun nextAnimationData(
        delta: Float,
        eatenFoodRelation: BodyRelation?,
    ): BodyAnimationData {
        val animationStatus = if (isHungry) {
            BodyAnimationData.Status.HUNGRY
        } else {
            BodyAnimationData.Status.NORMAL
        }

        fun createEat(): BodyAnimationData {
            return BodyAnimationData(
                action = BodyAnimationData.Action.EAT,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createTurn(): BodyAnimationData {
            return BodyAnimationData(
                action = BodyAnimationData.Action.TURN,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = !animationData.isFacingRight,
            )
        }

        fun createSwim(): BodyAnimationData {
            return BodyAnimationData(
                action = BodyAnimationData.Action.SWIM,
                status = animationStatus,
                stateTime = 0f,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun update(): BodyAnimationData {
            return animationData.copy(
                status = animationStatus,
                stateTime = animationData.stateTime + delta,
            )
        }

        val canAnimationActionChange = animationData.action == BodyAnimationData.Action.SWIM
        return if (canAnimationActionChange) {
            val canCreateEat = configAnimations.eat != null &&
                (eatenFoodRelation == BodyRelation.OVERLAP || eatenFoodRelation == BodyRelation.CONTAIN_CENTER)
            if (canCreateEat) {
                createEat()
            } else {
                val canCreateTurn = configAnimations.turn != null &&
                    (animationData.isFacingRight && expectedDirection == Direction.NEGATIVE ||
                        !animationData.isFacingRight && expectedDirection == Direction.POSITIVE)
                if (canCreateTurn) {
                    createTurn()
                } else {
                    update()
                }
            }
        } else {
            val isAnimationActionFinished = animationData.stateTime >= animation.duration
            if (isAnimationActionFinished) {
                createSwim()
            } else {
                update()
            }
        }
    }

    private fun nextAlphaTime(delta: Float): Float? {
        if (!isDead) {
            return null
        }
        return if (alphaTime == null) {
            if (isSinkingOrFloatingOutsideWater) {
                ALPHA_DELAY_DURATION
            } else {
                null
            }
        } else {
            alphaTime - delta
        }
    }

    fun draw(body: Body, batch: Batch, parentAlpha: Float) {
        val oldColor = batch.color.cpy()
        batch.color = batch.color.cpy().also { it.a = alpha * parentAlpha }
        batch.draw(
            textureRegion.texture,
            body.x,
            body.y,
            body.originX,
            body.originY,
            body.width,
            body.height,
            body.scaleX,
            body.scaleY,
            body.rotation,
            textureRegion.regionX,
            textureRegion.regionY,
            textureRegion.regionWidth,
            textureRegion.regionHeight,
            isFlipX,
            false,
        )
        batch.color = oldColor
    }

    fun postUpdate(
        bodyManager: BodyManager,
    ): Boolean {
        if (canRemove) {
            bodyManager.removeSelf()
            return true
        }
        return false
    }

    companion object {
        private const val ALPHA_DELAY_DURATION = 0f
        private const val ALPHA_DURATION = 1f
    }
}
