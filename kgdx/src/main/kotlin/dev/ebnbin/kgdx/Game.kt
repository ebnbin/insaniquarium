package dev.ebnbin.kgdx

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.kgdx.asset.AssetManager
import dev.ebnbin.kgdx.asset.Assets
import dev.ebnbin.kgdx.dev.DevInfoStage
import dev.ebnbin.kgdx.dev.DevMenuStage
import dev.ebnbin.kgdx.dev.DevMessageStage
import ktx.assets.disposeSafely

private var singleton: Game? = null

val game: Game
    get() = requireNotNull(singleton)

abstract class Game : ApplicationListener {
    private var resumed: Boolean = false

    private var canRender: Boolean = false

    internal lateinit var assetManager: AssetManager

    val assets: Assets
        get() = assetManager.assets

    private lateinit var devInfoStage: DevInfoStage
    private lateinit var devMessageStage: DevMessageStage
    private lateinit var devMenuStage: DevMenuStage

    private var screen: Screen? = null

    fun setScreen(createScreen: (() -> Screen)?) {
        val oldScreen = screen
        val resumed = resumed
        if (resumed) {
            oldScreen?.stageList?.reversed()?.forEach { stage ->
                stage.pause()
            }
        }
        oldScreen?.stageList?.reversed()?.forEach { stage ->
            stage.disposeSafely()
        }
        val newScreen = createScreen?.invoke()
        screen = newScreen
        newScreen?.stageList?.forEach { stage ->
            stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        }
        if (resumed) {
            newScreen?.stageList?.forEach { stage ->
                stage.resume()
            }
        }
    }

    private fun stageList(): List<LifecycleStage> {
        val stageList = mutableListOf<LifecycleStage>()
        screen?.stageList?.let { stageList.addAll(it) }
        stageList.add(devInfoStage)
        stageList.add(devMessageStage)
        stageList.add(devMenuStage)
        return stageList
    }

    override fun create() {
        singleton = this
        assetManager = AssetManager()
        devInfoStage = DevInfoStage()
        devMessageStage = DevMessageStage()
        devMenuStage = DevMenuStage()
        canRender = true
    }

    override fun resize(width: Int, height: Int) {
        stageList().forEach { stage ->
            stage.viewport.update(width, height, true)
        }
    }

    override fun resume() {
        canRender = true
    }

    override fun render() {
        if (!canRender) return
        if (!resumed) {
            resumed = true
            stageList().forEach { stage ->
                stage.resume()
            }
        }
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
        if (resumed) {
            stageList().reversed().forEach { stage ->
                stage.pause()
            }
            resumed = false
        }
        canRender = false
    }

    override fun dispose() {
        setScreen(null)
        devMenuStage.disposeSafely()
        devMessageStage.disposeSafely()
        devInfoStage.disposeSafely()
        assetManager.disposeSafely()
        singleton = null
    }

    fun addDevMessage(message: String) {
        devMessageStage.addMessage(message)
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
