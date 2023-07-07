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
         * 0: Targeting.
         * > 0: Idling.
         */
        val swimTicksX: Int? = null,
        val swimTicksY: Int? = null,

        val drivingTargetX: BodyDrivingTarget? = null,
        val drivingTargetY: BodyDrivingTarget? = null,

        val health: Int? = null,
        val hunger: Int? = null,
        val growth: Int? = null,
        val drop: Int? = null,

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
        val ticks: Int,
    )

    data class ScaleTransform(
        val totalTicks: Int,
        val tick: Int = 0,
        val startScale: Float,
        val endScale: Float,
    )

    val isDead: Boolean = body.config.isDead

    val health: Int? = status.health
    private val hunger: Int? = status.hunger
    private val growth: Int? = status.growth
    private val drop: Int? = status.drop

    private val isDeadFromHealth: Boolean = body.config.health != null && health == 0

    val hungerStatus: BodyHungerStatus? = body.config.hunger?.status(hunger)

    val isHungry: Boolean = hungerStatus == BodyHungerStatus.HUNGRY

    private val transformationFromHunger: BodyType? = body.config.hunger?.transformation?.takeIf { hunger == 0 }

    private val transformationFromGrowth: BodyType? = body.config.growth?.transformation?.takeIf {
        growth != null && growth >= body.config.growth.full
    }

    private val productionFromDrop: BodyType? = body.config.drop?.production?.takeIf {
        drop != null && drop >= body.config.drop.full
    }

    private val dropCount: Int = if (drop == null || body.config.drop == null || drop < body.config.drop.full) {
        0
    } else {
        drop / body.config.drop.full
    }

    private val animationData: BodyAnimationData = status.animationData
    private val alphaTime: Float? = status.alphaTime

    private val animation: TextureRegionAnimation = animationData.getAnimation(body.config.animations)

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

    fun tick(delta: Float, input: BodyInput, params: Params): BodyLife {
        val nextStatus = nextStatus(delta, input)
        return copy(
            params = params,
            status = nextStatus,
        )
    }

    fun nextStatus(
        delta: Float,
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
            tickDelta = delta,
            configSwimAct = body.config.swimActX,
            swimAct = if (status.swimTicksX == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = status.drivingTargetX?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    ticks = status.swimTicksX,
                )
            },
            tankSize = body.delegate.tankWidth,
            reachDrivingTarget = params.box.reachDrivingTargetX,
        )
        val nextSwimActY = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            tickDelta = delta,
            configSwimAct = body.config.swimActY,
            swimAct = if (status.swimTicksY == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = status.drivingTargetY?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    ticks = status.swimTicksY,
                )
            },
            tankSize = body.delegate.tankHeight,
            reachDrivingTarget = params.box.reachDrivingTargetY,
        )

        val nextDrivingTargetX: BodyDrivingTarget? =
            nextEatAct?.drivingTargetX ?: nextTouchAct?.drivingTargetX ?: nextSwimActX?.drivingTarget
        val nextDrivingTargetY: BodyDrivingTarget? =
            nextEatAct?.drivingTargetY ?: nextTouchAct?.drivingTargetY ?: nextSwimActY?.drivingTarget

        val nextHealth = nextHealth(delta, input, nextEatAct?.eatenFood)
        val nextHunger = nextHunger(delta, input, nextEatAct?.eatenFood)
        val nextGrowth = nextGrowth(delta, input, nextEatAct?.eatenFood)
        val nextDrop = nextDrop(delta, input, nextEatAct?.eatenFood)

        val eatenFoodRelation = nextEatAct?.foodRelation ?: BodyRelation.DISJOINT

        val nextAnimationData = nextAnimationData(delta, eatenFoodRelation)
        val nextAlphaTime = nextAlphaTime(delta)
        val nextScaleTransform = nextScaleTransform(delta, input)

        return Status(
            swimTicksX = nextSwimActX?.ticks,
            swimTicksY = nextSwimActY?.ticks,
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
        if (body.config.eatAct == null) {
            return null
        }

        fun targetFood(): Body? {
            if (hungerStatus == BodyHungerStatus.FULL) {
                return null
            }
            require(body.config.eatAct.foods.isNotEmpty())
            return delegate.findNearestBodyByType(body.config.eatAct.foods.keys)
        }

        var eatenFood: BodyConfig.Food? = null
        val targetFood = targetFood()
        val foodRelation = params.box.relation(targetFood?.box)

        if (targetFood != null && canEat && foodRelation == BodyRelation.CONTAIN_CENTER) {
            val food = body.config.eatAct.foods.getValue(targetFood.type)
            targetFood.tick(
                input = BodyInput(
                    healthDiff = food.healthDiffPerTick,
                ),
            )
            if (targetFood.life.canRemove) {
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
                    acceleration = body.config.eatAct.drivingAccelerationX,
                )
            },
            drivingTargetY = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.box.y,
                    acceleration = body.config.eatAct.drivingAccelerationY,
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
        if (body.config.touchAct == null) {
            return null
        }
        touchPoint ?: return null
        return TouchAct(
            drivingTargetX = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.x,
                acceleration = body.config.touchAct.drivingAccelerationX,
            ),
            drivingTargetY = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.y,
                acceleration = body.config.touchAct.drivingAccelerationY,
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
        val ticks = if (tickDelta == 0f) 0 else 1

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
                ticks = 0,
            )
        }

        fun createIdling(): SwimAct {
            val newTicks = Random.nextInt(
                from = configSwimAct.idlingTicksMin,
                until = configSwimAct.idlingTicksMax + 1,
            )
            return if (newTicks == 0) {
                createTargeting()
            } else {
                SwimAct(
                    drivingTarget = null,
                    ticks = newTicks,
                )
            }
        }

        fun updateIdling(swimAct: SwimAct): SwimAct {
            return swimAct.copy(
                ticks = swimAct.ticks - ticks,
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
            val isRemainingTimeUp = swimAct.ticks - ticks <= 0
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
        tickDelta: Float,
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Int? {
        body.config.health ?: return null
        return nextValue(
            value = health,
            init = body.config.health.init,
            tickDiff = if (tickDelta == 0f) 0 else body.config.health.diffPerTick,
            inputDiff = input.healthDiff,
            foodDiff = food?.health,
        ).coerceAtLeast(0)
    }

    private fun nextHunger(
        tickDelta: Float,
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Int? {
        body.config.hunger ?: return null
        return nextValue(
            value = hunger,
            init = body.config.hunger.init,
            tickDiff = if (tickDelta == 0f) 0 else body.config.hunger.diffPerTick,
            inputDiff = input.hungerDiff,
            foodDiff = food?.hunger,
        ).coerceIn(0, body.config.hunger.max)
    }

    private fun nextGrowth(
        tickDelta: Float,
        input: BodyInput,
        food: BodyConfig.Food?
    ): Int? {
        body.config.growth ?: return null
        return nextValue(
            value = growth,
            init = body.config.growth.init,
            tickDiff = if (tickDelta == 0f) 0 else body.config.growth.diffPerTick,
            inputDiff = input.growthDiff,
            foodDiff = food?.growth,
        )
    }

    private fun nextDrop(
        tickDelta: Float,
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Int? {
        body.config.drop ?: return null
        return nextValue(
            value = drop,
            init = body.config.drop.init,
            tickDiff = if (tickDelta == 0f) 0 else body.config.drop.diffPerTick,
            inputDiff = input.dropDiff,
            foodDiff = food?.drop,
        )
    }

    private fun nextValue(
        value: Int?,
        init: Int,
        tickDiff: Int,
        inputDiff: Int,
        foodDiff: Int?,
    ): Int {
        return (value ?: init) + tickDiff + inputDiff + (foodDiff ?: 0)
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
            val canCreateTurn = body.config.animations.turn != null &&
                (animationData.isFacingRight && params.box.expectedDirection == Direction.NEGATIVE ||
                    !animationData.isFacingRight && params.box.expectedDirection == Direction.POSITIVE)
            if (canCreateTurn && params.box.awayFromDrivingTargetX) {
                createTurn()
            } else {
                val canCreateEat = body.config.animations.eat != null &&
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
        tickDelta: Float,
        input: BodyInput,
    ): ScaleTransform? {
        val nextScaleTransform = input.scaleTransform ?: status.scaleTransform ?: return null
        if (nextScaleTransform.tick == nextScaleTransform.totalTicks) {
            return null
        }
        return nextScaleTransform.copy(
            tick = min(nextScaleTransform.totalTicks, nextScaleTransform.tick + (if (tickDelta == 0f) 0 else 1)),
        )
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        val oldColor = batch.color.cpy()
        batch.color = batch.color.cpy().also { it.a = alpha * parentAlpha }
        val scale = status.scaleTransform?.let {
            val progress = it.tick.toFloat() / it.totalTicks
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
            requireNotNull(growth)
            requireNotNull(body.config.growth)
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
                    growthDiff = growth - body.config.growth.full,
                    scaleTransform = ScaleTransform(
                        totalTicks = 5,
                        startScale = body.config.width / newConfig.width,
                        endScale = 1f,
                    ),
                ),
            )
            newBody.act(delta)
        }
        if (productionFromDrop != null) {
            requireNotNull(drop)
            requireNotNull(body.config.drop)
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
                    dropDiff = -(dropCount * body.config.drop.full),
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
                    healthDiff = -(health ?: 0),
                ),
            )
        }
        return hit
    }

    fun actDebug() {
        baseGame.putLog("swimTicks") {
            "${status.swimTicksX},${status.swimTicksY}"
        }
        baseGame.putLog("health") {
            "$health/${body.config.health?.full}"
        }
        baseGame.putLog("hunger") {
            "$hunger/${body.config.hunger?.full}"
        }
        baseGame.putLog("growth") {
            "$growth/${body.config.growth?.full}"
        }
        baseGame.putLog("drop  ") {
            "$drop/${body.config.drop?.full}"
        }
    }

    companion object {
        private const val ALPHA_DELAY_DURATION = 0f
        private const val ALPHA_DURATION = 1f
    }
}
