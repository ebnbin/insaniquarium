import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.BodyConfig
import java.io.File

object ImageHelper {
    fun aquarium(
        srcFile: File,
        dstFile: File,
    ) {
        val srcImage = srcFile.readImage()
        createImage(1280, 720)
            .useGraphics {
                drawScaledImage(srcImage, 0, 0, 32, 480, 0, 0, 160, 720) // left
                drawScaledImage(srcImage, 32, 0, 576, 480, 160, 0, 960, 720) // center
                drawScaledImage(srcImage, 608, 0, 32, 480, 1120, 0, 160, 720) // right
            }
            .write(dstFile)
    }

    fun clyde(
        srcFile: File,
        srcMaskFile: File,
        dstFile: File,
    ): BodyConfig {
        var bodyConfig: BodyConfig
        srcFile
            .readImage()
            .mask(srcMaskFile.readImage())
            .scale(1200, 120)
            .split(1, 10)
            .also {
                val nonTransparentSize = it.first().nonTransparentSize()
                val width = nonTransparentSize.first.toFloat().unitToMeter
                val height = nonTransparentSize.second.toFloat().unitToMeter
                bodyConfig = BodyConfig(
                    id = "clyde",
                    width = width,
                    height = height,
                    depth = width,
                    assetId = "clyde",
                )
            }
            .pack()
            .write(dstFile)
        return bodyConfig
    }
}
