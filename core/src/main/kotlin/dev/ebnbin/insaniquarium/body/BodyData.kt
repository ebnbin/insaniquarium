package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Point

data class BodyData(
    val type: BodyType,
    val id: String,
    val config: BodyConfig,
    val delegate: BodyActorDelegate,
    val boxStatus: BodyBox.Status,
    val lifeStatus: BodyLife.Status,
) {
    val box: BodyBox = BodyBox(
        config = config,
        delegate = delegate,
        params = BodyBox.Params(
            drivingTargetX = lifeStatus.drivingTargetX,
            drivingTargetY = lifeStatus.drivingTargetY,
        ),
        status = boxStatus,
    )

    //*****************************************************************************************************************

    val life: BodyLife = BodyLife(
        config = config,
        delegate = delegate,
        box = box,
        status = lifeStatus,
    )

    //*****************************************************************************************************************

    fun touch(point: Point): Boolean {
        return life.touch(point)
    }

    //*****************************************************************************************************************

    fun tick(input: BodyInput): BodyData {
        return copy(
            lifeStatus = life.nextStatus(
                input = input,
            ),
        )
    }

    fun postTick(): Boolean {
        return life.postTick()
    }

    fun act(delta: Float): BodyData {
        return copy(
            boxStatus = box.nextStatus(
                delta = delta,
            )
        )
    }

    fun postAct() {
        box.postAct()
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        life.draw(batch, parentAlpha)
    }

    //*****************************************************************************************************************

    fun actDebug() {
        baseGame.putLog("type,id         ") {
            "${type.serializedName},${id}"
        }
        box.actDebug()
        life.actDebug()
    }

    fun drawDebug(shapes: ShapeRenderer) {
        box.drawDebug(shapes)
    }

    //*****************************************************************************************************************

    companion object {
        fun create(
            bodyActor: BodyActor,
            boxStatus: BodyBox.Status,
            lifeStatus: BodyLife.Status,
        ): BodyData {
            return BodyData(
                type = bodyActor.type,
                id = bodyActor.id,
                config = bodyActor.config,
                delegate = BodyActorDelegate(bodyActor),
                boxStatus = boxStatus,
                lifeStatus = lifeStatus,
            )
        }
    }
}
