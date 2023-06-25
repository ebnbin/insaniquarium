package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import dev.ebnbin.insaniquarium.game
import dev.ebnbin.insaniquarium.tank.Tank

class Body(
    val tank: Tank,
    val type: BodyType,
    val id: String,
    createStatus: (body: Body) -> BodyStatus,
) : Actor() {
    val config: BodyConfig = game.config.body.getValue(type)

    var data: BodyData = BodyData.create(this, createStatus)
        private set

    override fun act(delta: Float) {
        super.act(delta)
        val input = BodyInput(
            delta = delta,
        )
        act(input)
    }

    fun act(input: BodyInput): Body {
        data = data.update(input)
        if (data.canRemove) {
            tank.removeBody(this)
            return this
        }
        data.act()
        if (debug) {
            data.actDebug()
        }
        return this
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        data.draw(batch, parentAlpha)
    }

    override fun drawDebug(shapes: ShapeRenderer) {
        super.drawDebug(shapes)
        data.drawDebug(shapes)
    }
}
