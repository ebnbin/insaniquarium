package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.kgdx.LifecycleStage

class AquariumStage(aquarium: Aquarium) : LifecycleStage() {
    init {
        Image(aquarium.textureAsset.get()).also {
            it.setFillParent(true)
            addActor(it)
        }
    }
}
