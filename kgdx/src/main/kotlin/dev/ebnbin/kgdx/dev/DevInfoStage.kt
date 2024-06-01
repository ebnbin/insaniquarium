package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.LifecycleStage

internal class DevInfoStage : LifecycleStage(FitViewport(Game.WORLD_WIDTH, Game.WORLD_HEIGHT)) {
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
        )
    }
}
