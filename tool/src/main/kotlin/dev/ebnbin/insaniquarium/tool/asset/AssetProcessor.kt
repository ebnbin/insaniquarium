package dev.ebnbin.insaniquarium.tool.asset

import dev.ebnbin.insaniquarium.asset.TextureAssetProcessor
import dev.ebnbin.kgdx.asset.Assets
import dev.ebnbin.kgdx.util.toJson
import java.io.File

object AssetProcessor {
    fun process() {
        val textureAssetMap = TextureAssetProcessor.process()
        val assets = Assets(
            json = mapOf(
                BodyDefProcessor(textureAssetMap).process(),
            ),
            texture = textureAssetMap,
        )
        File("../assets/assets/assets.json").writeText(assets.toJson())
    }
}
