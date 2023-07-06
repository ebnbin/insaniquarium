package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.utils.MeterFitViewport

class TankStage : BaseStage(viewport = MeterFitViewport()) {
    val tank: Tank = Tank().also {
        it.setPosition(width / 2f, height / 2f, Align.center)
        addActor(it)
    }

    override fun tick() {
        super.tick()
        tank.tick()
    }
}
