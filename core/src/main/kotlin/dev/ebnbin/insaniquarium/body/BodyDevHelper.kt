package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.XY
import dev.ebnbin.gdx.utils.colorMarkup
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.magnitude

object BodyDevHelper {
    fun act(data: BodyData, body: Body, delta: Float) {
        baseGame.putLog("size            ") {
            "${data.width.devText()},${data.height.devText()},${data.depth.devText()}"
        }
        baseGame.putLog("lrbt            ") {
            "${data.left.devText()},${data.right.devText()},${data.bottom.devText()},${data.top.devText()}"
        }
        baseGame.putLog("density         ") {
            data.density.devText()
        }
        baseGame.putLog("gravity,buoyancy") {
            "${data.gravityY.devText(XY.Y)},${data.buoyancyY.devText(XY.Y)}"
        }
        baseGame.putLog("drag            ") {
            "${data.dragX.devText(XY.X)},${data.dragY.devText(XY.Y)}"
        }
        baseGame.putLog("driving         ") {
            "${data.drivingX.devText(XY.X)},${data.drivingY.devText(XY.Y)}"
        }
        baseGame.putLog("normalReaction  ") {
            "${data.normalReactionX.devText(XY.X)},${data.normalReactionY.devText(XY.Y)}"
        }
        baseGame.putLog("normal          ") {
            "${data.normalX.devText(XY.X)},${data.normalY.devText(XY.Y)}"
        }
        baseGame.putLog("force           ") {
            "${data.forceX.devText(XY.X)},${data.forceY.devText(XY.Y)}"
        }
        baseGame.putLog("acceleration    ") {
            "${data.accelerationX.devText(XY.X)},${data.accelerationY.devText(XY.Y)}"
        }
        baseGame.putLog("velocity        ") {
            "${data.velocityX.devText(XY.X)},${data.velocityY.devText(XY.Y)}"
        }
        baseGame.putLog("position        ") {
            "${data.x.devText()},${data.y.devText()}"
        }
    }

    fun draw(data: BodyData, body: Body, shapes: ShapeRenderer) {
        shapes.rect(data.left, data.bottom, data.width, data.height)
        shapes.line(data.left, data.bottom, data.right, data.top)
        shapes.line(data.left, data.top, data.right, data.bottom)
        shapes.rect(data.depthLeft, data.bottom, data.depth, data.height)
        shapes.rect(data.left, data.depthBottom, data.width, data.depth)
        data.drivingTargetX?.let {
            shapes.line(it.position, 0f, it.position, data.tankHeight)
        }
        data.drivingTargetY?.let {
            shapes.line(0f, it.position, data.tankWidth, it.position)
        }
    }

    private fun Float.devText(xy: XY? = null): String {
        val directionText = when (direction) {
            Direction.ZERO -> " "
            Direction.POSITIVE -> when (xy) {
                XY.X -> "►"
                XY.Y -> "▲"
                null -> "+"
            }.colorMarkup(Color.GREEN)
            Direction.NEGATIVE -> when (xy) {
                XY.X -> "◄"
                XY.Y -> "▼"
                null -> "-"
            }.colorMarkup(Color.RED)
        }
        var nonZero = false
        val magnitudeText = "%11.6f".format(magnitude)
            .reversed()
            .map {
                if (nonZero) {
                    "$it"
                } else {
                    when (it) {
                        '0' -> {
                            "$it".colorMarkup(Color.GRAY)
                        }
                        '.' -> {
                            nonZero = true
                            "$it".colorMarkup(Color.GRAY)
                        }
                        else -> {
                            nonZero = true
                            "$it"
                        }
                    }
                }
            }
            .reversed()
            .joinToString("")
        return "$directionText$magnitudeText"
    }
}
