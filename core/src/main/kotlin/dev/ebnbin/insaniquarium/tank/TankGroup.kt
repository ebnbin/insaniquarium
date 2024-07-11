package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import dev.ebnbin.insaniquarium.body.BodyActor
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.body.BodyPosition
import kotlin.random.Random

class TankGroup : Group() {
    init {
        setSize(WIDTH_DP.dpToMeter, HEIGHT_DP.dpToMeter)
    }

    private var devSelectedBody: BodyActor? = null

    var touchPosition: BodyPosition? = null
        private set

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                touchPosition = BodyPosition(x, y)
                val tankStage = stage as TankStage? ?: return false
                val devBodyType = tankStage.devBodyType
                if (devBodyType != null) {
                    devCreateBody(
                        type = devBodyType,
                        count = 1,
                        x = x,
                        y = y,
                    )
                }
                return true
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                super.touchDragged(event, x, y, pointer)
                touchPosition = BodyPosition(x, y)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                touchPosition = null
            }
        })
    }

    fun isDevSelected(body: BodyActor): Boolean {
        return devSelectedBody === body
    }

    fun devSelectBody(body: BodyActor) {
        devUnselectBody(null)
        devSelectedBody = body
        body.devSelect()
    }

    fun devUnselectBody(body: BodyActor?) {
        if (body == null) {
            devSelectedBody?.devUnselect()
            devSelectedBody = null
        } else {
            if (devSelectedBody === body) {
                body.devUnselect()
                devSelectedBody = null
            }
        }
    }

    fun devCreateBody(
        type: BodyType?,
        count: Int,
        x: Float? = null,
        y: Float? = null,
    ) {
        repeat(count) {
            BodyActor(
                tankGroup = this,
                type = type ?: BodyType.entries.random(),
                position = BodyPosition(
                    x = x ?: (Random.nextFloat() * width),
                    y = y ?: (Random.nextFloat() * height),
                ),
            ).also {
                addActor(it)
            }
        }
    }

    fun devClearBodies() {
        clearChildren()
    }

    companion object {
        private const val WIDTH_DP = 960f
        private const val HEIGHT_DP = 600f
    }
}
