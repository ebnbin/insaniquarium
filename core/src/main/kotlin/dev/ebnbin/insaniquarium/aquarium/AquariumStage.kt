package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.kgdx.LifecycleStage

class AquariumStage : LifecycleStage() {
    init {
        val textureAsset = Aquarium.entries.random().textureAsset
        Image(textureAsset.get()).also {
            it.setFillParent(true)
            addActor(it)
        }
    }
}
