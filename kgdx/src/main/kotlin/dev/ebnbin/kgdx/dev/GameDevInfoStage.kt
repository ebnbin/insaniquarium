package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.scene.LifecycleStage
import dev.ebnbin.kgdx.util.WorldFitViewport

internal class GameDevInfoStage : LifecycleStage(WorldFitViewport()) {
    private val verticalGroup: VerticalGroup = VerticalGroup().also { verticalGroup ->
        verticalGroup.setFillParent(true)
        verticalGroup.align(Align.bottomLeft)
        verticalGroup.columnAlign(Align.left)
        addActor(verticalGroup)
    }

    private val infoList: MutableList<(delta: Float) -> String> = mutableListOf()

    fun putInfo(getText: (delta: Float) -> String) {
        infoList.add(getText)
    }

    override fun act(delta: Float) {
        verticalGroup.clearChildren()
        infoList.forEach { getText ->
            val devLabel = DevLabel {
                getText(it)
            }
            verticalGroup.addActor(devLabel)
        }
        infoList.clear()
        super.act(delta)
    }

    override val isRendering: Boolean
        get() = KgdxPreferenceManager.showGameDevInfo.value
}
