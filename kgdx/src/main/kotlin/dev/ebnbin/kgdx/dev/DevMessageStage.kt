package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import dev.ebnbin.kgdx.scene.LifecycleStage
import dev.ebnbin.kgdx.util.WorldFitViewport

internal class DevMessageStage : LifecycleStage(WorldFitViewport()) {
    private val verticalGroup: VerticalGroup = VerticalGroup().also {
        it.setFillParent(true)
        it.align(Align.bottom)
        it.columnAlign(Align.center)
        addActor(it)
    }

    fun addMessage(message: String) {
        if (verticalGroup.children.size >= MAX_SIZE) {
            verticalGroup.removeActorAt(0, true)
        }
        val devLabel = DevLabel(
            entry = DevEntry("") {
                message
            },
        )
        devLabel.userObject = System.currentTimeMillis()
        verticalGroup.addActor(devLabel)
    }

    override fun act(delta: Float) {
        super.act(delta)
        val iterator = verticalGroup.children.iterator()
        while (iterator.hasNext()) {
            val devLabel = iterator.next() as DevLabel
            if (System.currentTimeMillis() - devLabel.userObject as Long > DURATION) {
                verticalGroup.removeActor(devLabel)
            } else {
                break
            }
        }
    }

    companion object {
        private const val MAX_SIZE = 30
        private const val DURATION = 10_000L
    }
}
