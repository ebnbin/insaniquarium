package dev.ebnbin.kgdx.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import dev.ebnbin.kgdx.util.dpToPxFloat
import kotlin.math.roundToInt

class StretchableImage(
    private val texture: Texture,
    private val stretchable: TextureAsset.Stretchable,
) : Widget() {
    private class Region(
        val widgetPosition: Float,
        val widgetSize: Float,
        val texturePosition: Int,
        val textureSize: Int,
    )

    private lateinit var regionListX: List<Region>
    private lateinit var regionListY: List<Region>

    override fun sizeChanged() {
        super.sizeChanged()
        regionListX = regionList(
            widgetSize = width,
            textureSize = texture.width,
            stretchableList = stretchable.x,
            reversed = false,
        )
        regionListY = regionList(
            widgetSize = height,
            textureSize = texture.height,
            stretchableList = stretchable.y,
            reversed = true,
        )
    }

    private fun regionList(
        widgetSize: Float,
        textureSize: Int,
        stretchableList: List<Int>,
        reversed: Boolean,
    ): List<Region> {
        val widgetSizePx = widgetSize.dpToPxFloat
        if (widgetSizePx <= textureSize) {
            return listOf(
                Region(
                    widgetPosition = 0f,
                    widgetSize = widgetSize,
                    texturePosition = 0,
                    textureSize = textureSize,
                ),
            )
        }
        val stretchablePxList = stretchableList.map { it.toFloat().dpToPxFloat }
        val stretchableSizePx = stretchablePxList
            .chunked(2) { it[1] }
            .sum()
        val scale = (widgetSizePx - textureSize + stretchableSizePx) / stretchableSizePx
        val pixelList = stretchablePxList
            .chunked(2) { listOf(it[0], it[0] + it[1]) }
            .flatten()
            .toMutableList()
        pixelList.add(0, 0f)
        pixelList.add(textureSize.toFloat())
        var currentWidgetPosition = if (reversed) widgetSize else 0f
        val regionList = pixelList
            .dropLast(1)
            .mapIndexed { index, pixel ->
                val regionScale = (if (index % 2 == 0) 1f else scale) * KgdxPreferenceManager.dpi.value.dpsPerPx
                val regionTextureSize = pixelList[index + 1] - pixel
                val regionWidgetSize = regionTextureSize * regionScale
                if (reversed) {
                    currentWidgetPosition -= regionWidgetSize
                }
                val region = Region(
                    widgetPosition = currentWidgetPosition,
                    widgetSize = regionWidgetSize,
                    texturePosition = pixel.roundToInt(),
                    textureSize = regionTextureSize.roundToInt(),
                )
                if (!reversed) {
                    currentWidgetPosition += regionWidgetSize
                }
                region
            }
            .filter { it.textureSize > 0 }
        return if (regionList.size <= 1) {
            listOf(
                Region(
                    widgetPosition = 0f,
                    widgetSize = widgetSize,
                    texturePosition = 0,
                    textureSize = textureSize,
                ),
            )
        } else {
            regionList
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        regionListY.forEach { regionY ->
            regionListX.forEach { regionX ->
                batch.draw(
                    texture,
                    regionX.widgetPosition,
                    regionY.widgetPosition,
                    regionX.widgetSize,
                    regionY.widgetSize,
                    regionX.texturePosition,
                    regionY.texturePosition,
                    regionX.textureSize,
                    regionY.textureSize,
                    false,
                    false,
                )
            }
        }
    }
}
