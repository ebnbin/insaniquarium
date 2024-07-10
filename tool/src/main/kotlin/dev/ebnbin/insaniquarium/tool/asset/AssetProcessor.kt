package dev.ebnbin.insaniquarium.tool.asset

import dev.ebnbin.insaniquarium.asset.TextureAssetProcessor
import dev.ebnbin.kgdx.asset.AssetFileType
import dev.ebnbin.kgdx.asset.Assets
import dev.ebnbin.kgdx.asset.FreeTypeAsset
import dev.ebnbin.kgdx.util.toJson
import java.io.File

object AssetProcessor {
    fun process() {
        val textureAssetMap = TextureAssetProcessor.process()
        val assets = Assets(
            freeType = mapOf(
                "loading_progress" to FreeTypeAsset(
                    name = "loading_progress",
                    extension = "ttf",
                    fileType = AssetFileType.INTERNAL,
                    preload = false,
                    fontFileName = "kgdx_noto_sans_mono_bold",
                    fontSize = 32f,
                    defaultCharacters = false,
                    characters = "%0123456789",
                )
            ),
            json = mapOf(
                BodyDefProcessor(textureAssetMap).process(),
            ),
            texture = textureAssetMap,
        )
        File("../assets/assets/assets.json").writeText(assets.toJson())
    }
}
