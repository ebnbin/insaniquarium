package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.lifecycle.BaseGame
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

    var box: BodyBox = BodyBox(
        body = this,
        params = BodyBox.Params(
            drivingTargetX = initLifeStatus.drivingTargetX,
            drivingTargetY = initLifeStatus.drivingTargetY,
        ),
        status = initBoxStatus,
    )
        private set

    var life: BodyLife = BodyLife(
        body = this,
        params = BodyLife.Params(
            box = box,
        ),
        status = initLifeStatus,
    )
        private set

    fun tick(
        input: BodyInput = BodyInput(
            tickDelta = BaseGame.TICK,
        ),
    ): BodyLife {
        life = life.tick(
            input = input,
            params = BodyLife.Params(
                box = box,
            ),
        )
        life.postTick()
        return life
    }

    fun act(delta: Float): BodyBox {
        box = box.act(
            delta = delta,
            params = BodyBox.Params(
                drivingTargetX = life.status.drivingTargetX,
                drivingTargetY = life.status.drivingTargetY,
            ),
        )
        box.postAct()
        if (delegate.debug) {
            actDebug()
        }
        return box
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
        box.drawDebug(shapes)
    }

    fun touch(point: Point): Boolean {
        return life.touch(point)
    }
}
