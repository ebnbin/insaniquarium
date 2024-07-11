package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.Align
import dev.ebnbin.insaniquarium.tank.TankGroup
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.insaniquarium.tank.pxToMeter
import dev.ebnbin.kgdx.dev.DevEntry
import dev.ebnbin.kgdx.dev.toDevEntry
import dev.ebnbin.kgdx.util.colorMarkup
import dev.ebnbin.kgdx.util.diffParent
import dev.ebnbin.kgdx.util.diffStage
import kotlin.math.absoluteValue

class BodyActor(
    tankGroup: TankGroup,
    type: BodyType,
    position: BodyPosition,
) : Actor() {
    private val textureRegion: TextureRegion = type.def.textureAsset.getTextureRegionList().first()

    init {
        setSize(textureRegion.regionWidth.pxToMeter, textureRegion.regionHeight.pxToMeter)
    }

    private var data: BodyData = BodyData(
        type = type,
        tankWidth = tankGroup.width,
        tankHeight = tankGroup.height,
        velocityX = 0f,
        velocityY = 0f,
        position = position,
        drivingTargetX = null,
        drivingTargetY = null,
    )

    private var position: BodyPosition = data.position

    init {
        setPosition(position.x, position.y, Align.center)
    }

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
        val left = data.left - this.x
        val right = left + data.width
        val bottom = data.bottom - this.y
        val top = bottom + data.height
        return if (x >= left && x < right && y >= bottom && y < top) this else null
    }

    override fun act(delta: Float) {
        super.act(delta)
        val tankStage = stage as TankStage? ?: return
        val tickDelta = tankStage.tickDelta
        val position = if (tickDelta > 0f) {
            data = data.tick(
                tickDelta = tickDelta,
                touchPosition = (parent as TankGroup?)?.touchPosition,
            )
            data.position
        } else {
            BodyPosition(
                x = BodyHelper.position(
                    position = position.x,
                    velocity = data.velocityX,
                    delta = delta,
                    minPosition = data.minX,
                    maxPosition = data.maxX,
                ),
                y = BodyHelper.position(
                    position = position.y,
                    velocity = data.velocityY,
                    delta = delta,
                    minPosition = data.minY,
                    maxPosition = data.maxY,
                ),
            )
        }
        this.position = position
        setPosition(position.x, position.y, Align.center)
    }

    private val shapeRenderer: ShapeRenderer = ShapeRenderer().also {
        it.setAutoShapeType(true)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(
            textureRegion,
            x,
            y,
            width,
            height,
        )
        batch.end()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.transformMatrix = batch.transformMatrix
        shapeRenderer.begin()
        if ((parent as TankGroup?)?.isDevSelected(this) == true) {
            data.drawDebugBounds(shapeRenderer)
        }
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
        batch.begin()
    }

    override fun drawDebugBounds(shapes: ShapeRenderer) {
        super.drawDebugBounds(shapes)
        data.drawDebugBounds(shapes)
    }

    private val devInfoEntryList: List<DevEntry> = listOf(
        "type" toDevEntry {
            data.type.id
        },
        "size".key() toDevEntry {
            "${data.width.value(Sign.UNSIGNED)},${data.height.value(Sign.UNSIGNED)}"
        },
        "lrbt".key() toDevEntry {
            "${data.left.value(Sign.SIGNED)},${data.right.value(Sign.SIGNED)}," +
                "${data.bottom.value(Sign.SIGNED)},${data.top.value(Sign.SIGNED)}"
        },
        "isInsideLRBT".key() toDevEntry {
            "${data.isInsideLeft.value()},${data.isInsideRight.value()}," +
                "${data.isInsideBottom.value()},${data.isInsideTop.value()}"
        },
        "areaInWater/area".key() toDevEntry {
            "${data.areaInWater.value(Sign.UNSIGNED)}/${data.area.value(Sign.UNSIGNED)}"
        },
        "density".key() toDevEntry {
            data.density.value(Sign.UNSIGNED)
        },
        "mass".key() toDevEntry {
            data.mass.value(Sign.UNSIGNED)
        },
        "gravity".key() toDevEntry {
            "${0f.value(Sign.X)},${data.gravityY.value(Sign.Y)}"
        },
        "buoyancy".key() toDevEntry {
            "${0f.value(Sign.X)},${data.buoyancyY.value(Sign.Y)}"
        },
        "drag".key() toDevEntry {
            "${data.dragX.value(Sign.X)},${data.dragY.value(Sign.Y)}"
        },
        "drivingTarget".key() toDevEntry {
            "${data.drivingTargetX?.position.value(Sign.SIGNED)},${data.drivingTargetY?.position.value(Sign.SIGNED)}"
        },
        "drivingForce".key() toDevEntry {
            "${data.drivingForceX.value(Sign.X)},${data.drivingForceY.value(Sign.Y)}"
        },
        "normalReactionForce".key() toDevEntry {
            "${data.normalReactionForceX.value(Sign.X)},${data.normalReactionForceY.value(Sign.Y)}"
        },
        "normalForce".key() toDevEntry {
            "${data.normalForceX.value(Sign.X)},${data.normalForceY.value(Sign.Y)}"
        },
        "frictionReactionForce".key() toDevEntry {
            "${data.frictionReactionForceX.value(Sign.X)},${0f.value(Sign.Y)}"
        },
        "friction".key() toDevEntry {
            "${data.frictionX.value(Sign.X)},${0f.value(Sign.Y)}"
        },
        "force".key() toDevEntry {
            "${data.forceX.value(Sign.X)},${data.forceY.value(Sign.Y)}"
        },
        "acceleration".key() toDevEntry {
            "${data.accelerationX.value(Sign.X)},${data.accelerationY.value(Sign.Y)}"
        },
        "velocity".key() toDevEntry {
            "${data.velocityX.value(Sign.X)},${data.velocityY.value(Sign.Y)}"
        },
        "position".key() toDevEntry {
            "${data.position.x.value(Sign.SIGNED)},${data.position.y.value(Sign.SIGNED)}"
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
