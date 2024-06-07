package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import dev.ebnbin.insaniquarium.body.BodyActor
import dev.ebnbin.insaniquarium.body.BodyType
import kotlin.random.Random

class TankGroup : Group() {
    init {
        setSize(WIDTH_DP.dpToMeter, HEIGHT_DP.dpToMeter)
    }

    private var devSelectedBody: BodyActor? = null

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                devUnselectBody(null)
                return true
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

    fun devCreateBody(count: Int) {
        repeat(count) {
            BodyActor(
                tankGroup = this,
                type = BodyType.entries.random(),
                x = Random.nextFloat() * width,
                y = Random.nextFloat() * height,
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
