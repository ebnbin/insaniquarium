package dev.ebnbin.kgdx.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image

class ColorImage(color: Color) : Image(run {
    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).also { pixmap ->
        pixmap.setColor(color)
        pixmap.fill()
    }
    Texture(pixmap).also {
        pixmap.dispose()
    }
})
