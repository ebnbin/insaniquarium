package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.aquarium.Tank
import dev.ebnbin.insaniquarium.game

class Body(
    tank: Tank,
    id: String,
) : Actor() {
    private val config: BodyConfig = game.config.body.getValue(id)

    private val textureRegion: TextureRegion =
        game.assets.texture.getValue(config.assetId).getTextureRegionList().first()

    init {
        debug()

        setSize(textureRegion.regionWidth.toFloat().unitToMeter, textureRegion.regionHeight.toFloat().unitToMeter)
        setPosition(tank.width / 2f, tank.height / 2f, Align.center)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(
            textureRegion.texture,
            x,
            y,
            originX,
            originY,
            width,
            height,
            scaleX,
            scaleY,
            rotation,
            textureRegion.regionX,
            textureRegion.regionY,
            textureRegion.regionWidth,
            textureRegion.regionHeight,
            textureRegion.isFlipX,
            textureRegion.isFlipY,
        )
    }

    override fun drawDebugBounds(shapes: ShapeRenderer) {
        super.drawDebugBounds(shapes)
        shapes.rect(
            x + (width - config.width) / 2f,
            y + (height - config.height) / 2f,
            config.width,
            config.height,
        )
    }
}
