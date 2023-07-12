package dev.ebnbin.insaniquarium

import com.kotcrab.vis.ui.widget.Menu
import dev.ebnbin.gdx.lifecycle.BaseGame
import dev.ebnbin.gdx.utils.createListMenuItem
import dev.ebnbin.gdx.utils.createMenuItem
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.body.assets
import dev.ebnbin.insaniquarium.tank.TankStage

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
        config = Config.init()
        loadScreen(
            assetSet = setOf(
                assets.texture.getValue("aquarium_a"),
                assets.texture.getValue("aquarium_b"),
                assets.texture.getValue("aquarium_c"),
                assets.texture.getValue("aquarium_d"),
                assets.texture.getValue("aquarium_e"),
                assets.texture.getValue("aquarium_f"),
            ) + BodyType.values().flatMap { it.assets() },
            createStageList = {
                listOf(
                    AquariumStage(),
                    TankStage(),
                )
            },
            createDevMenu = { stageList ->
                val menu = Menu("aquarium")
                val tankStage = stageList.filterIsInstance<TankStage>().single()
                menu.createMenuItem("reset bodyType") {
                    tankStage.tank.devSelectedBodyType = null
                }
                menu.createListMenuItem(
                    title = "food",
                    dataList = BodyType.FOOD_LIST,
                    dataToString = { it.serializedName },
                    clicked = { _, bodyType ->
                        tankStage.tank.devSelectedBodyType = bodyType
                    },
                )
                menu.createListMenuItem(
                    title = "fish",
                    dataList = BodyType.FISH_LIST,
                    dataToString = { it.serializedName },
                    clicked = { _, bodyType ->
                        tankStage.tank.devSelectedBodyType = bodyType
                    },
                )
                menu.createListMenuItem(
                    title = "fish_corpse",
                    dataList = BodyType.FISH_CORPSE_LIST,
                    dataToString = { it.serializedName },
                    clicked = { _, bodyType ->
                        tankStage.tank.devSelectedBodyType = bodyType
                    },
                )
                menu.createListMenuItem(
                    title = "alien",
                    dataList = BodyType.ALIEN_LIST,
                    dataToString = { it.serializedName },
                    clicked = { _, bodyType ->
                        tankStage.tank.devSelectedBodyType = bodyType
                    },
                )
                menu.createListMenuItem(
                    title = "pet",
                    dataList = BodyType.PET_LIST,
                    dataToString = { it.serializedName },
                    clicked = { _, bodyType ->
                        tankStage.tank.devSelectedBodyType = bodyType
                    },
                )
                menu.createListMenuItem(
                    title = "money",
                    dataList = BodyType.MONEY_LIST,
                    dataToString = { it.serializedName },
                    clicked = { _, bodyType ->
                        tankStage.tank.devSelectedBodyType = bodyType
                    },
                )
                menu.createListMenuItem(
                    title = "add bodies",
                    dataList = listOf(1, 10, 100, 1000),
                    clicked = { _, count ->
                        repeat(count) {
                            tankStage.tank.devAddBody()
                        }
                    },
                )
                menu.createMenuItem("clear bodies") {
                    tankStage.tank.devClearBodies()
                }
                menu
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
