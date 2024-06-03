package dev.ebnbin.insaniquarium

import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.graphics.Texture
import dev.ebnbin.insaniquarium.aquarium.Aquarium
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.insaniquarium.asset.InsaniquariumTextureLoader
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.Screen
import dev.ebnbin.kgdx.asset.AssetLoaderRegistry
import dev.ebnbin.kgdx.game

val insaniquarium: Insaniquarium
    get() = game as Insaniquarium

class Insaniquarium : Game() {
    override fun create() {
        super.create()
        loadScreen(screenCreator = Screen.Creator(
            assetSet = Aquarium.entries.mapTo(mutableSetOf()) { it.textureAsset },
            createStageList = {
                listOf(AquariumStage())
            },
        ))
    }

    override fun registerAssetLoaders(assetLoaderRegistry: AssetLoaderRegistry, resolver: FileHandleResolver) {
        super.registerAssetLoaders(assetLoaderRegistry, resolver)
        assetLoaderRegistry.setLoader(Texture::class.java, null, InsaniquariumTextureLoader(resolver))
    }

    companion object {
        init {
            init(
                worldWidth = 1280f,
                worldHeight = 720f,
            )
        }
    }
}
