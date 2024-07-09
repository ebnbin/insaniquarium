package dev.ebnbin.insaniquarium.asset

import dev.ebnbin.kgdx.asset.AssetLoadingStage
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.ui.StretchableImage

class InsaniquariumAssetLoadingStage : AssetLoadingStage() {
    private lateinit var loadingBackgroundImage: StretchableImage

    override fun createBackgroundUI() {
        super.createBackgroundUI()
        loadingBackgroundImage = StretchableImage(
            textureAsset = game.assets.texture("loading_background"),
        ).also {
            it.setFillParent(true)
            addActor(it)
        }
    }

    override fun disposeBackgroundUI() {
        loadingBackgroundImage.remove()
        super.disposeBackgroundUI()
    }
}
