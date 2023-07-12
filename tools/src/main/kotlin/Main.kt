import dev.ebnbin.gdx.asset.Assets
import dev.ebnbin.gdx.asset.FreeTypeAsset
import dev.ebnbin.gdx.asset.TextureAsset
import dev.ebnbin.gdx.utils.toJson
import dev.ebnbin.insaniquarium.InsaniquariumGame
import java.io.File

fun main() {
    InsaniquariumGame

    MusicAssetHelper.dstDir.deleteRecursively()
    MusicAssetHelper.dstDir.mkdirs()
    SoundAssetHelper.dstDir.deleteRecursively()
    SoundAssetHelper.dstDir.mkdirs()
    TextureInfo.dstDir.deleteRecursively()
    TextureInfo.dstDir.mkdirs()

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

    val musicAssetMap = MusicInfo.all.associate {
        it.name to MusicAssetHelper.music(it)
    }
    val soundAssetMap = SoundInfo.all.associate {
        it.name to SoundAssetHelper.sound(it)
    }
    val textureAssetMap = mutableMapOf<String, TextureAsset>()
    TextureInfo.aquariumList.forEach {
        textureAssetMap[it.name] = TextureAssetHelper.aquarium(it)
    }
    TextureInfo.bodyList.forEach { info ->
        textureAssetMap.putAll(TextureAssetHelper.body(info).associateBy { it.name })
    }

    val assets = Assets(
        music = musicAssetMap.toSortedMap(),
        sound = soundAssetMap.toSortedMap(),
        texture = textureAssetMap.toSortedMap(),
    )
    File("../assets/assets.json").writeText(assets.toJson())
}
