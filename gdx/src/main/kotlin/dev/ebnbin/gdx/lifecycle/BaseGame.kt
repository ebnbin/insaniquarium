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
    private lateinit var stageList: List<BaseStage>

    abstract fun stageList(): List<BaseStage>

    private var created: Boolean = false
    private var resumed: Boolean = false

    override fun create() {
        created = true
        stageList = stageList()
    }

    override fun resize(width: Int, height: Int) {
        stageList.resize(width, height)
    }

    override fun resume() {
        if (resumed) {
            return
        }
        resumed = true
        stageList.resume()
    }

    override fun render() {
        if (!resumed) {
            resume()
        }
        stageList.act(Gdx.graphics.deltaTime)
        ScreenUtils.clear(Color.CLEAR)
        stageList.draw()
    }

    override fun pause() {
        if (!resumed) {
            return
        }
        stageList.pause()
        resumed = false
    }

    override fun dispose() {
        stageList.dispose()
        created = false
    }
}
