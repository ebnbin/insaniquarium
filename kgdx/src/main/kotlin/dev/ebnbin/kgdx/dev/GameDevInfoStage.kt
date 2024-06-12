package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.WorldFitViewport

internal class GameDevInfoStage : LifecycleStage(WorldFitViewport()) {
    private val verticalGroup: VerticalGroup = VerticalGroup().also { verticalGroup ->
        verticalGroup.setFillParent(true)
        verticalGroup.align(Align.bottomLeft)
        verticalGroup.columnAlign(Align.left)
        addActor(verticalGroup)
    }

    fun putInfo(stage: LifecycleStage, entry: DevEntry) {
        val devLabel = DevLabel(entry)
        devLabel.userObject = stage
        verticalGroup.addActor(devLabel)
    }

    fun removeInfo(stage: LifecycleStage, entry: DevEntry) {
        val iterator = verticalGroup.children.iterator()
        while (iterator.hasNext()) {
            val devLabel = iterator.next() as DevLabel
            if (devLabel.userObject === stage && devLabel.entry === entry) {
                verticalGroup.removeActor(devLabel)
            }
        }
    }

    fun removeInfo(stage: LifecycleStage) {
        val iterator = verticalGroup.children.iterator()
        while (iterator.hasNext()) {
            val devLabel = iterator.next() as DevLabel
            if (devLabel.userObject === stage) {
                verticalGroup.removeActor(devLabel)
            }
        }
    }

    override val isRendering: Boolean
        get() = KgdxPreferenceManager.showGameDevInfo.value
}
