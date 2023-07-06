package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.lifecycle.BaseGame
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.unitToMeter
import kotlin.math.min

data class BodyRenderer(
    private val config: BodyConfig.Renderer,
    private val delegate: BodyDelegate,
    private val isDead: Boolean,
    private val isSinkingOrFloatingOutsideWater: Boolean,
    private val expectedDirection: Direction,
    private val isHungry: Boolean,
    private val awayFromDrivingTargetX: Boolean,
    val status: Status,
) {
    data class Status(
        val animationData: BodyAnimationData = BodyAnimationData(),
        /**
         * >= 0f: Delaying.
         * < 0f: Changing alpha.
         */
        val alphaTime: Float? = null,
        val scaleTransform: ScaleTransform? = null,
    )

    data class ScaleTransform(
        val duration: Float,
        val time: Float = 0f,
        val startScale: Float,
        val endScale: Float,
    )

    private val animationData: BodyAnimationData = status.animationData
    private val alphaTime: Float? = status.alphaTime

    private val animation: TextureRegionAnimation = animationData.getAnimation(config.animations)

    private val textureRegion: TextureRegion = animation.getTextureRegion(animationData.stateTime)

    /**
     * Actor's width and height.
     */
    val width: Float = textureRegion.regionWidth.toFloat().unitToMeter
    val height: Float = textureRegion.regionHeight.toFloat().unitToMeter

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
        input: BodyInput,
        eatenFoodRelation: BodyRelation?,
    ): Status {
        val nextAnimationData = nextAnimationData(eatenFoodRelation)
        val nextAlphaTime = nextAlphaTime()
        val nextScaleTransform = nextScaleTransform(input)
        return Status(
            animationData = nextAnimationData,
            alphaTime = nextAlphaTime,
            scaleTransform = nextScaleTransform,
        )
    }

    private fun nextAnimationData(
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
                stateTime = animationData.stateTime + BaseGame.TICK,
            )
        }

        val canAnimationActionChange = animationData.action == BodyAnimationData.Action.SWIM
        return if (canAnimationActionChange) {
            val canCreateTurn = config.animations.turn != null &&
                (animationData.isFacingRight && expectedDirection == Direction.NEGATIVE ||
                    !animationData.isFacingRight && expectedDirection == Direction.POSITIVE)
            if (canCreateTurn && awayFromDrivingTargetX) {
                createTurn()
            } else {
                val canCreateEat = config.animations.eat != null &&
                    (eatenFoodRelation == BodyRelation.OVERLAP || eatenFoodRelation == BodyRelation.CONTAIN_CENTER)
                if (canCreateEat) {
                    createEat()
                } else {
                    if (canCreateTurn) {
                        createTurn()
                    } else {
                        update()
                    }
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

    private fun nextAlphaTime(): Float? {
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
            alphaTime - BaseGame.TICK
        }
    }

    private fun nextScaleTransform(
        input: BodyInput,
    ): ScaleTransform? {
        val nextScaleTransform = input.scaleTransform ?: status.scaleTransform ?: return null
        if (nextScaleTransform.time == nextScaleTransform.duration) {
            return null
        }
        return nextScaleTransform.copy(
            time = min(nextScaleTransform.duration, nextScaleTransform.time + BaseGame.TICK),
        )
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        val oldColor = batch.color.cpy()
        batch.color = batch.color.cpy().also { it.a = alpha * parentAlpha }
        val scale = status.scaleTransform?.let {
            val progress = it.time / it.duration
            it.startScale + (it.endScale - it.startScale) * progress
        } ?: 1f
        batch.draw(
            textureRegion.texture,
            delegate.x,
            delegate.y,
            delegate.width / 2f,
            delegate.height / 2f,
            delegate.width,
            delegate.height,
            scale,
            scale,
            delegate.rotation,
            textureRegion.regionX,
            textureRegion.regionY,
            textureRegion.regionWidth,
            textureRegion.regionHeight,
            isFlipX,
            false,
        )
        batch.color = oldColor
    }

    fun postTick(
        delegate: BodyDelegate,
    ): Boolean {
        if (canRemove) {
            delegate.removeFromTank()
            return true
        }
        return false
    }

    companion object {
        private const val ALPHA_DELAY_DURATION = 0f
        private const val ALPHA_DURATION = 1f
    }
}
