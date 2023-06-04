package dev.ebnbin.gdx.dev

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.pref.GdxPrefManager
import dev.ebnbin.gdx.utils.UnitFitViewport
import dev.ebnbin.gdx.utils.colorMarkup

class DevGameLogStage : BaseStage(viewport = UnitFitViewport()) {
    override val isEnabled: Boolean
        get() = GdxPrefManager.show_dev_game_log.data

    private val bitmapFont: BitmapFont = baseGame.assets.freeType.getValue("gdx_dev").get()

    private val label: Label = Label(null, Label.LabelStyle(bitmapFont, null)).also {
        it.setFillParent(true)
        it.setAlignment(Align.bottomLeft)
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
        val logText = logMap.logText(delta)
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
    }
}
