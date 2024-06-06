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

    init {
        setPosition(data.x, data.y, Align.center)
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
        if (parent.devSelectedBody === this) {
            parent.devSelectedBody = null
        }
    }

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                (parent as TankGroup?)?.devSelectedBody = this@BodyActor
                event.stop()
                return true
            }
        })
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
        data = data.act(delta)
        setPosition(data.x, data.y, Align.center)
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
        if ((parent as TankGroup?)?.devSelectedBody === this) {
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
}
