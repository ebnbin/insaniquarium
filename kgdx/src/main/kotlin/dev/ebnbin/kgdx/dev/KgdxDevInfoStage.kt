package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.WorldFitViewport
import dev.ebnbin.kgdx.util.colorMarkup
import dev.ebnbin.kgdx.util.toTime

internal class KgdxDevInfoStage : LifecycleStage(WorldFitViewport()) {
    init {
        VerticalGroup().also { verticalGroup ->
            verticalGroup.setFillParent(true)
            verticalGroup.align(Align.topRight)
            verticalGroup.columnAlign(Align.right)
            INFO_LIST.forEach { entry ->
                val devLabel = DevLabel(entry)
                verticalGroup.addActor(devLabel)
            }
            addActor(verticalGroup)
        }
    }

    override val isRendering: Boolean
        get() = KgdxPreferenceManager.showKgdxDevInfo.value

    companion object {
        private val INFO_LIST: List<DevLabel.Entry> = listOf(
            "fps" toDevEntry {
                "${Gdx.graphics.framesPerSecond}"
            },
            "screen" toDevEntry {
                "${Gdx.graphics.width}x${Gdx.graphics.height}"
            },
            "time" toDevEntry {
                val time = game.time.toTime()
                val timeString = if (time.hour > 0) {
                    "%d:%02d:%02d".format(time.hour, time.minute, time.second)
                } else {
                    "%02d:%02d".format(time.minute, time.second)
                }
                val millisecondString = ".%03d".format(time.millisecond).colorMarkup(Color.GRAY)
                "$timeString$millisecondString"
            },
            "asset" toDevEntry {
                val loadedAssets = game.assetManager.loadedAssets
                "${loadedAssets}/${loadedAssets + game.assetManager.queuedAssets}"
            },
        )
    }
}
