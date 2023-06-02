package dev.ebnbin.gdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import dev.ebnbin.gdx.lifecycle.BaseStage
import dev.ebnbin.gdx.utils.UnitFitViewport

class DevLogStage : BaseStage(viewport = UnitFitViewport()) {
    private val bitmapFont: BitmapFont = BitmapFont()

    private val label: Label = Label(null, Label.LabelStyle(bitmapFont, null)).also {
        it.setFillParent(true)
        it.setAlignment(Align.topRight)
        addActor(it)
    }

    override fun act(delta: Float) {
        super.act(delta)
        label.setText(Gdx.graphics.framesPerSecond)
    }

    override fun dispose() {
        bitmapFont.dispose()
        super.dispose()
    }
}
