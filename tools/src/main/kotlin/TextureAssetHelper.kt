import dev.ebnbin.gdx.asset.TextureAsset
import java.io.File

object TextureAssetHelper {
    fun aquarium(info: AquariumTextureInfo): TextureAsset {
        val srcFile = File(TextureInfo.aquariumSrcDir, info.srcFileName)
        val dstFile = File(TextureInfo.dstDir, "${info.name}.png")

        createImage(1280, 720)
            .useGraphics {
                val srcImage = srcFile.readImage()
                drawScaledImage(srcImage, 0, 0, 32, 480, 0, 0, 160, 720) // left
                drawScaledImage(srcImage, 32, 0, 576, 480, 160, 0, 960, 720) // center
                drawScaledImage(srcImage, 608, 0, 32, 480, 1120, 0, 160, 720) // right
            }
            .write(dstFile)

        return TextureAsset(
            name = info.name,
        )
    }

    fun body(info: BodyTextureInfo): List<TextureAsset> {
        val srcFile = File(TextureInfo.petSrcDir, info.srcFileName)
        val srcMaskFile = File(TextureInfo.petSrcDir, info.srcMaskFileName)
        val split = srcFile
            .readImage()
            .mask(srcMaskFile.readImage())
            .scale(info.scale)
            .split(info.row, info.column)
        return info.outputList.map {
            val dstFile = File(TextureInfo.dstDir, "${it.name}.png")
            val packResult = split.subList(it.tileStart, it.tileStart + it.tileCount).pack()
            packResult.image.write(dstFile)
            TextureAsset(
                name = it.name,
                region = TextureAsset.Region(
                    row = packResult.row,
                    column = packResult.column,
                    startIndex = it.startIndex,
                ),
            )
        }
    }
}
