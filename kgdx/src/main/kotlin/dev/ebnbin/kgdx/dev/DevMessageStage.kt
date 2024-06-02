package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.util.nanoTimestamp
import java.text.SimpleDateFormat
import java.util.Locale

internal class DevMessageStage : LifecycleStage(FitViewport(Game.WORLD_WIDTH, Game.WORLD_HEIGHT)) {
    private val verticalGroup: VerticalGroup = VerticalGroup().also {
        it.setFillParent(true)
        it.align(Align.bottomLeft)
        it.columnAlign(Align.left)
        addActor(it)
    }

    fun addMessage(message: String) {
        if (verticalGroup.children.size >= MAX_SIZE) {
            verticalGroup.removeActorAt(0, true)
        }
        val entry = DevLabel.Entry(
            key = nanoTimestamp(),
            keyToString = { key ->
                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(key as Long / 1_000_000)
            },
        ) {
            message
        }
        val devLabel = DevLabel(entry)
        verticalGroup.addActor(devLabel)
    }

    override fun act(delta: Float) {
        super.act(delta)
        val iterator = verticalGroup.children.iterator()
        while (iterator.hasNext()) {
            val devLabel = iterator.next() as DevLabel
            if (nanoTimestamp() - devLabel.entry.key as Long > DURATION) {
                iterator.remove()
            } else {
                break
            }
        }
    }

    companion object {
        private const val MAX_SIZE = 30
        private const val DURATION = 10_000_000_000L
    }
}
