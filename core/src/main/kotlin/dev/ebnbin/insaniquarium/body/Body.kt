package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import dev.ebnbin.insaniquarium.aquarium.Tank

class Body(
    val tank: Tank,
    params: BodyParams,
) : Actor() {
    var data: BodyData = BodyData.create(tank, params)
        private set

    override fun act(delta: Float) {
        super.act(delta)
        val input = BodyInput(
            body = this,
            delta = delta,
        )
        act(input)
    }

    fun act(input: BodyInput) {
        val nextData = data.update(input)
        if (nextData == null) {
            tank.removeBody(this)
            return
        }
        data = nextData
        data.act(this)
        if (debug) {
            data.actDebug(this, input.delta)
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        data.draw(this, batch, parentAlpha)
    }

    override fun drawDebug(shapes: ShapeRenderer) {
        super.drawDebug(shapes)
        data.drawDebug(this, shapes)
    }
}
