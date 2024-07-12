package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import dev.ebnbin.kgdx.game
import ktx.assets.disposeSafely
import ktx.graphics.copy

internal class DevLabel(
    val entry: DevEntry,
) : Label(null, LabelStyle(game.assets.freeType("kgdx_dev").get(), null)) {
    override fun act(delta: Float) {
        super.act(delta)
        val text = entry.getText(delta)
        setText(text)
    }

    private val backgroundTexture: Texture = run {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.drawPixel(0, 0, Color.WHITE.toIntBits())
        val texture = Texture(pixmap)
        pixmap.disposeSafely()
        texture
    }

    private val backgroundColor: Color = Color.BLACK.copy(alpha = 0.125f)

    override fun draw(batch: Batch, parentAlpha: Float) {
        val oldColor = batch.color
        batch.color = backgroundColor
        batch.draw(backgroundTexture, x, y, width, height)
        batch.color = oldColor
        super.draw(batch, parentAlpha)
    }
}
