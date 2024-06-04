package dev.ebnbin.kgdx

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.kgdx.LifecycleStage.Companion.act
import dev.ebnbin.kgdx.LifecycleStage.Companion.dispose
import dev.ebnbin.kgdx.LifecycleStage.Companion.draw
import dev.ebnbin.kgdx.LifecycleStage.Companion.pause
import dev.ebnbin.kgdx.LifecycleStage.Companion.resize
import dev.ebnbin.kgdx.LifecycleStage.Companion.resume
import dev.ebnbin.kgdx.asset.AssetLoaderRegistry
import dev.ebnbin.kgdx.asset.AssetLoadingStage
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

    private lateinit var assetLoadingStage: AssetLoadingStage
    private lateinit var devInfoStage: DevInfoStage
    private lateinit var devMessageStage: DevMessageStage
    private lateinit var devMenuStage: DevMenuStage

    internal var screen: Screen? = null
        set(value) {
            val resumed = resumed
            if (resumed) {
                field?.stageList?.pause()
            }
            field?.stageList?.dispose()
            field = value
            value?.stageList?.resize(Gdx.graphics.width, Gdx.graphics.height)
            if (resumed) {
                value?.stageList?.resume()
            }
        }

    fun loadScreen(screenCreator: Screen.Creator) {
        assetLoadingStage.load(screenCreator)
    }

    private fun globalStageList(): List<LifecycleStage> {
        return listOf(
            assetLoadingStage,
            devInfoStage,
            devMessageStage,
            devMenuStage,
        )
    }

    private fun stageList(): List<LifecycleStage> {
        val stageList = mutableListOf<LifecycleStage>()
        screen?.stageList?.let { stageList.addAll(it) }
        stageList.addAll(globalStageList())
        return stageList
    }

    override fun create() {
        singleton = this
        assetManager = AssetManager()
        assetLoadingStage = AssetLoadingStage()
        devInfoStage = DevInfoStage()
        devMessageStage = DevMessageStage()
        devMenuStage = DevMenuStage()
        globalStageList().resize(Gdx.graphics.width, Gdx.graphics.height)
        canRender = true
    }

    open fun registerAssetLoaders(assetLoaderRegistry: AssetLoaderRegistry, resolver: FileHandleResolver) {
    }

    override fun resize(width: Int, height: Int) {
        stageList().resize(width, height)
    }

    override fun resume() {
        canRender = true
    }

    override fun render() {
        if (!canRender) return
        if (!resumed) {
            resumed = true
            stageList().resume()
        }
        stageList().act(Gdx.graphics.deltaTime)
        ScreenUtils.clear(Color.CLEAR)
        stageList().draw()
    }

    override fun pause() {
        if (resumed) {
            stageList().pause()
            resumed = false
        }
        canRender = false
    }

    override fun dispose() {
        screen = null
        globalStageList().dispose()
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
