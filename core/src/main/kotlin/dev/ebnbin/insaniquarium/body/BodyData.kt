package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Direction
import dev.ebnbin.gdx.utils.World
import dev.ebnbin.gdx.utils.colorMarkup
import dev.ebnbin.gdx.utils.direction
import dev.ebnbin.gdx.utils.magnitude
import dev.ebnbin.gdx.utils.minMax
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.aquarium.Tank
import dev.ebnbin.insaniquarium.game

data class BodyData(
    val id: String,

    val tankWidth: Float,
    val tankHeight: Float,

    val velocityX: Float,
    val velocityY: Float,

    val x: Float,
    val y: Float,
) {
    val config: BodyConfig = game.config.body.getValue(id)

    //*****************************************************************************************************************

    val width: Float = config.width
    val height: Float = config.height

    val halfWidth: Float = width / 2f
    val halfHeight: Float = height / 2f

    val minX: Float = halfWidth
    val maxX: Float = tankWidth - halfWidth

    val minY: Float = halfHeight
    val maxY: Float = Float.MAX_VALUE

    val left: Float = x - halfWidth
    val right: Float = left + width
    val bottom: Float = y - halfHeight
    val top: Float = bottom + height

    val isInsideLeft: Boolean = left > 0f
    val isInsideRight: Boolean = right < tankWidth
    val isInsideBottom: Boolean = bottom > 0f

    /**
     * Percent of body inside tank.
     */
    val insideTopPercent: Float = ((height + tankHeight - top) / height).minMax(0f, 1f)

    val depth: Float = config.depth

    val area: Float = width * height

    val volume: Float = area * depth

    val density: Float = config.density ?: World.DENSITY_WATER

    val mass: Float = volume * density

    //*****************************************************************************************************************

    val gravityX: Float = 0f
    val gravityY: Float = -(mass * World.G)

    val buoyancyX: Float = 0f
    val buoyancyY: Float = +(World.DENSITY_WATER * World.G * volume * insideTopPercent)

    val normalReactionX: Float = gravityX + buoyancyX
    val normalReactionY: Float = gravityY + buoyancyY

    val normalX: Float = if (!isInsideLeft && normalReactionX < 0f || !isInsideRight && normalReactionX > 0f) {
        -normalReactionX
    } else {
        0f
    }
    val normalY: Float = if (!isInsideBottom && normalReactionY < 0f) {
        -normalReactionY
    } else {
        0f
    }

    val forceX: Float = normalReactionX + normalX
    val forceY: Float = normalReactionY + normalY

    val accelerationX: Float = forceX / mass
    val accelerationY: Float = forceY / mass

    //*****************************************************************************************************************

    private val textureRegion: TextureRegion =
        game.assets.texture.getValue(config.assetId).getTextureRegionList().first()

    private val actorWidth: Float = textureRegion.regionWidth.toFloat().unitToMeter
    private val actorHeight: Float = textureRegion.regionHeight.toFloat().unitToMeter

    //*****************************************************************************************************************

    fun update(body: Body, delta: Float): BodyData {
        val nextVelocityX = (velocityX + accelerationX * delta).let {
            if (!isInsideLeft && it < 0f || !isInsideRight && it > 0f) {
                0f
            } else {
                it
            }
        }
        val nextVelocityY = (velocityY + accelerationY * delta).let {
            if (!isInsideBottom && it < 0f) {
                0f
            } else {
                it
            }
        }

        val nextX = (x + nextVelocityX * delta).minMax(minX, maxX)
        val nextY = (y + nextVelocityY * delta).minMax(minY, maxY)

        return copy(
            velocityX = nextVelocityX,
            velocityY = nextVelocityY,
            x = nextX,
            y = nextY,
        )
    }

    fun act(body: Body) {
        devLog()

        body.setSize(actorWidth, actorHeight)
        body.setPosition(x, y, Align.center)
    }

    fun draw(body: Body, batch: Batch, parentAlpha: Float) {
        batch.draw(
            textureRegion.texture,
            body.x,
            body.y,
            body.originX,
            body.originY,
            body.width,
            body.height,
            body.scaleX,
            body.scaleY,
            body.rotation,
            textureRegion.regionX,
            textureRegion.regionY,
            textureRegion.regionWidth,
            textureRegion.regionHeight,
            textureRegion.isFlipX,
            textureRegion.isFlipY,
        )
    }

    fun drawDebugBounds(body: Body, shapes: ShapeRenderer) {
        shapes.rect(left, bottom, width, height)
    }

    companion object {
        fun create(
            tank: Tank,
            id: String,
            initX: Float? = null,
            initY: Float? = null,
        ): BodyData {
            return BodyData(
                id = id,
                tankWidth = tank.width,
                tankHeight = tank.height,
                velocityX = 0f,
                velocityY = 0f,
                x = initX ?: (tank.width / 2f),
                y = initY ?: (tank.height / 2f),
            )
        }
    }

    //*****************************************************************************************************************
    // dev

    private fun devLog() {
        baseGame.putLog("size          ") {
            "${width.devText()},${height.devText()},${depth.devText()}"
        }
        baseGame.putLog("lrbt          ") {
            "${left.devText()},${right.devText()},${bottom.devText()},${top.devText()}"
        }
        baseGame.putLog("density       ") {
            density.devText()
        }
        baseGame.putLog("gravity       ") {
            "${gravityX.devText(XY.X)},${gravityY.devText(XY.Y)}"
        }
        baseGame.putLog("buoyancy      ") {
            "${buoyancyX.devText(XY.X)},${buoyancyY.devText(XY.Y)}"
        }
        baseGame.putLog("normalReaction") {
            "${normalReactionX.devText(XY.X)},${normalReactionY.devText(XY.Y)}"
        }
        baseGame.putLog("normal        ") {
            "${normalX.devText(XY.X)},${normalY.devText(XY.Y)}"
        }
        baseGame.putLog("force         ") {
            "${forceX.devText(XY.X)},${forceY.devText(XY.Y)}"
        }
        baseGame.putLog("acceleration  ") {
            "${accelerationX.devText(XY.X)},${accelerationY.devText(XY.Y)}"
        }
        baseGame.putLog("velocity      ") {
            "${velocityX.devText(XY.X)},${velocityY.devText(XY.Y)}"
        }
        baseGame.putLog("position      ") {
            "${x.devText()},${y.devText()}"
        }
    }

    private enum class XY {
        X,
        Y,
        ;
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
