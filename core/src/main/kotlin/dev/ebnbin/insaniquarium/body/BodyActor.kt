package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import dev.ebnbin.insaniquarium.tank.TankGroup
import dev.ebnbin.insaniquarium.tank.pxToMeter

class BodyActor(
    tankGroup: TankGroup,
    private val type: BodyType,
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

    override fun drawDebugBounds(shapes: ShapeRenderer) {
        super.drawDebugBounds(shapes)
        shapes.rect(
            getX(Align.center) - type.def.width / 2f,
            getY(Align.center) - type.def.height / 2f,
            type.def.width,
            type.def.height,
        )
    }
}
