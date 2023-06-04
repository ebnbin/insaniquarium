package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.Group
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.Body
import kotlin.random.Random

class Tank : Group() {
    init {
        debug()

        setSize(960f.unitToMeter, 600f.unitToMeter)

        val clyde = Body(
            tank = this,
            id = "clyde",
        )
        addActor(clyde)
    }

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
