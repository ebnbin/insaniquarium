package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import dev.ebnbin.kgdx.Game
import ktx.assets.disposeSafely

internal class DevInfoStage : Stage(FitViewport(Game.WORLD_WIDTH, Game.WORLD_HEIGHT)) {
    private val bitmapFont: BitmapFont = BitmapFont()

    private val label: Label = Label(null, Label.LabelStyle(bitmapFont, null)).also {
        it.setFillParent(true)
        it.setAlignment(Align.topRight)
        addActor(it)
    }

    override fun act(delta: Float) {
        super.act(delta)
        label.setText("$delta")
    }

    override fun dispose() {
        bitmapFont.disposeSafely()
        super.dispose()
    }
}
