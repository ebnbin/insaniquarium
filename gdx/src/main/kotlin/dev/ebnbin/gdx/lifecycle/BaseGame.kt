package dev.ebnbin.gdx.lifecycle

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import com.kotcrab.vis.ui.widget.Menu
import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.asset.AssetHelper
import dev.ebnbin.gdx.asset.Assets
import dev.ebnbin.gdx.dev.DevGameLogStage
import dev.ebnbin.gdx.dev.DevGdxLogStage
import dev.ebnbin.gdx.dev.DevMenuStage
import dev.ebnbin.gdx.loading.LoadingStage
import dev.ebnbin.gdx.pref.GdxPrefManager
import dev.ebnbin.gdx.utils.act
import dev.ebnbin.gdx.utils.actionTime
import dev.ebnbin.gdx.utils.dispose
import dev.ebnbin.gdx.utils.draw
import dev.ebnbin.gdx.utils.fromJson
import dev.ebnbin.gdx.utils.pause
import dev.ebnbin.gdx.utils.resize
import dev.ebnbin.gdx.utils.resume
import dev.ebnbin.gdx.utils.tick
import kotlin.math.max
import kotlin.math.min

internal lateinit var gameGetter: () -> BaseGame

val baseGame: BaseGame
    get() = gameGetter()

abstract class BaseGame : ApplicationListener {
    lateinit var assets: Assets
        private set

    internal lateinit var assetHelper: AssetHelper

    private lateinit var inputMultiplexer: InputMultiplexer

    private lateinit var devGdxLogStage: DevGdxLogStage
    private lateinit var devGameLogStage: DevGameLogStage
    private lateinit var devMenuStage: DevMenuStage
    private lateinit var loadingStage: LoadingStage

    private fun globalStageList(): List<BaseStage> {
        return listOf(
            loadingStage,
            devMenuStage,
            devGameLogStage,
            devGdxLogStage,
        )
    }

    private fun stageList(): List<BaseStage> {
        return (screen?.stageList ?: emptyList()) + globalStageList()
    }

    private var created: Boolean = false
    private var resumed: Boolean = false

    override fun create() {
        created = true
        initAssets()
        assetHelper = AssetHelper(assets)
        inputMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = inputMultiplexer
        devGdxLogStage = DevGdxLogStage()
        devGameLogStage = DevGameLogStage()
        devMenuStage = DevMenuStage()
        loadingStage = LoadingStage()
        globalStageList().reversed().forEach {
            inputMultiplexer.addProcessor(it)
        }
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

    private var tickAccumulator: Float = 0f

    private var deltaAccumulator: Float = 0f

    override fun render() {
        if (!resumed) {
            resume()
        }
        val currentTime = System.nanoTime()
        if (currentTime - frameStartTime >= 1_000_000_000L) {
            frameStartTime = currentTime
            ticksPerSecond = tickCount
            actsPerSecond = actCount
            drawsPerSecond = drawCount
            tickAverageTime = tickTime / max(1, tickCount) / 1_000_000f
            actAverageTime = actTime / max(1, actCount) / 1_000_000f
            clearAverageTime = clearTime / max(1, drawCount) / 1_000_000f
            drawAverageTime = drawTime / max(1, drawCount) / 1_000_000f
            tickCount = 0
            actCount = 0
            tickTime = 0L
            actTime = 0L
            clearTime = 0L
            drawCount = 0
            drawTime = 0L
        }
        val tickDelta = min(DELTA_TICK, Gdx.graphics.deltaTime)
        tickAccumulator += tickDelta
        while (tickAccumulator >= DELTA_TICK) {
            tick(DELTA_TICK)
            tickAccumulator -= DELTA_TICK
        }
        val delta = min(DELTA_MAX, Gdx.graphics.deltaTime)
        if (GdxPrefManager.use_fixed_delta.data) {
            deltaAccumulator += delta
            while (deltaAccumulator >= DELTA_FIXED) {
                act(DELTA_FIXED)
                deltaAccumulator -= DELTA_FIXED
            }
        } else {
            act(delta)
        }
        draw()
    }

    internal var ticksPerSecond: Int = 0
        private set
    internal var actsPerSecond: Int = 0
        private set
    internal var drawsPerSecond: Int = 0
        private set

    internal var tickAverageTime: Float = 0f // ms
        private set
    internal var actAverageTime: Float = 0f // ms
        private set
    internal var clearAverageTime: Float = 0f // ms
        private set
    internal var drawAverageTime: Float = 0f // ms
        private set

    private var tickCount: Int = 0
    private var tickTime: Long = 0L // ns

    private fun tick(delta: Float) {
        ++tickCount
        tickTime += actionTime {
            stageList().tick(delta)
        }
    }

    private var actCount: Int = 0
    private var actTime: Long = 0L // ns

    private fun act(delta: Float) {
        ++actCount
        actTime += actionTime {
            stageList().act(delta)
        }
    }

    private var drawCount: Int = 0
    private var clearTime: Long = 0L // ns
    private var drawTime: Long = 0L // ns

    private fun draw() {
        ++drawCount
        clearTime += actionTime {
            ScreenUtils.clear(Color.CLEAR)
        }
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
        globalStageList().forEach {
            inputMultiplexer.removeProcessor(it)
            it.dispose()
        }
        Gdx.input.inputProcessor = null
        inputMultiplexer.clear()
        assetHelper.dispose()
        created = false
    }

    var screen: Screen? = null
        internal set(value) {
            val resumed = resumed
            if (resumed) {
                field?.stageList?.pause()
            }
            field?.devMenu?.let { devMenuStage.removeMenu(it) }
            field?.stageList?.forEach { inputMultiplexer.removeProcessor(it) }
            field?.stageList?.dispose()
            field = value
            field?.stageList?.reversed()?.forEach { inputMultiplexer.addProcessor(it) }
            field?.devMenu?.let { devMenuStage.addMenu(it) }
            field?.stageList?.resize()
            if (resumed) {
                field?.stageList?.resume()
            }
        }

    fun loadScreen(
        assetSet: Set<Asset<*>>,
        createStageList: () -> List<BaseStage>,
        createDevMenu: ((List<BaseStage>) -> Menu)? = null,
    ) {
        loadingStage.load(assetSet, createStageList, createDevMenu)
    }

    fun putLog(key: String, value: (delta: Float) -> String) {
        devGameLogStage.put(key, value)
    }

    fun removeLog(key: String) {
        devGameLogStage.remove(key)
    }

    fun restart() {
        pause()
        dispose()
        create()
        resize(Gdx.graphics.width, Gdx.graphics.height)
        resume()
    }

    companion object {
        internal const val DELTA_TICK = 1f / 20f
        private const val DELTA_MAX = 1f / 20f
        private const val DELTA_FIXED = 1f / 60f
    }
}
