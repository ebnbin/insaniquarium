package dev.ebnbin.insaniquarium

import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.graphics.Texture
import dev.ebnbin.insaniquarium.asset.InsaniquariumTextureLoader
import dev.ebnbin.kgdx.Game
import dev.ebnbin.kgdx.Screen
import dev.ebnbin.kgdx.asset.AssetLoaderRegistry
import dev.ebnbin.kgdx.game

val insaniquarium: Insaniquarium
    get() = game as Insaniquarium

class Insaniquarium : Game() {
    override fun registerAssetLoaders(assetLoaderRegistry: AssetLoaderRegistry, resolver: FileHandleResolver) {
        super.registerAssetLoaders(assetLoaderRegistry, resolver)
        assetLoaderRegistry.setLoader(Texture::class.java, null, InsaniquariumTextureLoader(resolver))
    }

    override fun devScreenList(): List<Screen.Creator> {
        val screenList = mutableListOf<Screen.Creator>()
        screenList.addAll(super.devScreenList())
        screenList.addAll(Screens.ALL)
        return screenList
    }

    companion object {
        init {
            init(
                id = "dev.ebnbin.insaniquarium",
                worldWidth = 1280f,
                worldHeight = 720f,
            )
        }
    }
}
