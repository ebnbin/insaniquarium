package dev.ebnbin.insaniquarium.aquarium

import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.ui.StretchableImage

class AquariumStage(aquarium: Aquarium) : LifecycleStage() {
    init {
        StretchableImage(
            texture = aquarium.textureAsset.get(),
            stretchable = requireNotNull(aquarium.textureAsset.stretchable),
        ).also {
            it.setFillParent(true)
            addActor(it)
        }
    }
}
