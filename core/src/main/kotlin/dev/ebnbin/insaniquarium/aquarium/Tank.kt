package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.Group
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.Body

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
}
