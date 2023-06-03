package dev.ebnbin.insaniquarium

import dev.ebnbin.gdx.lifecycle.BaseGame
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.insaniquarium.aquarium.TankStage

private var insaniquariumGame: InsaniquariumGame? = null

val game: InsaniquariumGame
    get() = requireNotNull(insaniquariumGame)

class InsaniquariumGame : BaseGame() {
    override fun create() {
        insaniquariumGame?.dispose()
        insaniquariumGame = this
        super.create()
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
        )
    }

    override fun dispose() {
        super.dispose()
        insaniquariumGame = null
    }

    companion object {
        init {
            dev.ebnbin.gdx.Gdx.init(
                gameGetter = ::game,
                unitWidth = 1280f,
                unitHeight = 720f,
                unitsPerMeter = 600f,
            )
        }
    }
}
