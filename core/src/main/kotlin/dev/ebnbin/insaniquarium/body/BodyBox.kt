package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.unitToMeter

data class BodyBox(
    val body: Body,
    val params: Params,
    val status: Status,
) {
    data class Params(
        val minX: Float,
        val maxX: Float,
        val minY: Float,
        val maxY: Float,
        val velocityX: Float,
        val velocityY: Float,
    )

    data class Status(
        val x: Float = 0f,
        val y: Float = 0f,
    )

    fun act(delta: Float, params: Params): BodyBox {
        val nextStatus = nextStatus(delta)
        return copy(
            params = params,
            status = nextStatus,
        )
    }

    fun nextStatus(delta: Float): Status {
        val nextX = nextX(delta)
        val nextY = nextY(delta)
        return Status(
            x = nextX,
            y = nextY,
        )
    }

    private fun nextX(delta: Float): Float {
        return BodyForceHelper.nextPosition(
            position = status.x,
            velocity = params.velocityX, // TODO nextVelocityX
            minPosition = params.minX,
            maxPosition = params.maxX,
            delta = delta,
        )
    }

    private fun nextY(delta: Float): Float {
        return BodyForceHelper.nextPosition(
            position = status.y,
            velocity = params.velocityY, // TODO nextVelocityY
            minPosition = params.minY,
            maxPosition = params.maxY,
            delta = delta,
        )
    }

    //*****************************************************************************************************************

    fun postAct() {
        val textureRegion = body.config.animations.swim.getTextureRegion(0)
        body.delegate.setSize(
            textureRegion.regionWidth.toFloat().unitToMeter,
            textureRegion.regionHeight.toFloat().unitToMeter,
        )
        body.delegate.setPosition(status.x, status.y)
    }

    //*****************************************************************************************************************

    fun actDebug() {
        baseGame.putLog("position        ") {
            "${status.x.devText()},${status.y.devText()}"
        }
    }
}
