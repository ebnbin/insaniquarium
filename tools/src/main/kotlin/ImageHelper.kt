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
    ) {
        srcFile
            .readImage()
            .mask(srcMaskFile.readImage())
            .scale(1200, 120)
            .split(1, 10)
            .pack()
            .write(dstFile)
    }
}
