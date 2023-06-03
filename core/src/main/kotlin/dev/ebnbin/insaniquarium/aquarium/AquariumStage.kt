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

    private val clyde: Body = Body(baseGame.assets.texture.getValue("clyde").getTextureRegionList().first()).also {
        it.debug()
        addActor(it)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        clyde.setPosition(width / 2f, height / 2f, Align.center)
    }
}
