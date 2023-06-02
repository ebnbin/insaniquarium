package dev.ebnbin.insaniquarium

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.gdx.lifecycle.BaseStage

class AquariumStage : BaseStage() {
    private val texture: Texture = Texture("texture/aquarium_a.png")

    init {
        val image = Image(texture)
        image.setFillParent(true)
        addActor(image)
    }

    override fun dispose() {
        texture.dispose()
        super.dispose()
    }
}
