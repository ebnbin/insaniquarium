package dev.ebnbin.gdx.lifecycle

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.gdx.utils.act
import dev.ebnbin.gdx.utils.dispose
import dev.ebnbin.gdx.utils.draw
import dev.ebnbin.gdx.utils.pause
import dev.ebnbin.gdx.utils.resize
import dev.ebnbin.gdx.utils.resume

internal lateinit var gameGetter: () -> BaseGame

val baseGame: BaseGame
    get() = gameGetter()

abstract class BaseGame : ApplicationListener {
    private var created: Boolean = false
    private var resized: Boolean = false
    private var resumed: Boolean = false

    override fun create() {
        created = true
    }

    override fun resize(width: Int, height: Int) {
        resized = true
        screen?.stageList?.resize(width, height)
    }

    override fun resume() {
        if (resumed) {
            return
        }
        resumed = true
        screen?.stageList?.resume()
    }

    override fun render() {
        if (!resumed) {
            resume()
        }
        screen?.stageList?.act(Gdx.graphics.deltaTime)
        ScreenUtils.clear(Color.CLEAR)
        screen?.stageList?.draw()
    }

    override fun pause() {
        if (!resumed) {
            return
        }
        screen?.stageList?.pause()
        resumed = false
    }

    override fun dispose() {
        resized = false
        screen?.stageList?.dispose()
        created = false
    }

    var screen: Screen? = null
        set(value) {
            val resized = resized
            val resumed = resumed
            if (resumed) {
                field?.stageList?.pause()
            }
            field?.stageList?.dispose()
            field = value
            if (resized) {
                field?.stageList?.resize()
            }
            if (resumed) {
                field?.stageList?.resume()
            }
        }
}
