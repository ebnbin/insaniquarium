package dev.ebnbin.kgdx

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.kgdx.dev.DevInfoStage
import ktx.assets.disposeSafely

private var singleton: Game? = null

val game: Game
    get() = requireNotNull(singleton)

abstract class Game : ApplicationListener {
    private lateinit var devInfoStage: DevInfoStage

    private var screen: Screen? = null

    fun setScreen(createScreen: (() -> Screen)?) {
        screen?.stageList?.reversed()?.forEach { stage ->
            stage.disposeSafely()
        }
        screen = createScreen?.invoke()
        screen?.stageList?.forEach { stage ->
            stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        }
    }

    private fun stageList(): List<Stage> {
        val stageList = mutableListOf<Stage>()
        screen?.stageList?.let { stageList.addAll(it) }
        stageList.add(devInfoStage)
        return stageList
    }

    override fun create() {
        singleton = this
        devInfoStage = DevInfoStage()
    }

    override fun resize(width: Int, height: Int) {
        stageList().forEach { stage ->
            stage.viewport.update(width, height, true)
        }
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        stageList().forEach { stage ->
            stage.act(deltaTime)
        }
        ScreenUtils.clear(Color.CLEAR)
        stageList().forEach { stage ->
            stage.viewport.apply(true)
            stage.draw()
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        setScreen(null)
        devInfoStage.disposeSafely()
        singleton = null
    }

    companion object {
        var WORLD_WIDTH = 0f
            private set
        var WORLD_HEIGHT = 0f
            private set

        fun init(
            worldWidth: Float,
            worldHeight: Float,
        ) {
            WORLD_WIDTH = worldWidth
            WORLD_HEIGHT = worldHeight
        }
    }
}
