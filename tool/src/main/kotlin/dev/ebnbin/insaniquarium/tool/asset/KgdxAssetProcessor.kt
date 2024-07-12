package dev.ebnbin.insaniquarium.tool.asset

import dev.ebnbin.kgdx.asset.AssetFileType
import dev.ebnbin.kgdx.asset.Assets
import dev.ebnbin.kgdx.asset.FreeTypeAsset
import dev.ebnbin.kgdx.util.toJson
import java.io.File

object KgdxAssetProcessor {
    fun process() {
        val kgdxAssets = Assets(
            freeType = mapOf(
                "kgdx_dev" to FreeTypeAsset(
                    name = "kgdx_dev",
                    extension = "ttf",
                    fileType = AssetFileType.INTERNAL,
                    preload = true,
                    fontFileName = "kgdx_noto_sans_mono",
                    fontSize = 16f,
                    defaultCharacters = true,
                    characters = "↔↕▲►▼◄",
                ),
            ),
        )
        File("../assets-kgdx/assets/kgdx_assets.json").writeText(kgdxAssets.toJson())
    }
}
