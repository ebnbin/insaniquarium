package dev.ebnbin.insaniquarium

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.gdx.BaseGame

class InsaniquariumGame : BaseGame() {
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var texture: Texture

    override fun create() {
        super.create()
        spriteBatch = SpriteBatch()
        texture = Texture("badlogic.jpg")
    }

    override fun render() {
        super.render()
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        spriteBatch.begin()
        spriteBatch.draw(texture, 0f, 0f)
        spriteBatch.end()
    }

    override fun dispose() {
        texture.dispose()
        spriteBatch.dispose()
        super.dispose()
    }
}
