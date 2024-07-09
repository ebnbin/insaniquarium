package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.utils.viewport.Viewport
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.Screen
import dev.ebnbin.kgdx.game

open class AssetLoadingStage(viewport: Viewport = defaultViewport()) : LifecycleStage(viewport) {
    private var isLoading: Boolean = false

    override val isRendering: Boolean
        get() = isLoading

    private lateinit var screenCreator: Screen.Creator

    fun load(screenCreator: Screen.Creator) {
        if (isLoading) return
        isLoading = true
        this.screenCreator = screenCreator
        createBackgroundUI()
        val oldScreen = game.screen
        game.screen = null
        game.assetManager.loadWithDiff(
            oldAssetSet = oldScreen?.assetSet ?: emptySet(),
            newAssetSet = screenCreator.assetSet,
        )
        createProgressUI()
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (game.assetManager.update()) {
            disposeProgressUI()
            val screen = Screen(
                name = screenCreator.name,
                assetSet = screenCreator.assetSet,
                stageList = screenCreator.createStageList(),
            )
            game.screen = screen
            disposeBackgroundUI()
            isLoading = false
        } else {
            updateProgressUI(game.assetManager.progress)
        }
    }

    protected open fun createBackgroundUI() {
    }

    protected open fun createProgressUI() {
    }

    protected open fun updateProgressUI(progress: Float) {
    }

    protected open fun disposeProgressUI() {
    }

    protected open fun disposeBackgroundUI() {
    }
}
