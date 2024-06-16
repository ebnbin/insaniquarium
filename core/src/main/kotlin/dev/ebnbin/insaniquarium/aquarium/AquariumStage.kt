package dev.ebnbin.insaniquarium.aquarium

import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.ui.StretchableImage

class AquariumStage(aquarium: Aquarium) : LifecycleStage() {
    init {
        StretchableImage(textureAsset = aquarium.textureAsset).also {
            it.setFillParent(true)
            addActor(it)
        }
    }
}
