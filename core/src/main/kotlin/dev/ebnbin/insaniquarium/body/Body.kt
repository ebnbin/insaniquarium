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
    initPosition: Position,
    initLifeStatus: BodyLife.Status,
) {
    val config: BodyConfig = game.config.body.getValue(type)

    init {
        val textureRegion = config.animations.swim.getTextureRegion(0)
        delegate.setSize(
            textureRegion.regionWidth.toFloat().unitToMeter,
            textureRegion.regionHeight.toFloat().unitToMeter,
        )
    }

    var position: Position = initPosition
        private set

    var life: BodyLife = BodyLife(
        body = this,
        params = BodyLife.Params(
            position = position,
        ),
        status = initLifeStatus,
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
        life = life.tick(
            delta = delta,
            input = input,
            params = BodyLife.Params(
                position = position,
            ),
        )
        life.postTick()
    }

    fun act(delta: Float) {
        actPosition(delta)
        if (delegate.debug) {
            actDebug()
        }
    }

    private fun actPosition(delta: Float) {
        position = Position(
            x = BodyForceHelper.nextPosition(
                position = position.x,
                velocity = life.status.velocityX,
                delta = delta,
            ),
            y = BodyForceHelper.nextPosition(
                position = position.y,
                velocity = life.status.velocityY,
                delta = delta,
            ),
        )
        delegate.setPosition(position.x, position.y)
    }

    private fun actDebug() {
        baseGame.putLog("type,id         ") {
            "${type.serializedName},${id}"
        }
        baseGame.putLog("position        ") {
            "${position.x.devText()},${position.y.devText()}"
        }
        life.actDebug()
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        life.draw(batch, parentAlpha)
    }

    fun drawDebug(shapes: ShapeRenderer) {
        life.drawDebug(shapes)
    }

    fun touch(point: Point): Boolean {
        return life.touch(point)
    }
}
