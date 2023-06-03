package dev.ebnbin.insaniquarium

import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.lifecycle.baseGame

class AquariumStage : BaseStage() {
    init {
        val textureAsset = baseGame.assets.texture.getValue("aquarium_a")
        val image = Image(textureAsset.get())
        image.setFillParent(true)
        addActor(image)
    }
}
