package dev.ebnbin.kgdx

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.kgdx.asset.AssetLoaderRegistry
import dev.ebnbin.kgdx.asset.AssetLoadingStage
import dev.ebnbin.kgdx.asset.AssetManager
import dev.ebnbin.kgdx.asset.Assets
import dev.ebnbin.kgdx.dev.DevMenuStage
import dev.ebnbin.kgdx.dev.DevMessageStage
import dev.ebnbin.kgdx.dev.DevSafeInsetStage
import dev.ebnbin.kgdx.dev.GameDevInfoStage
import dev.ebnbin.kgdx.dev.KgdxDevInfoStage
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.scene.LifecycleScene
import dev.ebnbin.kgdx.scene.LifecycleScene.Companion.dispose
import dev.ebnbin.kgdx.scene.LifecycleScene.Companion.pause
import dev.ebnbin.kgdx.scene.LifecycleScene.Companion.registerInputProcessor
import dev.ebnbin.kgdx.scene.LifecycleScene.Companion.render
import dev.ebnbin.kgdx.scene.LifecycleScene.Companion.resize
import dev.ebnbin.kgdx.scene.LifecycleScene.Companion.resume
import dev.ebnbin.kgdx.scene.LifecycleScene.Companion.unregisterInputProcessor
import dev.ebnbin.kgdx.scene.Screen
import ktx.assets.disposeSafely
import kotlin.math.min

val game: Game
    get() = Gdx.app.applicationListener as Game

abstract class Game : ApplicationListener {
    private var created: Boolean = false
    private var resumed: Boolean = false

    private var canRender: Boolean = false

    internal lateinit var assetManager: AssetManager

    val assets: Assets
        get() = assetManager.assets

    private lateinit var inputMultiplexer: InputMultiplexer

    private lateinit var assetLoadingStage: AssetLoadingStage
    internal lateinit var gameDevInfoStage: GameDevInfoStage
    private lateinit var kgdxDevInfoStage: KgdxDevInfoStage
    private lateinit var devMessageStage: DevMessageStage
    internal lateinit var devMenuStage: DevMenuStage
    private lateinit var devSafeInsetStage: DevSafeInsetStage

    internal var screen: Screen? = null
        set(value) {
            val resumed = resumed
            if (resumed) {
                field?.sceneList?.pause()
            }
            field?.sceneList?.unregisterInputProcessor(inputMultiplexer)
            field?.sceneList?.dispose()
            field = value
            field?.sceneList?.registerInputProcessor(inputMultiplexer)
            field?.sceneList?.resize(Gdx.graphics.width, Gdx.graphics.height)
            if (resumed) {
                field?.sceneList?.resume()
            }
        }

    fun loadScreen(screenCreator: Screen.Creator) {
        assetLoadingStage.load(screenCreator)
    }

    private fun globalSceneList(): List<LifecycleScene> {
        return listOf(
            assetLoadingStage,
            gameDevInfoStage,
            kgdxDevInfoStage,
            devMessageStage,
            devMenuStage,
            devSafeInsetStage,
        )
    }

    private fun sceneList(): List<LifecycleScene> {
        val stageList = mutableListOf<LifecycleScene>()
        screen?.sceneList?.let { stageList.addAll(it) }
        stageList.addAll(globalSceneList())
        return stageList
    }

    override fun create() {
        @Suppress("GDXKotlinLogLevel")
        Gdx.app.logLevel = Application.LOG_DEBUG
        created = true
        assetManager = AssetManager()
        inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer
        assetLoadingStage = createAssetLoadingStage()
        gameDevInfoStage = GameDevInfoStage()
        kgdxDevInfoStage = KgdxDevInfoStage()
        devMessageStage = DevMessageStage()
        devMenuStage = DevMenuStage()
        devSafeInsetStage = DevSafeInsetStage()
        globalSceneList().registerInputProcessor(inputMultiplexer)
        globalSceneList().resize(Gdx.graphics.width, Gdx.graphics.height)
        canRender = true
    }

    open fun registerAssetLoaders(assetLoaderRegistry: AssetLoaderRegistry, resolver: FileHandleResolver) {
    }

    open fun createAssetLoadingStage(): AssetLoadingStage {
        return AssetLoadingStage()
    }

    override fun resize(width: Int, height: Int) {
        sceneList().resize(width, height)
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
            sceneList().resume()
        }

        val clearColor = KgdxPreferenceManager.clearColor.value
        ScreenUtils.clear(clearColor)

        val delta = Gdx.graphics.deltaTime * KgdxPreferenceManager.gameSpeedFPS.value / 20f
        val limitedDelta = min(delta, LIMITED_DELTA)
        time += limitedDelta
        sceneList().render(delta = limitedDelta)
    }

    override fun pause() {
        if (resumed) {
            sceneList().pause()
            resumed = false
        }
        canRender = false
    }

    override fun dispose() {
        screen = null
        globalSceneList().unregisterInputProcessor(inputMultiplexer)
        globalSceneList().dispose()
        inputMultiplexer.clear()
        assetManager.disposeSafely()
        time = 0f
        created = false
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
        internal const val LIMITED_FRAMES_PER_SECOND = 20
        private const val LIMITED_DELTA = 1f / LIMITED_FRAMES_PER_SECOND

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
