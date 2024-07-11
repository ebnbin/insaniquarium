package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
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
        drivingTargetX = null,
        drivingTargetY = null,
    )
        private set

    private var position: BodyPosition = data.position

    init {
        actorWrapper.setPosition(this.position.x, this.position.y)
    }

    val devHelper: BodyDevHelper = BodyDevHelper(this)

    init {
        actorWrapper.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                devHelper.touchDown(event, x, y, pointer, button)
                return true
            }
        })
    }

    fun act(actDelta: Float, tickDelta: Float) {
        position = if (tickDelta > 0f) {
            data = data.tick(
                tickDelta = tickDelta,
                touchPosition = tank.touchPosition,
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

    fun addedToStage(stage: TankStage) {
        devHelper.addedToStage(stage)
    }

    fun removedFromStage(stage: TankStage) {
        devHelper.removedFromStage(stage)
    }

    fun addedToParent() {
        devHelper.addedToParent()
    }

    fun removedFromParent() {
        devHelper.removedFromParent()
    }
}
