package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.game

data class BodyData(
    val id: String,

    val x: Float,
    val y: Float,
) {
    val config: BodyConfig = game.config.body.getValue(id)

    //*****************************************************************************************************************

    val width: Float = config.width
    val height: Float = config.height

    val halfWidth: Float = width / 2f
    val halfHeight: Float = height / 2f

    val left: Float = x - halfWidth
    val right: Float = left + width
    val bottom: Float = y - halfHeight
    val top: Float = bottom + height

    //*****************************************************************************************************************

    private val textureRegion: TextureRegion =
        game.assets.texture.getValue(config.assetId).getTextureRegionList().first()

    private val actorWidth: Float = textureRegion.regionWidth.toFloat().unitToMeter
    private val actorHeight: Float = textureRegion.regionHeight.toFloat().unitToMeter

    //*****************************************************************************************************************

    fun act(body: Body, delta: Float) {
        body.setSize(actorWidth, actorHeight)
        body.setPosition(x, y, Align.center)
    }

    fun draw(body: Body, batch: Batch, parentAlpha: Float) {
        batch.draw(
            textureRegion.texture,
            body.x,
            body.y,
            body.originX,
            body.originY,
            body.width,
            body.height,
            body.scaleX,
            body.scaleY,
            body.rotation,
            textureRegion.regionX,
            textureRegion.regionY,
            textureRegion.regionWidth,
            textureRegion.regionHeight,
            textureRegion.isFlipX,
            textureRegion.isFlipY,
        )
    }

    fun drawDebugBounds(body: Body, shapes: ShapeRenderer) {
        shapes.rect(left, bottom, width, height)
    }
}
