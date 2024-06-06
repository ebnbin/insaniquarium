package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.Group
import dev.ebnbin.insaniquarium.body.BodyActor
import dev.ebnbin.insaniquarium.body.BodyType
import kotlin.random.Random

class TankGroup : Group() {
    init {
        setSize(WIDTH_DP.dpToMeter, HEIGHT_DP.dpToMeter)
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
