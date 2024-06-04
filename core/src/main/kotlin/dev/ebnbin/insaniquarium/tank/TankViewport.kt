package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.utils.viewport.ScreenViewport
import dev.ebnbin.kgdx.Game
import kotlin.math.max

class TankViewport : ScreenViewport() {
    override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
        unitsPerPixel = max(
            Game.WORLD_WIDTH.dpToMeter / screenWidth,
            Game.WORLD_HEIGHT.dpToMeter / screenHeight,
        )
        super.update(screenWidth, screenHeight, centerCamera)
    }
}

private const val DPS_PER_METER = 120f

val Float.dpToMeter: Float
    get() = this / DPS_PER_METER
