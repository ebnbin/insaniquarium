package dev.ebnbin.insaniquarium.tool.asset

import dev.ebnbin.insaniquarium.asset.TextureAssetProcessor
import dev.ebnbin.kgdx.asset.Assets
import dev.ebnbin.kgdx.util.toJson
import java.io.File

object AssetProcessor {
    fun process() {
        val assets = Assets(
            texture = TextureAssetProcessor.process(),
        )
        File("../assets/assets/assets.json").writeText(assets.toJson())
    }
}
