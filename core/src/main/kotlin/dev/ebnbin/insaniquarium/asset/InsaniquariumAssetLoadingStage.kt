package dev.ebnbin.insaniquarium.asset

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.asset.AssetLoadingStage
import dev.ebnbin.kgdx.asset.FreeTypeAsset
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.ui.StretchableImage

class InsaniquariumAssetLoadingStage : AssetLoadingStage() {
    private val backgroundAsset: TextureAsset = game.assets.texture("loading_background")
    private val progressAsset: FreeTypeAsset = game.assets.freeType("loading_progress")

    private var backgroundImage: StretchableImage? = null
    private var progressLabel: Label? = null

    override fun createBackgroundUI() {
        super.createBackgroundUI()
        backgroundImage = StretchableImage(
            textureAsset = backgroundAsset,
            getAssetBlocked = true,
        ).also {
            it.setFillParent(true)
            addActor(it)
        }
    }

    override fun createProgressUI() {
        super.createProgressUI()
        progressLabel = Label(null, Label.LabelStyle(progressAsset.get(blocked = true), null)).also {
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
        progressAsset.unload()
        super.disposeProgressUI()
    }

    override fun disposeBackgroundUI() {
        backgroundImage?.remove()
        backgroundImage = null
        backgroundAsset.unload()
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
