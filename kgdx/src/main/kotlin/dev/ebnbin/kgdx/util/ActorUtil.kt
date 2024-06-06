package dev.ebnbin.kgdx.util

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage

inline fun <reified T : Stage> Actor.diffStage(
    stage: Stage?,
    updateStage: (stage: Stage?) -> Unit,
    addedToStage: (stage: T) -> Unit,
    removedFromStage: (stage: T) -> Unit,
) {
    val oldStage = this.stage
    if (oldStage !== stage && oldStage is T) {
        removedFromStage(oldStage)
    }
    updateStage(stage)
    if (oldStage !== stage && stage is T) {
        addedToStage(stage)
    }
}
