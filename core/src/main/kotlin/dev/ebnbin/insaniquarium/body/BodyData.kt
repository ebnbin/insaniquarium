package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Position
import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.World
import dev.ebnbin.gdx.utils.XY
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.magnitude
import dev.ebnbin.insaniquarium.game
import kotlin.math.abs
import kotlin.math.min

data class BodyData(
    val body: Body,
    val state: BodyState,
) {
    private val halfWidth: Float = body.config.width / 2f
    private val halfHeight: Float = body.config.height / 2f

    val minX: Float = halfWidth
    val maxX: Float = body.delegate.tankWidth - halfWidth

    val minY: Float = halfHeight
    val maxY: Float = Float.MAX_VALUE

    val x: Float = state.position.x
    val y: Float = state.position.y

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

    val health: Int? = state.health
    private val hunger: Int? = state.hunger
    private val growth: Int? = state.growth
    private val drop: Int? = state.drop

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

    private val isCharged: Boolean = body.config.energy != null && state.energy != null && state.isCharging != null &&
        (state.isCharging && state.energy == body.config.energy.full || !state.isCharging && state.energy != 0)

    private val canCreateCharge = state.energy != null &&
        body.config.energy != null &&
        state.energy == body.config.energy.full &&
        state.animationData.status != BodyAnimations.Status.CHARGED

    private val canCreateDischarge = state.energy != null &&
        body.config.energy != null &&
        state.energy == 0 &&
        state.animationData.status == BodyAnimations.Status.CHARGED

    private val animationData: BodyAnimationState = state.animationData
    private val alphaTime: Float? = state.alphaTime

    private val animation: TextureRegionAnimation =
        body.config.animations.get(animationData.action, animationData.status)

    private val textureRegion: TextureRegion = animation.getTextureRegion(animationData.stateTick)

    private val isFlipX: Boolean = if (animationData.action == BodyAnimations.Action.TURN) {
        !animationData.isFacingRight
    } else {
        animationData.isFacingRight
    }

    private val alpha: Float = if (alphaTime == null || alphaTime >= 0f) {
        1f
    } else {
        ((ALPHA_DURATION + alphaTime) / ALPHA_DURATION).coerceIn(0f, 1f)
    }

    private val canEat = state.animationData.action == BodyAnimations.Action.SWIM ||
        state.animationData.action == BodyAnimations.Action.EAT

    private val rendererCanRemove: Boolean = alphaTime != null && alphaTime <= -ALPHA_DURATION

    private val lifeCanRemove: Boolean = isDeadFromHealth ||
        (transformationFromHunger != null && animation.canInterrupt(animationData.stateTick)) ||
        transformationFromGrowth != null

    val canRemove: Boolean = lifeCanRemove || rendererCanRemove

    //*****************************************************************************************************************

    private val velocityX: Float = state.velocityX
    private val velocityY: Float = state.velocityY

    private val left: Float = x - halfWidth
    private val right: Float = left + body.config.width
    private val bottom: Float = y - halfHeight
    private val top: Float = bottom + body.config.height

    private val isInsideLeft: Boolean = left > 0f
    private val isInsideRight: Boolean = right < body.delegate.tankWidth
    private val isInsideBottom: Boolean = bottom > 0f

    /**
     * Percent of body inside water.
     */
    private val insideTopPercent: Float = ((body.config.height + body.delegate.tankHeight - top) /
        body.config.height).coerceIn(0f, 1f)

    private val area: Float = body.config.width * body.config.height

    //*****************************************************************************************************************

    private val halfDepth: Float = body.config.depth / 2f

    private val depthLeft: Float = x - halfDepth
    private val depthBottom: Float = y - halfDepth

    private val areaX: Float = body.config.height * body.config.depth
    private val areaY: Float = body.config.width * body.config.depth

    private val volume: Float = area * body.config.depth

    private val mass: Float = volume * body.config.density

    //*****************************************************************************************************************

    private val gravityY: Float = BodyForceHelper.gravityY(
        mass = mass,
    )

    private val buoyancyY: Float = BodyForceHelper.buoyancyY(
        volume = volume,
        insideTopPercent = insideTopPercent,
    )

    private val dragX: Float = BodyForceHelper.drag(
        dragCoefficient = body.config.dragCoefficient,
        velocity = velocityX,
        referenceArea = areaX,
    )
    private val dragY: Float = BodyForceHelper.drag(
        dragCoefficient = body.config.dragCoefficient,
        velocity = velocityY,
        referenceArea = areaY,
    )

    private val drivingX: Float = BodyForceHelper.driving(
        drivingTarget = state.drivingTargetX,
        position = x,
        velocity = velocityX,
        mass = mass,
    )
    private val drivingY: Float = BodyForceHelper.driving(
        drivingTarget = state.drivingTargetY,
        position = y,
        velocity = velocityY,
        mass = mass,
    )

    private val frictionReactionX: Float = dragX + drivingX
    private val frictionReactionY: Float = gravityY + buoyancyY + dragY + drivingY

    private val normalForFrictionX: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideLeft,
        isInsideRightOrTop = isInsideRight,
        normalReaction = frictionReactionX,
    )
    private val normalForFrictionY: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideBottom,
        isInsideRightOrTop = true,
        normalReaction = frictionReactionY,
    )

    private val waterStaticFrictionMagnitude: Float = BodyForceHelper.staticFrictionMagnitude(
        frictionCoefficient = body.config.waterFrictionCoefficient,
        normalMagnitude = buoyancyY.magnitude,
        isNormalValid = true,
    )

    private val bottomStaticFrictionMagnitude: Float = BodyForceHelper.staticFrictionMagnitude(
        frictionCoefficient = body.config.bottomFrictionCoefficient,
        normalMagnitude = normalForFrictionY.magnitude,
        isNormalValid = !isInsideBottom && normalForFrictionY > 0f,
    )

    private val leftRightStaticFrictionMagnitude: Float = BodyForceHelper.staticFrictionMagnitude(
        frictionCoefficient = body.config.leftRightFrictionCoefficient,
        normalMagnitude = normalForFrictionX.magnitude,
        isNormalValid = !isInsideLeft && normalForFrictionX > 0f || !isInsideRight && normalForFrictionX < 0f,
    )

    private val staticFrictionMagnitudeX: Float = waterStaticFrictionMagnitude + bottomStaticFrictionMagnitude
    private val staticFrictionMagnitudeY: Float = waterStaticFrictionMagnitude + leftRightStaticFrictionMagnitude

    private val frictionX: Float = BodyForceHelper.friction(
        velocity = velocityX,
        staticFrictionMagnitude = staticFrictionMagnitudeX,
        frictionReaction = frictionReactionX,
    )
    private val frictionY: Float = BodyForceHelper.friction(
        velocity = velocityY,
        staticFrictionMagnitude = staticFrictionMagnitudeY,
        frictionReaction = frictionReactionY,
    )

    private val normalReactionX: Float = frictionReactionX + frictionX
    private val normalReactionY: Float = frictionReactionY + frictionY

    private val normalX: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideLeft,
        isInsideRightOrTop = isInsideRight,
        normalReaction = normalReactionX,
    )
    private val normalY: Float = BodyForceHelper.normal(
        isInsideLeftOrBottom = isInsideBottom,
        isInsideRightOrTop = true,
        normalReaction = normalReactionY,
    )

    private val forceX: Float = normalReactionX + normalX
    private val forceY: Float = normalReactionY + normalY

    private val accelerationX: Float = BodyForceHelper.acceleration(
        force = forceX,
        mass = mass,
    )
    private val accelerationY: Float = BodyForceHelper.acceleration(
        force = forceY,
        mass = mass,
    )

    //*****************************************************************************************************************

    val isSinkingOrFloatingOutsideWater: Boolean = when {
        body.config.density == World.DENSITY_WATER -> false
        body.config.density > World.DENSITY_WATER -> !isInsideBottom
        else -> insideTopPercent < 1f
    }

    val expectedDirection: Direction = drivingX.direction.takeIf { it != Direction.ZERO } ?: velocityX.direction

    val reachDrivingTargetX: Boolean = state.drivingTargetX?.position?.let { it in left..right } ?: false
    val reachDrivingTargetY: Boolean = state.drivingTargetY?.position?.let { it in bottom..top } ?: false

    val awayFromDrivingTargetX: Boolean = state.drivingTargetX != null &&
        abs(state.drivingTargetX.position - x) >= body.config.width / 12f

    //*****************************************************************************************************************

    private val vector2: Vector2 = Vector2(x, y)
    private val rectangle: Rectangle = Rectangle(left, bottom, body.config.width, body.config.height)

    fun distance(other: BodyData): Float {
        return vector2.dst(other.vector2)
    }

    fun hit(touchPoint: Point): Boolean {
        return rectangle.contains(touchPoint.x, touchPoint.y)
    }

    fun relation(other: BodyData?): BodyRelation {
        if (other == null) {
            return BodyRelation.DISJOINT
        }
        if (rectangle.contains(other.vector2)) {
            return BodyRelation.CONTAIN_CENTER
        }
        if (rectangle.overlaps(other.rectangle)) {
            return BodyRelation.OVERLAP
        }
        return BodyRelation.DISJOINT
    }

    //*****************************************************************************************************************

    fun tick(delta: Float, input: BodyInput, position: Position): BodyData {
        val nextState = nextState(position, delta, input)
        return copy(
            state = nextState,
        )
    }

    private fun nextState(
        position: Position,
        delta: Float,
        input: BodyInput,
    ): BodyState {
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
            drivingAcceleration = body.config.drivingAccelerationX,
            configSwimAct = body.config.swimActX,
            swimAct = if (state.swimTicksX == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = state.drivingTargetX?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    ticks = state.swimTicksX,
                )
            },
            tankSize = body.delegate.tankWidth,
            reachDrivingTarget = reachDrivingTargetX,
        )
        val nextSwimActY = nextSwimAct(
            enabled = !hasEatDrivingTarget && !hasTouchDrivingTarget,
            tickDelta = delta,
            drivingAcceleration = body.config.drivingAccelerationY,
            configSwimAct = body.config.swimActY,
            swimAct = if (state.swimTicksY == null) {
                null
            } else {
                SwimAct(
                    drivingTarget = state.drivingTargetY?.takeIf { it.type == BodyDrivingTarget.Type.SWIM },
                    ticks = state.swimTicksY,
                )
            },
            tankSize = body.delegate.tankHeight,
            reachDrivingTarget = reachDrivingTargetY,
        )

        val nextDrivingTargetX: BodyDrivingTarget? =
            nextEatAct?.drivingTargetX ?: nextTouchAct?.drivingTargetX ?: nextSwimActX?.drivingTarget
        val nextDrivingTargetY: BodyDrivingTarget? =
            nextEatAct?.drivingTargetY ?: nextTouchAct?.drivingTargetY ?: nextSwimActY?.drivingTarget

        val nextHealth = nextHealth(delta, input, nextEatAct?.eatenFood)
        val nextHunger = nextHunger(delta, input, nextEatAct?.eatenFood)
        val nextGrowth = nextGrowth(delta, input, nextEatAct?.eatenFood)
        val nextDrop = nextDrop(delta, input, nextEatAct?.eatenFood)
        val nextEnergy = nextEnergy(delta, input, nextEatAct?.eatenFood)

        val eatenFoodRelation = nextEatAct?.foodRelation ?: BodyRelation.DISJOINT

        val nextAnimationData = nextAnimationData(delta, eatenFoodRelation)
        val nextAlphaTime = nextAlphaTime(delta)
        val nextScaleTransform = nextScaleTransform(delta, input)

        val nextVelocityX = nextVelocityX(delta)
        val nextVelocityY = nextVelocityY(delta)

        return BodyState(
            position = position,
            swimTicksX = nextSwimActX?.ticks,
            swimTicksY = nextSwimActY?.ticks,
            drivingTargetX = nextDrivingTargetX,
            drivingTargetY = nextDrivingTargetY,
            health = nextHealth,
            hunger = nextHunger,
            growth = nextGrowth,
            drop = nextDrop,
            energy = nextEnergy?.first,
            isCharging = nextEnergy?.second,
            animationData = nextAnimationData,
            alphaTime = nextAlphaTime,
            scaleTransform = nextScaleTransform,
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
        )
    }

    private fun nextVelocityX(delta: Float): Float {
        return BodyForceHelper.nextVelocity(
            velocity = velocityX,
            acceleration = accelerationX,
            isInsideLeftOrBottom = isInsideLeft,
            isInsideRightOrTop = isInsideRight,
            friction = frictionX,
            delta = delta,
        )
    }

    private fun nextVelocityY(delta: Float): Float {
        return BodyForceHelper.nextVelocity(
            velocity = velocityY,
            acceleration = accelerationY,
            isInsideLeftOrBottom = isInsideBottom,
            isInsideRightOrTop = true,
            friction = frictionY,
            delta = delta,
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
        val foodRelation = relation(targetFood?.data)

        if (targetFood != null && canEat && foodRelation == BodyRelation.CONTAIN_CENTER) {
            val food = body.config.eatAct.foods.getValue(targetFood.type)
            targetFood.tick(
                input = BodyInput(
                    healthDiff = food.healthDiffPerTick,
                ),
            )
            if (targetFood.data.canRemove) {
                eatenFood = food
            }
        }
        return EatAct(
            drivingTargetX = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.data.state.position.x,
                    acceleration = body.config.drivingAccelerationX * body.config.eatAct.drivingAccelerationMultiplier,
                )
            },
            drivingTargetY = if (targetFood == null) {
                null
            } else {
                BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.EAT,
                    position = targetFood.data.state.position.y,
                    acceleration = body.config.drivingAccelerationY * body.config.eatAct.drivingAccelerationMultiplier,
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
                acceleration = body.config.drivingAccelerationX * body.config.touchAct.drivingAccelerationMultiplier,
            ),
            drivingTargetY = BodyDrivingTarget(
                type = BodyDrivingTarget.Type.TOUCH,
                position = touchPoint.y,
                acceleration = body.config.drivingAccelerationY * body.config.touchAct.drivingAccelerationMultiplier,
            ),
        )
    }

    private fun nextSwimAct(
        enabled: Boolean,
        tickDelta: Float,
        drivingAcceleration: Float,
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
            if (state.animationData.status == BodyAnimations.Status.CHARGED) {
                return SwimAct(
                    drivingTarget = null,
                    ticks = 1,
                )
            }
            return SwimAct(
                drivingTarget = BodyDrivingTarget(
                    type = BodyDrivingTarget.Type.SWIM,
                    position = Random.nextFloat(0f, tankSize),
                    acceleration = drivingAcceleration * configSwimAct.drivingAccelerationMultiplier,
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

    private fun nextEnergy(
        tickDelta: Float,
        input: BodyInput,
        food: BodyConfig.Food?,
    ): Pair<Int, Boolean>? {
        body.config.energy ?: return null
        val defaultEnergy = state.energy ?: body.config.energy.init
        var nextIsCharging = state.isCharging ?: true
        if (nextIsCharging && defaultEnergy == body.config.energy.full &&
            animationData.action == BodyAnimations.Action.CHARGE && animation.canInterrupt(animationData.stateTick)) {
            nextIsCharging = false
        } else if (!nextIsCharging && defaultEnergy == 0 &&
            animationData.action == BodyAnimations.Action.DISCHARGE && animation.canInterrupt(animationData.stateTick)) {
            nextIsCharging = true
        }
        val nextEnergy = nextValue(
            value = state.energy,
            init = body.config.energy.init,
            tickDiff = if (tickDelta == 0f) {
                0
            } else {
                if (nextIsCharging) {
                    body.config.energy.diffPerTick
                } else {
                    body.config.energy.dischargeDiffPerTick
                }
            },
            inputDiff = input.energyDiff,
            foodDiff = food?.energy,
        ).coerceIn(0, body.config.energy.full)
        return nextEnergy to nextIsCharging
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

    private fun nextAnimationData(
        tickDelta: Float,
        eatenFoodRelation: BodyRelation?,
    ): BodyAnimationState {
        val animationStatus = if (isHungry) {
            BodyAnimations.Status.HUNGRY
        } else if (isCharged) {
            BodyAnimations.Status.CHARGED
        } else {
            BodyAnimations.Status.NORMAL
        }

        fun createCharge(): BodyAnimationState {
            return BodyAnimationState(
                action = BodyAnimations.Action.CHARGE,
                status = animationStatus,
                stateTick = 0,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createDischarge(): BodyAnimationState {
            return BodyAnimationState(
                action = BodyAnimations.Action.DISCHARGE,
                status = animationStatus,
                stateTick = 0,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createEat(): BodyAnimationState {
            return BodyAnimationState(
                action = BodyAnimations.Action.EAT,
                status = animationStatus,
                stateTick = 0,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun createTurn(): BodyAnimationState {
            return BodyAnimationState(
                action = BodyAnimations.Action.TURN,
                status = animationStatus,
                stateTick = 0,
                isFacingRight = !animationData.isFacingRight,
            )
        }

        fun createSwim(): BodyAnimationState {
            return BodyAnimationState(
                action = BodyAnimations.Action.SWIM,
                status = animationStatus,
                stateTick = 0,
                isFacingRight = animationData.isFacingRight,
            )
        }

        fun update(): BodyAnimationState {
            return animationData.copy(
                status = if (animationStatus == BodyAnimations.Status.CHARGED) {
                    animationData.status
                } else {
                    animationStatus
                },
                stateTick = animationData.stateTick + (if (tickDelta == 0f) 0 else 1),
            )
        }

        return if (animation.canInterrupt(animationData.stateTick)) {
            val canCreateTurn = body.config.animations.turn != null &&
                (animationData.isFacingRight && expectedDirection == Direction.NEGATIVE ||
                    !animationData.isFacingRight && expectedDirection == Direction.POSITIVE) &&
                animationData.status != BodyAnimations.Status.CHARGED
            if (canCreateTurn && awayFromDrivingTargetX) {
                createTurn()
            } else {
                if (canCreateCharge) {
                    createCharge()
                } else if (canCreateDischarge) {
                    createDischarge()
                } else {
                    val canCreateEat = body.config.animations.eat != null &&
                        (eatenFoodRelation == BodyRelation.OVERLAP || eatenFoodRelation == BodyRelation.CONTAIN_CENTER)
                    if (canCreateEat) {
                        createEat()
                    } else {
                        if (canCreateTurn) {
                            createTurn()
                        } else {
                            if (animationData.action == BodyAnimations.Action.SWIM) {
                                update()
                            } else {
                                createSwim()
                            }
                        }
                    }
                }
            }
        } else {
            update()
        }
    }

    private fun nextAlphaTime(tickDelta: Float): Float? {
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
            alphaTime - tickDelta
        }
    }

    private fun nextScaleTransform(
        tickDelta: Float,
        input: BodyInput,
    ): ScaleTransform? {
        val nextScaleTransform = input.scaleTransform ?: state.scaleTransform ?: return null
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
        val scale = state.scaleTransform?.let {
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

        if (state.animationData.action == BodyAnimations.Action.DISCHARGE &&
            animationData.stateTick == 0 &&
            body.config.energy?.dischargeProduction != null) {
            val newBody = body.delegate.addBody(
                type = body.config.energy.dischargeProduction,
                state = BodyState(
                    position = state.position,
                ),
            )
            newBody.act(delta)
        }
        if (isDeadFromHealth) {
            body.delegate.removeFromTank()
            return true
        }
        if (transformationFromHunger != null &&
            animation.canInterrupt(animationData.stateTick)) {
            val newBody = body.delegate.replaceBody(
                type = transformationFromHunger,
                state = BodyState(
                    position = state.position,
                    animationData = animationData.copy(
                        stateTick = 0,
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
                state = state.copy(
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
                    state = BodyState(
                        position = state.position,
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
        val hit = hit(point)
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
        baseGame.putLog("type,id         ") {
            "${body.type.serializedName},${body.id}"
        }
        baseGame.putLog("position        ") {
            "${state.position.x.devText()},${state.position.y.devText()}"
        }
        baseGame.putLog("size            ") {
            "${body.config.width.devText()},${body.config.height.devText()}"
        }
        baseGame.putLog("lrbt            ") {
            "${left.devText()},${right.devText()},${bottom.devText()},${top.devText()}"
        }
        baseGame.putLog("depth           ") {
            body.config.depth.devText()
        }
        baseGame.putLog("density         ") {
            body.config.density.devText()
        }
        baseGame.putLog("gravity,buoyancy") {
            "${gravityY.devText(XY.Y)},${buoyancyY.devText(XY.Y)}"
        }
        baseGame.putLog("drag            ") {
            "${dragX.devText(XY.X)},${dragY.devText(XY.Y)}"
        }
        baseGame.putLog("driving         ") {
            "${drivingX.devText(XY.X)},${drivingY.devText(XY.Y)}"
        }
        baseGame.putLog("frictionReaction") {
            "${frictionReactionX.devText(XY.X)},${frictionReactionY.devText(XY.Y)}"
        }
        baseGame.putLog("normalForFriction") {
            "${normalForFrictionX.devText(XY.X)},${normalForFrictionY.devText(XY.Y)}"
        }
        baseGame.putLog("waterStaticFrictionMagnitude") {
            waterStaticFrictionMagnitude.devText()
        }
        baseGame.putLog("bottomStaticFrictionMagnitude") {
            bottomStaticFrictionMagnitude.devText()
        }
        baseGame.putLog("leftRightStaticFrictionMagnitude") {
            leftRightStaticFrictionMagnitude.devText()
        }
        baseGame.putLog("staticFrictionMagnitude") {
            "${staticFrictionMagnitudeX.devText(XY.X)},${staticFrictionMagnitudeY.devText(XY.Y)}"
        }
        baseGame.putLog("friction        ") {
            "${frictionX.devText(XY.X)},${frictionY.devText(XY.Y)}"
        }
        baseGame.putLog("normalReaction  ") {
            "${normalReactionX.devText(XY.X)},${normalReactionY.devText(XY.Y)}"
        }
        baseGame.putLog("normal          ") {
            "${normalX.devText(XY.X)},${normalY.devText(XY.Y)}"
        }
        baseGame.putLog("force           ") {
            "${forceX.devText(XY.X)},${forceY.devText(XY.Y)}"
        }
        baseGame.putLog("acceleration    ") {
            "${accelerationX.devText(XY.X)},${accelerationY.devText(XY.Y)}"
        }
        baseGame.putLog("velocity        ") {
            "${velocityX.devText(XY.X)},${velocityY.devText(XY.Y)}"
        }

        baseGame.putLog("swimTicks") {
            "${state.swimTicksX},${state.swimTicksY}"
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
        baseGame.putLog("energy") {
            "${state.energy}/${body.config.energy?.full}"
        }
        baseGame.putLog("animation") {
            "${state.animationData}"
        }
    }

    fun drawDebug(shapes: ShapeRenderer) {
        shapes.rect(left, bottom, body.config.width, body.config.height)
        shapes.line(left, bottom, right, top)
        shapes.line(left, top, right, bottom)
        shapes.rect(depthLeft, bottom, body.config.depth, body.config.height)
        shapes.rect(left, depthBottom, body.config.width, body.config.depth)
        state.drivingTargetX?.let {
            shapes.line(it.position, 0f, it.position, body.delegate.tankHeight)
        }
        state.drivingTargetY?.let {
            shapes.line(0f, it.position, body.delegate.tankWidth, it.position)
        }
    }

    companion object {
        private const val ALPHA_DELAY_DURATION = 0f
        private const val ALPHA_DURATION = 1f
    }
}
