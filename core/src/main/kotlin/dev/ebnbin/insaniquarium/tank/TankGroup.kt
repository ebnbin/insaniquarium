package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.Group
import dev.ebnbin.insaniquarium.body.BodyActor
import dev.ebnbin.insaniquarium.body.BodyType

class TankGroup : Group() {
    init {
        setSize(WIDTH_DP.dpToMeter, HEIGHT_DP.dpToMeter)

        BodyActor(
            tankGroup = this,
            type = BodyType.entries.random(),
        ).also {
            addActor(it)
        }
    }

    companion object {
        private const val WIDTH_DP = 960f
        private const val HEIGHT_DP = 600f
    }
}
