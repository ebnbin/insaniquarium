package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.utils.MeterFitViewport
import dev.ebnbin.insaniquarium.body.Body

class TankStage : BaseStage(viewport = MeterFitViewport()) {
    init {
        val clyde = Body("clyde")
        clyde.debug()
        clyde.setPosition(width / 2f, height / 2f, Align.center)
        addActor(clyde)
    }
}
