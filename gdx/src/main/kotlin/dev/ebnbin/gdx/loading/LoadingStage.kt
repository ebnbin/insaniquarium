package dev.ebnbin.gdx.loading

import com.badlogic.gdx.utils.viewport.Viewport
import com.kotcrab.vis.ui.widget.Menu
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
    private var createDevMenu: ((List<BaseStage>) -> Menu)? = null

    fun load(
        assetSet: Set<Asset<*>>,
        createStageList: () -> List<BaseStage>,
        createDevMenu: ((List<BaseStage>) -> Menu)? = null,
    ) {
        if (isLoading) {
            return
        }
        isLoading = true
        this.assetSet = assetSet
        this.createStageList = createStageList
        this.createDevMenu = createDevMenu
        val oldScreen = baseGame.screen
        baseGame.screen = null
        baseGame.assetHelper.loadAllDiff(
            oldAssetSet = oldScreen?.assetSet ?: emptySet(),
            newAssetSet = assetSet,
        )
        isUpdating = true
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (!isUpdating) {
            return
        }
        if (baseGame.assetHelper.update()) {
            val stageList = createStageList()
            val screen = Screen(
                assetSet = assetSet,
                stageList = stageList,
                devMenu = createDevMenu?.invoke(stageList),
            )
            baseGame.screen = screen
            isUpdating = false
            isLoading = false
        }
    }
}
