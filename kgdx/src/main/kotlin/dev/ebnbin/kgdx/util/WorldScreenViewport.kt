package dev.ebnbin.kgdx.util

import com.badlogic.gdx.utils.viewport.ScreenViewport
import dev.ebnbin.kgdx.Game
import kotlin.math.max

class WorldScreenViewport : ScreenViewport() {
    override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
        unitsPerPixel = max(Game.WORLD_WIDTH / screenWidth, Game.WORLD_HEIGHT / screenHeight)
        super.update(screenWidth, screenHeight, centerCamera)
    }
}
