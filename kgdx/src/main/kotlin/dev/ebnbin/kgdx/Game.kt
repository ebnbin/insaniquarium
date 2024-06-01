package dev.ebnbin.kgdx

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.kgdx.asset.AssetManager
import dev.ebnbin.kgdx.asset.Assets
import dev.ebnbin.kgdx.asset.FreeTypeAsset
import dev.ebnbin.kgdx.dev.DevInfoStage
import dev.ebnbin.kgdx.dev.DevMenuStage
import ktx.assets.disposeSafely

private var singleton: Game? = null

val game: Game
    get() = requireNotNull(singleton)

abstract class Game : ApplicationListener {
    internal lateinit var assetManager: AssetManager

    private lateinit var devInfoStage: DevInfoStage
    private lateinit var devMenuStage: DevMenuStage

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

    private fun stageList(): List<Stage> {
        val stageList = mutableListOf<Stage>()
        screen?.stageList?.let { stageList.addAll(it) }
        stageList.add(devInfoStage)
        stageList.add(devMenuStage)
        return stageList
    }

    override fun create() {
        singleton = this
        assetManager = AssetManager()
        devInfoStage = DevInfoStage()
        devMenuStage = DevMenuStage()
    }

    override fun resize(width: Int, height: Int) {
        stageList().forEach { stage ->
            stage.viewport.update(width, height, true)
        }
    }

    override fun render() {
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
    }

    override fun resume() {
    }

    override fun dispose() {
        setScreen(null)
        devMenuStage.disposeSafely()
        devInfoStage.disposeSafely()
        assetManager.disposeSafely()
        singleton = null
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

        val ASSETS: Assets = Assets(
            freeType = mapOf(
                "kgdx_noto_sans_mono" to FreeTypeAsset(
                    name = "kgdx_noto_sans_mono",
                    extension = "ttf",
                    fontFileName = "kgdx_noto_sans_mono",
                ),
            ),
        )
    }
}
