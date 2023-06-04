package dev.ebnbin.insaniquarium

import com.badlogic.gdx.Gdx
import com.kotcrab.vis.ui.widget.Menu
import dev.ebnbin.gdx.lifecycle.BaseGame
import dev.ebnbin.gdx.utils.fromJson
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.insaniquarium.aquarium.TankStage

private var insaniquariumGame: InsaniquariumGame? = null

val game: InsaniquariumGame
    get() = requireNotNull(insaniquariumGame)

class InsaniquariumGame : BaseGame() {
    lateinit var config: Config
        private set

    override fun create() {
        insaniquariumGame?.dispose()
        insaniquariumGame = this
        super.create()
        config = Gdx.files.internal("config.json").readString().fromJson()
        loadScreen(
            assetSet = setOf(
                assets.texture.getValue("aquarium_a"),
                assets.texture.getValue("aquarium_b"),
                assets.texture.getValue("aquarium_c"),
                assets.texture.getValue("aquarium_d"),
                assets.texture.getValue("aquarium_e"),
                assets.texture.getValue("aquarium_f"),
                assets.texture.getValue("clyde"),
            ),
            createStageList = {
                listOf(
                    AquariumStage(),
                    TankStage(),
                )
            },
            createDevMenu = {
                Menu("aquarium")
            },
        )
    }

    override fun dispose() {
        super.dispose()
        insaniquariumGame = null
    }

    companion object {
        init {
            dev.ebnbin.gdx.Gdx.init(
                id = "dev.ebnbin.insaniquarium",
                gameGetter = ::game,
                unitWidth = 1280f,
                unitHeight = 720f,
                unitsPerMeter = 600f,
            )
        }
    }
}
