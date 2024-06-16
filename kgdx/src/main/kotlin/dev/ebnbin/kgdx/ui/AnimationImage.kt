package dev.ebnbin.kgdx.ui

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.util.animationFrame

class AnimationImage(textureAsset: TextureAsset) : Image(textureAsset.getTextureRegionList().first()) {
    private val animation: TextureAsset.Region.Animation = requireNotNull(textureAsset.region?.animation)

    private val drawableList: List<Drawable> = textureAsset.getTextureRegionList().map { textureRegion ->
        TextureRegionDrawable(textureRegion)
    }

    private var stateTime: Float = 0f

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
        val drawable = drawableList.animationFrame(
            duration = animation.duration,
            mode = animation.mode,
            stateTime = stateTime,
        )
        setDrawable(drawable)
    }
}
