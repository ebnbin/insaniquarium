package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.unitToMeter

data class BodyData(
    val type: BodyType,
    val id: String,
    val config: BodyConfig,
    val delegate: BodyActorDelegate,
    val boxStatus: BodyBox.Status,
    val lifeStatus: BodyLife.Status,
) {
    val box: BodyBox = BodyBox(
        config = config.box,
        tankWidth = delegate.tankWidth,
        tankHeight = delegate.tankHeight,
        drivingTargetX = lifeStatus.drivingTargetX,
        drivingTargetY = lifeStatus.drivingTargetY,
        status = boxStatus,
    )

    //*****************************************************************************************************************

    val life: BodyLife = BodyLife(
        config = config,
        box = box,
        status = lifeStatus,
    )

    //*****************************************************************************************************************

    fun hit(touchPoint: Point): Boolean {
        val hit = box.hit(touchPoint)
        if (hit) {
            delegate.tick(
                input = BodyInput(
                    healthDiff = -(life.health ?: 0f),
                ),
            )
        }
        return hit
    }

    //*****************************************************************************************************************

    fun tick(input: BodyInput): BodyData {
        return copy(
            lifeStatus = life.nextStatus(
                delegate = delegate,
                input = input,
            ),
        )
    }

    fun postTick(): Boolean {
        return life.postTick(
            delegate = delegate,
        )
    }

    fun update(delta: Float): BodyData {
        return copy(
            boxStatus = box.nextStatus(
                delta = delta,
            )
        )
    }

    fun act() {
        val textureRegion = config.renderer.animations.swim.getTextureRegion(0f)
        delegate.setSize(
            textureRegion.regionWidth.toFloat().unitToMeter,
            textureRegion.regionHeight.toFloat().unitToMeter,
        )
        delegate.setPosition(box.x, box.y)
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        life.draw(delegate, batch, parentAlpha)
    }

    //*****************************************************************************************************************

    fun actDebug() {
        BodyDevHelper.act(this)
    }

    fun drawDebug(shapes: ShapeRenderer) {
        BodyDevHelper.draw(this, shapes)
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
