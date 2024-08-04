package dev.ebnbin.insaniquarium.tank

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import dev.ebnbin.insaniquarium.body.BodyData
import dev.ebnbin.insaniquarium.body.BodyHelper
import dev.ebnbin.insaniquarium.body.BodyPosition
import dev.ebnbin.insaniquarium.body.BodyType
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class Tank(
    val actorWrapper: TankActorWrapper,
) {
    private val engine: Engine = Engine()

    private val updateType: UpdateType = UpdateType()

    val data: TankData = TankData()

    init {
        actorWrapper.setSize(data.width, data.height)
    }

    val devHelper: TankDevHelper = TankDevHelper(this)

    var touchPosition: BodyPosition? = null
        private set

    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (devHelper.touchDown(event, x, y, pointer, button)) return false
        touchPosition = BodyPosition(x, y)
        return true
    }

    fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        touchPosition = BodyPosition(x, y)
    }

    fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        touchPosition = null
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
        devHelper.draw(batch, parentAlpha)
        updateType.type = UpdateType.Type.DRAW
        engine.update(0f)
    }

    fun addedToStage(stage: TankStage) {
        devHelper.addedToStage(stage)
        engine.addSystem(ActSystem(updateType))
        engine.addSystem(TickSystem(updateType))
        engine.addSystem(DrawSystem(updateType, actorWrapper.batch))
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
        engine.removeAllEntities()
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
    val bodyData: ComponentMapper<BodyDataComponent> = mapperFor()
    val bodyPosition: ComponentMapper<BodyPositionComponent> = mapperFor()
    val textureRegion: ComponentMapper<TextureRegionComponent> = mapperFor()
}

private class TickSystem(
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

private class ActSystem(
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
        bodyPosition.bodyPosition = BodyPosition(
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
    }
}

private class DrawSystem(
    private val updateType: UpdateType,
    private val batch: Batch,
) : IteratingSystem(allOf(
    BodyPositionComponent::class,
    TextureRegionComponent::class,
).get()) {
    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.DRAW
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
