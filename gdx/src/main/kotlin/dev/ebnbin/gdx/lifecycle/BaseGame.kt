package dev.ebnbin.gdx.lifecycle

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.asset.AssetHelper
import dev.ebnbin.gdx.asset.Assets
import dev.ebnbin.gdx.dev.DevLogStage
import dev.ebnbin.gdx.dev.DevMenuStage
import dev.ebnbin.gdx.loading.LoadingStage
import dev.ebnbin.gdx.utils.act
import dev.ebnbin.gdx.utils.dispose
import dev.ebnbin.gdx.utils.draw
import dev.ebnbin.gdx.utils.fromJson
import dev.ebnbin.gdx.utils.pause
import dev.ebnbin.gdx.utils.resize
import dev.ebnbin.gdx.utils.resume

internal lateinit var gameGetter: () -> BaseGame

val baseGame: BaseGame
    get() = gameGetter()

abstract class BaseGame : ApplicationListener {
    lateinit var assets: Assets
        private set

    internal lateinit var assetHelper: AssetHelper

    private lateinit var devLogStage: DevLogStage
    private lateinit var devMenuStage: DevMenuStage
    private lateinit var loadingStage: LoadingStage

    private fun stageList(): List<BaseStage> {
        return (screen?.stageList ?: emptyList()) + listOf(
            loadingStage,
            devMenuStage,
            devLogStage,
        )
    }

    private var created: Boolean = false
    private var resumed: Boolean = false

    override fun create() {
        created = true
        initAssets()
        assetHelper = AssetHelper(assets)
        devLogStage = DevLogStage()
        devMenuStage = DevMenuStage()
        loadingStage = LoadingStage()
    }

    private fun initAssets() {
        val jsonFile = Gdx.files.internal("assets.json")
        assets = if (jsonFile.exists()) {
            jsonFile.readString().fromJson()
        } else {
            Assets()
        }
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

    override fun render() {
        if (!resumed) {
            resume()
        }
        stageList().act(Gdx.graphics.deltaTime)
        ScreenUtils.clear(Color.CLEAR)
        stageList().draw()
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
        devLogStage.dispose()
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
        devLogStage.put(key, value)
    }

    fun removeLog(key: String) {
        devLogStage.remove(key)
    }

    fun restart() {
        pause()
        dispose()
        create()
        resize(Gdx.graphics.width, Gdx.graphics.height)
        resume()
    }
}
