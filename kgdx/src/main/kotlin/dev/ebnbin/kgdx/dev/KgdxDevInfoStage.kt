package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.scene.LifecycleStage
import dev.ebnbin.kgdx.util.WorldFitViewport
import dev.ebnbin.kgdx.util.colorMarkup
import dev.ebnbin.kgdx.util.toTime
import kotlin.math.min

internal class KgdxDevInfoStage : LifecycleStage(WorldFitViewport()) {
    init {
        VerticalGroup().also { verticalGroup ->
            verticalGroup.setFillParent(true)
            verticalGroup.align(Align.topRight)
            verticalGroup.columnAlign(Align.right)
            INFO_LIST.forEach { entry ->
                val devLabel = DevLabel {
                    entry.getText(it)
                }
                verticalGroup.addActor(devLabel)
            }
            addActor(verticalGroup)
        }
    }

    override val isRendering: Boolean
        get() = KgdxPreferenceManager.showKgdxDevInfo.value

    companion object {
        private val INFO_LIST: List<DevEntry> = listOf(
            "memory(M)" toDevEntry {
                val freeMemory = Runtime.getRuntime().freeMemory()
                val totalMemory = Runtime.getRuntime().totalMemory()
                val usedMemory = totalMemory - freeMemory
                "%.1f/%.1f".format(
                    usedMemory / 1024f / 1024f,
                    totalMemory / 1024f / 1024f,
                )
            },
            "fps" toDevEntry {
                val fps = Gdx.graphics.framesPerSecond
                val color = when {
                    fps >= 60 -> Color.GREEN
                    fps >= Game.LIMITED_FRAMES_PER_SECOND -> Color.YELLOW
                    else -> Color.RED
                }
                "$fps".colorMarkup(color)
            },
            "screen" toDevEntry {
                "${Gdx.graphics.width}x${Gdx.graphics.height}"
            },
            "scale" toDevEntry {
                val scaleWidth = Gdx.graphics.width / Game.WORLD_WIDTH
                val scaleHeight = Gdx.graphics.height / Game.WORLD_HEIGHT
                val scale = min(scaleWidth, scaleHeight)
                val dpiScale = KgdxPreferenceManager.dpi.value.pxsPerDp
                val scaleColor = if (scale >= dpiScale) {
                    Color.GREEN
                } else {
                    Color.RED
                }
                val scaleText = "%.2f".format(scale).colorMarkup(scaleColor)
                val scaleSign = when {
                    scaleWidth == scaleHeight -> ""
                    scaleWidth > scaleHeight -> "↔"
                    else -> "↕"
                }
                "$scaleText$scaleSign"
            },
            "time" toDevEntry {
                val time = game.time.toTime()
                val timeString = if (time.hour > 0) {
                    "%d:%02d:%02d".format(time.hour, time.minute, time.second)
                } else if (time.minute > 0) {
                    "%d:%02d".format(time.minute, time.second)
                } else {
                    "%d".format(time.second)
                }
                val millisecondString = ".%d".format(time.millisecond / 100).colorMarkup(Color.GRAY)
                "$timeString$millisecondString"
            },
            "asset" toDevEntry {
                val loadedAssets = game.assetManager.loadedAssets
                val queuedAssets = game.assetManager.queuedAssets
                val totalAssets = loadedAssets + queuedAssets
                val progress = game.assetManager.progress
                if (progress < 1f) {
                    "${loadedAssets}/${totalAssets}"
                } else {
                    "$totalAssets"
                }
            },
            "touch" toDevEntry {
                val color = if (Gdx.input.isTouched) Color.YELLOW else null
                "${Gdx.input.x},${Gdx.input.y}".colorMarkup(color)
            },
        )
    }
}
