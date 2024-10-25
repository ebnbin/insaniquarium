package dev.ebnbin.insaniquarium.tank

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.insaniquarium.body.BodyDef
import dev.ebnbin.insaniquarium.body.BodyDrivingTarget
import dev.ebnbin.insaniquarium.body.BodyHelper
import dev.ebnbin.insaniquarium.body.BodyPosition
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.preference.PreferenceManager
import dev.ebnbin.kgdx.dev.DevEntry
import dev.ebnbin.kgdx.dev.toDevEntry
import dev.ebnbin.kgdx.ui.AnimationImage
import dev.ebnbin.kgdx.util.ShapeRendererHelper
import dev.ebnbin.kgdx.util.checkBoxMenuItem
import dev.ebnbin.kgdx.util.colorMarkup
import dev.ebnbin.kgdx.util.listMenuItem
import dev.ebnbin.kgdx.util.menuItem
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import kotlin.math.absoluteValue

// 3000 bodies: tick 11ms, draw 93ms
// 3000 bodies: tick 8ms, draw 93ms
class Tank(
    val groupWrapper: TankGroupWrapper,
) {
    private val engine: Engine = Engine().also { engine ->
        engine.addEntityListener(
            allOf(
                TankComponent::class,
                BodyDataComponent::class,
                BodyPositionComponent::class,
                TextureRegionComponent::class,
            ).get(),
            object : EntityListener {
                override fun entityAdded(entity: Entity) {
                }

                override fun entityRemoved(entity: Entity) {
                    if (tankComponent.selectedBodyEntity() === entity) {
                        tankComponent.selectBodyEntity(null)
                    }
                }
            },
        )
    }

    private val updateType: UpdateType = UpdateType()

    val tankComponent: TankComponent = TankComponent(
        groupWrapper = groupWrapper,
    )

    private val shapeRendererHelper: ShapeRendererHelper = ShapeRendererHelper()

    init {
        groupWrapper.setSize(tankComponent.width, tankComponent.height)
    }

    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        tankComponent.touchPosition = BodyPosition(x, y)
        val entities = engine.getEntitiesFor(allOf(
            BodyDataComponent::class,
        ).get())
        var handled = false
        for (i in entities.size() - 1 downTo 0) {
            val entity = entities[i]
            val bodyData = ComponentMappers.bodyData.get(entity)
            if (bodyData.contains(x, y)) {
                tankComponent.selectBodyEntity(entity)
                handled = true
                break
            }
        }
        if (!handled) {
            if (tankComponent.selectedBodyEntity() == null) {
                val bodyType = tankComponent.selectedBodyType
                if (bodyType != null) {
                    addBody(
                        type = bodyType,
                        position = BodyPosition(x, y),
                    )
                }
            } else {
                tankComponent.selectBodyEntity(null)
            }
        }
        return true
    }

    fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        tankComponent.touchPosition = BodyPosition(x, y)
    }

    fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        tankComponent.touchPosition = null
    }

    fun bodyCount(): Int {
        return engine.entities.size()
    }

    fun tick(delta: Float) {
        updateType.type = UpdateType.Type.TICK
        engine.update(delta)
    }

    fun act(delta: Float, enableTick: Boolean) {
        updateType.type = if (enableTick) {
            UpdateType.Type.ACT_ENABLE_TICK
        } else {
            UpdateType.Type.ACT_DISABLE_TICK
        }
        engine.update(delta)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        updateType.type = UpdateType.Type.DRAW
        engine.update(0f)
    }

    fun addedToStage(stage: TankStage) {
        engine.addSystem(BodyActSystem(updateType))
        engine.addSystem(BodyTickSystem(updateType))
        engine.addSystem(BodyDrawSystem(updateType, groupWrapper))
        engine.addSystem(TankDrawDebugSystem(updateType, groupWrapper, shapeRendererHelper))
        engine.addSystem(BodyDrawDebugSystem(updateType, groupWrapper, shapeRendererHelper))
    }

    fun removedFromStage(stage: TankStage) {
        engine.removeAllSystems()
    }

    private fun addBody(
        type: BodyType,
        position: BodyPosition,
    ) {
        val entity = engine.createEntity()
        entity.add(tankComponent)
        entity.add(BodyDefComponent(
            def = type.def,
        ))
        entity.add(BodyInitDataComponent(
            velocityX = 0f,
            velocityY = 0f,
            position = position,
            swimBehaviorX = null,
            swimBehaviorY = null,
        ))
        entity.add(BodyDataComponent())
        entity.add(BodyPositionComponent(
            bodyPosition = position,
        ))
        entity.add(TextureRegionComponent(
            textureRegion = type.def.textureAsset.getTextureRegionList().first(),
        ))
        engine.addEntity(entity)
    }

    private fun addBodies(
        type: BodyType?,
        count: Int,
        x: Float? = null,
        y: Float? = null,
    ) {
        repeat(count) {
            addBody(
                type = type ?: BodyType.entries.random(),
                position = BodyPosition(
                    x = x ?: (Math.random().toFloat() * groupWrapper.width),
                    y = y ?: (Math.random().toFloat() * groupWrapper.height),
                ),
            )
        }
    }

    private fun clearBodies() {
        engine.removeAllEntities(allOf(
            TankComponent::class,
            BodyDataComponent::class,
            BodyPositionComponent::class,
            TextureRegionComponent::class,
        ).get())
    }

    val hasDevMenu: Boolean = true

    fun createDevMenu(menuBar: MenuBar, menu: Menu) {
        menu.apply {
            checkBoxMenuItem(
                menuBar = menuBar,
                text = PreferenceManager.enableBodySmoothPosition.key,
                valueProperty = PreferenceManager.enableBodySmoothPosition::value,
            )
            listMenuItem(
                menuBar = menuBar,
                text = "body type pet A",
                valueList = BodyType.DEV_PET_LIST_A,
                valueToText = { it.id },
                valueToImage = { AnimationImage(textureAsset = it.def.textureAsset) },
            ) {
                tankComponent.selectedBodyType = it
            }
            listMenuItem(
                menuBar = menuBar,
                text = "body type pet B",
                valueList = BodyType.DEV_PET_LIST_B,
                valueToText = { it.id },
                valueToImage = { AnimationImage(textureAsset = it.def.textureAsset) },
            ) {
                tankComponent.selectedBodyType = it
            }
            menuItem(
                menuBar = menuBar,
                text = "reset body type",
            ) {
                tankComponent.selectedBodyType = null
            }
            listMenuItem(
                menuBar = menuBar,
                text = "create body",
                valueList = listOf(1, 10, 100, 1000),
            ) { count ->
                addBodies(
                    type = tankComponent.selectedBodyType,
                    count = count,
                )
            }
            menuItem(
                menuBar = menuBar,
                text = "clear bodies",
            ) {
                clearBodies()
            }
        }
    }
}

data class UpdateType(
    var type: Type = Type.NONE,
) {
    enum class Type {
        NONE,
        TICK,
        ACT_DISABLE_TICK,
        ACT_ENABLE_TICK,
        DRAW,
        ;
    }
}

class TankComponent(
    val groupWrapper: TankGroupWrapper,
    val width: Float = WIDTH_DP.dpToMeter,
    val height: Float = HEIGHT_DP.dpToMeter,
    var touchPosition: BodyPosition? = null,
    var selectedBodyType: BodyType? = null,
    private var selectedBodyEntity: Entity? = null,
    private var devInfoEntryList: List<DevEntry>? = null,
) : Component {
    fun selectBodyEntity(entity: Entity?) {
        if (selectedBodyEntity == null) {
            if (entity == null) {
                return
            } else {
                selectedBodyEntity = entity
                val bodyDef = ComponentMappers.bodyDef.get(entity)
                val bodyData = ComponentMappers.bodyData.get(entity)
                devInfoEntryList = createDevInfoEntryList(bodyDef, bodyData).onEach { entry ->
                    groupWrapper.stage.putDevInfo(entry)
                }
            }
        } else {
            if (entity == null) {
                devInfoEntryList?.reversed()?.forEach { entry ->
                    groupWrapper.stage.removeDevInfo(entry)
                }
                devInfoEntryList = null
                selectedBodyEntity = null
            } else {
                if (selectedBodyEntity === entity) {
                    return
                } else {
                    devInfoEntryList?.reversed()?.forEach { entry ->
                        groupWrapper.stage.removeDevInfo(entry)
                    }
                    selectedBodyEntity = entity
                    val bodyDef = ComponentMappers.bodyDef.get(entity)
                    val bodyData = ComponentMappers.bodyData.get(entity)
                    devInfoEntryList = createDevInfoEntryList(bodyDef, bodyData).onEach { entry ->
                        groupWrapper.stage.putDevInfo(entry)
                    }
                }
            }
        }
    }

    fun selectedBodyEntity(): Entity? {
        return selectedBodyEntity
    }

    private fun createDevInfoEntryList(bodyDef: BodyDefComponent, data: BodyDataComponent): List<DevEntry> {
        return listOf(
            "type" toDevEntry {
                bodyDef.def.id
            },
            "size".key() toDevEntry {
                "${data.width.value(Sign.UNSIGNED)},${data.height.value(Sign.UNSIGNED)}"
            },
            "lrbt".key() toDevEntry {
                "${data.left.value(Sign.SIGNED)},${data.right.value(Sign.SIGNED)}," +
                    "${data.bottom.value(Sign.SIGNED)},${data.top.value(Sign.SIGNED)}"
            },
            "isInsideLRBT".key() toDevEntry {
                "${data.isInsideLeft.value()},${data.isInsideRight.value()}," +
                    "${data.isInsideBottom.value()},${data.isInsideTop.value()}"
            },
            "areaInWater/area".key() toDevEntry {
                "${data.areaInWater.value(Sign.UNSIGNED)}/${data.area.value(Sign.UNSIGNED)}"
            },
            "density".key() toDevEntry {
                data.density.value(Sign.UNSIGNED)
            },
            "mass".key() toDevEntry {
                data.mass.value(Sign.UNSIGNED)
            },
            "gravity".key() toDevEntry {
                "${0f.value(Sign.X)},${data.gravityY.value(Sign.Y)}"
            },
            "buoyancy".key() toDevEntry {
                "${0f.value(Sign.X)},${data.buoyancyY.value(Sign.Y)}"
            },
            "drag".key() toDevEntry {
                "${data.dragX.value(Sign.X)},${data.dragY.value(Sign.Y)}"
            },
            "drivingTarget".key() toDevEntry {
                "${data.drivingTargetX?.position.value(Sign.SIGNED)}," +
                    data.drivingTargetY?.position.value(Sign.SIGNED)
            },
            "drivingForce".key() toDevEntry {
                "${data.drivingForceX.value(Sign.X)},${data.drivingForceY.value(Sign.Y)}"
            },
            "normalReactionForce".key() toDevEntry {
                "${data.normalReactionForceX.value(Sign.X)}," +
                    "${data.normalReactionForceY.value(Sign.Y)}"
            },
            "normalForce".key() toDevEntry {
                "${data.normalForceX.value(Sign.X)},${data.normalForceY.value(Sign.Y)}"
            },
            "frictionReactionForce".key() toDevEntry {
                "${data.frictionReactionForceX.value(Sign.X)},${0f.value(Sign.Y)}"
            },
            "friction".key() toDevEntry {
                "${data.frictionX.value(Sign.X)},${0f.value(Sign.Y)}"
            },
            "force".key() toDevEntry {
                "${data.forceX.value(Sign.X)},${data.forceY.value(Sign.Y)}"
            },
            "acceleration".key() toDevEntry {
                "${data.accelerationX.value(Sign.X)},${data.accelerationY.value(Sign.Y)}"
            },
            "velocity".key() toDevEntry {
                "${data.velocityX.value(Sign.X)},${data.velocityY.value(Sign.Y)}"
            },
            "position".key() toDevEntry {
                "${data.position.x.value(Sign.SIGNED)},${data.position.y.value(Sign.SIGNED)}"
            },
            "swimBehavior".key() toDevEntry {
                "${data.swimBehaviorX?.drivingTarget?.position.value(Sign.SIGNED)}," +
                    "${data.swimBehaviorY?.drivingTarget?.position.value(Sign.SIGNED)}," +
                    "${data.swimBehaviorX?.cooldownTicks},${data.swimBehaviorY?.cooldownTicks}"
            },
        )
    }

    private enum class Sign {
        X,
        Y,
        SIGNED,
        UNSIGNED,
        ;
    }

    private fun String.key(): String {
        return "%-21s".format(this)
    }

    private fun Float?.value(sign: Sign): String {
        if (this == null) return "%8s".format("null").colorMarkup(Color.YELLOW)
        return "%s%7.3f".format(
            when {
                this > 0f -> when (sign) {
                    Sign.X -> "►"
                    Sign.Y -> "▲"
                    Sign.SIGNED -> "+"
                    Sign.UNSIGNED -> " "
                }.colorMarkup(Color.GREEN)
                this < 0f -> when (sign) {
                    Sign.X -> "◄"
                    Sign.Y -> "▼"
                    Sign.SIGNED -> "-"
                    Sign.UNSIGNED -> " "
                }.colorMarkup(Color.RED)
                else -> " "
            },
            absoluteValue,
        )
    }

    private fun Boolean.value(): String {
        return "%8s".format(toString()).colorMarkup(if (this) Color.GREEN else Color.RED)
    }

    companion object {
        private const val WIDTH_DP = 960f
        private const val HEIGHT_DP = 600f
    }
}

class BodyInitDataComponent(
    var initialized: Boolean = false,
    val velocityX: Float,
    val velocityY: Float,
    val position: BodyPosition,
    val swimBehaviorX: BodyHelper.SwimBehavior?,
    val swimBehaviorY: BodyHelper.SwimBehavior?,
) : Component

class BodyDefComponent(
    val def: BodyDef,
) : Component

class BodyDataComponent(
    var width: Float = 0f,
    var height: Float = 0f,
    var halfWidth: Float = 0f,
    var halfHeight: Float = 0f,
    var minX: Float = 0f,
    var maxX: Float = 0f,
    var minY: Float = 0f,
    var maxY: Float = 0f,
    var left: Float = 0f,
    var right: Float = 0f,
    var bottom: Float = 0f,
    var top: Float = 0f,
    var isInsideLeft: Boolean = false,
    var isInsideRight: Boolean = false,
    var isInsideBottom: Boolean = false,
    var isInsideTop: Boolean = false,
    var area: Float = 0f,
    var areaX: Float = 0f,
    var areaY: Float = 0f,
    var areaInWater: Float = 0f,
    var density: Float = 0f,
    var mass: Float = 0f,
    var gravityY: Float = 0f,
    var buoyancyY: Float = 0f,
    var dragX: Float = 0f,
    var dragY: Float = 0f,
    var drivingTargetX: BodyDrivingTarget? = null,
    var drivingTargetY: BodyDrivingTarget? = null,
    var drivingForceX: Float = 0f,
    var drivingForceY: Float = 0f,
    var normalReactionForceX: Float = 0f,
    var normalReactionForceY: Float = 0f,
    var normalForceX: Float = 0f,
    var normalForceY: Float = 0f,
    var frictionReactionForceX: Float = 0f,
    var frictionX: Float = 0f,
    var forceX: Float = 0f,
    var forceY: Float = 0f,
    var accelerationX: Float = 0f,
    var accelerationY: Float = 0f,
    var velocityX: Float = 0f,
    var velocityY: Float = 0f,
    var position: BodyPosition = BodyPosition(0f, 0f),
    var swimBehaviorX: BodyHelper.SwimBehavior? = null,
    var swimBehaviorY: BodyHelper.SwimBehavior? = null,
) : Component {
    fun contains(x: Float, y: Float): Boolean {
        return x in left..right && y in bottom..top
    }
}

class BodyPositionComponent(
    var bodyPosition: BodyPosition,
) : Component

class TextureRegionComponent(
    val textureRegion: TextureRegion,
) : Component {
    val width: Float = textureRegion.regionWidth.pxToMeter
    val height: Float = textureRegion.regionHeight.pxToMeter
}

object ComponentMappers {
    val tank: ComponentMapper<TankComponent> = mapperFor()
    val bodyDef: ComponentMapper<BodyDefComponent> = mapperFor()
    val bodyInitData: ComponentMapper<BodyInitDataComponent> = mapperFor()
    val bodyData: ComponentMapper<BodyDataComponent> = mapperFor()
    val bodyPosition: ComponentMapper<BodyPositionComponent> = mapperFor()
    val textureRegion: ComponentMapper<TextureRegionComponent> = mapperFor()
}

class TankDrawDebugSystem(
    private val updateType: UpdateType,
    private val groupWrapper: TankGroupWrapper,
    private val shapeRendererHelper: ShapeRendererHelper,
) : EntitySystem() {
    private val batch: Batch = groupWrapper.batch

    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.DRAW
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        shapeRendererHelper.draw(batch = batch) {
            rect(groupWrapper.x, groupWrapper.y, groupWrapper.width, groupWrapper.height)
        }
    }
}

class BodyTickSystem(
    private val updateType: UpdateType,
) : IteratingSystem(allOf(
    TankComponent::class,
    BodyDataComponent::class,
).get()) {
    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.TICK
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tank = ComponentMappers.tank.get(entity)
        val bodyDef = ComponentMappers.bodyDef.get(entity).def
        val bodyInitData = ComponentMappers.bodyInitData.get(entity)
        val bodyData = ComponentMappers.bodyData.get(entity)

        val lastVelocityX: Float
        val lastVelocityY: Float
        val lastPosition: BodyPosition
        val lastSwimBehaviorX: BodyHelper.SwimBehavior?
        val lastSwimBehaviorY: BodyHelper.SwimBehavior?

        if (bodyInitData.initialized) {
            lastVelocityX = bodyData.velocityX
            lastVelocityY = bodyData.velocityY
            lastPosition = bodyData.position
            lastSwimBehaviorX = bodyData.swimBehaviorX
            lastSwimBehaviorY = bodyData.swimBehaviorY
        } else {
            lastVelocityX = bodyInitData.velocityX
            lastVelocityY = bodyInitData.velocityY
            lastPosition = bodyInitData.position
            lastSwimBehaviorX = bodyInitData.swimBehaviorX
            lastSwimBehaviorY = bodyInitData.swimBehaviorY
            bodyInitData.initialized = true
        }

        bodyData.width = bodyDef.width
        bodyData.height = bodyDef.height

        bodyData.halfWidth = bodyData.width / 2f
        bodyData.halfHeight = bodyData.height / 2f

        bodyData.minX = bodyData.halfWidth
        bodyData.maxX = tank.width - bodyData.halfWidth
        bodyData.minY = bodyData.halfHeight
        bodyData.maxY = Float.MAX_VALUE

        bodyData.left = lastPosition.x - bodyData.halfWidth
        bodyData.right = bodyData.left + bodyData.width
        bodyData.bottom = lastPosition.y - bodyData.halfHeight
        bodyData.top = bodyData.bottom + bodyData.height

        bodyData.isInsideLeft = bodyData.left > 0f
        bodyData.isInsideRight = bodyData.right < tank.width
        bodyData.isInsideBottom = bodyData.bottom > 0f
        bodyData.isInsideTop = true

        bodyData.area = bodyData.width * bodyData.height

        bodyData.areaX = bodyData.height * bodyData.height
        bodyData.areaY = bodyData.width * bodyData.width

        bodyData.areaInWater = ((tank.height - bodyData.bottom) / bodyData.height).coerceIn(0f, 1f) * bodyData.area

        bodyData.density = bodyDef.density

        bodyData.mass = bodyData.area * bodyData.density

        bodyData.gravityY = BodyHelper.gravityY(bodyData.mass)

        bodyData.buoyancyY = BodyHelper.buoyancyY(bodyData.areaInWater)

        bodyData.dragX = BodyHelper.drag(
            velocity = lastVelocityX,
            dragCoefficient = bodyDef.dragCoefficient,
            crossSectionalArea = bodyData.areaX,
        )
        bodyData.dragY = BodyHelper.drag(
            velocity = lastVelocityY,
            dragCoefficient = bodyDef.dragCoefficient,
            crossSectionalArea = bodyData.areaY,
        )

        bodyData.drivingTargetX = lastSwimBehaviorX?.drivingTarget
        bodyData.drivingTargetY = lastSwimBehaviorY?.drivingTarget

        bodyData.drivingForceX = BodyHelper.drivingForce(
            drivingAcceleration = bodyDef.drivingAccelerationX,
            drivingTarget = bodyData.drivingTargetX,
            position = lastPosition.x,
            mass = bodyData.mass,
            velocity = lastVelocityX,
        )
        bodyData.drivingForceY = BodyHelper.drivingForce(
            drivingAcceleration = bodyDef.drivingAccelerationY,
            drivingTarget = bodyData.drivingTargetY,
            position = lastPosition.y,
            mass = bodyData.mass,
            velocity = lastVelocityY,
        )

        bodyData.normalReactionForceX = BodyHelper.force(bodyData.dragX, bodyData.drivingForceX)
        bodyData.normalReactionForceY = BodyHelper.force(bodyData.gravityY, bodyData.drivingForceY, bodyData.buoyancyY, bodyData.dragY)

        bodyData.normalForceX = BodyHelper.normalForce(
            isInsideLeftOrBottom = bodyData.isInsideLeft,
            isInsideRightOrTop = bodyData.isInsideRight,
            normalReactionForce = bodyData.normalReactionForceX
        )
        bodyData.normalForceY = BodyHelper.normalForce(
            isInsideLeftOrBottom = bodyData.isInsideBottom,
            isInsideRightOrTop = bodyData.isInsideTop,
            normalReactionForce = bodyData.normalReactionForceY
        )

        bodyData.frictionReactionForceX = BodyHelper.force(bodyData.normalReactionForceX, bodyData.normalForceX)

        bodyData.frictionX = BodyHelper.frictionX(
            normalReactionForceY = bodyData.normalReactionForceY,
            frictionCoefficient = bodyDef.frictionCoefficient,
            isInsideBottom = bodyData.isInsideBottom,
            frictionReactionForceX = bodyData.frictionReactionForceX,
            velocityX = lastVelocityX,
        )

        bodyData.forceX = BodyHelper.force(bodyData.frictionReactionForceX, bodyData.frictionX)
        bodyData.forceY = BodyHelper.force(bodyData.normalReactionForceY, bodyData.normalForceY)

        bodyData.accelerationX = BodyHelper.acceleration(bodyData.forceX, bodyData.mass)
        bodyData.accelerationY = BodyHelper.acceleration(bodyData.forceY, bodyData.mass)

        bodyData.velocityX = BodyHelper.velocity(
            velocity = lastVelocityX,
            acceleration = bodyData.accelerationX,
            delta = deltaTime,
            isInsideLeftOrBottom = bodyData.isInsideLeft,
            isInsideRightOrTop = bodyData.isInsideRight,
            friction = bodyData.frictionX,
        )
        bodyData.velocityY = BodyHelper.velocity(
            velocity = lastVelocityY,
            acceleration = bodyData.accelerationY,
            delta = deltaTime,
            isInsideLeftOrBottom = bodyData.isInsideBottom,
            isInsideRightOrTop = bodyData.isInsideTop,
            friction = 0f,
        )

        bodyData.position = BodyPosition(
            x = BodyHelper.position(
                position = lastPosition.x,
                velocity = bodyData.velocityX,
                delta = deltaTime,
                minPosition = bodyData.minX,
                maxPosition = bodyData.maxX,
            ),
            y = BodyHelper.position(
                position = lastPosition.y,
                velocity = bodyData.velocityY,
                delta = deltaTime,
                minPosition = bodyData.minY,
                maxPosition = bodyData.maxY,
            ),
        )
        bodyData.swimBehaviorX = BodyHelper.swimBehavior(
            swimBehavior = lastSwimBehaviorX,
            tankSize = tank.width,
            leftOrBottom = bodyData.left,
            rightOrTop = bodyData.right,
            defSwimBehavior = bodyDef.swimBehaviorX,
        )
        bodyData.swimBehaviorY = BodyHelper.swimBehavior(
            swimBehavior = lastSwimBehaviorY,
            tankSize = tank.height,
            leftOrBottom = bodyData.bottom,
            rightOrTop = bodyData.top,
            defSwimBehavior = bodyDef.swimBehaviorY,
        )
    }
}

class BodyActSystem(
    private val updateType: UpdateType,
) : IteratingSystem(allOf(
    BodyDataComponent::class,
    BodyPositionComponent::class,
).get()) {
    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.ACT_DISABLE_TICK ||
            updateType.type == UpdateType.Type.ACT_ENABLE_TICK
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyData = ComponentMappers.bodyData.get(entity)
        val bodyPosition = ComponentMappers.bodyPosition.get(entity)
        bodyPosition.bodyPosition = if (updateType.type == UpdateType.Type.ACT_ENABLE_TICK) {
            bodyData.position
        } else {
            if (PreferenceManager.enableBodySmoothPosition.value) {
                BodyPosition(
                    x = BodyHelper.position(
                        position = bodyPosition.bodyPosition.x,
                        velocity = bodyData.velocityX,
                        delta = deltaTime,
                        minPosition = bodyData.minX,
                        maxPosition = bodyData.maxX,
                    ),
                    y = BodyHelper.position(
                        position = bodyPosition.bodyPosition.y,
                        velocity = bodyData.velocityY,
                        delta = deltaTime,
                        minPosition = bodyData.minY,
                        maxPosition = bodyData.maxY,
                    ),
                )
            } else {
                bodyData.position
            }
        }
    }
}

class BodyDrawSystem(
    private val updateType: UpdateType,
    private val groupWrapper: TankGroupWrapper,
) : IteratingSystem(allOf(
    BodyPositionComponent::class,
    TextureRegionComponent::class,
).get()) {
    private val batch: Batch = groupWrapper.batch

    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.DRAW
    }

    override fun update(deltaTime: Float) {
        groupWrapper.applyTransform(batch)
        super.update(deltaTime)
        groupWrapper.resetTransform(batch)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyPosition = ComponentMappers.bodyPosition.get(entity)
        val textureRegion = ComponentMappers.textureRegion.get(entity)
        batch.draw(
            textureRegion.textureRegion,
            bodyPosition.bodyPosition.x - textureRegion.width / 2,
            bodyPosition.bodyPosition.y - textureRegion.height / 2,
            textureRegion.width,
            textureRegion.height,
        )
    }
}

class BodyDrawDebugSystem(
    private val updateType: UpdateType,
    private val groupWrapper: TankGroupWrapper,
    private val shapeRendererHelper: ShapeRendererHelper,
) : IteratingSystem(allOf(
    TankComponent::class,
    BodyDataComponent::class,
).get()) {
    private val batch: Batch = groupWrapper.batch

    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.DRAW
    }

    override fun update(deltaTime: Float) {
        groupWrapper.applyTransform(batch)
        super.update(deltaTime)
        groupWrapper.resetTransform(batch)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tank = ComponentMappers.tank.get(entity)
        val bodyData = ComponentMappers.bodyData.get(entity)
        shapeRendererHelper.draw(
            enabled = tank.selectedBodyEntity() === entity,
            batch = batch,
        ) {
            rect(
                bodyData.left,
                bodyData.bottom,
                bodyData.width,
                bodyData.height,
            )
            bodyData.swimBehaviorX?.drivingTarget?.let { drivingTarget ->
                line(
                    drivingTarget.position,
                    0f,
                    drivingTarget.position,
                    tank.height,
                )
            }
            bodyData.swimBehaviorY?.drivingTarget?.let { drivingTarget ->
                line(
                    0f,
                    drivingTarget.position,
                    tank.width,
                    drivingTarget.position,
                )
            }
        }
    }
}
