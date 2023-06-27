import dev.ebnbin.gdx.asset.Assets
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.BodyConfig
import java.awt.image.BufferedImage
import java.io.File

object ConfigHelper {
    fun body(assets: Assets, info: BodyConfigInfo): BodyConfig {
        val cache: MutableMap<String, BufferedImage> = mutableMapOf()

        fun size(size: BodyConfigInfo.Size): Float {
            val image = cache.getOrPut(size.textureName) {
                File(TextureInfo.dstDir, "${size.textureName}.png").readImage()
            }
            val region = assets.texture.getValue(info.width.textureName).region
            require(region != null)
            val split = image.split(region.row, region.column)
            val nonTransparentSize = split[size.index ?: region.startIndex].nonTransparentSize()
            return if (size.wh == BodyConfigInfo.WH.WIDTH) {
                nonTransparentSize.first.toFloat().unitToMeter
            } else {
                nonTransparentSize.second.toFloat().unitToMeter
            }
        }

        return BodyConfig(
            group = info.group,
            width = size(info.width),
            height = size(info.height),
            depth = size(info.depth),
            density = info.density,
            dragCoefficient = info.dragCoefficient,
            waterFrictionCoefficient = info.waterFrictionCoefficient,
            leftRightFrictionCoefficient = info.leftRightFrictionCoefficient,
            bottomFrictionCoefficient = info.bottomFrictionCoefficient,
            isDead = info.isDead,
            health = info.health,
            hunger = info.hunger,
            growth = info.growth,
            drop = info.drop,
            eatAct = info.eatAct,
            touchAct = info.touchAct,
            swimActX = info.swimActX,
            swimActY = info.swimActY,
            animations = info.animations,
        ).also {
            cache.clear()
        }
    }
}
