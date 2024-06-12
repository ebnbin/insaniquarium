package dev.ebnbin.kgdx.ui

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.collections.toGdxArray

class TextureRegionImage(
    textureRegionList: List<TextureRegion>,
    duration: Float,
    playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
) : Image(textureRegionList.first()) {
    private val animation: Animation<Drawable> = Animation(
        duration / textureRegionList.size,
        textureRegionList.map { TextureRegionDrawable(it) }.toGdxArray(),
        playMode,
    )

    private var stateTime: Float = 0f

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
        setDrawable(animation.getKeyFrame(stateTime))
    }
}
