package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.LifecycleStage

class TankStage : LifecycleStage(TankViewport()) {
    private val tankGroup: TankGroup = TankGroup().also {
        addActor(it)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        tankGroup.setPosition(
            (width - tankGroup.width) / 2f,
            (height - tankGroup.height) / 3f,
            Align.bottomLeft,
        )
    }
}
