package dev.ebnbin.gdx.lifecycle

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.asset.AssetHelper
import dev.ebnbin.gdx.asset.Assets
import dev.ebnbin.gdx.dev.DevGdxLogStage
import dev.ebnbin.gdx.dev.DevMenuStage
import dev.ebnbin.gdx.loading.LoadingStage
import dev.ebnbin.gdx.utils.act
import dev.ebnbin.gdx.utils.actionTime
import dev.ebnbin.gdx.utils.dispose
import dev.ebnbin.gdx.utils.draw
import dev.ebnbin.gdx.utils.fromJson
import dev.ebnbin.gdx.utils.pause
import dev.ebnbin.gdx.utils.resize
import dev.ebnbin.gdx.utils.resume
import kotlin.math.max

internal lateinit var gameGetter: () -> BaseGame

val baseGame: BaseGame
    get() = gameGetter()

abstract class BaseGame : ApplicationListener {
    lateinit var assets: Assets
        private set

    internal lateinit var assetHelper: AssetHelper

    private lateinit var devGdxLogStage: DevGdxLogStage
    private lateinit var devMenuStage: DevMenuStage
    private lateinit var loadingStage: LoadingStage

    private fun stageList(): List<BaseStage> {
        return (screen?.stageList ?: emptyList()) + listOf(
            loadingStage,
            devMenuStage,
            devGdxLogStage,
        )
    }

    private var created: Boolean = false
    private var resumed: Boolean = false

    override fun create() {
        created = true
        initAssets()
        assetHelper = AssetHelper(assets)
        devGdxLogStage = DevGdxLogStage()
        devMenuStage = DevMenuStage()
        loadingStage = LoadingStage()
    }

    private fun initAssets() {
        assets = listOf(
            "assets_gdx.json",
            "assets.json",
        )
            .map { Gdx.files.internal(it) }
            .filter { it.exists() }
            .map { it.readString().fromJson<Assets>() }
            .fold(Assets()) { acc, assets -> acc + assets }
    }

    override fun resize(width: Int, height: Int) {
        stageList().resize(width, height)
    }

    override fun resume() {
        if (resumed) {
            return
        }
        resumed = true
        stageList().resume()
    }

    private var frameStartTime: Long = 0L

    override fun render() {
        if (!resumed) {
            resume()
        }
        val currentTime = System.nanoTime()
        if (currentTime - frameStartTime >= 1_000_000_000L) {
            frameStartTime = currentTime
            actAverageTime = actTime / max(1, actCount) / 1_000_000f
            clearAverageTime = clearTime / max(1, clearCount) / 1_000_000f
            drawAverageTime = drawTime / max(1, drawCount) / 1_000_000f
            actCount = 0
            actTime = 0L
            clearCount = 0
            clearTime = 0L
            drawCount = 0
            drawTime = 0L
        }
        act()
        clear()
        draw()
    }

    private var actCount: Int = 0
    private var actTime: Long = 0L // ns
    internal var actAverageTime: Float = 0f // ms
        private set

    private fun act() {
        ++actCount
        actTime += actionTime {
            stageList().act(Gdx.graphics.deltaTime)
        }
    }

    private var clearCount: Int = 0
    private var clearTime: Long = 0L // ns
    internal var clearAverageTime: Float = 0f // ms
        private set

    private fun clear() {
        ++clearCount
        clearTime += actionTime {
            ScreenUtils.clear(Color.CLEAR)
        }
    }

    private var drawCount: Int = 0
    private var drawTime: Long = 0L // ns
    internal var drawAverageTime: Float = 0f // ms
        private set

    private fun draw() {
        ++drawCount
        drawTime += actionTime {
            stageList().draw()
        }
    }

    override fun pause() {
        if (!resumed) {
            return
        }
        stageList().pause()
        resumed = false
    }

    override fun dispose() {
        screen = null
        loadingStage.dispose()
        devMenuStage.dispose()
        devGdxLogStage.dispose()
        assetHelper.dispose()
        created = false
    }

    var screen: Screen? = null
        internal set(value) {
            val resumed = resumed
            if (resumed) {
                field?.stageList?.pause()
            }
            field?.stageList?.dispose()
            field = value
            field?.stageList?.resize()
            if (resumed) {
                field?.stageList?.resume()
            }
        }

    fun loadScreen(
        assetSet: Set<Asset<*>>,
        createStageList: () -> List<BaseStage>,
    ) {
        loadingStage.load(assetSet, createStageList)
    }

    fun putLog(key: String, value: (delta: Float) -> String) {
        devGdxLogStage.put(key, value)
    }

    fun removeLog(key: String) {
        devGdxLogStage.remove(key)
    }

    fun restart() {
        pause()
        dispose()
        create()
        resize(Gdx.graphics.width, Gdx.graphics.height)
        resume()
    }
}
