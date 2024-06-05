package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.game

class TankGroup : Group() {
    init {
        setSize(WIDTH_DP.dpToMeter, HEIGHT_DP.dpToMeter)

        Image(game.assets.texture("stinky").getTextureRegionList().first()).also {
            it.setSize(1f, 1f)
            it.setPosition(width / 2f, height / 2f, Align.center)
            addActor(it)
        }
    }

    companion object {
        private const val WIDTH_DP = 960f
        private const val HEIGHT_DP = 600f
    }
}
