package dev.ebnbin.kgdx

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import ktx.assets.disposeSafely

private var singleton: Game? = null

val game: Game
    get() = requireNotNull(singleton)

abstract class Game : ApplicationListener {
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

    override fun create() {
        singleton = this
    }

    override fun resize(width: Int, height: Int) {
        screen?.stageList?.forEach { stage ->
            stage.viewport.update(width, height, true)
        }
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        screen?.stageList?.forEach { stage ->
            stage.act(deltaTime)
        }
        ScreenUtils.clear(Color.CLEAR)
        screen?.stageList?.forEach { stage ->
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
        singleton = null
    }
}
