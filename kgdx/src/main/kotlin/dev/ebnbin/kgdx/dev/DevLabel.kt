package dev.ebnbin.kgdx.dev

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.util.colorMarkup
import ktx.assets.disposeSafely
import ktx.graphics.copy

internal class DevLabel(
    val entry: Entry,
) : Label(null, LabelStyle(game.assets.freeType("kgdx_noto_sans_mono").get(), null)) {
    data class Entry(
        val key: Any,
        val keyToString: (key: Any) -> String = { "$it" },
        val getValue: (delta: Float) -> String,
    )

    override fun act(delta: Float) {
        super.act(delta)
        val keyText = "${entry.keyToString(entry.key)}=".colorMarkup(Color.LIGHT_GRAY)
        val valueText = entry.getValue(delta)
        val text = " $keyText$valueText "
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
