package dev.ebnbin.gdx.animation

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Interpolation

data class TextureRegionAnimation(
    @Expose
    val assetId: String,
    @Expose
    val ticks: Int,
    @Expose
    val mode: AnimationMode = AnimationMode.NORMAL,
    @Expose
    val interpolation: Interpolation = Interpolation.LINEAR,
    @Expose
    val finishTicks: Int = ticks,
) {
    fun getTextureRegion(stateTick: Int): TextureRegion {
        val textureRegionList = baseGame.assets.texture.getValue(assetId).getTextureRegionList()
        return textureRegionList.animFrame(
            duration = ticks.toFloat(),
            mode = mode,
            interpolation = interpolation,
            stateTime = stateTick.toFloat(),
        )
    }

    fun canInterrupt(stateTick: Int): Boolean {
        if (finishTicks == 0) {
            return true
        }
        if (stateTick < finishTicks) {
            return false
        }
        return when (mode) {
            AnimationMode.NORMAL, AnimationMode.REVERSED -> {
                true
            }
            AnimationMode.LOOP, AnimationMode.LOOP_REVERSED, AnimationMode.LOOP_PINGPONG -> {
                stateTick % finishTicks == 0
            }
        }
    }
}
