package dev.ebnbin.gdx.utils

import com.badlogic.gdx.utils.viewport.ScreenViewport
import kotlin.math.max

open class UnitScreenViewport : ScreenViewport() {
    override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
        unitsPerPixel = max(
            World.unitWidth / screenWidth,
            World.unitHeight / screenHeight,
        )
        super.update(screenWidth, screenHeight, centerCamera)
    }
}
