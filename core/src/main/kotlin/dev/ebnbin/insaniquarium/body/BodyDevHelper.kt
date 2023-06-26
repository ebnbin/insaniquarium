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
    fun act(data: BodyData) {
        baseGame.putLog("type,id         ") {
            "${data.body.type.serializedName},${data.body.id}"
        }
        data.force.devPutLogs()
        baseGame.putLog("health          ") {
            "${data.status.health?.devText()}"
        }
        baseGame.putLog("hunger          ") {
            "${data.status.hunger?.devText()},${data.hungerStatus}"
        }
        baseGame.putLog("growth          ") {
            "${data.status.growth?.devText()}"
        }
        baseGame.putLog("drop            ") {
            "${data.status.drop?.devText()}"
        }
    }

    fun draw(data: BodyData, shapes: ShapeRenderer) {
        shapes.rect(data.left, data.bottom, data.width, data.height)
        shapes.line(data.left, data.bottom, data.right, data.top)
        shapes.line(data.left, data.top, data.right, data.bottom)
        val halfDepth: Float = data.force.depth / 2f
        val depthLeft: Float = data.status.x - halfDepth
        val depthBottom: Float = data.status.y - halfDepth
        shapes.rect(depthLeft, data.bottom, data.force.depth, data.height)
        shapes.rect(data.left, depthBottom, data.width, data.force.depth)
        data.status.drivingTargetX?.let {
            shapes.line(it.position, 0f, it.position, data.body.tank.height)
        }
        data.status.drivingTargetY?.let {
            shapes.line(0f, it.position, data.body.tank.width, it.position)
        }
    }
}

fun Float.devText(xy: XY? = null): String {
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
