package dev.ebnbin.insaniquarium

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.game
import ktx.assets.disposeSafely

val insaniquarium: Insaniquarium
    get() = game as Insaniquarium

class Insaniquarium : Game() {
    private lateinit var aquariumStage: AquariumStage

    override fun create() {
        super.create()
        aquariumStage = AquariumStage()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        aquariumStage.viewport.update(width, height, true)
    }

    override fun render() {
        super.render()
        aquariumStage.act(Gdx.graphics.deltaTime)
        ScreenUtils.clear(Color.CLEAR)
        aquariumStage.viewport.apply(true)
        aquariumStage.draw()
    }

    override fun dispose() {
        aquariumStage.disposeSafely()
        super.dispose()
    }
}
