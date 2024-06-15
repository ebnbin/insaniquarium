package dev.ebnbin.kgdx.ui

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import dev.ebnbin.kgdx.util.AnimationMode
import dev.ebnbin.kgdx.util.animationFrame

class TextureRegionImage(
    textureRegionList: List<TextureRegion>,
    private val duration: Float,
    private val animationMode: AnimationMode = AnimationMode.LOOP,
) : Image(textureRegionList.first()) {
    private val drawableList: List<Drawable> = textureRegionList.map { TextureRegionDrawable(it) }

    private var stateTime: Float = 0f

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
        val drawable = drawableList.animationFrame(
            duration = duration,
            mode = animationMode,
            stateTime = stateTime,
        )
        setDrawable(drawable)
    }
}
