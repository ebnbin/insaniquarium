package dev.ebnbin.kgdx.dev

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
            val devLabel = DevLabel(
                entry = DevLabel.Entry(
                    key = "delta",
                    getValue = {
                        "%.3f".format(it)
                    },
                ),
            )
            verticalGroup.addActor(devLabel)
            addActor(verticalGroup)
        }
    }
}
