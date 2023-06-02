package dev.ebnbin.insaniquarium

import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.gdx.asset.TextureAsset
import dev.ebnbin.gdx.lifecycle.BaseStage

class AquariumStage : BaseStage() {
    private val textureAsset: TextureAsset = TextureAsset("aquarium_a.png")

    init {
        val image = Image(textureAsset.get())
        image.setFillParent(true)
        addActor(image)
    }

    override fun dispose() {
        textureAsset.unload()
        super.dispose()
    }
}
