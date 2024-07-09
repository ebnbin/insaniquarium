package dev.ebnbin.insaniquarium.asset

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.asset.AssetLoadingStage
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.ui.StretchableImage

class InsaniquariumAssetLoadingStage : AssetLoadingStage() {
    private var backgroundImage: StretchableImage? = null
    private var progressLabel: Label? = null

    override fun createBackgroundUI() {
        super.createBackgroundUI()
        backgroundImage = StretchableImage(
            textureAsset = game.assets.texture("loading_background"),
        ).also {
            it.setFillParent(true)
            addActor(it)
        }
    }

    override fun createProgressUI() {
        super.createProgressUI()
        progressLabel = Label(null, Label.LabelStyle(game.assets.freeType("loading_progress").get(), null)).also {
            it.setAlignment(Align.bottomRight)
            it.setPosition(
                (width - Game.WORLD_WIDTH) / 2f + Game.WORLD_WIDTH - 160f,
                (height - Game.WORLD_HEIGHT) / 3f + 40f,
                Align.bottomRight,
            )
            addActor(it)
        }
    }

    override fun updateProgressUI(progress: Float) {
        super.updateProgressUI(progress)
        progressLabel?.setText("${(progress * 100).toInt()}%")
    }

    override fun disposeProgressUI() {
        progressLabel?.remove()
        progressLabel = null
        super.disposeProgressUI()
    }

    override fun disposeBackgroundUI() {
        backgroundImage?.remove()
        backgroundImage = null
        super.disposeBackgroundUI()
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        progressLabel?.setPosition(
            (width - Game.WORLD_WIDTH) / 2f + Game.WORLD_WIDTH - 160f,
            (height - Game.WORLD_HEIGHT) / 3f + 40f,
            Align.bottomRight,
        )
    }
}
