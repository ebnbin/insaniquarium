package dev.ebnbin.insaniquarium

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.game

val insaniquarium: Insaniquarium
    get() = game as Insaniquarium

class Insaniquarium : Game() {
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var texture: Texture

    override fun create() {
        super.create()
        spriteBatch = SpriteBatch()
        texture = Texture("libgdx.png")
    }

    override fun render() {
        super.render()
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
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
