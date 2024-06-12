package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.Gdx
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
import dev.ebnbin.kgdx.util.diffParent
import dev.ebnbin.kgdx.util.diffStage

class BodyActor(
    tankGroup: TankGroup,
    type: BodyType,
    x: Float,
    y: Float,
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
        x = x,
        y = y,
    )

    private var position: Pair<Float, Float> = data.x to data.y

    init {
        setPosition(position.first, position.second, Align.center)
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
            data = data.tick(tickDelta)
            data.x to data.y
        } else {
            position.first + data.velocityX * delta to position.second + data.velocityY * delta
        }
        this.position = position
        setPosition(position.first, position.second, Align.center)
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
        "size" toDevEntry {
            "%.3f,%.3f".format(
                data.width,
                data.height,
            )
        },
        "lrbt" toDevEntry {
            "%.3f,%.3f,%.3f,%.3f".format(
                data.left,
                data.right,
                data.bottom,
                data.top,
            )
        },
        "area" toDevEntry {
            "%.3f".format(
                data.area,
            )
        },
        "areaInWater" toDevEntry {
            "%.3f".format(
                data.areaInWater,
            )
        },
        "density" toDevEntry {
            "%.3f".format(
                data.density,
            )
        },
        "mass" toDevEntry {
            "%.3f".format(
                data.mass,
            )
        },
        "gravity" toDevEntry {
            "%.3f".format(
                data.gravityY,
            )
        },
        "buoyancy" toDevEntry {
            "%.3f".format(
                data.buoyancyY,
            )
        },
        "force" toDevEntry {
            "%.3f,%.3f".format(
                data.forceX,
                data.forceY,
            )
        },
        "acceleration" toDevEntry {
            "%.3f,%.3f".format(
                data.accelerationX,
                data.accelerationY,
            )
        },
        "velocity" toDevEntry {
            "%.3f,%.3f".format(
                data.velocityX,
                data.velocityY,
            )
        },
        "position" toDevEntry {
            "%.3f,%.3f".format(
                data.x,
                data.y,
            )
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
}
