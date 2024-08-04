package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.InputEvent
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.kgdx.dev.DevEntry
import dev.ebnbin.kgdx.dev.toDevEntry
import dev.ebnbin.kgdx.util.ShapeRendererHelper
import dev.ebnbin.kgdx.util.colorMarkup
import kotlin.math.absoluteValue

class BodyDevHelper(
    private val body: Body,
) {

    private val shapeRendererHelper: ShapeRendererHelper = ShapeRendererHelper()

    fun draw(batch: Batch, parentAlpha: Float) {
        shapeRendererHelper.draw(
            enabled = body.tank.devHelper.isSelected(body),
            batch = batch,
        ) {
            drawDebugBounds(this)
            body.data.swimBehaviorX?.drivingTarget?.let { drivingTarget ->
                line(drivingTarget.position, 0f, drivingTarget.position, body.data.tankData.height)
            }
            body.data.swimBehaviorY?.drivingTarget?.let { drivingTarget ->
                line(0f, drivingTarget.position, body.data.tankData.width, drivingTarget.position)
            }
        }
    }

    fun drawDebugBounds(shape: ShapeRenderer) {
        shape.rect(body.data.left, body.data.bottom, body.data.width, body.data.height)
    }

    fun devSelect() {
        body.actorWrapper.tankStage?.let { putDevInfo(it) }
    }

    fun devUnselect() {
        body.actorWrapper.tankStage?.let { removeDevInfo(it) }
    }

    private val devInfoEntryList: List<DevEntry> = listOf(
        "type" toDevEntry {
            body.data.type.id
        },
        "size".key() toDevEntry {
            "${body.data.width.value(Sign.UNSIGNED)},${body.data.height.value(Sign.UNSIGNED)}"
        },
        "lrbt".key() toDevEntry {
            "${body.data.left.value(Sign.SIGNED)},${body.data.right.value(Sign.SIGNED)}," +
                "${body.data.bottom.value(Sign.SIGNED)},${body.data.top.value(Sign.SIGNED)}"
        },
        "isInsideLRBT".key() toDevEntry {
            "${body.data.isInsideLeft.value()},${body.data.isInsideRight.value()}," +
                "${body.data.isInsideBottom.value()},${body.data.isInsideTop.value()}"
        },
        "areaInWater/area".key() toDevEntry {
            "${body.data.areaInWater.value(Sign.UNSIGNED)}/${body.data.area.value(Sign.UNSIGNED)}"
        },
        "density".key() toDevEntry {
            body.data.density.value(Sign.UNSIGNED)
        },
        "mass".key() toDevEntry {
            body.data.mass.value(Sign.UNSIGNED)
        },
        "gravity".key() toDevEntry {
            "${0f.value(Sign.X)},${body.data.gravityY.value(Sign.Y)}"
        },
        "buoyancy".key() toDevEntry {
            "${0f.value(Sign.X)},${body.data.buoyancyY.value(Sign.Y)}"
        },
        "drag".key() toDevEntry {
            "${body.data.dragX.value(Sign.X)},${body.data.dragY.value(Sign.Y)}"
        },
        "drivingTarget".key() toDevEntry {
            "${body.data.drivingTargetX?.position.value(Sign.SIGNED)}," +
                body.data.drivingTargetY?.position.value(Sign.SIGNED)
        },
        "drivingForce".key() toDevEntry {
            "${body.data.drivingForceX.value(Sign.X)},${body.data.drivingForceY.value(Sign.Y)}"
        },
        "normalReactionForce".key() toDevEntry {
            "${body.data.normalReactionForceX.value(Sign.X)},${body.data.normalReactionForceY.value(Sign.Y)}"
        },
        "normalForce".key() toDevEntry {
            "${body.data.normalForceX.value(Sign.X)},${body.data.normalForceY.value(Sign.Y)}"
        },
        "frictionReactionForce".key() toDevEntry {
            "${body.data.frictionReactionForceX.value(Sign.X)},${0f.value(Sign.Y)}"
        },
        "friction".key() toDevEntry {
            "${body.data.frictionX.value(Sign.X)},${0f.value(Sign.Y)}"
        },
        "force".key() toDevEntry {
            "${body.data.forceX.value(Sign.X)},${body.data.forceY.value(Sign.Y)}"
        },
        "acceleration".key() toDevEntry {
            "${body.data.accelerationX.value(Sign.X)},${body.data.accelerationY.value(Sign.Y)}"
        },
        "velocity".key() toDevEntry {
            "${body.data.velocityX.value(Sign.X)},${body.data.velocityY.value(Sign.Y)}"
        },
        "position".key() toDevEntry {
            "${body.data.position.x.value(Sign.SIGNED)},${body.data.position.y.value(Sign.SIGNED)}"
        },
        "swimBehavior".key() toDevEntry {
            "${body.data.swimBehaviorX?.drivingTarget?.position.value(Sign.SIGNED)}," +
                "${body.data.swimBehaviorY?.drivingTarget?.position.value(Sign.SIGNED)}," +
                "${body.data.swimBehaviorX?.cooldownTicks},${body.data.swimBehaviorY?.cooldownTicks}"
        },
    )

    private fun putDevInfo(stage: TankStage) {
        devInfoEntryList.forEach { entry ->
            stage.putDevInfo(entry)
        }
    }

    private fun removeDevInfo(stage: TankStage) {
        devInfoEntryList.reversed().forEach { entry ->
            stage.removeDevInfo(entry)
        }
    }

    fun addedToStage(stage: TankStage) {
    }

    fun removedFromStage(stage: TankStage) {
        body.tank.devHelper.unselectBody(body)
        removeDevInfo(stage)
    }

    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        body.tank.devHelper.selectBody(body)
        event.stop()
        return true
    }

    private enum class Sign {
        X,
        Y,
        SIGNED,
        UNSIGNED,
        ;
    }

    private fun String.key(): String {
        return "%-21s".format(this)
    }

    private fun Float?.value(sign: Sign): String {
        if (this == null) return "%8s".format("null").colorMarkup(Color.YELLOW)
        return "%s%7.3f".format(
            when {
                this > 0f -> when (sign) {
                    Sign.X -> "►"
                    Sign.Y -> "▲"
                    Sign.SIGNED -> "+"
                    Sign.UNSIGNED -> " "
                }.colorMarkup(Color.GREEN)
                this < 0f -> when (sign) {
                    Sign.X -> "◄"
                    Sign.Y -> "▼"
                    Sign.SIGNED -> "-"
                    Sign.UNSIGNED -> " "
                }.colorMarkup(Color.RED)
                else -> " "
            },
            absoluteValue,
        )
    }

    private fun Boolean.value(): String {
        return "%8s".format(toString()).colorMarkup(if (this) Color.GREEN else Color.RED)
    }
}
