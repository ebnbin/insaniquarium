package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.lifecycle.baseGame

class AquariumStage : BaseStage() {
    init {
        val aquariumTextureAssetKeyList = listOf(
            "aquarium_a",
            "aquarium_b",
            "aquarium_c",
            "aquarium_d",
            "aquarium_e",
            "aquarium_f",
        )
        val aquariumImage = Image(baseGame.assets.texture.getValue(aquariumTextureAssetKeyList.random()).get())
        aquariumImage.setFillParent(true)
        addActor(aquariumImage)
    }

    private val clydeImage: Image = Image(baseGame.assets.texture.getValue("clyde").get()).also {
        addActor(it)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        clydeImage.setPosition(width / 2f, height / 2f, Align.center)
    }
}
