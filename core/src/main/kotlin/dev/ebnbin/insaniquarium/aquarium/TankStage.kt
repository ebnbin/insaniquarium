package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.utils.MeterFitViewport

class TankStage : BaseStage(viewport = MeterFitViewport()) {
    init {
        val tank = Tank()
        tank.setPosition(width / 2f, height / 2f, Align.center)
        addActor(tank)
    }
}
