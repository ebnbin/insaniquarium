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
import dev.ebnbin.insaniquarium.body.BodyData
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

    val data: TankData = TankData()

    init {
        groupWrapper.setSize(data.width, data.height)
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
            if (bodyData.bodyData.contains(x, y)) {
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

    fun act(delta: Float) {
        updateType.type = UpdateType.Type.ACT
        engine.update(delta)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        updateType.type = UpdateType.Type.DRAW
        engine.update(0f)
    }

    fun addedToStage(stage: TankStage) {
        engine.addSystem(BodyActSystem(updateType))
        engine.addSystem(BodyTickSystem(updateType))
        engine.addSystem(TankDrawSystem(updateType, groupWrapper, shapeRendererHelper))
        engine.addSystem(BodyDrawSystem(updateType, groupWrapper, shapeRendererHelper))
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
        entity.add(BodyDataComponent(
            bodyData = BodyData(
                tankData = data,
                type = type,
                velocityX = 0f,
                velocityY = 0f,
                position = position,
                swimBehaviorX = null,
                swimBehaviorY = null,
            ),
        ))
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

private data class UpdateType(
    var type: Type = Type.NONE,
) {
    enum class Type {
        NONE,
        TICK,
        ACT,
        DRAW,
        ;
    }
}

class TankComponent(
    val groupWrapper: TankGroupWrapper,
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
                val bodyData = ComponentMappers.bodyData.get(entity)
                devInfoEntryList = createDevInfoEntryList(bodyData).onEach { entry ->
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
                    val bodyData = ComponentMappers.bodyData.get(entity)
                    devInfoEntryList = createDevInfoEntryList(bodyData).onEach { entry ->
                        groupWrapper.stage.putDevInfo(entry)
                    }
                }
            }
        }
    }

    fun selectedBodyEntity(): Entity? {
        return selectedBodyEntity
    }

    private fun createDevInfoEntryList(data: BodyDataComponent): List<DevEntry> {
        return listOf(
            "type" toDevEntry {
                data.bodyData.type.id
            },
            "size".key() toDevEntry {
                "${data.bodyData.width.value(Sign.UNSIGNED)},${data.bodyData.height.value(Sign.UNSIGNED)}"
            },
            "lrbt".key() toDevEntry {
                "${data.bodyData.left.value(Sign.SIGNED)},${data.bodyData.right.value(Sign.SIGNED)}," +
                    "${data.bodyData.bottom.value(Sign.SIGNED)},${data.bodyData.top.value(Sign.SIGNED)}"
            },
            "isInsideLRBT".key() toDevEntry {
                "${data.bodyData.isInsideLeft.value()},${data.bodyData.isInsideRight.value()}," +
                    "${data.bodyData.isInsideBottom.value()},${data.bodyData.isInsideTop.value()}"
            },
            "areaInWater/area".key() toDevEntry {
                "${data.bodyData.areaInWater.value(Sign.UNSIGNED)}/${data.bodyData.area.value(Sign.UNSIGNED)}"
            },
            "density".key() toDevEntry {
                data.bodyData.density.value(Sign.UNSIGNED)
            },
            "mass".key() toDevEntry {
                data.bodyData.mass.value(Sign.UNSIGNED)
            },
            "gravity".key() toDevEntry {
                "${0f.value(Sign.X)},${data.bodyData.gravityY.value(Sign.Y)}"
            },
            "buoyancy".key() toDevEntry {
                "${0f.value(Sign.X)},${data.bodyData.buoyancyY.value(Sign.Y)}"
            },
            "drag".key() toDevEntry {
                "${data.bodyData.dragX.value(Sign.X)},${data.bodyData.dragY.value(Sign.Y)}"
            },
            "drivingTarget".key() toDevEntry {
                "${data.bodyData.drivingTargetX?.position.value(Sign.SIGNED)}," +
                    data.bodyData.drivingTargetY?.position.value(Sign.SIGNED)
            },
            "drivingForce".key() toDevEntry {
                "${data.bodyData.drivingForceX.value(Sign.X)},${data.bodyData.drivingForceY.value(Sign.Y)}"
            },
            "normalReactionForce".key() toDevEntry {
                "${data.bodyData.normalReactionForceX.value(Sign.X)}," +
                    "${data.bodyData.normalReactionForceY.value(Sign.Y)}"
            },
            "normalForce".key() toDevEntry {
                "${data.bodyData.normalForceX.value(Sign.X)},${data.bodyData.normalForceY.value(Sign.Y)}"
            },
            "frictionReactionForce".key() toDevEntry {
                "${data.bodyData.frictionReactionForceX.value(Sign.X)},${0f.value(Sign.Y)}"
            },
            "friction".key() toDevEntry {
                "${data.bodyData.frictionX.value(Sign.X)},${0f.value(Sign.Y)}"
            },
            "force".key() toDevEntry {
                "${data.bodyData.forceX.value(Sign.X)},${data.bodyData.forceY.value(Sign.Y)}"
            },
            "acceleration".key() toDevEntry {
                "${data.bodyData.accelerationX.value(Sign.X)},${data.bodyData.accelerationY.value(Sign.Y)}"
            },
            "velocity".key() toDevEntry {
                "${data.bodyData.velocityX.value(Sign.X)},${data.bodyData.velocityY.value(Sign.Y)}"
            },
            "position".key() toDevEntry {
                "${data.bodyData.position.x.value(Sign.SIGNED)},${data.bodyData.position.y.value(Sign.SIGNED)}"
            },
            "swimBehavior".key() toDevEntry {
                "${data.bodyData.swimBehaviorX?.drivingTarget?.position.value(Sign.SIGNED)}," +
                    "${data.bodyData.swimBehaviorY?.drivingTarget?.position.value(Sign.SIGNED)}," +
                    "${data.bodyData.swimBehaviorX?.cooldownTicks},${data.bodyData.swimBehaviorY?.cooldownTicks}"
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
}

private class BodyDataComponent(
    var bodyData: BodyData,
) : Component

private class BodyPositionComponent(
    var bodyPosition: BodyPosition,
) : Component

private class TextureRegionComponent(
    val textureRegion: TextureRegion,
) : Component {
    val width: Float = textureRegion.regionWidth.pxToMeter
    val height: Float = textureRegion.regionHeight.pxToMeter
}

private object ComponentMappers {
    val tank: ComponentMapper<TankComponent> = mapperFor()
    val bodyData: ComponentMapper<BodyDataComponent> = mapperFor()
    val bodyPosition: ComponentMapper<BodyPositionComponent> = mapperFor()
    val textureRegion: ComponentMapper<TextureRegionComponent> = mapperFor()
}

private class TankDrawSystem(
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

private class BodyTickSystem(
    private val updateType: UpdateType,
) : IteratingSystem(allOf(
    BodyDataComponent::class,
    BodyPositionComponent::class,
).get()) {
    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.TICK
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyData = ComponentMappers.bodyData.get(entity)
        val bodyPosition = ComponentMappers.bodyPosition.get(entity)
        bodyData.bodyData = bodyData.bodyData.tick(deltaTime)
        bodyPosition.bodyPosition = bodyData.bodyData.position
    }
}

private class BodyActSystem(
    private val updateType: UpdateType,
) : IteratingSystem(allOf(
    BodyDataComponent::class,
    BodyPositionComponent::class,
).get()) {
    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.ACT
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyData = ComponentMappers.bodyData.get(entity)
        val bodyPosition = ComponentMappers.bodyPosition.get(entity)
        bodyPosition.bodyPosition = if (PreferenceManager.enableBodySmoothPosition.value) {
            BodyPosition(
                x = BodyHelper.position(
                    position = bodyPosition.bodyPosition.x,
                    velocity = bodyData.bodyData.velocityX,
                    delta = deltaTime,
                    minPosition = bodyData.bodyData.minX,
                    maxPosition = bodyData.bodyData.maxX,
                ),
                y = BodyHelper.position(
                    position = bodyPosition.bodyPosition.y,
                    velocity = bodyData.bodyData.velocityY,
                    delta = deltaTime,
                    minPosition = bodyData.bodyData.minY,
                    maxPosition = bodyData.bodyData.maxY,
                ),
            )
        } else {
            bodyData.bodyData.position
        }
    }
}

private class BodyDrawSystem(
    private val updateType: UpdateType,
    private val groupWrapper: TankGroupWrapper,
    private val shapeRendererHelper: ShapeRendererHelper,
) : IteratingSystem(allOf(
    TankComponent::class,
    BodyDataComponent::class,
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
        val tankComponent = ComponentMappers.tank.get(entity)
        val bodyData = ComponentMappers.bodyData.get(entity)
        val bodyPosition = ComponentMappers.bodyPosition.get(entity)
        val textureRegion = ComponentMappers.textureRegion.get(entity)
        batch.draw(
            textureRegion.textureRegion,
            bodyPosition.bodyPosition.x - textureRegion.width / 2,
            bodyPosition.bodyPosition.y - textureRegion.height / 2,
            textureRegion.width,
            textureRegion.height,
        )
        shapeRendererHelper.draw(
            enabled = tankComponent.selectedBodyEntity() === entity,
            batch = batch,
        ) {
            rect(
                bodyData.bodyData.left,
                bodyData.bodyData.bottom,
                bodyData.bodyData.width,
                bodyData.bodyData.height,
            )
            bodyData.bodyData.swimBehaviorX?.drivingTarget?.let { drivingTarget ->
                line(
                    drivingTarget.position,
                    0f,
                    drivingTarget.position,
                    bodyData.bodyData.tankData.height,
                )
            }
            bodyData.bodyData.swimBehaviorY?.drivingTarget?.let { drivingTarget ->
                line(
                    0f,
                    drivingTarget.position,
                    bodyData.bodyData.tankData.width,
                    drivingTarget.position,
                )
            }
        }
    }
}
