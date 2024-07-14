package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.InputEvent
import dev.ebnbin.insaniquarium.preference.PreferenceManager
import dev.ebnbin.insaniquarium.tank.Tank
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.insaniquarium.tank.pxToMeter

class Body(
    val actorWrapper: BodyActorWrapper,
    val tank: Tank,
    val type: BodyType,
    position: BodyPosition,
) {
    private val textureRegion: TextureRegion = type.def.textureAsset.getTextureRegionList().first()

    init {
        actorWrapper.setSize(textureRegion.regionWidth.pxToMeter, textureRegion.regionHeight.pxToMeter)
    }

    var data: BodyData = BodyData(
        tankData = tank.data,
        type = type,
        velocityX = 0f,
        velocityY = 0f,
        position = position,
        swimBehaviorX = null,
        swimBehaviorY = null,
    )
        private set

    private var position: BodyPosition = data.position

    init {
        actorWrapper.setPosition(this.position.x, this.position.y)
    }

    val devHelper: BodyDevHelper = BodyDevHelper(this)

    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        devHelper.touchDown(event, x, y, pointer, button)
        return true
    }

    fun addedToStage(stage: TankStage) {
        devHelper.addedToStage(stage)
    }

    fun removedFromStage(stage: TankStage) {
        devHelper.removedFromStage(stage)
    }

    fun hit(x: Float, y: Float): Boolean {
        val left = data.left - actorWrapper.x
        val right = left + data.width
        val bottom = data.bottom - actorWrapper.y
        val top = bottom + data.height
        return x >= left && x < right && y >= bottom && y < top
    }

    fun act(actDelta: Float, tickDelta: Float) {
        position = if (tickDelta > 0f) {
            data = data.tick(
                tickDelta = tickDelta,
            )
            data.position
        } else {
            if (PreferenceManager.enableBodySmoothPosition.value) {
                BodyPosition(
                    x = BodyHelper.position(
                        position = position.x,
                        velocity = data.velocityX,
                        delta = actDelta,
                        minPosition = data.minX,
                        maxPosition = data.maxX,
                    ),
                    y = BodyHelper.position(
                        position = position.y,
                        velocity = data.velocityY,
                        delta = actDelta,
                        minPosition = data.minY,
                        maxPosition = data.maxY,
                    ),
                )
            } else {
                data.position
            }
        }
        actorWrapper.setPosition(position.x, position.y)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            textureRegion,
            actorWrapper.x,
            actorWrapper.y,
            actorWrapper.width,
            actorWrapper.height,
        )
        devHelper.draw(batch, parentAlpha)
    }

    fun drawDebugBounds(shape: ShapeRenderer) {
        devHelper.drawDebugBounds(shape)
    }
}
