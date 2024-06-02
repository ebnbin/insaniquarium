package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.ebnbin.kgdx.game

sealed class Asset<T>(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("extension")
    val extension: String,
) {
    abstract val directory: String

    abstract val type: Class<T>

    abstract val parameters: AssetLoaderParameters<T>

    internal fun createAssetDescriptor(): AssetDescriptor<T> {
        return AssetDescriptor("$directory/$name.$extension", type, parameters)
    }

    fun get(): T {
        val assetDescriptor = createAssetDescriptor()
        return if (game.assetManager.isLoaded(assetDescriptor)) {
            game.assetManager.get(assetDescriptor)
        } else {
            game.assetManager.load(assetDescriptor)
            game.assetManager.finishLoadingAsset(assetDescriptor)
        }
    }

    internal open fun loaded(asset: T) {
    }

    internal open fun unloaded() {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Asset<*>
        if (directory != other.directory) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = directory.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
