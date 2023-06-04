package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import dev.ebnbin.insaniquarium.aquarium.Tank

class Body(
    val tank: Tank,
    val id: String,
) : Actor() {
    init {
        debug()
    }

    var data: BodyData = BodyData.create(tank, id)
        private set

    override fun act(delta: Float) {
        super.act(delta)
        data = data.update(this, delta)
        data.act(this)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        data.draw(this, batch, parentAlpha)
    }

    override fun drawDebugBounds(shapes: ShapeRenderer) {
        super.drawDebugBounds(shapes)
        data.drawDebugBounds(this, shapes)
    }
}
