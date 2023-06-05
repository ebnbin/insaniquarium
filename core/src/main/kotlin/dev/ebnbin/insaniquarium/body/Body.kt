package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import dev.ebnbin.insaniquarium.aquarium.Tank

class Body(
    val tank: Tank,
    val id: String,
    initX: Float? = null,
    initY: Float? = null,
) : Actor() {
    init {
        debug()
    }

    var data: BodyData = BodyData.create(tank, id, initX, initY)
        private set

    override fun act(delta: Float) {
        super.act(delta)
        data = data.update(this, delta)
        data.act(this)
        data.actDebug(this, delta)
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
