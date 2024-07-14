package dev.ebnbin.insaniquarium.tool.asset

import com.badlogic.gdx.graphics.Pixmap
import dev.ebnbin.insaniquarium.body.BodyDef
import dev.ebnbin.insaniquarium.body.BodyDefJsonWrapper
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.tank.dpToMeter
import dev.ebnbin.kgdx.asset.AssetFileType
import dev.ebnbin.kgdx.asset.AssetId
import dev.ebnbin.kgdx.asset.AssetType
import dev.ebnbin.kgdx.asset.JsonAsset
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.util.Dpi
import dev.ebnbin.kgdx.util.nonTransparentSize
import dev.ebnbin.kgdx.util.tile
import dev.ebnbin.kgdx.util.toJson
import ktx.assets.disposeSafely
import java.io.File

class BodyDefProcessor(
    private val textureAssetMap: Map<String, TextureAsset>,
) {
    private val nonTransparentSizeCache: MutableMap<String, Pair<Int, Int>> = mutableMapOf()

    fun process(): Pair<String, JsonAsset> {
        val bodyDefJsonWrapper = BodyDefJsonWrapper(
            data = BodyType.entries.associate { type ->
                val nonTransparentSize = nonTransparentSize(type.id)
                val def = BodyDef(
                    id = type.id,
                    width = nonTransparentSize.first.toFloat().dpToMeter,
                    height = nonTransparentSize.second.toFloat().dpToMeter,
                    density = when (type) {
                        BodyType.STINKY -> 1.5f
                        BodyType.NIKO -> 1.5f
                        BodyType.RUFUS -> 1.5f
                        BodyType.WADSWORTH -> 0.95f
                        BodyType.BLIP -> 0.95f
                        BodyType.RHUBARB -> 1.5f
                        BodyType.BRINKLEY -> 1.1f
                        else -> 1f
                    },
                    dragCoefficient = 1f,
                    drivingAccelerationX = when (type) {
                        BodyType.NIKO -> 0f
                        else -> 2f
                    },
                    drivingAccelerationY = when (type) {
                        BodyType.STINKY,
                        BodyType.NIKO,
                        BodyType.RUFUS,
                        BodyType.RHUBARB -> 0f
                        else -> 1f
                    },
                    frictionCoefficient = 1f,
                    swimBehaviorX = when (type) {
                        BodyType.STINKY,
                        BodyType.RUFUS,
                        BodyType.RHUBARB,
                        BodyType.BRINKLEY -> BodyDef.SwimBehavior(
                            drivingAccelerationMultiplier = 0.25f,
                            cooldownTicksMin = 40,
                            cooldownTicksMax = 160,
                        )
                        BodyType.NIKO -> null
                        else -> BodyDef.SwimBehavior(
                            drivingAccelerationMultiplier = 0.25f,
                            cooldownTicksMin = 40,
                            cooldownTicksMax = 160,
                        )
                    },
                    swimBehaviorY = when (type) {
                        BodyType.STINKY,
                        BodyType.NIKO,
                        BodyType.RUFUS,
                        BodyType.RHUBARB -> null
                        BodyType.WADSWORTH,
                        BodyType.BLIP -> BodyDef.SwimBehavior(
                            drivingAccelerationMultiplier = 1f,
                            cooldownTicksMin = 120,
                            cooldownTicksMax = 200,
                        )
                        BodyType.BRINKLEY -> BodyDef.SwimBehavior(
                            drivingAccelerationMultiplier = 1.25f,
                            cooldownTicksMin = 120,
                            cooldownTicksMax = 200,
                        )
                        else -> BodyDef.SwimBehavior(
                            drivingAccelerationMultiplier = 0.25f,
                            cooldownTicksMin = 120,
                            cooldownTicksMax = 200,
                        )
                    },
                )
                def.id to def
            },
        )
        val file = File("../assets/assets/json/body_def.json")
        file.parentFile?.mkdirs()
        file.writeText(bodyDefJsonWrapper.toJson())
        val jsonAsset = JsonAsset(
            name = "body_def",
            extension = "json",
            fileType = AssetFileType.INTERNAL,
            preload = true,
            classOfT = BodyDefJsonWrapper::class.java,
        )
        val pair = jsonAsset.name to jsonAsset
        nonTransparentSizeCache.clear()
        return pair
    }

    private fun nonTransparentSize(name: String): Pair<Int, Int> {
        return nonTransparentSizeCache.getOrPut(name) {
            val assetId = AssetId(
                fileType = AssetFileType.LOCAL,
                type = AssetType.TEXTURE,
                nameWithExtension = "$name.png",
            )
            val pixmap = Pixmap(assetId.fileHandle(dpi = Dpi.M))
            val region = requireNotNull(textureAssetMap.getValue(name).region)
            val tiledPixmap = pixmap.tile(
                row = region.row,
                column = region.column,
                tileStart = 0,
                tileCount = 1,
            ).first()
            pixmap.disposeSafely()
            val nonTransparentSize = tiledPixmap.nonTransparentSize()
            tiledPixmap.disposeSafely()
            nonTransparentSize
        }
    }
}
