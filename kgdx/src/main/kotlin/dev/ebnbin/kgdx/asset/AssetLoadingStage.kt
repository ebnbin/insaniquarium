package dev.ebnbin.kgdx.asset

import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.Screen
import dev.ebnbin.kgdx.game

class AssetLoadingStage : LifecycleStage() {
    private var isLoading: Boolean = false

    override val isRendering: Boolean
        get() = isLoading

    private lateinit var screenCreator: Screen.Creator

    fun load(screenCreator: Screen.Creator) {
        if (isLoading) return
        isLoading = true
        this.screenCreator = screenCreator
        val oldScreen = game.screen
        game.screen = null
        oldScreen?.assetSet?.forEach { asset ->
            game.assetManager.unload(asset)
        }
        screenCreator.assetSet.forEach { asset ->
            game.assetManager.load(asset)
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (game.assetManager.update()) {
            val screen = Screen(
                name = screenCreator.name,
                assetSet = screenCreator.assetSet,
                stageList = screenCreator.createStageList(),
            )
            game.screen = screen
            isLoading = false
        }
    }
}
