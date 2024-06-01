package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.game

internal class DevInfoStage : LifecycleStage(FitViewport(Game.WORLD_WIDTH, Game.WORLD_HEIGHT)) {
    private val label: Label = run {
        val freeTypeAsset = game.assets.freeType("kgdx_noto_sans_mono")
        Label(null, Label.LabelStyle(freeTypeAsset.get(), null)).also {
            it.setFillParent(true)
            it.setAlignment(Align.topRight)
            addActor(it)
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        label.setText("$delta")
    }
}
