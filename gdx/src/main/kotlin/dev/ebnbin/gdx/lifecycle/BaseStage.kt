package dev.ebnbin.gdx.lifecycle

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import dev.ebnbin.gdx.utils.UnitScreenViewport

abstract class BaseStage : Stage {
    constructor(viewport: Viewport = UnitScreenViewport()) : super(viewport)

    constructor(viewport: Viewport = UnitScreenViewport(), batch: Batch) : super(viewport, batch)
}
