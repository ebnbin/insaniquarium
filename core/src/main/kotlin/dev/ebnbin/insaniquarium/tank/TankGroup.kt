package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import dev.ebnbin.kgdx.util.diffStage

class TankGroup : Group() {
    val tank: Tank = Tank(
        groupWrapper = TankGroupWrapper(this),
    )

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        tank.draw(batch, parentAlpha)
    }

    override fun setStage(stage: Stage?) {
        diffStage<TankStage>(
            stage = stage,
            updateStage = { super.setStage(it) },
            addedToStage = ::addedToStage,
            removedFromStage = ::removedFromStage,
        )
    }

    private fun addedToStage(stage: TankStage) {
        tank.addedToStage(stage)
    }

    private fun removedFromStage(stage: TankStage) {
        tank.removedFromStage(stage)
    }
}
