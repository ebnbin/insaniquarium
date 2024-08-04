package dev.ebnbin.insaniquarium.tank

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import dev.ebnbin.insaniquarium.body.BodyActor
import dev.ebnbin.insaniquarium.body.BodyData
import dev.ebnbin.insaniquarium.body.BodyHelper
import dev.ebnbin.insaniquarium.body.BodyPosition
import dev.ebnbin.insaniquarium.body.BodyType

private const val USE_ASHLEY = true

class Tank(
    val groupWrapper: TankGroupWrapper,
) {
    private val engine: Engine = Engine()

    private val updateType: UpdateType = UpdateType()

    val data: TankData = TankData()

    init {
        groupWrapper.setSize(data.width, data.height)
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

    fun tick(delta: Float) {
        if (USE_ASHLEY) {
            updateType.type = UpdateType.Type.TICK
            engine.update(delta)
        }
    }

    fun act(delta: Float) {
        if (USE_ASHLEY) {
            updateType.type = UpdateType.Type.ACT
            engine.update(delta)
        }
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        devHelper.draw(batch, parentAlpha)
        if (USE_ASHLEY) {
            updateType.type = UpdateType.Type.DRAW
            engine.update(0f)
        }
    }

    fun addedToStage(stage: TankStage) {
        devHelper.addedToStage(stage)
        if (USE_ASHLEY) {
            engine.addSystem(ActSystem(updateType))
            engine.addSystem(TickSystem(updateType))
            engine.addSystem(DrawSystem(updateType, groupWrapper.batch))
        }
    }

    fun removedFromStage(stage: TankStage) {
        if (USE_ASHLEY) {
            engine.removeAllSystems()
        }
        devHelper.removedFromStage(stage)
    }

    fun addBody(
        type: BodyType,
        position: BodyPosition,
    ) {
        if (USE_ASHLEY) {
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
        } else {
            val actor = BodyActor(
                tank = this,
                type = type,
                position = position,
            )
            groupWrapper.addActor(actor)
        }
    }

    fun clearBodies() {
        if (USE_ASHLEY) {
            engine.removeAllEntities()
        } else {
            groupWrapper.clearChildren()
        }
    }
}

private data class UpdateType(
    var type: Type = Type.TICK,
) {
    enum class Type {
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

private class TickSystem(
    private val updateType: UpdateType,
) : IteratingSystem(
    Family.all(BodyDataComponent::class.java, BodyPositionComponent::class.java).get(),
) {
    private val bodyDataCM = ComponentMapper.getFor(BodyDataComponent::class.java)
    private val bodyPositionCM = ComponentMapper.getFor(BodyPositionComponent::class.java)

    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.TICK
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyData = bodyDataCM.get(entity)
        val bodyPosition = bodyPositionCM.get(entity)
        bodyData.bodyData = bodyData.bodyData.tick(deltaTime)
        bodyPosition.bodyPosition = bodyData.bodyData.position
    }
}

private class ActSystem(
    private val updateType: UpdateType,
) : IteratingSystem(
    Family.all(BodyDataComponent::class.java, BodyPositionComponent::class.java).get(),
) {
    private val bodyDataCM = ComponentMapper.getFor(BodyDataComponent::class.java)
    private val bodyPositionCM = ComponentMapper.getFor(BodyPositionComponent::class.java)

    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.ACT
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyData = bodyDataCM.get(entity)
        val bodyPosition = bodyPositionCM.get(entity)
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
) : EntitySystem() {
    private lateinit var entities: ImmutableArray<Entity>

    private val bodyPositionCM = ComponentMapper.getFor(BodyPositionComponent::class.java)
    private val textureRegionCM = ComponentMapper.getFor(TextureRegionComponent::class.java)

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        entities = engine.getEntitiesFor(Family.all(
            BodyPositionComponent::class.java,
            TextureRegionComponent::class.java,
        ).get())
    }

    override fun checkProcessing(): Boolean {
        return updateType.type == UpdateType.Type.DRAW
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        for (entity in entities) {
            val bodyPosition = bodyPositionCM.get(entity).bodyPosition
            val textureRegion = textureRegionCM.get(entity)
            batch.draw(
                textureRegion.textureRegion,
                bodyPosition.x - textureRegion.width / 2,
                bodyPosition.y - textureRegion.height / 2,
                textureRegion.width,
                textureRegion.height,
            )
        }
    }
}
