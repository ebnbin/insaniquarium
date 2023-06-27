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
            if (data.canTransformByHunger) {
                require(config.hunger != null)
                require(config.hunger.transformation != null)
                val newBody = tank.replaceBody(
                    oldBody = this,
                    type = config.hunger.transformation,
                    createStatus = {
                        data.status.copy(
                            swimActX = null,
                            swimActY = null,
                            health = null,
                            hunger = null,
                            growth = null,
                            drop = null,
                            disappearAct = null,
                            drivingTargetX = null,
                            drivingTargetY = null,
                            animationData = data.status.animationData.copy(
                                stateTime = 0f,
                            ),
                        )
                    },
                )
                newBody.act(delta = input.delta)
            } else if (data.canTransformByGrowth) {
                require(config.growth != null)
                val growth = data.status.growth
                require(growth != null)
                val newBody = tank.replaceBody(
                    oldBody = this,
                    type = config.growth.transformation,
                    createStatus = {
                        data.status.copy(
                            growth = growth + config.growth.initialThreshold,
                        )
                    },
                )
                newBody.act(delta = input.delta)
            } else {
                tank.removeBody(this)
            }
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
