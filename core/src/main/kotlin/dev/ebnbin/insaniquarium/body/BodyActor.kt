package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import dev.ebnbin.insaniquarium.tank.Tank
import dev.ebnbin.insaniquarium.tank.TankGroup
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.kgdx.dev.DevEntry
import dev.ebnbin.kgdx.dev.toDevEntry
import dev.ebnbin.kgdx.util.ShapeRendererHelper
import dev.ebnbin.kgdx.util.colorMarkup
import dev.ebnbin.kgdx.util.diffParent
import dev.ebnbin.kgdx.util.diffStage
import kotlin.math.absoluteValue

class BodyActor(
    tank: Tank,
    type: BodyType,
    position: BodyPosition,
) : Actor() {
    val body: Body = Body(
        actorWrapper = BodyActorWrapper(this),
        tank = tank,
        type = type,
        position = position,
    )

    override fun setStage(stage: Stage?) {
        diffStage<TankStage>(
            stage = stage,
            updateStage = { super.setStage(it) },
            addedToStage = ::addedToStage,
            removedFromStage = ::removedFromStage,
        )
    }

    private fun addedToStage(stage: TankStage) {
    }

    private fun removedFromStage(stage: TankStage) {
        removeDevInfo(stage)
    }

    override fun setParent(parent: Group?) {
        diffParent<TankGroup>(
            parent = parent,
            updateParent = { super.setParent(it) },
            addedToParent = ::addedToParent,
            removedFromParent = ::removedFromParent,
        )
    }

    private fun addedToParent(parent: TankGroup) {
    }

    private fun removedFromParent(parent: TankGroup) {
        parent.devUnselectBody(this)
    }

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                (parent as TankGroup?)?.devSelectBody(this@BodyActor)
                event.stop()
                return true
            }
        })
    }

    fun devSelect() {
        stage?.let { putDevInfo(it as TankStage) }
    }

    fun devUnselect() {
        stage?.let { removeDevInfo(it as TankStage) }
    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor? {
        if (touchable && this.touchable != Touchable.enabled) return null
        if (!isVisible) return null
        val left = body.data.left - this.x
        val right = left + body.data.width
        val bottom = body.data.bottom - this.y
        val top = bottom + body.data.height
        return if (x >= left && x < right && y >= bottom && y < top) this else null
    }

    override fun act(delta: Float) {
        super.act(delta)
        val tankStage = stage as TankStage? ?: return
        val tickDelta = tankStage.tickDelta
        body.act(
            actDelta = delta,
            tickDelta = tickDelta,
            touchPosition = (parent as TankGroup?)?.touchPosition,
        )
    }

    private val shapeRendererHelper: ShapeRendererHelper = ShapeRendererHelper()

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        body.draw(batch, parentAlpha)
        shapeRendererHelper.draw(batch) {
            if ((parent as TankGroup?)?.isDevSelected(this@BodyActor) == true) {
                body.data.drawDebugBounds(this)
            }
        }
    }

    override fun drawDebugBounds(shapes: ShapeRenderer) {
        super.drawDebugBounds(shapes)
        body.data.drawDebugBounds(shapes)
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
