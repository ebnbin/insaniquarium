package dev.ebnbin.gdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.pref.GdxPrefManager
import dev.ebnbin.gdx.utils.UnitFitViewport
import dev.ebnbin.gdx.utils.colorMarkup
import dev.ebnbin.gdx.utils.toTimestampString

class DevGdxLogStage : BaseStage(viewport = UnitFitViewport()) {
    override val isEnabled: Boolean
        get() = GdxPrefManager.show_dev_gdx_log.data

    private val bitmapFont: BitmapFont = baseGame.assets.freeType.getValue("gdx_dev").get()

    private val label: Label = Label(null, Label.LabelStyle(bitmapFont, null)).also {
        it.setFillParent(true)
        it.setAlignment(Align.topRight)
        addActor(it)
    }

    override fun act(delta: Float) {
        super.act(delta)
        val logText = createLogMap().logText(delta)
        label.setText(logText)
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
                "timestamp" to {
                    val timestamp = System.currentTimeMillis()
                    val timeText = timestamp.toTimestampString("yyyy-MM-dd,HH:mm:ss")
                    val millisecondText = timestamp.toTimestampString(":SSS").colorMarkup(Color.GRAY)
                    "$timeText$millisecondText"
                },
                "realDelta,delta(ms)" to {
                    "%6.3f,%6.3f".format(
                        Gdx.graphics.deltaTime * 1000f,
                        it * 1000f,
                    )
                },
                "acts/draws/framesPerSecond" to {
                    val fps = Gdx.graphics.framesPerSecond
                    val color = when {
                        fps >= 60 -> Color.GREEN
                        fps >= 20 -> Color.ORANGE
                        else -> Color.RED
                    }
                    "${baseGame.actsPerSecond},${baseGame.drawsPerSecond},${"$fps".colorMarkup(color)}"
                },
                "act/clear/drawAverageTime(ms)" to {
                    "%6.3f,%6.3f,%6.3f".format(
                        baseGame.actAverageTime,
                        baseGame.clearAverageTime,
                        baseGame.drawAverageTime,
                    )
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
