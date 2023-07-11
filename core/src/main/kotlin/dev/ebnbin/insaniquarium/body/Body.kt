package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.insaniquarium.game

class Body(
    val type: BodyType,
    val id: String,
    val delegate: BodyActorDelegate,
    initBoxStatus: BodyBox.Status,
    initLifeStatus: BodyLife.Status,
) {
    val config: BodyConfig = game.config.body.getValue(type)

    var life: BodyLife = BodyLife(
        body = this,
        params = BodyLife.Params(
            x = initBoxStatus.x,
            y = initBoxStatus.y,
        ),
        status = initLifeStatus,
    )
        private set

    var box: BodyBox = BodyBox(
        body = this,
        params = BodyBox.Params(
            minX = life.minX,
            maxX = life.maxX,
            minY = life.minY,
            maxY = life.maxY,
            velocityX = life.status.velocityX,
            velocityY = life.status.velocityY,
        ),
        status = initBoxStatus,
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
                x = box.status.x,
                y = box.status.y,
            ),
        )
        life.postTick()
    }

    fun act(delta: Float) {
        box = box.act(
            delta = delta,
            params = BodyBox.Params(
                minX = life.minX,
                maxX = life.maxX,
                minY = life.minY,
                maxY = life.maxY,
                velocityX = life.status.velocityX,
                velocityY = life.status.velocityY,
            ),
        )
        box.postAct()
        if (delegate.debug) {
            actDebug()
        }
    }

    private fun actDebug() {
        baseGame.putLog("type,id         ") {
            "${type.serializedName},${id}"
        }
        box.actDebug()
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
