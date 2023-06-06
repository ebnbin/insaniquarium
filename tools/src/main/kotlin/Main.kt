import dev.ebnbin.gdx.asset.Assets
import dev.ebnbin.gdx.asset.TextureAsset
import dev.ebnbin.gdx.utils.toJson
import dev.ebnbin.insaniquarium.InsaniquariumGame
import java.io.File

fun main() {
    InsaniquariumGame

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
