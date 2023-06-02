package dev.ebnbin.insaniquarium

import dev.ebnbin.gdx.lifecycle.BaseGame
import dev.ebnbin.gdx.lifecycle.BaseStage

private var insaniquariumGame: InsaniquariumGame? = null

val game: InsaniquariumGame
    get() = requireNotNull(insaniquariumGame)

class InsaniquariumGame : BaseGame() {
    override fun stageList(): List<BaseStage> {
        return listOf(
            AquariumStage(),
        )
    }

    override fun create() {
        insaniquariumGame?.dispose()
        insaniquariumGame = this
        super.create()
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
            )
        }
    }
}
