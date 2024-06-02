package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.game

class AquariumStage : LifecycleStage() {
    init {
        val textureAsset = game.assets.texture("aquarium_a")
        Image(textureAsset.get()).also {
            it.setFillParent(true)
            addActor(it)
        }
    }
}
