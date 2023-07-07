package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import dev.ebnbin.insaniquarium.game
import dev.ebnbin.insaniquarium.tank.Tank

class BodyActor(
    val tank: Tank,
    val type: BodyType,
    val id: String,
    boxStatus: BodyBox.Status,
    lifeStatus: BodyLife.Status,
) : Actor() {
    val config: BodyConfig = game.config.body.getValue(type)

    var data: BodyData = BodyData.create(this, boxStatus, lifeStatus)
        private set

    fun tick() {
        performTick()
    }

    fun performTick(input: BodyInput = BodyInput()): BodyData {
        data = data.tick(input)
        data.postTick()
        return data
    }

    override fun act(delta: Float) {
        super.act(delta)
        performAct(delta)
    }

    fun performAct(delta: Float): BodyData {
        data = data.update(delta)
        data.act()
        if (debug) {
            data.actDebug()
        }
        return data
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
