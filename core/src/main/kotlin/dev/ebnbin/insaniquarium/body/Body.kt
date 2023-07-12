package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Position
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.game

class Body(
    val type: BodyType,
    val id: String,
    val delegate: BodyActorDelegate,
    state: BodyState,
) {
    val config: BodyConfig = game.config.body.getValue(type)

    init {
        val textureRegion = baseGame.assets.texture.getValue(config.animations.swim.assetId).getTextureRegionList()
            .first()
        delegate.setSize(
            textureRegion.regionWidth.toFloat().unitToMeter,
            textureRegion.regionHeight.toFloat().unitToMeter,
        )
    }

    private var position: Position = state.position

    var data: BodyData = BodyData(
        body = this,
        state = state,
    )
        private set

    fun tick(delta: Float) {
        tick(
            delta = delta,
            input = BodyInput(),
        )
    }

    fun tick(input: BodyInput) {
        tick(
            delta = 0f,
            input = input,
        )
    }

    private fun tick(
        delta: Float,
        input: BodyInput,
    ) {
        data = data.tick(
            delta = delta,
            input = input,
            position = position,
        )
        data.postTick()
    }

    fun act(delta: Float) {
        actPosition(delta)
        if (delegate.debug) {
            data.actDebug()
        }
    }

    private fun actPosition(delta: Float) {
        position = Position(
            x = BodyForceHelper.nextPosition(
                position = position.x,
                velocity = data.state.velocityX,
                delta = delta,
                minPosition = data.minX,
                maxPosition = data.maxX,
            ),
            y = BodyForceHelper.nextPosition(
                position = position.y,
                velocity = data.state.velocityY,
                delta = delta,
                minPosition = data.minY,
                maxPosition = data.maxY,
            ),
        )
        delegate.setPosition(position.x, position.y)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        data.draw(batch, parentAlpha)
    }

    fun drawDebug(shapes: ShapeRenderer) {
        data.drawDebug(shapes)
    }

    fun touch(point: Point): Boolean {
        return data.touch(point)
    }
}
