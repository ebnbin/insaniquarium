package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.viewport.ScreenViewport
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.ui.ColorImage
import ktx.graphics.copy

internal class DevSafeInsetStage : LifecycleStage(ScreenViewport()) {
    private val leftImage: ColorImage = ColorImage(COLOR).also {
        addActor(it)
    }
    private val topImage: ColorImage = ColorImage(COLOR).also {
        addActor(it)
    }
    private val rightImage: ColorImage = ColorImage(COLOR).also {
        addActor(it)
    }
    private val bottomImage: ColorImage = ColorImage(COLOR).also {
        addActor(it)
    }

    override val isRendering: Boolean
        get() = KgdxPreferenceManager.showSafeInset.value

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        val safeInsetLeft = Gdx.graphics.safeInsetLeft.toFloat()
        val safeInsetTop = Gdx.graphics.safeInsetTop.toFloat()
        val safeInsetRight = Gdx.graphics.safeInsetRight.toFloat()
        val safeInsetBottom = Gdx.graphics.safeInsetBottom.toFloat()
        leftImage.setSize(safeInsetLeft, height)
        leftImage.setPosition(0f, 0f)
        topImage.setSize(width, safeInsetTop)
        topImage.setPosition(0f, height - safeInsetTop)
        rightImage.setSize(safeInsetRight, height)
        rightImage.setPosition(width - safeInsetRight, 0f)
        bottomImage.setSize(width, safeInsetBottom)
        bottomImage.setPosition(0f, 0f)
    }

    companion object {
        private val COLOR: Color = Color.RED.copy(alpha = 0.25f)
    }
}
