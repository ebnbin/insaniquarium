package dev.ebnbin.insaniquarium

import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.gdx.BaseGame

class InsaniquariumGame : BaseGame() {
    private lateinit var aquariumStage: AquariumStage

    override fun create() {
        super.create()
        aquariumStage = AquariumStage()
    }

    override fun render() {
        super.render()
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        aquariumStage.draw()
    }

    override fun dispose() {
        aquariumStage.dispose()
        super.dispose()
    }
}
