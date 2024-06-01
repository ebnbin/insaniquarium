package dev.ebnbin.insaniquarium

import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.Screen
import dev.ebnbin.kgdx.game

val insaniquarium: Insaniquarium
    get() = game as Insaniquarium

class Insaniquarium : Game() {
    override fun create() {
        super.create()
        setScreen {
            Screen(stageList = listOf(AquariumStage()))
        }
    }

    companion object {
        init {
            init(
                worldWidth = 1280f,
                worldHeight = 720f,
            )
        }
    }
}
