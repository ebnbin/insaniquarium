package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.util.WorldFitViewport

internal class DevInfoStage : LifecycleStage(WorldFitViewport()) {
    init {
        VerticalGroup().also { verticalGroup ->
            verticalGroup.setFillParent(true)
            verticalGroup.align(Align.topRight)
            verticalGroup.columnAlign(Align.right)
            ENTRY_LIST.forEach { entry ->
                val devLabel = DevLabel(entry)
                verticalGroup.addActor(devLabel)
            }
            addActor(verticalGroup)
        }
    }

    companion object {
        private val ENTRY_LIST: List<DevLabel.Entry> = listOf(
            DevLabel.Entry("fps") {
                "${Gdx.graphics.framesPerSecond}"
            },
            DevLabel.Entry("asset") {
                val loadedAssets = game.assetManager.loadedAssets
                "${loadedAssets}/${loadedAssets + game.assetManager.queuedAssets}"
            },
        )
    }
}
