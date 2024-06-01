package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.assets.disposeSafely

internal class DevStage : Stage() {
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
