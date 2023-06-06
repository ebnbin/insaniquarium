import dev.ebnbin.gdx.asset.Assets
import dev.ebnbin.gdx.asset.FreeTypeAsset
import dev.ebnbin.gdx.asset.TextureAsset
import dev.ebnbin.gdx.utils.toJson
import dev.ebnbin.insaniquarium.InsaniquariumGame
import java.io.File

fun main() {
    InsaniquariumGame

    val gdxAssets = Assets(
        freeType = mapOf(
            "gdx_dev" to FreeTypeAsset(
                name = "gdx_dev",
                preload = true,
                fontFileName = "gdx_noto_sans_mono",
                shadowOffsetX = 1f,
                shadowOffsetY = 1f,
                characters = "▲►▼◄",
            ),
        ),
    )
    File("../assets_gdx/assets_gdx.json").writeText(gdxAssets.toJson())

    val textureAssetMap = mutableMapOf<String, TextureAsset>()
    TextureInfo.aquariumList.forEach {
        textureAssetMap[it.name] = ImageHelper.aquarium(it)
    }
    TextureInfo.petList.forEach {
        textureAssetMap[it.name] = ImageHelper.pet(it)
    }

    val assets = Assets(
        texture = textureAssetMap.toSortedMap(),
    )
    File("../assets/assets.json").writeText(assets.toJson())
}
