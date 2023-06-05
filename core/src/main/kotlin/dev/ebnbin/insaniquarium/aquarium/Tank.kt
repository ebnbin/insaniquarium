package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.Body
import kotlin.random.Random

class Tank : Group() {
    init {
        debug()

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                touchX = x
                touchY = y
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                touchX = null
                touchY = null
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                super.touchDragged(event, x, y, pointer)
                touchX = x
                touchY = y
            }
        })

        setSize(960f.unitToMeter, 600f.unitToMeter)

        val clyde = Body(
            tank = this,
            id = "clyde",
        )
        addActor(clyde)
    }

    var touchX: Float? = null
        private set
    var touchY: Float? = null
        private set

    fun devAddBody() {
        val body = Body(
            tank = this,
            id = "clyde",
            initX = Random.nextFloat() * width,
            initY = Random.nextFloat() * height,
        )
        addActor(body)
    }

    fun devClearBodies() {
        clearChildren()
    }
}
