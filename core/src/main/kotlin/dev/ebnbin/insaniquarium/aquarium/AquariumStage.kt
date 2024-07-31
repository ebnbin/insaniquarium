package dev.ebnbin.insaniquarium.aquarium

import dev.ebnbin.kgdx.scene.LifecycleStage
import dev.ebnbin.kgdx.ui.StretchableImage

class AquariumStage(aquariumType: AquariumType) : LifecycleStage() {
    init {
        StretchableImage(textureAsset = aquariumType.textureAsset).also {
            it.setFillParent(true)
            addActor(it)
        }
    }
}
