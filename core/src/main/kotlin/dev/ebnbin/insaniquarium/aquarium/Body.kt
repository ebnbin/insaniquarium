package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor

class Body(
    private val textureRegion: TextureRegion,
) : Actor() {
    init {
        setSize(textureRegion.regionWidth.toFloat(), textureRegion.regionHeight.toFloat())
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
}
