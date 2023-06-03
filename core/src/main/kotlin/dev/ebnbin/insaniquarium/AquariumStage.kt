package dev.ebnbin.insaniquarium

import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.lifecycle.baseGame

class AquariumStage : BaseStage() {
    init {
        val textureAssetKeyList = listOf(
            "aquarium_a",
            "aquarium_b",
            "aquarium_c",
            "aquarium_d",
            "aquarium_e",
            "aquarium_f",
        )
        val textureAsset = baseGame.assets.texture.getValue(textureAssetKeyList.random())
        val image = Image(textureAsset.get())
        image.setFillParent(true)
        addActor(image)
    }
}
