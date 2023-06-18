package dev.ebnbin.gdx.animation

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Interpolation

data class TextureRegionAnimation(
    @Expose
    val assetId: String,
    @Expose
    val duration: Float = 1f,
    @Expose
    val mode: AnimationMode = AnimationMode.NORMAL,
    @Expose
    val interpolation: Interpolation = Interpolation.LINEAR,
) {
    fun getTextureRegion(stateTime: Float): TextureRegion {
        val textureRegionList = baseGame.assets.texture.getValue(assetId).getTextureRegionList()
        return textureRegionList.animFrame(
            duration = duration,
            mode = mode,
            interpolation = interpolation,
            stateTime = stateTime,
        )
    }
}
