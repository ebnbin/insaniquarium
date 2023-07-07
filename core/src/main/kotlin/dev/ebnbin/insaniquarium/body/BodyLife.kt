package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.insaniquarium.game
import kotlin.math.min

data class BodyLife(
    val body: Body,
    val params: Params,
    val status: Status,
) {
    data class Params(
        val box: BodyBox,
    )

    data class Status(
        /**
         * null: Neither targeting nor idling.
         * 0f: Targeting or finish idling.
         * > 0f: Idling.
         */
        val swimTimeX: Float? = null,
        val swimTimeY: Float? = null,

        val drivingTargetX: BodyDrivingTarget? = null,
        val drivingTargetY: BodyDrivingTarget? = null,

        val health: Float? = null,
        val hunger: Float? = null,
        val growth: Float? = null,
        val drop: Float? = null,

        val animationData: BodyAnimationData = BodyAnimationData(),
        /**
         * >= 0f: Delaying.
         * < 0f: Changing alpha.
         */
        val alphaTime: Float? = null,
        val scaleTransform: ScaleTransform? = null,
    )

    private data class EatAct(
        val drivingTargetX: BodyDrivingTarget?,
        val drivingTargetY: BodyDrivingTarget?,
        val foodRelation: BodyRelation,
        val eatenFood: BodyConfig.Food?,
    )

    private data class TouchAct(
        val drivingTargetX: BodyDrivingTarget,
        val drivingTargetY: BodyDrivingTarget,
    )

    private data class SwimAct(
        val drivingTarget: BodyDrivingTarget?,
        val time: Float,
    )

    data class ScaleTransform(
        val duration: Float,
        val time: Float = 0f,
        val startScale: Float,
        val endScale: Float,
    )

    val isDead: Boolean = body.config.life.isDead

    val health: Float? = status.health
    private val hunger: Float? = status.hunger
    private val growth: Float? = status.growth
    private val drop: Float? = status.drop

    private val isDeadFromHealth: Boolean = body.config.life.health != null && health == 0f

    val hungerStatus: BodyHungerStatus? = body.config.life.hunger?.status(hunger)

    val isHungry: Boolean = hungerStatus == BodyHungerStatus.HUNGRY

    private val transformationFromHunger: BodyType? = body.config.life.hunger?.transformation?.takeIf { hunger == 0f }

    private val transformationFromGrowth: BodyType? = body.config.life.growth?.transformation?.takeIf { growth != null && growth <= 0f }

    private val productionFromDrop: BodyType? = body.config.life.drop?.production?.takeIf { drop != null && drop <= 0f }

    private val dropCount: Int = if (drop == null || drop > 0f) {
        0
    } else {
        -drop.toInt() + 1
    }

    private val animationData: BodyAnimationData = status.animationData
    private val alphaTime: Float? = status.alphaTime

    private val animation: TextureRegionAnimation = animationData.getAnimation(body.config.renderer.animations)

    private val textureRegion: TextureRegion = animation.getTextureRegion(animationData.stateTime)

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

    private val canEat = status.animationData.canEat

    private val rendererCanRemove: Boolean = alphaTime != null && alphaTime <= -ALPHA_DURATION

    private val lifeCanRemove: Boolean = isDeadFromHealth ||
        (transformationFromHunger != null && animationData.isSwimming) ||
        transformationFromGrowth != null

    val canRemove: Boolean = lifeCanRemove || rendererCanRemove

    fun tick(input: BodyInput, params: Params): BodyLife {
        val nextStatus = nextStatus(input)
        return copy(
            params = params,
            status = nextStatus,
        )
    }

    fun nextStatus(
        input: BodyInput,
    ): Status {
        val nextEatAct = nextEatAct(
            delegate = body.delegate,
        )

        val hasEatDrivingTarget = nextEatAct?.drivingTargetX != null || nextEatAct?.drivingTargetY != null

        val nextTouchAct = nextTouchAct(
            enabled = !hasEatDrivingTarget,
            touchPoint = body.delegate.touchPoint,
        )

        val hasTouchDrivingTarget = nextTouchAct != null

        val nextSwimActX = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            tickDelta = input.tickDelta,
            configSwimAct = body.config.life.swimActX,
            swimAct = if (status.swimTimeX == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = status.drivingTargetX?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    time = status.swimTimeX,
                )
            },
            tankSize = body.delegate.tankWidth,
            reachDrivingTarget = params.box.reachDrivingTargetX,
        )
        val nextSwimActY = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            tickDelta = input.tickDelta,
            configSwimAct = body.config.life.swimActY,
            swimAct = if (status.swimTimeY == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = status.drivingTargetY?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    time = status.swimTimeY,
                )
            },
            tankSize = body.delegate.tankHeight,
            reachDrivingTarget = params.box.reachDrivingTargetY,
        )

        val nextDrivingTargetX: BodyDrivingTarget? =
            nextEatAct?.drivingTargetX ?: nextTouchAct?.drivingTargetX ?: nextSwimActX?.drivingTarget
        val nextDrivingTargetY: BodyDrivingTarget? =
            nextEatAct?.drivingTargetY ?: nextTouchAct?.drivingTargetY ?: nextSwimActY?.drivingTarget

        val nextHealth = nextHealth(input, nextEatAct?.eatenFood)
        val nextHunger = nextHunger(input, nextEatAct?.eatenFood)
        val nextGrowth = nextGrowth(input, nextEatAct?.eatenFood)
        val nextDrop = nextDrop(input, nextEatAct?.eatenFood)

        val eatenFoodRelation = nextEatAct?.foodRelation ?: BodyRelation.DISJOINT

        val nextAnimationData = nextAnimationData(input.tickDelta, eatenFoodRelation)
        val nextAlphaTime = nextAlphaTime(input.tickDelta)
        val nextScaleTransform = nextScaleTransform(input)

        return Status(
            swimTimeX = nextSwimActX?.time,
            swimTimeY = nextSwimActY?.time,
            drivingTargetX = nextDrivingTargetX,
            drivingTargetY = nextDrivingTargetY,
            health = nextHealth,
            hunger = nextHunger,
            growth = nextGrowth,
            drop = nextDrop,
            animationData = nextAnimationData,
            alphaTime = nextAlphaTime,
            scaleTransform = nextScaleTransform,
        )
    }

    private fun nextEatAct(
        delegate: BodyActorDelegate,
    ): EatAct? {
        if (body.config.life.eatAct == null) {
            return null
        }

        fun targetFood(): Body? {
            if (hungerStatus == BodyHungerStatus.FULL) {
                return null
            }
            require(body.config.life.eatAct.foods.isNotEmpty())
            return delegate.findNearestBodyByType(body.config.life.eatAct.foods.keys)
        }

        var eatenFood: BodyConfig.Food? = null
        val targetFood = targetFood()
        val foodRelation = params.box.relation(targetFood?.box)

        if (targetFood != null && canEat && foodRelation == BodyRelation.CONTAIN_CENTER) {
            val food = body.config.life.eatAct.foods.getValue(targetFood.type)
            val foodBody = targetFood.tick(
                input = BodyInput(
                    healthDiff = food.healthDiffPerTick,
                ),
            )
            if (foodBody.canRemove) {
                eatenFood = food
            }
        }
        return EatAct(
            drivingTargetX = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.box.x,
                    acceleration = body.config.life.eatAct.drivingAccelerationX,
                )
            },
            drivingTargetY = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.box.y,
                    acceleration = body.config.life.eatAct.drivingAccelerationY,
                )
            },
            foodRelation = foodRelation,
            eatenFood = eatenFood,
        )
    }

    private fun nextTouchAct(
        enabled: Boolean,
        touchPoint: Point?,
    ): TouchAct? {
        if (!enabled) {
            return null
        }
        if (body.config.life.touchAct == null) {
            return null
        }
        touchPoint ?: return null
        return TouchAct(
            drivingTargetX = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.x,
                acceleration = body.config.life.touchAct.drivingAccelerationX,
            ),
            drivingTargetY = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.y,
                acceleration = body.config.life.touchAct.drivingAccelerationY,
            ),
        )
    }

    private fun nextSwimAct(
        enabled: Boolean,
        tickDelta: Float,
        configSwimAct: BodyConfig.SwimAct?,
        swimAct: SwimAct?,
        tankSize: Float,
        reachDrivingTarget: Boolean,
    ): SwimAct? {
        if (!enabled) {
            return null
        }
        if (configSwimAct == null) {
            return null
        }

        fun createTargeting(): SwimAct {
            return SwimAct(
                drivingTarget = BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.SWIM,
                    position = Random.nextFloat(0f, tankSize),
                    acceleration = configSwimAct.drivingAcceleration,
                ),
                time = 0f,
            )
        }

        fun createIdling(): SwimAct {
            return SwimAct(
                drivingTarget = null,
                time = Random.nextFloat(
                    configSwimAct.idlingTimeRandomStart,
                    configSwimAct.idlingTimeRandomEnd,
                ),
            )
        }

        fun updateIdling(swimAct: SwimAct): SwimAct {
            return swimAct.copy(
                time = swimAct.time - tickDelta,
            )
        }

        if (swimAct == null) {
            return if (Random.nextBoolean()) {
                createTargeting()
            } else {
                createIdling()
            }
        }
        return if (swimAct.drivingTarget == null) {
            // Idling
            val isRemainingTimeUp = swimAct.time - tickDelta <= 0f
            if (isRemainingTimeUp) {
                createTargeting()
            } else {
                updateIdling(swimAct)
            }
        } else {
            // Targeting
            if (reachDrivingTarget) {
                createIdling()
            } else {
                swimAct
            }
        }
    }

    private fun nextHealth(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        body.config.life.health ?: return null
        return nextValue(
            value = health,
            initialThreshold = body.config.life.health.initialThreshold,
            diffPerTick = body.config.life.health.diffPerTick,
            inputDiff = input.healthDiff,
            foodDiff = food?.health,
        ).coerceIn(0f, 1f)
    }

    private fun nextHunger(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        body.config.life.hunger ?: return null
        return nextValue(
            value = hunger,
            initialThreshold = body.config.life.hunger.initialThreshold,
            diffPerTick = body.config.life.hunger.diffPerTick,
            inputDiff = input.hungerDiff,
            foodDiff = food?.hunger,
        ).coerceIn(0f, body.config.life.hunger.maxThreshold)
    }

    private fun nextGrowth(
        input: BodyInput,
        food: BodyConfig.Food?
    ): Float? {
        body.config.life.growth ?: return null
        return nextValue(
            value = growth,
            initialThreshold = body.config.life.growth.initialThreshold,
            diffPerTick = body.config.life.growth.diffPerTick,
            inputDiff = input.growthDiff,
            foodDiff = food?.growth,
        )
    }

    private fun nextDrop(
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Float? {
        body.config.life.drop ?: return null
        return nextValue(
            value = drop,
            initialThreshold = body.config.life.drop.initialThreshold,
            diffPerTick = body.config.life.drop.diffPerTick,
            inputDiff = input.dropDiff,
            foodDiff = food?.drop,
        )
    }

    private fun nextValue(
        value: Float?,
        initialThreshold: Float,
        diffPerTick: Float,
        inputDiff: Float,
        foodDiff: Float?,
    ): Float {
        var nextValue = value ?: initialThreshold
        nextValue += diffPerTick
        nextValue += inputDiff
        nextValue += (foodDiff ?: 0f)
        return nextValue
    }

    private fun nextAnimationData(
        tickDelta: Float,
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
                stateTime = animationData.stateTime + tickDelta,
            )
        }

        val canAnimationActionChange = animationData.action == BodyAnimationData.Action.SWIM
        return if (canAnimationActionChange) {
            val canCreateTurn = body.config.renderer.animations.turn != null &&
                (animationData.isFacingRight && params.box.expectedDirection == Direction.NEGATIVE ||
                    !animationData.isFacingRight && params.box.expectedDirection == Direction.POSITIVE)
            if (canCreateTurn && params.box.awayFromDrivingTargetX) {
                createTurn()
            } else {
                val canCreateEat = body.config.renderer.animations.eat != null &&
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

    private fun nextAlphaTime(tickDelta: Float): Float? {
        if (!isDead) {
            return null
        }
        return if (alphaTime == null) {
            if (params.box.isSinkingOrFloatingOutsideWater) {
                ALPHA_DELAY_DURATION
            } else {
                null
            }
        } else {
            alphaTime - tickDelta
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
            time = min(nextScaleTransform.duration, nextScaleTransform.time + input.tickDelta),
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
            body.delegate.x,
            body.delegate.y,
            body.delegate.width / 2f,
            body.delegate.height / 2f,
            body.delegate.width,
            body.delegate.height,
            scale,
            scale,
            body.delegate.rotation,
            textureRegion.regionX,
            textureRegion.regionY,
            textureRegion.regionWidth,
            textureRegion.regionHeight,
            isFlipX,
            false,
        )
        batch.color = oldColor
    }

    /**
     * @return True if removed.
     */
    fun postTick(): Boolean {
        val delta = 0f

        if (isDeadFromHealth) {
            body.delegate.removeFromTank()
            return true
        }
        if (transformationFromHunger != null &&
            animationData.action == BodyAnimationData.Action.SWIM) {
            val newBody = body.delegate.replaceBody(
                type = transformationFromHunger,
                boxStatus = params.box.status,
                lifeStatus = Status(
                    animationData = animationData.copy(
                        stateTime = 0f,
                    ),
                ),
            )
            newBody.act(delta)
            return true
        }
        if (transformationFromGrowth != null) {
            require(growth != null)
            val newConfig = game.config.body.getValue(transformationFromGrowth)
            val newBody = body.delegate.replaceBody(
                type = transformationFromGrowth,
                boxStatus = params.box.status,
                lifeStatus = status.copy(
                    growth = null,
                ),
            )
            newBody.tick(
                input = BodyInput(
                    growthDiff = growth,
                    scaleTransform = ScaleTransform(
                        duration = 0.25f,
                        startScale = body.config.box.width / newConfig.box.width,
                        endScale = 1f,
                    ),
                ),
            )
            newBody.act(delta)
        }
        if (productionFromDrop != null) {
            repeat(dropCount) {
                val newBody = body.delegate.addBody(
                    type = productionFromDrop,
                    boxStatus = BodyBox.Status(
                        x = params.box.x,
                        y = params.box.y,
                    ),
                )
                newBody.act(delta)
            }
            body.tick(
                input = BodyInput(
                    dropDiff = dropCount.toFloat(),
                ),
            )

            if (rendererCanRemove) {
                body.delegate.removeFromTank()
                return true
            }
            return false
        }

        if (rendererCanRemove) {
            body.delegate.removeFromTank()
            return true
        }
        return false
    }

    fun touch(point: Point): Boolean {
        val hit = params.box.hit(point)
        if (hit) {
            body.tick(
                input = BodyInput(
                    healthDiff = -(health ?: 0f),
                ),
            )
        }
        return hit
    }

    fun actDebug() {
        baseGame.putLog("health") {
            if (health == null) "null" else "%.3f".format(health)
        }
        baseGame.putLog("hunger") {
            if (hunger == null) "null" else "%.3f,%s".format(hunger, hungerStatus)
        }
        baseGame.putLog("growth") {
            if (growth == null) "null" else "%.3f".format(growth)
        }
        baseGame.putLog("drop  ") {
            if (drop == null) "null" else "%.3f".format(drop)
        }
    }

    companion object {
        private const val ALPHA_DELAY_DURATION = 0f
        private const val ALPHA_DURATION = 1f
    }
}
