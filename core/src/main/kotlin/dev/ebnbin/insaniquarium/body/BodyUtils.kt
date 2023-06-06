package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.TextureRegion
import dev.ebnbin.gdx.utils.animFrame
import dev.ebnbin.insaniquarium.game

fun BodyConfig.Anim.getFrame(stateTime: Float): TextureRegion {
    val textureRegionList = game.assets.texture.getValue(assetId).getTextureRegionList()
    return textureRegionList.animFrame(
        duration = duration,
        mode = mode,
        interpolation = interpolation,
        stateTime = stateTime,
    )
}
