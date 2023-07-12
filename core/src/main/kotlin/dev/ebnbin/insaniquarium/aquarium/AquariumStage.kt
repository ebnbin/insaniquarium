package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.scenes.scene2d.ui.Image
import dev.ebnbin.gdx.lifecycle.BaseStage

class AquariumStage(
    aquarium: Aquarium = Aquarium.values().random(),
) : BaseStage() {
    init {
        val aquariumImage = Image(aquarium.textureAsset.get())
        aquariumImage.setFillParent(true)
        addActor(aquariumImage)
    }

    private val music: Music = aquarium.musicAsset.get()

    override fun resume() {
        super.resume()
        music.play()
    }

    override fun pause() {
        music.pause()
        super.pause()
    }

    override fun dispose() {
        music.stop()
        super.dispose()
    }
}
