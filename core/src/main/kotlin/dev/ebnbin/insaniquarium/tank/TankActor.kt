package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import dev.ebnbin.kgdx.dev.DevEntry
import dev.ebnbin.kgdx.dev.toDevEntry
import dev.ebnbin.kgdx.util.diffStage
import kotlin.system.measureNanoTime

class TankActor : Actor() {
    val tank: Tank = Tank(
        actorWrapper = TankActorWrapper(this),
    )

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return tank.touchDown(event, x, y, pointer, button)
            }

            override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
                tank.touchDragged(event, x, y, pointer)
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                tank.touchUp(event, x, y, pointer, button)
            }
        })
    }

    private var tickNanoAccumulator: Long = 0L
    private var tickMillis: Long = 0L

    private var drawStartNano: Long = System.nanoTime()
    private var drawNanoAccumulator: Long = 0L
    private var drawCount: Int = 0
    private var drawMillis: Long = 0

    override fun act(delta: Float) {
        val tankStage = stage as TankStage? ?: return
        val tickDelta = tankStage.tickDelta
        if (tickDelta > 0) {
            tickNanoAccumulator += measureNanoTime {
                super.act(delta)
                tank.tick(tickDelta)
            }
            if (tankStage.ticks % 20 == 0) {
                tickMillis = tickNanoAccumulator / 1_000_000 / 20
                tickNanoAccumulator = 0L
            }
        } else {
            super.act(delta)
            tank.act(delta)
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        drawNanoAccumulator += measureNanoTime {
            super.draw(batch, parentAlpha)
            tank.draw(batch, parentAlpha)
        }
        ++drawCount
        if (System.nanoTime() - drawStartNano > 1_000_000_000) {
            drawMillis = drawNanoAccumulator / 1_000_000 / drawCount
            drawStartNano = System.nanoTime()
            drawNanoAccumulator = 0L
            drawCount = 0
        }
    }

    override fun setStage(stage: Stage?) {
        diffStage<TankStage>(
            stage = stage,
            updateStage = { super.setStage(it) },
            addedToStage = ::addedToStage,
            removedFromStage = ::removedFromStage,
        )
    }

    private val bodyCountDevEntry: DevEntry = "bodyCount" toDevEntry {
        "${tank.bodyCount()}"
    }
    private val tickTimeDevEntry: DevEntry = "tickTime" toDevEntry {
        "$tickMillis"
    }
    private val drawTimeDevEntry: DevEntry = "drawTime" toDevEntry {
        "$drawMillis"
    }

    private fun addedToStage(stage: TankStage) {
        stage.putDevInfo(bodyCountDevEntry)
        stage.putDevInfo(tickTimeDevEntry)
        stage.putDevInfo(drawTimeDevEntry)
        tank.addedToStage(stage)
    }

    private fun removedFromStage(stage: TankStage) {
        tank.removedFromStage(stage)
        stage.removeDevInfo(drawTimeDevEntry)
        stage.removeDevInfo(tickTimeDevEntry)
        stage.removeDevInfo(bodyCountDevEntry)
    }
}
