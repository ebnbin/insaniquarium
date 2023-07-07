package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import dev.ebnbin.insaniquarium.tank.Tank

class BodyActor(
    val tank: Tank,
    val type: BodyType,
    val id: String,
    boxStatus: BodyBox.Status,
    lifeStatus: BodyLife.Status,
) : Actor() {
    val body: Body = Body(
        type = type,
        id = id,
        delegate = BodyActorDelegate(this),
        initBoxStatus = boxStatus,
        initLifeStatus = lifeStatus,
    )

    fun tick(delta: Float) {
        body.tick(delta)
    }

    override fun act(delta: Float) {
        super.act(delta)
        body.act(delta)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        body.draw(batch, parentAlpha)
    }

    override fun drawDebug(shapes: ShapeRenderer) {
        super.drawDebug(shapes)
        body.drawDebug(shapes)
    }
}
