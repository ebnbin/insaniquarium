package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.game

class TankStage : LifecycleStage(TankViewport()) {
    init {
        val textureAsset = game.assets.texture("stinky")
        Image(textureAsset.getTextureRegionList().first()).also {
            it.setSize(1f, 1f)
            addActor(it)
        }
    }
}
