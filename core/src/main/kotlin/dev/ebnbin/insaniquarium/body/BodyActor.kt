package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import dev.ebnbin.insaniquarium.tank.TankGroup
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.insaniquarium.tank.pxToMeter
import dev.ebnbin.kgdx.util.diffStage

class BodyActor(
    tankGroup: TankGroup,
    type: BodyType,
) : Actor() {
    private val textureRegion: TextureRegion = type.def.textureAsset.getTextureRegionList().first()

    init {
        setSize(textureRegion.regionWidth.pxToMeter, textureRegion.regionHeight.pxToMeter)
    }

    private var data: BodyData = BodyData(
        type = type,
        tankWidth = tankGroup.width,
        tankHeight = tankGroup.height,
        velocityX = 0f,
        velocityY = 0f,
        x = tankGroup.width / 2f,
        y = tankGroup.height / 2f,
    )

    init {
        setPosition(data.x, data.y, Align.center)
    }

    override fun setStage(stage: Stage?) {
        diffStage<TankStage>(
            stage = stage,
            updateStage = { super.setStage(it) },
            addedToStage = ::addedToStage,
            removedFromStage = ::removedFromStage,
        )
    }

    private fun addedToStage(stage: TankStage) {
    }

    private fun removedFromStage(stage: TankStage) {
    }

    override fun act(delta: Float) {
        super.act(delta)
        data = data.act(delta)
        setPosition(data.x, data.y, Align.center)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(
            textureRegion,
            x,
            y,
            width,
            height,
        )
    }

    override fun drawDebugBounds(shapes: ShapeRenderer) {
        super.drawDebugBounds(shapes)
        data.drawDebugBounds(shapes)
    }
}
