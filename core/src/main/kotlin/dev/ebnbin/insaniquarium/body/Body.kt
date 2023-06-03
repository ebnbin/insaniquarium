package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import dev.ebnbin.insaniquarium.aquarium.Tank

class Body(
    tank: Tank,
    id: String,
) : Actor() {
    init {
        debug()
    }

    val data: BodyData = BodyData(
        id = id,
        x = tank.width / 2f,
        y = tank.height / 2f,
    )

    override fun act(delta: Float) {
        super.act(delta)
        data.act(this, delta)
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
