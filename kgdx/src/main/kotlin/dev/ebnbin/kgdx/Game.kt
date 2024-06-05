package dev.ebnbin.kgdx

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.FileHandleResolver
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
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import ktx.assets.disposeSafely

private var singleton: Game? = null

val game: Game
    get() = requireNotNull(singleton)

abstract class Game : ApplicationListener {
    private var created: Boolean = false
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
        created = true
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

    internal var time: Float = 0f
        private set

    override fun render() {
        if (!canRender) return
        if (!resumed) {
            resumed = true
            stageList().resume()
        }
        val delta = Gdx.graphics.deltaTime
        time += delta
        stageList().act(delta)
        val clearColor = KgdxPreferenceManager.clearColor.value
        ScreenUtils.clear(clearColor)
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
        time = 0f
        created = false
        singleton = null
    }

    internal fun recreate() {
        if (resumed) {
            pause()
        }
        if (created) {
            dispose()
        }
        create()
        resize(Gdx.graphics.width, Gdx.graphics.height)
        resume()
    }

    fun addDevMessage(message: String) {
        devMessageStage.addMessage(message)
    }

    open fun devScreenList(): List<Screen.Creator> {
        return emptyList()
    }

    companion object {
        var ID = ""
            private set

        var WORLD_WIDTH = 0f
            private set
        var WORLD_HEIGHT = 0f
            private set

        fun init(
            id: String,
            worldWidth: Float,
            worldHeight: Float,
        ) {
            ID = id
            WORLD_WIDTH = worldWidth
            WORLD_HEIGHT = worldHeight
        }
    }
}
