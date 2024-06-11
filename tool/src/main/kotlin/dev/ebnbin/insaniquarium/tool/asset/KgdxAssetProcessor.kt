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
                "kgdx_noto_sans_mono" to FreeTypeAsset(
                    name = "kgdx_noto_sans_mono",
                    extension = "ttf",
                    fileType = AssetFileType.INTERNAL,
                    preload = true,
                    fontFileName = "kgdx_noto_sans_mono",
                    defaultCharacters = true,
                    characters = null,
                ),
            ),
        )
        File("../assets-kgdx/assets/kgdx_assets.json").writeText(kgdxAssets.toJson())
    }
}
