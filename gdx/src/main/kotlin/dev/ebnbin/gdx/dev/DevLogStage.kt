package dev.ebnbin.gdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.UnitFitViewport
import dev.ebnbin.gdx.utils.colorMarkup

class DevLogStage : BaseStage(viewport = UnitFitViewport()) {
    private val bitmapFont: BitmapFont = BitmapFont().also {
        it.data.markupEnabled = true
    }

    private val leftLabel: Label = Label(null, Label.LabelStyle(bitmapFont, null)).also {
        it.setFillParent(true)
        it.setAlignment(Align.bottomLeft)
        addActor(it)
    }

    private val rightLabel: Label = Label(null, Label.LabelStyle(bitmapFont, null)).also {
        it.setFillParent(true)
        it.setAlignment(Align.topRight)
        addActor(it)
    }

    private val logMap: MutableMap<String, (delta: Float) -> String> = mutableMapOf()

    internal fun put(key: String, value: (delta: Float) -> String) {
        logMap[key] = value
    }

    internal fun remove(key: String) {
        logMap.remove(key)
    }

    override fun act(delta: Float) {
        super.act(delta)
        val leftLogText = logMap.logText(delta)
        leftLabel.setText(leftLogText)
        val rightLogText = createLogMap().logText(delta)
        rightLabel.setText(rightLogText)
    }

    override fun dispose() {
        bitmapFont.dispose()
        super.dispose()
    }

    companion object {
        private fun Map<String, (Float) -> String>.logText(delta: Float): String {
            return entries.joinToString("\n") {
                val key = "${it.key}=".colorMarkup(Color.LIGHT_GRAY)
                val value = it.value(delta)
                "$key$value"
            }
        }

        private fun createLogMap(): Map<String, (delta: Float) -> String> {
            return mapOf(
                "fps" to {
                    val fps = Gdx.graphics.framesPerSecond
                    val color = when {
                        fps >= 60 -> Color.GREEN
                        fps >= 30 -> Color.ORANGE
                        else -> Color.RED
                    }
                    "$fps".colorMarkup(color)
                },
                "assets" to {
                    val loaded = baseGame.assetHelper.loadedAssets
                    if (baseGame.assetHelper.isFinished) {
                        "$loaded"
                    } else {
                        val all = loaded + baseGame.assetHelper.queuedAssets
                        val progress = (baseGame.assetHelper.progress * 100f).toInt()
                        "%d/%d,%d%%".format(loaded, all, progress).colorMarkup(Color.YELLOW)
                    }
                }
            )
        }
    }
}
