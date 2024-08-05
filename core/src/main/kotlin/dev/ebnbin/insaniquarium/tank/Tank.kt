package dev.ebnbin.insaniquarium.tank

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import dev.ebnbin.insaniquarium.body.BodyData
import dev.ebnbin.insaniquarium.body.BodyHelper
import dev.ebnbin.insaniquarium.body.BodyPosition
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.preference.PreferenceManager
import dev.ebnbin.kgdx.util.ShapeRendererHelper
import ktx.ashley.allOf
import ktx.ashley.mapperFor

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
                    if (tankComponent.selectedBodyEntity === entity) {
                        tankComponent.selectedBodyEntity = null
                    }
                }
            },
        )
    }

    private val updateType: UpdateType = UpdateType()

    val tankComponent: TankComponent = TankComponent()

    private val shapeRendererHelper: ShapeRendererHelper = ShapeRendererHelper()

    val data: TankData = TankData()

    init {
        groupWrapper.setSize(data.width, data.height)
    }

    val devHelper: TankDevHelper = TankDevHelper(this)

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
                tankComponent.selectedBodyEntity = entity
                handled = true
                break
            }
        }
        if (!handled) {
            if (tankComponent.selectedBodyEntity == null) {
                val bodyType = tankComponent.selectedBodyType
                if (bodyType != null) {
                    addBody(
                        type = bodyType,
                        position = BodyPosition(x, y),
                    )
                }
            } else {
                tankComponent.selectedBodyEntity = null
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
        devHelper.addedToStage(stage)
        engine.addSystem(BodyActSystem(updateType))
        engine.addSystem(BodyTickSystem(updateType))
        engine.addSystem(TankDrawSystem(updateType, groupWrapper, shapeRendererHelper))
        engine.addSystem(BodyDrawSystem(updateType, groupWrapper, shapeRendererHelper))
    }

    fun removedFromStage(stage: TankStage) {
        engine.removeAllSystems()
        devHelper.removedFromStage(stage)
    }

    fun addBody(
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

    fun clearBodies() {
        engine.removeAllEntities(allOf(
            TankComponent::class,
            BodyDataComponent::class,
            BodyPositionComponent::class,
            TextureRegionComponent::class,
        ).get())
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
    var touchPosition: BodyPosition? = null,
    var selectedBodyType: BodyType? = null,
    var selectedBodyEntity: Entity? = null,
) : Component

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
            enabled = tankComponent.selectedBodyEntity === entity,
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
