package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.g2d.TextureRegion
import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.animFrame
import dev.ebnbin.insaniquarium.game

fun BodyType.assets(): Set<Asset<*>> {
    val config = game.config.body.getValue(serializedName)
    return setOfNotNull(
        config.anim.assetId,
        config.turnAct?.anim?.assetId,
    ).mapTo(mutableSetOf()) { baseGame.assets.texture.getValue(it) }
}

fun BodyConfig.Anim.getFrame(stateTime: Float): TextureRegion {
    val textureRegionList = game.assets.texture.getValue(assetId).getTextureRegionList()
    return textureRegionList.animFrame(
        duration = duration,
        mode = mode,
        interpolation = interpolation,
        stateTime = stateTime,
    )
}
