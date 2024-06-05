package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import dev.ebnbin.insaniquarium.tank.TankGroup
import dev.ebnbin.insaniquarium.tank.pxToMeter

class BodyActor(
    tankGroup: TankGroup,
    type: BodyType,
) : Actor() {
    private val textureRegion: TextureRegion = type.def.textureAsset.getTextureRegionList().first()

    init {
        setSize(textureRegion.regionWidth.pxToMeter, textureRegion.regionHeight.pxToMeter)
        setPosition(tankGroup.width / 2f, tankGroup.height / 2f, Align.center)
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
}
