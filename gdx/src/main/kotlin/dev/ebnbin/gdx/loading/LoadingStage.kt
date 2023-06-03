package dev.ebnbin.gdx.loading

import com.badlogic.gdx.utils.viewport.Viewport
import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.lifecycle.Screen
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.UnitScreenViewport

open class LoadingStage(viewport: Viewport = UnitScreenViewport()) : BaseStage(viewport) {
    override val isEnabled: Boolean
        get() = isLoading

    private var isLoading: Boolean = false
    private var isUpdating: Boolean = false

    private lateinit var assetSet: Set<Asset<*>>
    private lateinit var createStageList: () -> List<BaseStage>

    fun load(
        assetSet: Set<Asset<*>>,
        createStageList: () -> List<BaseStage>,
    ) {
        if (isLoading) {
            return
        }
        isLoading = true
        this.assetSet = assetSet
        this.createStageList = createStageList
        val oldScreen = baseGame.screen
        baseGame.screen = null
        oldScreen?.assetSet?.forEach {
            baseGame.assetHelper.unload(it) // TODO: diff
        }
        assetSet.forEach {
            baseGame.assetHelper.load(it)
        }
        isUpdating = true
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (!isUpdating) {
            return
        }
        if (baseGame.assetHelper.update()) {
            val screen = Screen(
                assetSet = assetSet,
                stageList = createStageList(),
            )
            baseGame.screen = screen
            isUpdating = false
            isLoading = false
        }
    }
}
