package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import dev.ebnbin.kgdx.game

sealed class Asset<T>(
    val name: String,
    val extension: String,
) {
    abstract val directory: String

    abstract val type: Class<T>

    abstract val parameters: AssetLoaderParameters<T>

    private val assetDescriptor: AssetDescriptor<T>
        get() = AssetDescriptor("$directory/$name.$extension", type, parameters)

    fun get(): T {
        val assetDescriptor = assetDescriptor
        return if (game.assetManager.isLoaded(assetDescriptor)) {
            game.assetManager.get(assetDescriptor)
        } else {
            game.assetManager.load(assetDescriptor)
            game.assetManager.finishLoadingAsset(assetDescriptor)
        }
    }
}
