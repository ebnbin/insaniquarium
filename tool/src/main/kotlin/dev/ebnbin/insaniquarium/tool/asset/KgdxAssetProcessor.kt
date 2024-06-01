package dev.ebnbin.insaniquarium.tool.asset

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
                    fontFileName = "kgdx_noto_sans_mono",
                ),
            ),
        )
        File("../assets-kgdx/kgdx_assets.json").writeText(kgdxAssets.toJson())
    }
}
