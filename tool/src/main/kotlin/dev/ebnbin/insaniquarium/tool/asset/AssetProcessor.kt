package dev.ebnbin.insaniquarium.tool.asset

import dev.ebnbin.kgdx.asset.Asset
import dev.ebnbin.kgdx.asset.Assets
import dev.ebnbin.kgdx.asset.TextureAsset
import dev.ebnbin.kgdx.util.toJson
import java.io.File

object AssetProcessor {
    fun process() {
        val assets = Assets(
            texture = mapOf(
                "aquarium_a" to TextureAsset(
                    name = "aquarium_a",
                    extension = "png",
                    fileType = Asset.FileType.LOCAL,
                ),
            ),
        )
        File("../assets/assets/assets.json").writeText(assets.toJson())
    }
}
